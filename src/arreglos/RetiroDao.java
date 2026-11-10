package arreglos;

import clases.Conexion;
import clases.Retiro;
import java.sql.*;
import java.util.ArrayList;

public class RetiroDao {

    /** Inserta el retiro y devuelve el num_retiro generado por MySQL. */
    public int adicionar(Retiro r) throws Exception {
        try (Connection cn = Conexion.getConnection()) {
            return adicionar(cn, r);
        }
    }

    /** Versión para usar dentro de una transacción. */
    public int adicionar(Connection cn, Retiro r) throws Exception {
        String sql = "INSERT INTO retiros (num_matricula, fecha, hora) VALUES (?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getNumMatricula());
            ps.setDate(2, java.sql.Date.valueOf(formatoFecha(r.getFecha())));
            ps.setTime(3, java.sql.Time.valueOf(formatoHora(r.getHora())));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void eliminar(int numRetiro) throws Exception {
        try (Connection cn = Conexion.getConnection()) {
            eliminar(cn, numRetiro);
        }
    }

    public void eliminar(Connection cn, int numRetiro) throws Exception {
        String sql = "DELETE FROM retiros WHERE num_retiro = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, numRetiro);
            ps.executeUpdate();
        }
    }

    public Retiro buscar(int numRetiro) throws Exception {
        try (Connection cn = Conexion.getConnection()) {
            return buscar(cn, numRetiro);
        }
    }

    public Retiro buscar(Connection cn, int numRetiro) throws Exception {
        String sql = "SELECT * FROM retiros WHERE num_retiro = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, numRetiro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public ArrayList<Retiro> listar() throws Exception {
        ArrayList<Retiro> lista = new ArrayList<>();
        String sql = "SELECT * FROM retiros ORDER BY num_retiro";
        try (Connection cn = Conexion.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Retiro mapear(ResultSet rs) throws SQLException {
        return new Retiro(
            rs.getInt("num_retiro"),
            rs.getInt("num_matricula"),
            rs.getDate("fecha").toString(),
            rs.getTime("hora").toString()
        );
    }

    private String formatoFecha(String f) {
        if (f.contains("/")) {
            String[] p = f.split("/");
            return p[2] + "-" + p[1] + "-" + p[0];
        }
        return f;
    }

    private String formatoHora(String h) {
        if (h != null && h.length() == 5) return h + ":00";
        return h;
    }
}

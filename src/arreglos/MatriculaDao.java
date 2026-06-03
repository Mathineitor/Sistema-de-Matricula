package arreglos;

import clases.Conexion;
import clases.Matricula;
import java.sql.*;
import java.util.ArrayList;

public class MatriculaDao {

    /**
     * Inserta matrícula SIN num_matricula — MySQL lo genera automáticamente.
     * Retorna el número generado para mostrarlo en la GUI.
     */
    public int adicionar(Matricula m) throws Exception {
        String sql = "INSERT INTO matriculas (cod_alumno, cod_curso, fecha, hora, activo) "
                   + "VALUES (?,?,?,?,1)";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getCodigoAlumno());
            ps.setInt(2, m.getCodigoCurso());
            ps.setDate(3, java.sql.Date.valueOf(formatoFecha(m.getFecha())));
            ps.setTime(4, java.sql.Time.valueOf(formatoHora(m.getHora())));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void eliminar(int numMatricula) throws Exception {
        String sql = "DELETE FROM matriculas WHERE num_matricula = ?";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, numMatricula);
            ps.executeUpdate();
        }
    }

    public void actualizarCurso(int numMatricula, int codCurso) throws Exception {
        String sql = "UPDATE matriculas SET cod_curso=? WHERE num_matricula=?";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCurso);
            ps.setInt(2, numMatricula);
            ps.executeUpdate();
        }
    }

    public Matricula buscar(int numMatricula) throws Exception {
        String sql = "SELECT * FROM matriculas WHERE num_matricula = ?";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, numMatricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Matricula buscarPorAlumno(int codAlumno) throws Exception {
        String sql = "SELECT * FROM matriculas WHERE cod_alumno = ? AND activo = 1";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codAlumno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public ArrayList<Matricula> listar() throws Exception {
        ArrayList<Matricula> lista = new ArrayList<>();
        String sql = "SELECT * FROM matriculas ORDER BY num_matricula";
        try (Connection cn = Conexion.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Matricula mapear(ResultSet rs) throws SQLException {
        return new Matricula(
            rs.getInt("num_matricula"),
            rs.getInt("cod_alumno"),
            rs.getInt("cod_curso"),
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
package arreglos;

import clases.Conexion;
import clases.Curso;
import java.sql.*;
import java.util.ArrayList;

public class CursoDao {
    
    public void adicionar(Curso c) throws Exception {
        String sql = "INSERT INTO cursos (cod_curso, asignatura, ciclo, creditos, horas) VALUES (?,?,?,?,?)";
        try (Connection cn = Conexion.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, c.getCodCurso());
            ps.setString(2, c.getAsignatura());
            ps.setInt(3, c.getCiclo());
            ps.setInt(4, c.getCreditos());
            ps.setInt(5, c.getHoras());
            ps.executeUpdate();
        }
    }

    public void eliminar(int codCurso) throws Exception {
        String sql = "DELETE FROM cursos WHERE cod_curso = ?";
        try (Connection cn = Conexion.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCurso);
            ps.executeUpdate();
        }
    }

    public void actualizar(Curso c) throws Exception {
        String sql = "UPDATE cursos SET asignatura=?, ciclo=?, creditos=?, horas=? WHERE cod_curso=?";
        try (Connection cn = Conexion.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getAsignatura());
            ps.setInt(2, c.getCiclo());
            ps.setInt(3, c.getCreditos());
            ps.setInt(4, c.getHoras());
            ps.setInt(5, c.getCodCurso());
            ps.executeUpdate();
        }
    }

    public Curso buscar(int codCurso) throws Exception {
        String sql = "SELECT * FROM cursos WHERE cod_curso=?";
        try (Connection cn = Conexion.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCurso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Curso(
                        rs.getInt("cod_curso"),
                        rs.getString("asignatura"),
                        rs.getInt("ciclo"),
                        rs.getInt("creditos"),
                        rs.getInt("horas")
                    );
                }
            }
        }
        return null;
    }

    public ArrayList<Curso> listar() throws Exception {
        ArrayList<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM cursos ORDER BY cod_curso";
        try (Connection cn = Conexion.getConnection(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Curso(
                    rs.getInt("cod_curso"),
                    rs.getString("asignatura"),
                    rs.getInt("ciclo"),
                    rs.getInt("creditos"),
                    rs.getInt("horas")
                ));
            }
        }
        return lista;
    }

    public int codigoCorrelativo() throws Exception {
        String sql = "SELECT MAX(cod_curso) AS maxcod FROM cursos";
        try (Connection cn = Conexion.getConnection(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next() && rs.getInt("maxcod") > 0) return rs.getInt("maxcod") + 1;
        }
        return 101; // valor inicial
    }
}

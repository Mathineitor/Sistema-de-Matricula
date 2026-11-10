package arreglos;

import clases.Conexion;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;


public class DashboardDao {
        /** Total de alumnos registrados en el sistema */
    public int totalAlumnos() throws Exception {
        return contarConFiltro("SELECT COUNT(*) FROM alumnos");
    }

    /** Alumnos con estado = 1 (MATRICULADO) */
    public int totalMatriculados() throws Exception {
        return contarConFiltro("SELECT COUNT(*) FROM alumnos WHERE estado = 1");
    }

    /** Alumnos con estado = 2 (RETIRADO) */
    public int totalRetirados() throws Exception {
        return contarConFiltro("SELECT COUNT(*) FROM alumnos WHERE estado = 2");
    }

    /** Alumnos con estado = 0 (REGISTRADO, sin matricula) */
    public int totalPendientes() throws Exception {
        return contarConFiltro("SELECT COUNT(*) FROM alumnos WHERE estado = 0");
    }

    /** Total de cursos en el sistema */
    public int totalCursos() throws Exception {
        return contarConFiltro("SELECT COUNT(*) FROM cursos");
    }

    /** Total de matrículas activas */
    public int totalMatriculas() throws Exception {
        return contarConFiltro("SELECT COUNT(*) FROM matriculas WHERE activo = 1");
    }

    /**
     * Devuelve un mapa: nombre del curso → cantidad de alumnos matriculados.
     * Usado para el gráfico de barras.
     */
    public Map<String, Integer> alumnosPorCurso() throws Exception {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        String sql = "SELECT c.asignatura, COUNT(m.num_matricula) AS total "
                   + "FROM cursos c "
                   + "LEFT JOIN matriculas m ON c.cod_curso = m.cod_curso AND m.activo = 1 "
                   + "GROUP BY c.cod_curso, c.asignatura "
                   + "ORDER BY c.cod_curso";
        try (Connection cn = Conexion.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Acorta nombres muy largos para que quepan en la barra
                String nombre = rs.getString("asignatura");
                if (nombre.length() > 20) nombre = nombre.substring(0, 18) + "...";
                mapa.put(nombre, rs.getInt("total"));
            }
        }
        return mapa;
    }

    // ── helper privado ────────────────────────────────────────────────────
    private int contarConFiltro(String sql) throws Exception {
        try (Connection cn = Conexion.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}

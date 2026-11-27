package servicios;

import arreglos.AlumnoDao;
import arreglos.CursoDao;
import arreglos.DashboardDao;
import arreglos.MatriculaDao;
import clases.Alumno;
import clases.Curso;
import clases.EstadoAlumno;
import clases.Matricula;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mediador.ReglaNegocioException;

/**
 * PATRÓN FACADE (Estructural).
 * Fachada de SOLO LECTURA para consultas, reportes y dashboard. Los diálogos dejan de
 * instanciar y coordinar 3-4 DAOs: piden lo que necesitan y reciben un objeto armado.
 */
public class ConsultasFacade {
    private final AlumnoDao alumnos;
    private final CursoDao cursos;
    private final MatriculaDao matriculas;
    private final DashboardDao dashboard;

    public ConsultasFacade() {
        this(new AlumnoDao(), new CursoDao(), new MatriculaDao(), new DashboardDao());
    }

    public ConsultasFacade(AlumnoDao a, CursoDao c, MatriculaDao m, DashboardDao d) {
        this.alumnos = a; this.cursos = c; this.matriculas = m; this.dashboard = d;
    }

    /** Matrícula + alumno + curso en una sola llamada. */
    public FichaMatricula fichaMatricula(int numMatricula) throws Exception {
        Matricula m = matriculas.buscar(numMatricula);
        if (m == null) throw new ReglaNegocioException("La matrícula no existe");
        Alumno a = alumnos.buscar(m.getCodigoAlumno());
        Curso c = cursos.buscar(m.getCodigoCurso());
        return new FichaMatricula(m, a, c);
    }

    /** Reportes de alumnos (pendientes = REGISTRADO, vigentes = MATRICULADO, ...). */
    public List<Alumno> alumnosPorEstado(EstadoAlumno estado) throws Exception {
        List<Alumno> resultado = new ArrayList<>();
        for (Alumno a : alumnos.listar())
            if (a.getEstado() == estado.getValor()) resultado.add(a);
        return resultado;
    }

    /** Todas las cifras del dashboard en un solo objeto. */
    public ResumenDashboard resumenDashboard() throws Exception {
        return new ResumenDashboard(
            dashboard.totalAlumnos(), dashboard.totalPendientes(), dashboard.totalMatriculados(),
            dashboard.totalRetirados(), dashboard.totalCursos(), dashboard.totalMatriculas(),
            dashboard.alumnosPorCurso());
    }

    // ── Objetos de transferencia (inmutables) ─────────────────────────────

    public static final class FichaMatricula {
        private final Matricula matricula;
        private final Alumno alumno;
        private final Curso curso;

        FichaMatricula(Matricula m, Alumno a, Curso c) { this.matricula = m; this.alumno = a; this.curso = c; }

        public Matricula getMatricula() { return matricula; }
        public Alumno getAlumno()       { return alumno; }
        public Curso getCurso()         { return curso; }
        public EstadoAlumno getEstadoAlumno() { return EstadoAlumno.desde(alumno.getEstado()); }
    }

    public static final class ResumenDashboard {
        public final int totalAlumnos, pendientes, matriculados, retirados, cursos, matriculas;
        public final Map<String, Integer> alumnosPorCurso;

        ResumenDashboard(int total, int pendientes, int matriculados, int retirados,
                         int cursos, int matriculas, Map<String, Integer> porCurso) {
            this.totalAlumnos = total; this.pendientes = pendientes; this.matriculados = matriculados;
            this.retirados = retirados; this.cursos = cursos; this.matriculas = matriculas;
            this.alumnosPorCurso = Collections.unmodifiableMap(new LinkedHashMap<>(porCurso));
        }
    }
}

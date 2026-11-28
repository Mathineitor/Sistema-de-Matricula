package servicios;

import arreglos.AlumnoDao;
import arreglos.CursoDao;
import arreglos.DashboardDao;
import arreglos.MatriculaDao;
import arreglos.RetiroDao;
import clases.Retiro;
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
 * PATR\u00d3N FACADE (Estructural).
 * Fachada de SOLO LECTURA para consultas, reportes y dashboard. Los di\u00e1logos dejan de
 * instanciar y coordinar 3-4 DAOs: piden lo que necesitan y reciben un objeto armado.
 */
public class ConsultasFacade {
    private final AlumnoDao alumnos;
    private final CursoDao cursos;
    private final MatriculaDao matriculas;
    private final DashboardDao dashboard;
    private final RetiroDao retiros = new RetiroDao();

    public ConsultasFacade() {
        this(new AlumnoDao(), new CursoDao(), new MatriculaDao(), new DashboardDao());
    }

    public ConsultasFacade(AlumnoDao a, CursoDao c, MatriculaDao m, DashboardDao d) {
        this.alumnos = a; this.cursos = c; this.matriculas = m; this.dashboard = d;
    }

    /** Matr\u00edcula + alumno + curso en una sola llamada. */
    public FichaMatricula fichaMatricula(int numMatricula) throws Exception {
        Matricula m = matriculas.buscar(numMatricula);
        if (m == null) throw new ReglaNegocioException("La matr\u00edcula no existe");
        Alumno a = alumnos.buscar(m.getCodigoAlumno());
        Curso c = cursos.buscar(m.getCodigoCurso());
        return new FichaMatricula(m, a, c);
    }

    public List<Alumno> alumnos() throws Exception { return alumnos.listar(); }

    public List<Curso> cursos() throws Exception { return cursos.listar(); }

    /** Todas las matriculas ya armadas (matricula + alumno + curso). */
    public List<FichaMatricula> fichasMatriculas() throws Exception {
        List<FichaMatricula> lista = new ArrayList<>();
        for (Matricula m : matriculas.listar())
            lista.add(new FichaMatricula(m, alumnos.buscar(m.getCodigoAlumno()), cursos.buscar(m.getCodigoCurso())));
        return lista;
    }

    /** Todos los retiros armados (retiro + su ficha de matricula). */
    public List<FichaRetiro> fichasRetiros() throws Exception {
        List<FichaRetiro> lista = new ArrayList<>();
        for (Retiro r : retiros.listar())
            lista.add(new FichaRetiro(r, fichaMatricula(r.getNumMatricula())));
        return lista;
    }

    /** Matricula vigente de un alumno, o null si no tiene. */
    public FichaMatricula matriculaVigenteDe(int codAlumno) throws Exception {
        Matricula m = matriculas.buscarPorAlumno(codAlumno);
        return m == null ? null : new FichaMatricula(m, alumnos.buscar(codAlumno), cursos.buscar(m.getCodigoCurso()));
    }

    /** Alumnos con matricula en un curso. */
    public List<Alumno> alumnosDelCurso(int codCurso) throws Exception {
        List<Alumno> lista = new ArrayList<>();
        for (Matricula m : matriculas.listar())
            if (m.getCodigoCurso() == codCurso) lista.add(alumnos.buscar(m.getCodigoAlumno()));
        return lista;
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

    // \u2500\u2500 Objetos de transferencia (inmutables) \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500

    public static final class FichaMatricula {
        private final Matricula matricula;
        private final Alumno alumno;
        private final Curso curso;

        public FichaMatricula(Matricula m, Alumno a, Curso c) { this.matricula = m; this.alumno = a; this.curso = c; }

        public Matricula getMatricula() { return matricula; }
        public Alumno getAlumno()       { return alumno; }
        public Curso getCurso()         { return curso; }
        public EstadoAlumno getEstadoAlumno() { return EstadoAlumno.desde(alumno.getEstado()); }
    }

    public static final class FichaRetiro {
        private final Retiro retiro;
        private final FichaMatricula ficha;

        public FichaRetiro(Retiro r, FichaMatricula f) { this.retiro = r; this.ficha = f; }

        public Retiro getRetiro()        { return retiro; }
        public FichaMatricula getFicha() { return ficha; }
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

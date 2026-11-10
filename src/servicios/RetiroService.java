package servicios;

import arreglos.AlumnoDao;
import arreglos.CursoDao;
import arreglos.MatriculaDao;
import arreglos.RetiroDao;
import clases.Alumno;
import clases.Conexion;
import clases.EstadoAlumno;
import clases.Matricula;
import clases.Retiro;
import mediador.ReglaNegocioException;

public class RetiroService {
    private final AlumnoDao    alumnos    = new AlumnoDao();
    private final CursoDao     cursos     = new CursoDao();
    private final MatriculaDao matriculas = new MatriculaDao();
    private final RetiroDao    retiros    = new RetiroDao();

    /** Inserta el retiro y pasa al alumno a RETIRADO, en una transacción. Devuelve el número generado. */
    public int registrar(Retiro r) throws Exception {
        return Conexion.enTransaccion(cn -> {
            Matricula m = matriculas.buscar(cn, r.getNumMatricula());
            if (m == null)
                throw new ReglaNegocioException("La matrícula no existe");
            Alumno a = alumnos.buscar(cn, m.getCodigoAlumno());
            if (a.getEstado() == EstadoAlumno.RETIRADO.getValor())
                throw new ReglaNegocioException("El alumno ya se encuentra retirado");
            if (a.getEstado() != EstadoAlumno.MATRICULADO.getValor())
                throw new ReglaNegocioException("El alumno no se encuentra matriculado");

            int numero = retiros.adicionar(cn, r);
            alumnos.actualizarEstado(cn, a.getCodAlumno(), EstadoAlumno.RETIRADO.getValor());
            return numero;
        });
    }

    /** Elimina el retiro y devuelve al alumno a MATRICULADO, en una transacción. */
    public void eliminar(int numRetiro) throws Exception {
        Conexion.enTransaccion(cn -> {
            Retiro r = retiros.buscar(cn, numRetiro);
            if (r == null)
                throw new ReglaNegocioException("El retiro no existe");
            Matricula m = matriculas.buscar(cn, r.getNumMatricula());

            retiros.eliminar(cn, numRetiro);
            alumnos.actualizarEstado(cn, m.getCodigoAlumno(), EstadoAlumno.MATRICULADO.getValor());
            return null;
        });
    }

    public void modificarCurso(int numRetiro, int codCurso) throws Exception {
        Retiro r = retiros.buscar(numRetiro);
        if (r == null) throw new ReglaNegocioException("El retiro no existe");
        if (cursos.buscar(codCurso) == null)
            throw new ReglaNegocioException("El curso seleccionado no existe");
        matriculas.actualizarCurso(r.getNumMatricula(), codCurso);
    }
}

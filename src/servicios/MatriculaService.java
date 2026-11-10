package servicios;

import arreglos.AlumnoDao;
import arreglos.CursoDao;
import arreglos.MatriculaDao;
import clases.Alumno;
import clases.Conexion;
import clases.EstadoAlumno;
import clases.Matricula;
import mediador.ReglaNegocioException;

public class MatriculaService {
    private final AlumnoDao    alumnos    = new AlumnoDao();
    private final CursoDao     cursos     = new CursoDao();
    private final MatriculaDao matriculas = new MatriculaDao();

    /** Inserta la matrícula y pasa al alumno a MATRICULADO, todo en una transacción. Devuelve el número generado. */
    public int registrar(Matricula m) throws Exception {
        if (cursos.buscar(m.getCodigoCurso()) == null)
            throw new ReglaNegocioException("El curso seleccionado no existe");

        return Conexion.enTransaccion(cn -> {
            Alumno a = alumnos.buscar(cn, m.getCodigoAlumno());
            if (a == null)
                throw new ReglaNegocioException("El alumno no existe");
            if (a.getEstado() == EstadoAlumno.MATRICULADO.getValor())
                throw new ReglaNegocioException("El alumno ya se encuentra matriculado");
            if (a.getEstado() == EstadoAlumno.RETIRADO.getValor())
                throw new ReglaNegocioException("El alumno se encuentra retirado y no puede matricularse");

            int numero = matriculas.adicionar(cn, m);
            alumnos.actualizarEstado(cn, a.getCodAlumno(), EstadoAlumno.MATRICULADO.getValor());
            return numero;
        });
    }

    public void modificarCurso(int numMatricula, int codCurso) throws Exception {
        if (matriculas.buscar(numMatricula) == null)
            throw new ReglaNegocioException("La matrícula no existe");
        if (cursos.buscar(codCurso) == null)
            throw new ReglaNegocioException("El curso seleccionado no existe");
        matriculas.actualizarCurso(numMatricula, codCurso);
    }

    /** Elimina la matrícula y devuelve al alumno a REGISTRADO, en una transacción. */
    public void eliminar(int numMatricula) throws Exception {
        Conexion.enTransaccion(cn -> {
            Matricula m = matriculas.buscar(cn, numMatricula);
            if (m == null)
                throw new ReglaNegocioException("La matrícula no existe");
            Alumno a = alumnos.buscar(cn, m.getCodigoAlumno());
            if (a.getEstado() != EstadoAlumno.MATRICULADO.getValor())
                throw new ReglaNegocioException("No es posible eliminar la matrícula, el alumno se encuentra retirado");

            matriculas.eliminar(cn, numMatricula);
            alumnos.actualizarEstado(cn, a.getCodAlumno(), EstadoAlumno.REGISTRADO.getValor());
            return null;
        });
    }
}

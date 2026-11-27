package estados;

import clases.EstadoAlumno;
import mediador.ReglaNegocioException;

/** Alumno con matrícula activa. */
public final class AlumnoMatriculado implements EstadoDelAlumno {
    static final AlumnoMatriculado INSTANCIA = new AlumnoMatriculado();
    private AlumnoMatriculado() { }

    @Override public EstadoAlumno getEstado() { return EstadoAlumno.MATRICULADO; }
    @Override public EstadoDelAlumno matricular() throws ReglaNegocioException {
        throw new ReglaNegocioException("El alumno ya se encuentra matriculado");
    }
    @Override public EstadoDelAlumno retirar() { return AlumnoRetirado.INSTANCIA; }
    @Override public EstadoDelAlumno anularMatricula() { return AlumnoRegistrado.INSTANCIA; }
    @Override public EstadoDelAlumno anularRetiro() { return this; }
    @Override public void validarEliminacion() throws ReglaNegocioException {
        throw new ReglaNegocioException("No puede eliminar a un alumno ya matriculado");
    }
}

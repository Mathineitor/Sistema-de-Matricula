package estados;

import clases.EstadoAlumno;
import mediador.ReglaNegocioException;

/** Alumno que se retiró. */
public final class AlumnoRetirado implements EstadoDelAlumno {
    static final AlumnoRetirado INSTANCIA = new AlumnoRetirado();
    private AlumnoRetirado() { }

    @Override public EstadoAlumno getEstado() { return EstadoAlumno.RETIRADO; }
    @Override public EstadoDelAlumno matricular() throws ReglaNegocioException {
        throw new ReglaNegocioException("El alumno se encuentra retirado y no puede matricularse");
    }
    @Override public EstadoDelAlumno retirar() throws ReglaNegocioException {
        throw new ReglaNegocioException("El alumno ya se encuentra retirado");
    }
    @Override public EstadoDelAlumno anularMatricula() throws ReglaNegocioException {
        throw new ReglaNegocioException("No es posible eliminar la matrícula, el alumno se encuentra retirado");
    }
    @Override public EstadoDelAlumno anularRetiro() { return AlumnoMatriculado.INSTANCIA; }
    @Override public void validarEliminacion() throws ReglaNegocioException {
        throw new ReglaNegocioException("No puede eliminar a un alumno ya matriculado");
    }
}

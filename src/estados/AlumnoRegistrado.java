package estados;

import clases.EstadoAlumno;
import mediador.ReglaNegocioException;

/** Alumno sin matrícula activa. */
public final class AlumnoRegistrado implements EstadoDelAlumno {
    static final AlumnoRegistrado INSTANCIA = new AlumnoRegistrado();   // sin datos: se comparte
    private AlumnoRegistrado() { }

    @Override public EstadoAlumno getEstado() { return EstadoAlumno.REGISTRADO; }
    @Override public EstadoDelAlumno matricular() { return AlumnoMatriculado.INSTANCIA; }
    @Override public EstadoDelAlumno retirar() throws ReglaNegocioException {
        throw new ReglaNegocioException("El alumno no se encuentra matriculado");
    }
    @Override public EstadoDelAlumno anularMatricula() throws ReglaNegocioException {
        throw new ReglaNegocioException("El alumno no tiene una matrícula vigente");
    }
    @Override public EstadoDelAlumno anularRetiro() { return AlumnoMatriculado.INSTANCIA; }
    @Override public void validarEliminacion() { /* permitido */ }
}

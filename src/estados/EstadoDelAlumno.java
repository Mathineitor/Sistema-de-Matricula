package estados;

import clases.EstadoAlumno;
import mediador.ReglaNegocioException;

/**
 * PATRÓN STATE (Comportamiento).
 * Cada estado del alumno decide qué transiciones permite. Los servicios ya no tienen
 * cadenas de if sobre enteros: le piden al estado la siguiente situación o una excepción.
 */
public interface EstadoDelAlumno {
    EstadoAlumno getEstado();

    EstadoDelAlumno matricular() throws ReglaNegocioException;
    EstadoDelAlumno retirar() throws ReglaNegocioException;
    EstadoDelAlumno anularMatricula() throws ReglaNegocioException;
    EstadoDelAlumno anularRetiro();
    void validarEliminacion() throws ReglaNegocioException;

    static EstadoDelAlumno desde(int valorBD) {
        return desde(EstadoAlumno.desde(valorBD));
    }

    static EstadoDelAlumno desde(EstadoAlumno estado) {
        switch (estado) {
            case REGISTRADO:  return AlumnoRegistrado.INSTANCIA;
            case MATRICULADO: return AlumnoMatriculado.INSTANCIA;
            default:          return AlumnoRetirado.INSTANCIA;
        }
    }
}

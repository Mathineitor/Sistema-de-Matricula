package mediador;

/** Se lanza cuando una operación viola una regla del negocio (no es un fallo técnico). */
public class ReglaNegocioException extends Exception {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}

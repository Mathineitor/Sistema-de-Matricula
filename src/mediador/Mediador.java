package mediador;

/** Interfaz Mediador: único punto de comunicación entre los Colegas. */
public interface Mediador {
    void registrar(Colega colega);
    void eliminar(Colega colega);
    void notificar(Colega emisor, Evento evento, Object datos);
}

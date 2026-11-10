package mediador;

/** Interfaz Colega: cualquier componente que habla SOLO con el Mediador. */
public interface Colega {
    void setMediador(Mediador mediador);
    void recibir(Evento evento, Object datos);
}

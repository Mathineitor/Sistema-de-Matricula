package auditoria;

import clases.Calendario;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * PATRÓN SINGLETON (Creacional).
 * Una única bitácora en memoria para toda la aplicación, segura entre hilos
 * (Initialization-on-demand holder: la instancia se crea perezosamente y sin bloqueos).
 */
public final class Bitacora {
    private static final int CAPACIDAD = 500;

    private final Deque<String> entradas = new ArrayDeque<>();

    private Bitacora() { }                                   // nadie más puede instanciarla

    private static final class Holder {
        private static final Bitacora INSTANCIA = new Bitacora();
    }

    public static Bitacora getInstancia() {
        return Holder.INSTANCIA;
    }

    public synchronized void registrar(String nivel, String mensaje) {
        String linea = "[" + Calendario.fechaActual() + " " + Calendario.horaActual() + "] "
                     + "[" + nivel + "] " + mensaje;
        if (entradas.size() == CAPACIDAD) entradas.removeFirst();   // buffer circular
        entradas.addLast(linea);
        System.out.println(linea);
    }

    /** Últimas n entradas (copia inmutable en la práctica: el llamador no toca el estado interno). */
    public synchronized List<String> ultimas(int n) {
        List<String> todas = new ArrayList<>(entradas);
        return new ArrayList<>(todas.subList(Math.max(0, todas.size() - n), todas.size()));
    }

    public synchronized void limpiar() {
        entradas.clear();
    }
}

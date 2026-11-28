package clases;

/** Ciclos academicos (indice 0 = Primero ... 9 = Decimo). Unica fuente de verdad para toda la app. */
public final class Ciclo {
    private static final String[] NOMBRES = {
        "Primero", "Segundo", "Tercero", "Cuarto", "Quinto",
        "Sexto", "S\u00e9ptimo", "Octavo", "Noveno", "D\u00e9cimo"
    };

    private Ciclo() { }

    public static int total() { return NOMBRES.length; }

    public static String[] nombres() { return NOMBRES.clone(); }

    public static boolean esValido(int i) { return i >= 0 && i < NOMBRES.length; }

    public static String nombre(int i) { return esValido(i) ? NOMBRES[i] : "-"; }
}

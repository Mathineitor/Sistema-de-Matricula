package clases;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Utilidades de texto: normalizacion de nombres y claves de busqueda. */
public final class Texto {
    private static final Set<String> CONECTORES = new HashSet<>(Arrays.asList(
        "de", "del", "la", "las", "los", "el", "y", "e", "o", "u", "a", "al", "en", "con", "para", "por"));
    private static final Set<String> ROMANOS = new HashSet<>(Arrays.asList(
        "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x"));

    private Texto() { }

    /** Quita espacios sobrantes (inicio, fin y dobles). */
    public static String limpiar(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ");
    }

    /** "jUAN de la CRUZ" -> "Juan de la Cruz" (nombres y apellidos). */
    public static String titulo(String s) {
        return convertir(s, false);
    }

    /** Igual que titulo(), pero respeta numeros romanos: "calculo ii" -> "Calculo II". */
    public static String tituloCurso(String s) {
        return convertir(s, true);
    }

    /** Clave para buscar: sin tildes y en minusculas. */
    public static String clave(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}", "").toLowerCase();
    }

    private static String convertir(String s, boolean romanos) {
        String[] palabras = limpiar(s).toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < palabras.length; i++) {
            String w = palabras[i];
            if (w.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            if (i > 0 && CONECTORES.contains(w)) sb.append(w);
            else if (romanos && i > 0 && ROMANOS.contains(w)) sb.append(w.toUpperCase());
            else sb.append(capitalizar(w));
        }
        return sb.toString();
    }

    private static String capitalizar(String w) {
        StringBuilder sb = new StringBuilder(w.length());
        boolean inicio = true;
        for (char c : w.toCharArray()) {
            sb.append(inicio ? Character.toUpperCase(c) : c);
            inicio = (c == '-' || c == '\'' || c == '(');
        }
        return sb.toString();
    }
}

package gui;

import clases.Texto;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Lista desplegable con busqueda: se escribe parte del nombre (sin importar mayusculas ni tildes)
 * y la lista se filtra. Evita tener que memorizar codigos.
 */
public class ComboBuscador<T> extends JComboBox<ComboBuscador.Opcion<T>> {

    /** Elemento de la lista: guarda el objeto real y el texto que se muestra. */
    public static final class Opcion<T> {
        final T valor;
        final String texto;
        final String clave;

        Opcion(T valor, String texto) {
            this.valor = valor;
            this.texto = texto;
            this.clave = Texto.clave(texto);
        }

        @Override public String toString() { return texto; }
    }

    private final Function<T, String> textoDe;
    private final List<Opcion<T>> todas = new ArrayList<>();
    private final JTextField editor;
    private Consumer<T> alSeleccionar;
    private boolean ajustando;

    public ComboBuscador(Function<T, String> textoDe) {
        this.textoDe = textoDe;
        setEditable(true);
        setMaximumRowCount(8);
        editor = (JTextField) getEditor().getEditorComponent();

        setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foc) {
                Component c = super.getListCellRendererComponent(l, v, i, sel, foc);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return c;
            }
        });

        editor.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: case KeyEvent.VK_DOWN: case KeyEvent.VK_LEFT: case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_TAB: case KeyEvent.VK_ESCAPE: case KeyEvent.VK_SHIFT: case KeyEvent.VK_CONTROL:
                    case KeyEvent.VK_ALT: case KeyEvent.VK_HOME: case KeyEvent.VK_END:
                        return;
                    case KeyEvent.VK_ENTER:
                        resolverTexto();
                        return;
                    default:
                        filtrar(editor.getText());
                }
            }
        });
        editor.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() { public void run() { editor.selectAll(); } });
            }
            @Override public void focusLost(FocusEvent e) { resolverTexto(); }
        });
        addItemListener(new java.awt.event.ItemListener() {
            @Override public void itemStateChanged(ItemEvent e) {
                if (ajustando || e.getStateChange() != ItemEvent.SELECTED) return;
                Object o = e.getItem();
                if (o instanceof Opcion && alSeleccionar != null) {
                    @SuppressWarnings("unchecked") Opcion<T> op = (Opcion<T>) o;
                    alSeleccionar.accept(op.valor);
                }
            }
        });
    }

    /** Carga la lista completa (reemplaza lo anterior). */
    public void setItems(List<T> items) {
        todas.clear();
        for (T t : items) todas.add(new Opcion<>(t, textoDe.apply(t)));
        limpiar();
    }

    public void setAlSeleccionar(Consumer<T> accion) { this.alSeleccionar = accion; }

    /** Objeto elegido o null si no hay una seleccion valida. */
    @SuppressWarnings("unchecked")
    public T getSeleccionado() {
        Object o = getSelectedItem();
        if (o instanceof Opcion) return ((Opcion<T>) o).valor;
        return null;
    }

    /** Selecciona el primer elemento que cumpla la condicion. */
    public void seleccionarSi(Predicate<T> condicion) {
        for (Opcion<T> op : todas) {
            if (condicion.test(op.valor)) {
                ajustando = true;
                restaurar();
                setSelectedItem(op);
                ajustando = false;
                if (alSeleccionar != null) alSeleccionar.accept(op.valor);
                return;
            }
        }
    }

    /** Quita la seleccion y vuelve a mostrar todos los elementos. */
    public void limpiar() {
        ajustando = true;
        restaurar();
        setSelectedItem(null);
        editor.setText("");
        ajustando = false;
    }

    @Override public void setEnabled(boolean b) {
        super.setEnabled(b);
        editor.setEnabled(b);
        editor.setEditable(b);
    }

    private void restaurar() {
        DefaultComboBoxModel<Opcion<T>> m = new DefaultComboBoxModel<>();
        for (Opcion<T> op : todas) m.addElement(op);
        setModel(m);
    }

    private void filtrar(String texto) {
        ajustando = true;
        String q = Texto.clave(texto.trim());
        DefaultComboBoxModel<Opcion<T>> m = new DefaultComboBoxModel<>();
        for (Opcion<T> op : todas) if (op.clave.contains(q)) m.addElement(op);
        setModel(m);
        setSelectedItem(null);
        editor.setText(texto);
        editor.setCaretPosition(texto.length());
        ajustando = false;
        if (isShowing() && m.getSize() > 0) setPopupVisible(true); else setPopupVisible(false);
    }

    /** Si el usuario escribio sin elegir y solo queda una coincidencia, la selecciona. */
    private void resolverTexto() {
        if (getSelectedItem() instanceof Opcion) return;
        String q = Texto.clave(editor.getText().trim());
        if (q.isEmpty()) return;
        Opcion<T> unica = null;
        int n = 0;
        for (Opcion<T> op : todas) if (op.clave.contains(q)) { unica = op; n++; }
        if (n == 1) {
            final Opcion<T> elegida = unica;
            seleccionarSi(new Predicate<T>() { public boolean test(T t) { return t == elegida.valor; } });
            setPopupVisible(false);
        }
    }
}

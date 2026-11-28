package gui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.*;

/**
 * Tema visual de toda la aplicacion: paleta, tipografia, botones, campos, tablas y
 * encabezado de las ventanas. Un solo lugar para cambiar el aspecto del sistema.
 */
public final class Tema {

    // ---- Paleta ----------------------------------------------------------
    public static final Color FONDO        = new Color(0xF3F5F9);
    public static final Color TARJETA      = Color.WHITE;
    public static final Color TEXTO        = new Color(0x1F2937);
    public static final Color TEXTO_SUAVE  = new Color(0x6B7280);
    public static final Color BORDE        = new Color(0xD5DAE3);
    public static final Color CAMPO_BLOQ   = new Color(0xF1F3F7);
    public static final Color PRIMARIO     = new Color(0x2563EB);
    public static final Color EXITO        = new Color(0x16A34A);
    public static final Color AVISO        = new Color(0xD97706);
    public static final Color PELIGRO      = new Color(0xDC2626);
    public static final Color SELECCION    = new Color(0xDBEAFE);
    public static final Color ZEBRA        = new Color(0xF8FAFC);
    public static final Color ENCABEZADO_1 = new Color(0x0F2A4A);
    public static final Color ENCABEZADO_2 = new Color(0x1D4F91);

    public static final String FAMILIA = elegirFamilia(new String[] {"Segoe UI", "Inter", "Noto Sans", "Ubuntu", "DejaVu Sans"}, Font.SANS_SERIF);
    public static final String FAMILIA_MONO = elegirFamilia(new String[] {"Consolas", "JetBrains Mono", "DejaVu Sans Mono"}, Font.MONOSPACED);

    private Tema() { }

    public static Font fuente(int estilo, float tam) {
        return new Font(FAMILIA, estilo, 1).deriveFont(estilo, tam);
    }

    private static String elegirFamilia(String[] candidatas, String porDefecto) {
        java.util.Set<String> disponibles = new java.util.HashSet<>(java.util.Arrays.asList(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String f : candidatas) if (disponibles.contains(f)) return f;
        return porDefecto;
    }

    // ---- Instalacion global ---------------------------------------------
    /** Llamar una sola vez, antes de crear cualquier ventana. */
    public static void instalar() {
        UIManager.put("nimbusBase", new Color(0x1D4F91));
        UIManager.put("nimbusBlueGrey", new Color(0x9AA6BC));
        UIManager.put("nimbusFocus", PRIMARIO);
        UIManager.put("nimbusSelectionBackground", PRIMARIO);
        UIManager.put("control", FONDO);
        UIManager.put("defaultFont", fuente(Font.PLAIN, 13));
        try {
            for (UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(i.getName())) { UIManager.setLookAndFeel(i.getClassName()); break; }
            }
        } catch (Exception ignorada) { /* se usa el aspecto por defecto */ }
        UIManager.put("OptionPane.yesButtonText", "S\u00ED");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.messageFont", fuente(Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", fuente(Font.PLAIN, 13));
    }

    // ---- Montaje de ventanas --------------------------------------------
    /** Ventana nueva: cuerpo disenado por nosotros, con encabezado. */
    public static void montar(JDialog d, String titulo, String subtitulo, JPanel cuerpo) {
        d.setTitle(titulo);
        cuerpo.setBackground(FONDO);
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(new Encabezado(titulo, subtitulo), BorderLayout.NORTH);
        raiz.add(cuerpo, BorderLayout.CENTER);
        d.setContentPane(raiz);
        estilizar(cuerpo);
        d.setResizable(false);
        d.pack();
        d.setLocationRelativeTo(null);
    }

    /** Ventana existente (posiciones fijas): agrega el encabezado y estiliza lo que ya tiene. */
    public static void aplicar(JDialog d, String titulo, String subtitulo) {
        d.setTitle(titulo);
        Container cuerpo = d.getContentPane();
        cuerpo.setBackground(FONDO);
        Dimension antes = d.getSize();
        JPanel raiz = new JPanel(new BorderLayout());
        Encabezado enc = new Encabezado(titulo, subtitulo);
        raiz.add(enc, BorderLayout.NORTH);
        raiz.add(cuerpo, BorderLayout.CENTER);
        d.setContentPane(raiz);
        estilizar(cuerpo);
        d.setResizable(false);
        d.setSize(antes.width, antes.height + enc.getPreferredSize().height);
    }

    // ---- Componentes propios --------------------------------------------
    static final class Encabezado extends JPanel {
        private final String titulo, subtitulo;

        Encabezado(String titulo, String subtitulo) {
            this.titulo = titulo;
            this.subtitulo = subtitulo;
            setPreferredSize(new Dimension(10, 70));
            setOpaque(true);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, ENCABEZADO_1, w, 0, ENCABEZADO_2));
            g2.fillRect(0, 0, w, h);
            g2.setColor(new Color(0x60A5FA));
            g2.fillRect(0, h - 3, w, 3);
            // insignia con la inicial
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRoundRect(22, 16, 40, 40, 12, 12);
            g2.setColor(Color.WHITE);
            g2.setFont(fuente(Font.BOLD, 20));
            String ini = titulo.isEmpty() ? "" : titulo.substring(0, 1).toUpperCase();
            FontMetrics fm0 = g2.getFontMetrics();
            g2.drawString(ini, 22 + (40 - fm0.stringWidth(ini)) / 2, 16 + (40 + fm0.getAscent() - fm0.getDescent()) / 2);
            g2.setFont(fuente(Font.BOLD, 19));
            g2.drawString(titulo, 78, 33);
            g2.setColor(new Color(0xBFD3F2));
            g2.setFont(fuente(Font.PLAIN, 12.5f));
            g2.drawString(subtitulo, 78, 53);
            g2.dispose();
        }
    }

    /** Panel blanco con bordes redondeados y titulo opcional. Se usa con posiciones fijas (layout nulo). */
    public static class Tarjeta extends JPanel {
        private final String titulo;

        public Tarjeta(String titulo) {
            super(null);
            this.titulo = titulo;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth() - 1, h = getHeight() - 1;
            g2.setColor(new Color(0, 0, 0, 14));
            g2.fillRoundRect(1, 2, w, h, 14, 14);
            g2.setColor(TARJETA);
            g2.fillRoundRect(0, 0, w, h - 1, 14, 14);
            g2.setColor(BORDE);
            g2.drawRoundRect(0, 0, w, h - 1, 14, 14);
            if (titulo != null) {
                g2.setColor(PRIMARIO);
                g2.fillRoundRect(16, 14, 4, 16, 4, 4);
                g2.setColor(TEXTO);
                g2.setFont(fuente(Font.BOLD, 14));
                g2.drawString(titulo, 28, 27);
            }
            g2.dispose();
        }
    }

    /** Etiqueta pequena y gris para encabezar un campo. */
    public static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente(Font.BOLD, 12));
        l.setForeground(TEXTO_SUAVE);
        return l;
    }

    /** Campo de solo lectura (valores calculados). */
    public static JTextField campoLectura() {
        JTextField t = new JTextField();
        t.setEditable(false);
        return t;
    }

    // ---- Estilizado recursivo -------------------------------------------
    public static void estilizar(Component c) {
        if (c instanceof JButton)            estilizarBoton((JButton) c);
        else if (c instanceof JTextField)    estilizarCampo((JTextField) c);
        else if (c instanceof JTextArea)     estilizarArea((JTextArea) c);
        else if (c instanceof JComboBox)     estilizarCombo((JComboBox<?>) c);
        else if (c instanceof JLabel)        estilizarEtiqueta((JLabel) c);
        else if (c instanceof JTable)        estilizarTabla((JTable) c);
        else if (c instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) c;
            sp.setBorder(new LineBorder(BORDE));
            sp.getViewport().setBackground(Color.WHITE);
            if (sp.getViewport().getView() != null) estilizar(sp.getViewport().getView());
        } else if (c instanceof Container) {
            for (Component hijo : ((Container) c).getComponents()) estilizar(hijo);
        }
    }

    private static boolean esDeSistema(Font f) {
        return f == null || f instanceof UIResource || "Tahoma".equals(f.getFamily());
    }

    static void estilizarEtiqueta(JLabel l) {
        if (l.getText() == null || l.getText().isEmpty()) return;
        if (esDeSistema(l.getFont())) l.setFont(fuente(l.getFont().isBold() ? Font.BOLD : Font.PLAIN, 13));
        if (l.getForeground() == null || l.getForeground() instanceof UIResource) l.setForeground(TEXTO);
    }

    static void estilizarCombo(JComboBox<?> cbo) {
        if (esDeSistema(cbo.getFont())) cbo.setFont(fuente(Font.PLAIN, 13));
    }

    static void estilizarArea(JTextArea t) {
        t.setFont(new Font(FAMILIA_MONO, Font.PLAIN, 13));
        t.setForeground(TEXTO);
        t.setBackground(Color.WHITE);
        t.setMargin(new Insets(12, 16, 12, 16));
        t.setEditable(false);
    }

    static void estilizarCampo(final JTextField t) {
        if (esDeSistema(t.getFont())) t.setFont(fuente(Font.PLAIN, 13));
        t.setForeground(TEXTO);
        t.setCaretColor(PRIMARIO);
        aplicarBordeCampo(t, false);
        actualizarFondoCampo(t);
        t.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { aplicarBordeCampo(t, true); }
            @Override public void focusLost(FocusEvent e)   { aplicarBordeCampo(t, false); }
        });
        PropertyChangeListener oyente = new PropertyChangeListener() {
            @Override public void propertyChange(PropertyChangeEvent e) { actualizarFondoCampo(t); }
        };
        t.addPropertyChangeListener("editable", oyente);
        t.addPropertyChangeListener("enabled", oyente);
    }

    private static void aplicarBordeCampo(JTextField t, boolean foco) {
        t.setBorder(new CompoundBorder(
            new LineBorder(foco ? PRIMARIO : BORDE, 1, true),
            new EmptyBorder(3, 8, 3, 8)));
    }

    private static void actualizarFondoCampo(JTextField t) {
        t.setBackground(t.isEditable() && t.isEnabled() ? Color.WHITE : CAMPO_BLOQ);
    }

    // ---- Botones ----------------------------------------------------------
    private enum Rol {
        PRIMARIO(0x2563EB, 0x1D4ED8, 0x1E40AF), EXITO(0x16A34A, 0x15803D, 0x166534),
        AVISO(0xD97706, 0xB45309, 0x92400E), PELIGRO(0xDC2626, 0xB91C1C, 0x991B1B), NEUTRO(0xFFFFFF, 0xF3F4F6, 0xE5E7EB);
        final Color base, sobre, presionado;
        Rol(int b, int s, int p) { base = new Color(b); sobre = new Color(s); presionado = new Color(p); }
    }

    static void estilizarBoton(JButton b) {
        String t = b.getText() == null ? "" : b.getText().toLowerCase();
        Rol rol = Rol.NEUTRO;
        if (t.startsWith("adicionar") || t.startsWith("nuevo")) rol = Rol.EXITO;
        else if (t.startsWith("modificar"))                      rol = Rol.AVISO;
        else if (t.startsWith("eliminar"))                       rol = Rol.PELIGRO;
        else if (t.startsWith("aceptar") || t.startsWith("consultar") || t.startsWith("listar")) rol = Rol.PRIMARIO;
        b.setUI(new BotonUI(rol));
        b.setFont(fuente(Font.BOLD, 13));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static final class BotonUI extends BasicButtonUI {
        private final Rol rol;
        BotonUI(Rol rol) { this.rol = rol; }

        @Override public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = c.getWidth(), h = c.getHeight();
            Color fondo, texto;
            if (!b.isEnabled()) { fondo = new Color(0xE5E7EB); texto = new Color(0x9CA3AF); }
            else {
                fondo = m.isPressed() ? rol.presionado : m.isRollover() ? rol.sobre : rol.base;
                texto = rol == Rol.NEUTRO ? TEXTO : Color.WHITE;
            }
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
            if (rol == Rol.NEUTRO && b.isEnabled()) {
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
            }
            g2.setFont(b.getFont());
            FontMetrics fm = g2.getFontMetrics();
            String t = b.getText() == null ? "" : b.getText();
            g2.setColor(texto);
            g2.drawString(t, (w - fm.stringWidth(t)) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    // ---- Tablas -----------------------------------------------------------
    public static void estilizarTabla(final JTable t) {
        t.setFont(fuente(Font.PLAIN, 13));
        t.setRowHeight(28);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFillsViewportHeight(true);
        t.setSelectionBackground(SELECCION);
        t.setSelectionForeground(TEXTO);
        t.setForeground(TEXTO);
        t.setBackground(Color.WHITE);

        DefaultTableCellRenderer celdas = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int fila, int col) {
                super.getTableCellRendererComponent(tb, v, sel, false, fila, col);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setBackground(sel ? SELECCION : (fila % 2 == 0 ? Color.WHITE : ZEBRA));
                setForeground(TEXTO);
                setHorizontalAlignment(v instanceof Number ? SwingConstants.CENTER : SwingConstants.LEFT);
                return this;
            }
        };
        t.setDefaultRenderer(Object.class, celdas);
        t.setDefaultRenderer(Number.class, celdas);
        t.setDefaultRenderer(Integer.class, celdas);

        // las columnas se reparten solas segun su contenido y el ancho disponible
        t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        t.getModel().addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override public void tableChanged(javax.swing.event.TableModelEvent e) {
                SwingUtilities.invokeLater(new Runnable() { public void run() { ajustarColumnas(t); } });
            }
        });
        if (t.getParent() instanceof JViewport) {
            t.getParent().addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override public void componentResized(java.awt.event.ComponentEvent e) { ajustarColumnas(t); }
            });
        }

        JTableHeader cab = t.getTableHeader();
        cab.setReorderingAllowed(false);
        cab.setPreferredSize(new Dimension(10, 32));
        cab.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int fila, int col) {
                super.getTableCellRendererComponent(tb, v, false, false, fila, col);
                setOpaque(true);
                setBackground(ENCABEZADO_1);
                setForeground(Color.WHITE);
                setFont(fuente(Font.BOLD, 12));
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 1, new Color(0x274A78)), new EmptyBorder(0, 10, 0, 10)));
                return this;
            }
        });
    }

    /** Da a cada columna el ancho que necesita su contenido y reparte el espacio que sobre. */
    public static void ajustarColumnas(JTable t) {
        int n = t.getColumnCount();
        if (n == 0 || !(t.getParent() instanceof JViewport)) return;
        int disponible = t.getParent().getWidth();
        if (disponible <= 0) return;
        FontMetrics fc = t.getFontMetrics(t.getFont());
        FontMetrics fh = t.getFontMetrics(fuente(Font.BOLD, 12));
        int[] necesario = new int[n];
        int total = 0;
        for (int c = 0; c < n; c++) {
            int w = fh.stringWidth(String.valueOf(t.getColumnName(c))) + 28;
            for (int r = 0; r < Math.min(t.getRowCount(), 300); r++) {
                Object v = t.getValueAt(r, c);
                w = Math.max(w, fc.stringWidth(v == null ? "" : v.toString()) + 28);
            }
            necesario[c] = w;
            total += w;
        }
        double factor = (double) disponible / total;      // >1 reparte lo que sobra, <1 comprime parejo
        int usado = 0;
        for (int c = 0; c < n; c++) {
            int w = (c == n - 1) ? disponible - usado : (int) Math.floor(necesario[c] * factor);
            t.getColumnModel().getColumn(c).setPreferredWidth(w);
            t.getColumnModel().getColumn(c).setWidth(w);
            usado += w;
        }
    }
}

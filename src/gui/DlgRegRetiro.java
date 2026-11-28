package gui;

import clases.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import mediador.*;
import servicios.ConsultasFacade;
import servicios.ConsultasFacade.FichaMatricula;
import servicios.ConsultasFacade.FichaRetiro;

/** Registro de retiros. La matricula se busca escribiendo el nombre del alumno. */
public class DlgRegRetiro extends JDialog implements ActionListener, MouseListener, Colega {

    private final ConsultasFacade consultas = new ConsultasFacade();
    private Mediador mediador;
    private List<FichaMatricula> fichas = new ArrayList<>();
    private List<FichaRetiro> retiros = new ArrayList<>();

    private JComboBox<Integer> cboNumRetiro;
    private ComboBuscador<FichaMatricula> cboMatricula;
    private ComboBuscador<Curso> cboCurso;
    private JTextField txtNumMatricula, txtVigente, txtCodCurso, txtEstado;
    private JButton btnAceptar, btnCancelar, btnAdicionar, btnModificar, btnEliminar;
    private JTable tblRetiro;
    private DefaultTableModel modelo;

    public DlgRegRetiro() {
        setModal(true);
        JPanel cuerpo = new JPanel(null);
        cuerpo.setPreferredSize(new Dimension(960, 580));

        Tema.Tarjeta datos = new Tema.Tarjeta("Datos del retiro");
        datos.setBounds(20, 20, 700, 276);
        cuerpo.add(datos);

        JLabel pista = Tema.etiqueta("Escribe el nombre del alumno");
        pista.setBounds(480, 14, 200, 18);
        datos.add(pista);

        colocar(datos, Tema.etiqueta("N.\u00ba de retiro"), 24, 46, 190, 16);
        cboNumRetiro = new JComboBox<Integer>();
        cboNumRetiro.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) cargarDesdeNumero();
        });
        colocar(datos, cboNumRetiro, 24, 64, 190, 30);

        colocar(datos, Tema.etiqueta("Alumno (matr\u00edcula)"), 24, 106, 240, 16);
        cboMatricula = new ComboBuscador<FichaMatricula>(f ->
            f.getAlumno().getNombres() + " " + f.getAlumno().getApellidos() + "  \u00b7  Mat. " + f.getMatricula().getNumeroMatricula());
        cboMatricula.setAlSeleccionar(this::mostrarMatricula);
        colocar(datos, cboMatricula, 24, 124, 420, 30);
        colocar(datos, Tema.etiqueta("N.\u00ba matr\u00edcula"), 460, 106, 100, 16);
        txtNumMatricula = Tema.campoLectura();
        colocar(datos, txtNumMatricula, 460, 124, 100, 30);
        colocar(datos, Tema.etiqueta("Vigente"), 576, 106, 100, 16);
        txtVigente = Tema.campoLectura();
        colocar(datos, txtVigente, 576, 124, 100, 30);

        colocar(datos, Tema.etiqueta("Curso"), 24, 166, 200, 16);
        cboCurso = new ComboBuscador<Curso>(c -> c.getAsignatura() + "  \u00b7  " + c.getCodCurso());
        cboCurso.setAlSeleccionar(c -> txtCodCurso.setText(c == null ? "" : String.valueOf(c.getCodCurso())));
        colocar(datos, cboCurso, 24, 184, 420, 30);
        colocar(datos, Tema.etiqueta("C\u00f3digo"), 460, 166, 100, 16);
        txtCodCurso = Tema.campoLectura();
        colocar(datos, txtCodCurso, 460, 184, 100, 30);
        colocar(datos, Tema.etiqueta("Estado"), 576, 166, 100, 16);
        txtEstado = Tema.campoLectura();
        colocar(datos, txtEstado, 576, 184, 100, 30);

        btnAceptar = boton("Aceptar", this);
        colocar(datos, btnAceptar, 24, 228, 140, 34);
        btnCancelar = boton("Cancelar", this);
        colocar(datos, btnCancelar, 176, 228, 140, 34);

        Tema.Tarjeta acciones = new Tema.Tarjeta("Acciones");
        acciones.setBounds(740, 20, 200, 276);
        cuerpo.add(acciones);
        btnAdicionar = boton("Adicionar", this);
        colocar(acciones, btnAdicionar, 20, 54, 160, 42);
        btnModificar = boton("Modificar", this);
        colocar(acciones, btnModificar, 20, 110, 160, 42);
        btnEliminar = boton("Eliminar", this);
        colocar(acciones, btnEliminar, 20, 166, 160, 42);

        Tema.Tarjeta listado = new Tema.Tarjeta("Retiros registrados");
        listado.setBounds(20, 312, 920, 248);
        cuerpo.add(listado);
        modelo = new DefaultTableModel(new Object[] {"N.\u00ba retiro", "N.\u00ba mat.", "Nombres", "Apellidos",
                "Asignatura", "Fecha", "Hora"}, 0) {
            @Override public boolean isCellEditable(int f, int c) { return false; }
        };
        tblRetiro = new JTable(modelo);
        tblRetiro.addMouseListener(this);
        JScrollPane sp = new JScrollPane(tblRetiro);
        sp.setBounds(12, 40, 896, 196);
        listado.add(sp);

        Tema.montar(this, "Registro de retiros", "Registra el retiro de alumnos matriculados", cuerpo);
        listar();
        deshabilitarTodo();
    }

    private static void colocar(JPanel p, JComponent c, int x, int y, int w, int h) {
        c.setBounds(x, y, w, h);
        p.add(c);
    }

    private static JButton boton(String texto, ActionListener oyente) {
        JButton b = new JButton(texto);
        b.addActionListener(oyente);
        return b;
    }

    // ---- Colega ------------------------------------------------------------
    @Override public void setMediador(Mediador mediador) { this.mediador = mediador; }

    @Override public void recibir(Evento evento, Object datos) {
        switch (evento) {
            case DATOS_ACTUALIZADOS:
                listar();
                break;
            case OPERACION_EXITOSA:
                JOptionPane.showMessageDialog(this, (String) datos, "Listo", JOptionPane.INFORMATION_MESSAGE);
                deshabilitarTodo();
                break;
            case OPERACION_RECHAZADA:
            case ERROR_TECNICO:
                JOptionPane.showMessageDialog(this, (String) datos, "No se pudo completar", JOptionPane.WARNING_MESSAGE);
                cboMatricula.requestFocus();
                break;
            default:
                break;
        }
    }

    // ---- Seleccion ---------------------------------------------------------
    private void mostrarMatricula(final FichaMatricula f) {
        if (f == null) { txtNumMatricula.setText(""); txtVigente.setText(""); txtEstado.setText(""); return; }
        Alumno a = f.getAlumno();
        txtNumMatricula.setText(String.valueOf(f.getMatricula().getNumeroMatricula()));
        txtVigente.setText(a.getEstado() == EstadoAlumno.MATRICULADO.getValor() ? "S\u00ed" : "No");
        txtEstado.setText(Texto.titulo(EstadoAlumno.desde(a.getEstado()).name()));
        cboCurso.seleccionarSi(c -> c.getCodCurso() == f.getCurso().getCodCurso());
    }

    private void cargarDesdeNumero() {
        Object item = cboNumRetiro.getSelectedItem();
        if (item == null) return;
        final int num = (Integer) item;
        for (final FichaRetiro r : retiros) {
            if (r.getRetiro().getNumRetiro() == num) {
                cboMatricula.seleccionarSi(f -> f.getMatricula().getNumeroMatricula() == r.getFicha().getMatricula().getNumeroMatricula());
                return;
            }
        }
    }

    // ---- Botones -----------------------------------------------------------
    @Override public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnAceptar)        aceptar();
        else if (o == btnCancelar)  deshabilitarTodo();
        else if (o == btnAdicionar) adicionar();
        else if (o == btnModificar) modificar();
        else if (o == btnEliminar)  eliminar();
    }

    private void adicionar() {
        limpiar();
        // al retirar solo tienen sentido los alumnos que hoy estan matriculados
        List<FichaMatricula> vigentes = new ArrayList<>();
        for (FichaMatricula f : fichas)
            if (f.getAlumno().getEstado() == EstadoAlumno.MATRICULADO.getValor()) vigentes.add(f);
        cboMatricula.setItems(ordenar(vigentes));
        habilitar(false, true, false, true, true, false, true, false);
        cboMatricula.requestFocus();
    }

    private void modificar() {
        if (!btnAdicionar.isEnabled()) deshabilitarTodo();
        habilitar(true, false, true, true, true, true, false, false);
        cboNumRetiro.requestFocus();
    }

    private void aceptar() {
        if (!btnAdicionar.isEnabled()) {
            FichaMatricula f = cboMatricula.getSeleccionado();
            if (f == null) { error("Seleccione un alumno de la lista", cboMatricula); return; }
            // 0 = marcador: MySQL asigna el numero real
            mediador.notificar(this, Evento.RETIRO_ADICIONAR,
                new Retiro(0, f.getMatricula().getNumeroMatricula(), Calendario.fechaActual(), Calendario.horaActual()));
        } else if (!btnModificar.isEnabled()) {
            Object num = cboNumRetiro.getSelectedItem();
            Curso c = cboCurso.getSeleccionado();
            if (num == null) { error("Seleccione un n\u00famero de retiro", cboNumRetiro); return; }
            if (c == null) { error("Seleccione un curso de la lista", cboCurso); return; }
            mediador.notificar(this, Evento.RETIRO_MODIFICAR, new CambioCurso((Integer) num, c.getCodCurso()));
        }
    }

    private void eliminar() {
        Object num = cboNumRetiro.getSelectedItem();
        if (num == null) { error("Seleccione un retiro en la tabla", cboNumRetiro); return; }
        FichaMatricula f = cboMatricula.getSeleccionado();
        int r = JOptionPane.showConfirmDialog(this,
            "\u00bfEst\u00e1 seguro de eliminar este retiro?\nAlumno: "
                + (f == null ? "-" : f.getAlumno().getNombres() + " " + f.getAlumno().getApellidos())
                + "\nCurso: " + (f == null ? "-" : f.getCurso().getAsignatura()),
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) mediador.notificar(this, Evento.RETIRO_ELIMINAR, (Integer) num);
    }

    // ---- Estado de la pantalla -------------------------------------------
    private void habilitar(boolean numRetiro, boolean matricula, boolean curso, boolean aceptar, boolean cancelar,
                           boolean adicionar, boolean modificar, boolean eliminar) {
        cboNumRetiro.setEnabled(numRetiro);
        cboMatricula.setEnabled(matricula);
        cboCurso.setEnabled(curso);
        btnAceptar.setEnabled(aceptar);
        btnCancelar.setEnabled(cancelar);
        btnAdicionar.setEnabled(adicionar);
        btnModificar.setEnabled(modificar);
        btnEliminar.setEnabled(eliminar);
    }

    private void deshabilitarTodo() {
        cargarListas();
        habilitar(false, false, false, false, false, true, true, true);
        limpiar();
    }

    private void limpiar() {
        cboNumRetiro.setSelectedIndex(-1);
        cboMatricula.limpiar();
        cboCurso.limpiar();
        mostrarMatricula(null);
        txtCodCurso.setText("");
    }

    private List<FichaMatricula> ordenar(List<FichaMatricula> lista) {
        lista.sort(Comparator.comparing(f -> Texto.clave(f.getAlumno().getNombres() + " " + f.getAlumno().getApellidos())));
        return lista;
    }

    private void cargarListas() {
        try {
            cboNumRetiro.removeAllItems();
            retiros = consultas.fichasRetiros();
            for (FichaRetiro r : retiros) cboNumRetiro.addItem(r.getRetiro().getNumRetiro());
            fichas = consultas.fichasMatriculas();
            cboMatricula.setItems(ordenar(new ArrayList<>(fichas)));
            List<Curso> cursos = consultas.cursos();
            cursos.sort(Comparator.comparing(c -> Texto.clave(c.getAsignatura())));
            cboCurso.setItems(cursos);
        } catch (Exception ex) {
            mensaje("No se pudieron cargar los datos");
        }
        cboNumRetiro.setSelectedIndex(-1);
    }

    private void listar() {
        modelo.setRowCount(0);
        try {
            retiros = consultas.fichasRetiros();
            for (FichaRetiro fr : retiros) {
                Retiro r = fr.getRetiro();
                Alumno a = fr.getFicha().getAlumno();
                modelo.addRow(new Object[] {r.getNumRetiro(), r.getNumMatricula(), a.getNombres(), a.getApellidos(),
                    fr.getFicha().getCurso().getAsignatura(), r.getFecha(), r.getHora()});
            }
        } catch (Exception ex) {
            mensaje("Error listando retiros");
        }
    }

    private void mensaje(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    private void error(String s, JComponent c) {
        JOptionPane.showMessageDialog(this, s, "Atenci\u00f3n", JOptionPane.WARNING_MESSAGE);
        c.requestFocus();
    }

    // ---- Tabla -------------------------------------------------------------
    @Override public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tblRetiro && btnAdicionar.isEnabled()) {
            int fila = tblRetiro.getSelectedRow();
            if (fila >= 0) cboNumRetiro.setSelectedItem(tblRetiro.getValueAt(fila, 0));
        }
    }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
}

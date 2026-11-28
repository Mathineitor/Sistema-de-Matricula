package gui;

import clases.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import mediador.*;
import servicios.ConsultasFacade;
import servicios.ConsultasFacade.FichaMatricula;

/** Registro de matriculas. El alumno y el curso se buscan escribiendo su nombre. */
public class DlgRegMatricula extends JDialog implements ActionListener, MouseListener, Colega {

    private final ConsultasFacade consultas = new ConsultasFacade();
    private Mediador mediador;
    private List<FichaMatricula> fichas;

    private JComboBox<Integer> cboNumMatricula;
    private ComboBuscador<Alumno> cboAlumno;
    private ComboBuscador<Curso> cboCurso;
    private JTextField txtCodAlumno, txtEstadoAlumno, txtCodCurso, txtCiclo;
    private JButton btnAceptar, btnCancelar, btnAdicionar, btnModificar, btnEliminar;
    private JTable tblMatricula;
    private DefaultTableModel modelo;

    public DlgRegMatricula() {
        setModal(true);
        JPanel cuerpo = new JPanel(null);
        cuerpo.setPreferredSize(new Dimension(960, 580));

        // ---- Tarjeta de datos ------------------------------------------
        Tema.Tarjeta datos = new Tema.Tarjeta("Datos de la matr\u00edcula");
        datos.setBounds(20, 20, 700, 276);
        cuerpo.add(datos);

        JLabel pista = Tema.etiqueta("Escribe parte del nombre para buscar");
        pista.setBounds(440, 14, 240, 18);
        datos.add(pista);

        colocar(datos, Tema.etiqueta("N.\u00ba de matr\u00edcula"), 24, 46, 190, 16);
        cboNumMatricula = new JComboBox<Integer>();
        cboNumMatricula.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) cargarDesdeNumero();
        });
        colocar(datos, cboNumMatricula, 24, 64, 190, 30);

        colocar(datos, Tema.etiqueta("Alumno"), 24, 106, 200, 16);
        cboAlumno = new ComboBuscador<Alumno>(a -> a.getNombres() + " " + a.getApellidos() + "  \u00b7  " + a.getCodAlumno());
        cboAlumno.setAlSeleccionar(this::mostrarAlumno);
        colocar(datos, cboAlumno, 24, 124, 420, 30);
        colocar(datos, Tema.etiqueta("C\u00f3digo"), 460, 106, 100, 16);
        txtCodAlumno = Tema.campoLectura();
        colocar(datos, txtCodAlumno, 460, 124, 100, 30);
        colocar(datos, Tema.etiqueta("Estado"), 576, 106, 100, 16);
        txtEstadoAlumno = Tema.campoLectura();
        colocar(datos, txtEstadoAlumno, 576, 124, 100, 30);

        colocar(datos, Tema.etiqueta("Curso"), 24, 166, 200, 16);
        cboCurso = new ComboBuscador<Curso>(c -> c.getAsignatura() + "  \u00b7  " + c.getCodCurso());
        cboCurso.setAlSeleccionar(this::mostrarCurso);
        colocar(datos, cboCurso, 24, 184, 420, 30);
        colocar(datos, Tema.etiqueta("C\u00f3digo"), 460, 166, 100, 16);
        txtCodCurso = Tema.campoLectura();
        colocar(datos, txtCodCurso, 460, 184, 100, 30);
        colocar(datos, Tema.etiqueta("Ciclo"), 576, 166, 100, 16);
        txtCiclo = Tema.campoLectura();
        colocar(datos, txtCiclo, 576, 184, 100, 30);

        btnAceptar = boton("Aceptar", this);
        colocar(datos, btnAceptar, 24, 228, 140, 34);
        btnCancelar = boton("Cancelar", this);
        colocar(datos, btnCancelar, 176, 228, 140, 34);

        // ---- Tarjeta de acciones ---------------------------------------
        Tema.Tarjeta acciones = new Tema.Tarjeta("Acciones");
        acciones.setBounds(740, 20, 200, 276);
        cuerpo.add(acciones);
        btnAdicionar = boton("Adicionar", this);
        colocar(acciones, btnAdicionar, 20, 54, 160, 42);
        btnModificar = boton("Modificar", this);
        colocar(acciones, btnModificar, 20, 110, 160, 42);
        btnEliminar = boton("Eliminar", this);
        colocar(acciones, btnEliminar, 20, 166, 160, 42);

        // ---- Tabla ------------------------------------------------------
        Tema.Tarjeta listado = new Tema.Tarjeta("Matr\u00edculas registradas");
        listado.setBounds(20, 312, 920, 248);
        cuerpo.add(listado);
        modelo = new DefaultTableModel(new Object[] {"N.\u00ba", "C\u00f3d. alumno", "Nombres", "Apellidos",
                "C\u00f3d. curso", "Asignatura", "Fecha", "Hora", "Activo"}, 0) {
            @Override public boolean isCellEditable(int f, int c) { return false; }
        };
        tblMatricula = new JTable(modelo);
        tblMatricula.addMouseListener(this);
        JScrollPane sp = new JScrollPane(tblMatricula);
        sp.setBounds(12, 40, 896, 196);
        listado.add(sp);

        Tema.montar(this, "Registro de matr\u00edculas", "Inscribe alumnos en los cursos del ciclo", cuerpo);
        listar();
        deshabilitarTodo();
    }

    // ---- Utilidades de construccion --------------------------------------
    private static void colocar(JPanel p, JComponent c, int x, int y, int w, int h) {
        c.setBounds(x, y, w, h);
        p.add(c);
    }

    private static JButton boton(String texto, ActionListener oyente) {
        JButton b = new JButton(texto);
        b.addActionListener(oyente);
        return b;
    }

    // ---- Colega: comunicacion con el Mediador ----------------------------
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
                cboAlumno.requestFocus();
                break;
            default:
                break;
        }
    }

    // ---- Seleccion -------------------------------------------------------
    private void mostrarAlumno(Alumno a) {
        txtCodAlumno.setText(a == null ? "" : String.valueOf(a.getCodAlumno()));
        txtEstadoAlumno.setText(a == null ? "" : Texto.titulo(EstadoAlumno.desde(a.getEstado()).name()));
    }

    private void mostrarCurso(Curso c) {
        txtCodCurso.setText(c == null ? "" : String.valueOf(c.getCodCurso()));
        txtCiclo.setText(c == null ? "" : Ciclo.nombre(c.getCiclo()));
    }

    private void cargarDesdeNumero() {
        Object item = cboNumMatricula.getSelectedItem();
        if (item == null || fichas == null) return;
        final int num = (Integer) item;
        for (final FichaMatricula f : fichas) {
            if (f.getMatricula().getNumeroMatricula() == num) {
                cboAlumno.seleccionarSi(a -> a.getCodAlumno() == f.getAlumno().getCodAlumno());
                cboCurso.seleccionarSi(c -> c.getCodCurso() == f.getCurso().getCodCurso());
                return;
            }
        }
    }

    // ---- Botones ---------------------------------------------------------
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
        habilitar(false, true, true, true, true, false, true, false);
        cboAlumno.requestFocus();
    }

    private void modificar() {
        if (!btnAdicionar.isEnabled()) deshabilitarTodo();
        habilitar(true, false, true, true, true, true, false, false);
        cboNumMatricula.requestFocus();
    }

    private void aceptar() {
        Alumno a = cboAlumno.getSeleccionado();
        Curso c = cboCurso.getSeleccionado();
        if (!btnAdicionar.isEnabled()) {
            if (a == null) { error("Seleccione un alumno de la lista", cboAlumno); return; }
            if (c == null) { error("Seleccione un curso de la lista", cboCurso); return; }
            // 0 = marcador: MySQL asigna el numero real
            mediador.notificar(this, Evento.MATRICULA_ADICIONAR,
                new Matricula(0, a.getCodAlumno(), c.getCodCurso(), Calendario.fechaActual(), Calendario.horaActual()));
        } else if (!btnModificar.isEnabled()) {
            Object num = cboNumMatricula.getSelectedItem();
            if (num == null) { error("Seleccione un n\u00famero de matr\u00edcula", cboNumMatricula); return; }
            if (a == null || c == null) { error("Seleccione un curso de la lista", cboCurso); return; }
            mediador.notificar(this, Evento.MATRICULA_MODIFICAR,
                new Matricula((Integer) num, a.getCodAlumno(), c.getCodCurso(), null, null));
        }
    }

    private void eliminar() {
        Object num = cboNumMatricula.getSelectedItem();
        if (num == null) { error("Seleccione una matr\u00edcula en la tabla", cboNumMatricula); return; }
        Alumno a = cboAlumno.getSeleccionado();
        Curso c = cboCurso.getSeleccionado();
        int r = JOptionPane.showConfirmDialog(this,
            "\u00bfEst\u00e1 seguro de eliminar esta matr\u00edcula?\nAlumno: " + (a == null ? "-" : a.getNombres() + " " + a.getApellidos())
                + "\nCurso: " + (c == null ? "-" : c.getAsignatura()),
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) mediador.notificar(this, Evento.MATRICULA_ELIMINAR, (Integer) num);
    }

    // ---- Estado de la pantalla -------------------------------------------
    private void habilitar(boolean num, boolean alumno, boolean curso, boolean aceptar, boolean cancelar,
                           boolean adicionar, boolean modificar, boolean eliminar) {
        cboNumMatricula.setEnabled(num);
        cboAlumno.setEnabled(alumno);
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
        cboNumMatricula.setSelectedIndex(-1);
        cboAlumno.limpiar();
        cboCurso.limpiar();
        mostrarAlumno(null);
        mostrarCurso(null);
    }

    private void cargarListas() {
        try {
            cboNumMatricula.removeAllItems();
            fichas = consultas.fichasMatriculas();
            for (FichaMatricula f : fichas) cboNumMatricula.addItem(f.getMatricula().getNumeroMatricula());
            List<Alumno> alumnos = consultas.alumnos();
            alumnos.sort(Comparator.comparing(a -> Texto.clave(a.getNombres() + " " + a.getApellidos())));
            cboAlumno.setItems(alumnos);
            List<Curso> cursos = consultas.cursos();
            cursos.sort(Comparator.comparing(c -> Texto.clave(c.getAsignatura())));
            cboCurso.setItems(cursos);
        } catch (Exception ex) {
            mensaje("No se pudieron cargar los datos");
        }
        cboNumMatricula.setSelectedIndex(-1);
    }

    private void listar() {
        modelo.setRowCount(0);
        try {
            fichas = consultas.fichasMatriculas();
            for (FichaMatricula f : fichas) {
                Matricula m = f.getMatricula();
                Alumno a = f.getAlumno();
                modelo.addRow(new Object[] {m.getNumeroMatricula(), a.getCodAlumno(), a.getNombres(), a.getApellidos(),
                    f.getCurso().getCodCurso(), f.getCurso().getAsignatura(), m.getFecha(), m.getHora(),
                    a.getEstado() == EstadoAlumno.MATRICULADO.getValor() ? "S\u00ed" : "No"});
            }
        } catch (Exception ex) {
            mensaje("Error listando matr\u00edculas");
        }
    }

    // ---- Mensajes ----------------------------------------------------------
    private void mensaje(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    private void error(String s, JComponent c) {
        JOptionPane.showMessageDialog(this, s, "Atenci\u00f3n", JOptionPane.WARNING_MESSAGE);
        c.requestFocus();
    }

    // ---- Tabla -----------------------------------------------------------
    @Override public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tblMatricula && btnAdicionar.isEnabled()) {
            int fila = tblMatricula.getSelectedRow();
            if (fila >= 0) cboNumMatricula.setSelectedItem(tblMatricula.getValueAt(fila, 0));
        }
    }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
}

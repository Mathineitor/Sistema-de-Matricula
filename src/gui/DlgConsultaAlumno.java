package gui;

import clases.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import servicios.ConsultasFacade;
import servicios.ConsultasFacade.FichaMatricula;

/** Consulta de un alumno: se busca escribiendo su nombre. */
public class DlgConsultaAlumno extends JDialog implements ActionListener {

    private final ConsultasFacade consultas = new ConsultasFacade();
    private ComboBuscador<Alumno> cboAlumno;
    private JButton btnConsultar;
    private JTextArea txtResultado;

    public DlgConsultaAlumno() {
        JPanel cuerpo = new JPanel(null);
        cuerpo.setPreferredSize(new Dimension(780, 540));

        Tema.Tarjeta buscar = new Tema.Tarjeta("Buscar alumno");
        buscar.setBounds(20, 20, 740, 104);
        cuerpo.add(buscar);
        JLabel pista = Tema.etiqueta("Escribe parte del nombre o apellido");
        pista.setBounds(500, 14, 220, 18);
        buscar.add(pista);
        cboAlumno = new ComboBuscador<Alumno>(a -> a.getNombres() + " " + a.getApellidos() + "  \u00b7  " + a.getCodAlumno());
        cboAlumno.setAlSeleccionar(a -> listar());
        cboAlumno.setBounds(24, 48, 500, 34);
        buscar.add(cboAlumno);
        btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(this);
        btnConsultar.setBounds(548, 48, 168, 34);
        buscar.add(btnConsultar);

        Tema.Tarjeta ficha = new Tema.Tarjeta("Ficha del alumno");
        ficha.setBounds(20, 140, 740, 380);
        cuerpo.add(ficha);
        txtResultado = new JTextArea();
        JScrollPane sp = new JScrollPane(txtResultado);
        sp.setBounds(12, 42, 716, 326);
        ficha.add(sp);

        Tema.montar(this, "Consulta de alumno", "Datos personales y matr\u00edcula vigente", cuerpo);
        cargarAlumnos();
    }

    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConsultar) {
            if (cboAlumno.getSeleccionado() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un alumno de la lista", "Atenci\u00f3n", JOptionPane.WARNING_MESSAGE);
                cboAlumno.requestFocus();
            } else listar();
        }
    }

    private void cargarAlumnos() {
        try {
            List<Alumno> alumnos = consultas.alumnos();
            alumnos.sort(Comparator.comparing(a -> Texto.clave(a.getNombres() + " " + a.getApellidos())));
            cboAlumno.setItems(alumnos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando alumnos");
        }
    }

    private void listar() {
        Alumno x = cboAlumno.getSeleccionado();
        if (x == null) return;
        txtResultado.setText("");
        try {
            linea("C\u00d3DIGO", x.getCodAlumno());
            linea("NOMBRES", x.getNombres());
            linea("APELLIDOS", x.getApellidos());
            linea("DNI", x.getDni());
            linea("EDAD", x.getEdad());
            linea("CELULAR", x.getCelular());
            linea("ESTADO", Texto.titulo(EstadoAlumno.desde(x.getEstado()).name()));
            if (x.getEstado() != EstadoAlumno.REGISTRADO.getValor()) {
                FichaMatricula f = consultas.matriculaVigenteDe(x.getCodAlumno());
                if (f != null) {
                    txtResultado.append("\n");
                    linea("N.\u00ba DE MATR\u00cdCULA", f.getMatricula().getNumeroMatricula());
                    linea("ASIGNATURA", f.getCurso().getAsignatura());
                    linea("CICLO", Ciclo.nombre(f.getCurso().getCiclo()));
                    linea("CR\u00c9DITOS", f.getCurso().getCreditos());
                    linea("HORAS", f.getCurso().getHoras());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error consultando alumno");
        }
    }

    private void linea(String etiqueta, Object valor) {
        txtResultado.append(String.format("%-20s: %s%n", etiqueta, valor));
    }
}

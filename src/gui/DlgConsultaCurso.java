package gui;

import clases.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import servicios.ConsultasFacade;

/** Consulta de un curso: se busca escribiendo el nombre de la asignatura. */
public class DlgConsultaCurso extends JDialog implements ActionListener {

    private final ConsultasFacade consultas = new ConsultasFacade();
    private ComboBuscador<Curso> cboCurso;
    private JButton btnConsultar;
    private JTextArea txtResultado;

    public DlgConsultaCurso() {
        JPanel cuerpo = new JPanel(null);
        cuerpo.setPreferredSize(new Dimension(780, 540));

        Tema.Tarjeta buscar = new Tema.Tarjeta("Buscar curso");
        buscar.setBounds(20, 20, 740, 104);
        cuerpo.add(buscar);
        JLabel pista = Tema.etiqueta("Escribe parte del nombre de la asignatura");
        pista.setBounds(450, 14, 270, 18);
        buscar.add(pista);
        cboCurso = new ComboBuscador<Curso>(c -> c.getAsignatura() + "  \u00b7  " + c.getCodCurso());
        cboCurso.setAlSeleccionar(c -> listar());
        cboCurso.setBounds(24, 48, 500, 34);
        buscar.add(cboCurso);
        btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(this);
        btnConsultar.setBounds(548, 48, 168, 34);
        buscar.add(btnConsultar);

        Tema.Tarjeta ficha = new Tema.Tarjeta("Ficha del curso");
        ficha.setBounds(20, 140, 740, 380);
        cuerpo.add(ficha);
        txtResultado = new JTextArea();
        JScrollPane sp = new JScrollPane(txtResultado);
        sp.setBounds(12, 42, 716, 326);
        ficha.add(sp);

        Tema.montar(this, "Consulta de curso", "Datos del curso y alumnos matriculados", cuerpo);
        cargarCursos();
    }

    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConsultar) {
            if (cboCurso.getSeleccionado() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un curso de la lista", "Atenci\u00f3n", JOptionPane.WARNING_MESSAGE);
                cboCurso.requestFocus();
            } else listar();
        }
    }

    private void cargarCursos() {
        try {
            List<Curso> cursos = consultas.cursos();
            cursos.sort(Comparator.comparing(c -> Texto.clave(c.getAsignatura())));
            cboCurso.setItems(cursos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando cursos");
        }
    }

    private void listar() {
        Curso c = cboCurso.getSeleccionado();
        if (c == null) return;
        txtResultado.setText("");
        try {
            linea("C\u00d3DIGO", c.getCodCurso());
            linea("ASIGNATURA", c.getAsignatura());
            linea("CICLO", Ciclo.nombre(c.getCiclo()));
            linea("CR\u00c9DITOS", c.getCreditos());
            linea("HORAS", c.getHoras());
            txtResultado.append("\n");
            List<Alumno> alumnos = consultas.alumnosDelCurso(c.getCodCurso());
            int n = alumnos.size();
            if (n == 0) txtResultado.append("NO HAY ALUMNOS MATRICULADOS\n");
            else {
                txtResultado.append(n == 1 ? "HAY 1 ALUMNO MATRICULADO\n" : "HAY " + n + " ALUMNOS MATRICULADOS\n");
                for (Alumno a : alumnos) txtResultado.append("   \u2022 " + a.getNombres() + " " + a.getApellidos() + "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error consultando curso");
        }
    }

    private void linea(String etiqueta, Object valor) {
        txtResultado.append(String.format("%-14s: %s%n", etiqueta, valor));
    }
}

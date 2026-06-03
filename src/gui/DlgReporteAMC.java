package gui;

import arreglos.*;
import clases.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class DlgReporteAMC extends JDialog implements ActionListener {
	private JButton btnListar;
	private JTextArea txtResultado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgReporteAMC dialog = new DlgReporteAMC();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgReporteAMC() {
		setTitle("ALUMNOS MATRICULADOS POR CURSO");
		setBounds(100, 100, 624, 592);
		getContentPane().setLayout(null);
		{
			btnListar = new JButton("Listar");
			btnListar.addActionListener(this);
			btnListar.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnListar.setBounds(254, 10, 103, 28);
			getContentPane().add(btnListar);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 48, 590, 497);
			getContentPane().add(scrollPane);
			
			txtResultado = new JTextArea();
			txtResultado.setFont(new Font("Monospaced", Font.BOLD, 13));
			scrollPane.setViewportView(txtResultado);
		}
	}
	
	//DECLARACION GLOBAL
	AlumnoDao aa = new AlumnoDao();
CursoDao ac = new CursoDao();
MatriculaDao am = new MatriculaDao();

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnListar) {
			actionPerformedBtnListar(e);
		}
	}
protected void actionPerformedBtnListar(ActionEvent e) {
    txtResultado.setText("");
    try {
        for (Curso curso : ac.listar()) {
            boolean hayAlumnos = false;
            imprimir(" " + curso.getAsignatura());
            imprimir("");

            for (Matricula m : am.listar()) {
                if (m.getCodigoCurso() == curso.getCodCurso()) {
                    Alumno a = aa.buscar(m.getCodigoAlumno());
                    if (a.getEstado() == EstadoAlumno.MATRICULADO.getValor()) { // matriculado
                        if (!hayAlumnos) hayAlumnos = true;
                        imprimir(" * ALUMNO : " + a.getNombres() + " " + a.getApellidos());
                    }
                }
            }

            if (!hayAlumnos) {
                imprimir(" NO EXISTEN ALUMNOS MATRICULADOS EN ESTE CURSO");
            }
            imprimir("-------------------------------------------------------------------------");
        }
    } catch (Exception ex) {
        mensaje("Error generando reporte");
    }
}
void mensaje(String s) {
    JOptionPane.showMessageDialog(this, s);
}
	
String nombreEstado(int i) {
    return EstadoAlumno.desde(i).toString();
}
	
	void imprimir(String s) {
		txtResultado.append(s + "\n");
	}
}

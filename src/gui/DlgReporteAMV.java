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
import javax.swing.ScrollPaneConstants;

public class DlgReporteAMV extends JDialog implements ActionListener {
	private JButton btnListar;
	private JScrollPane scrollPane;
	private JTextArea txtResultado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgReporteAMV dialog = new DlgReporteAMV();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgReporteAMV() {
		setTitle("ALUMNOS CON MATR\u00CDCULA VIGENTE");
		setBounds(100, 100, 624, 592);
		getContentPane().setLayout(null);
		
		btnListar = new JButton("Listar");
		btnListar.addActionListener(this);
		btnListar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnListar.setBounds(254, 10, 103, 28);
		getContentPane().add(btnListar);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 48, 590, 497);
		getContentPane().add(scrollPane);
		
		txtResultado = new JTextArea();
		txtResultado.setFont(new Font("Monospaced", Font.BOLD, 13));
		scrollPane.setViewportView(txtResultado);
	}
	
	//DECLARACION GLOBAL
AlumnoDao aa = new AlumnoDao();	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnListar) {
			actionPerformedBtnListar(e);
		}
	}
protected void actionPerformedBtnListar(ActionEvent e) {
    txtResultado.setText("");
    try {
        for (Alumno a : aa.listar()) {
            if (a.getEstado() == 1) { // vigente (matriculado)
                imprimir(" CODIGO    : " + a.getCodAlumno());
                imprimir(" NOMBRES   : " + a.getNombres());
                imprimir(" APELLIDOS : " + a.getApellidos());
                imprimir(" DNI       : " + a.getDni());
                imprimir(" EDAD      : " + a.getEdad());
                imprimir(" CELULAR   : " + a.getCelular());
                imprimir(" ESTADO    : " + nombreEstado(a.getEstado()));
                imprimir("-------------------------------------------------------------------------");
            }
        }
    } catch (Exception ex) {
        mensaje("Error generando reporte");
    }
}
void mensaje(String s) {
    JOptionPane.showMessageDialog(this, s);
}
	
	String nombreEstado(int i) {
		switch (i) {
		case 0: return "REGISTRADO";
		case 1: return "MATRICULADO";
		case 2: return "RETIRADO";
		default: return null;
		}
	}
	
	void imprimir(String s) {
		txtResultado.append(s + "\n");
	}
}

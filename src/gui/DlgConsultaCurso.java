package gui;

import clases.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import arreglos.CursoDao;
import arreglos.MatriculaDao;
import clases.Curso;
import clases.Matricula;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgConsultaCurso extends JDialog implements ActionListener {
	private JLabel lblNewLabel;
	private JComboBox cboCodigo;
	private JButton btnConsultar;
	private JScrollPane scrollPane;
	private JTextArea txtResultado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgConsultaCurso dialog = new DlgConsultaCurso();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgConsultaCurso() {
		setBounds(100, 100, 509, 300);
		getContentPane().setLayout(null);
		
		lblNewLabel = new JLabel("C\u00F3digo de curso:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(10, 18, 115, 15);
		getContentPane().add(lblNewLabel);
		
		cboCodigo = new JComboBox();
		cboCodigo.setBounds(124, 16, 115, 21);
		getContentPane().add(cboCodigo);
		
		btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(this);
		btnConsultar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConsultar.setBounds(388, 13, 97, 23);
		getContentPane().add(btnConsultar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 49, 475, 204);
		getContentPane().add(scrollPane);
		
		txtResultado = new JTextArea();
		txtResultado.setFont(new Font("Monospaced", Font.BOLD, 13));
		scrollPane.setViewportView(txtResultado);
		
		listarCboCodigo();
	}
	
	//declaraciones globales
	CursoDao ac = new CursoDao();
MatriculaDao am = new MatriculaDao();
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConsultar) {
			actionPerformedBtnConsultar(e);
		}
	}
	protected void actionPerformedBtnConsultar(ActionEvent e) {
		try {
			txtResultado.setText("");
			listar();
			cboCodigo.requestFocus();
		} catch (Exception error) {
			mensaje("Seleccione un codigo de curso");
		}
	}
	

	void imprimir(){
		imprimir("");
	}
	
void listar() {
    try {
        Curso c = ac.buscar(leerCurso());
        imprimir("CODIGO               : " + c.getCodCurso());
        imprimir("ASIGNATURA           : " + c.getAsignatura());
        imprimir("CICLO                : " + nombreCiclo(c.getCiclo()));
        imprimir("CANTIDAD DE CREDITOS : " + c.getCreditos());
        imprimir("CANTIDAD DE HORAS    : " + c.getHoras());
        imprimir();

        int x = 0;
        for (Matricula m : am.listar()) {
            if (m.getCodigoCurso() == c.getCodCurso()) {
                x++;
            }
        }

        if (x == 0) imprimir("NO HAY ALUMNOS MATRICULADOS");
        else if (x == 1) imprimir("HAY " + x + " ALUMNO MATRICULADO");
        else imprimir("HAY " + x + " ALUMNOS MATRICULADOS");
    } catch (Exception ex) {
        mensaje("Error consultando curso");
    }
}
void listarCboCodigo() {
    cboCodigo.removeAllItems();
    try {
        for (Curso c : ac.listar()) {
            cboCodigo.addItem(c.getCodCurso());
        }
    } catch (Exception ex) {
        mensaje("Error cargando cursos");
    }
    cboCodigo.setSelectedIndex(-1);
}
	String nombreCiclo(int i) {
		switch (i) {
			case 0: return "PRIMERO";
			case 1: return "SEGUNDO";
			case 2: return "TERCERO";
			case 3: return "CUARTO";
			case 4: return "QUINTO";
			case 5: return "SEXTO";
			default:return null;
		}
	}
//  M�todos tipo void (con par�metros)
	void imprimir(String s){
		txtResultado.append(s + "\n"); 
	}
	void mensaje(String s) {
		JOptionPane.showMessageDialog(this, s);
	}
	//otros metodos
	public int leerCurso() {
		return Integer.parseInt(cboCodigo.getSelectedItem().toString());
	}
}

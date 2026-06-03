package gui;

import arreglos.*;
import clases.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgConsultaRetiro extends JDialog implements ActionListener {
	private JLabel lblNmeroDeRetiro;
	private JComboBox cboCodigo;
	private JButton btnConsultar;
	private JScrollPane scrollPane;
	private JTextArea txtResultado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgConsultaRetiro dialog = new DlgConsultaRetiro();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgConsultaRetiro() {
		setBounds(100, 100, 500, 323);
		getContentPane().setLayout(null);
		
		lblNmeroDeRetiro = new JLabel("N\u00FAmero de retiro:");
		lblNmeroDeRetiro.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNmeroDeRetiro.setBounds(10, 15, 143, 15);
		getContentPane().add(lblNmeroDeRetiro);
		
		cboCodigo = new JComboBox();
		cboCodigo.setSelectedIndex(-1);
		cboCodigo.setBounds(154, 13, 115, 21);
		getContentPane().add(cboCodigo);
		
		btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(this);
		btnConsultar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConsultar.setBounds(378, 10, 97, 23);
		getContentPane().add(btnConsultar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 46, 465, 230);
		getContentPane().add(scrollPane);
		
		txtResultado = new JTextArea();
		txtResultado.setFont(new Font("Monospaced", Font.BOLD, 13));
		scrollPane.setViewportView(txtResultado);
		
		listarCboCodigo();
	}
	
	//declaracion global

AlumnoDao aa = new AlumnoDao();
CursoDao ac = new CursoDao();
MatriculaDao am = new MatriculaDao();
RetiroDao ar = new RetiroDao();
	
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
		}
		catch (Exception error) {
			mensaje("Seleccione un numero de retiro");
		}
	}
		
	void imprimir(){
		imprimir("");
	}
	//metodo Listar
	void listar() {
    try {
        Retiro r = ar.buscar(leerCodigo());
        Matricula m = am.buscar(r.getNumMatricula());
        Alumno x = aa.buscar(m.getCodigoAlumno());
        Curso c = ac.buscar(m.getCodigoCurso());

        imprimir("CODIGO         : " + r.getNumRetiro());
        imprimir("NUM. MATRICULA : " + m.getNumeroMatricula());
        imprimir("");
        imprimir("COD. ALUMNO    : " + x.getCodAlumno());
        imprimir("NOMBRES        : " + x.getNombres());
        imprimir("APELLIDOS      : " + x.getApellidos());
        imprimir("DNI            : " + x.getDni());
        imprimir("EDAD           : " + x.getEdad());
        imprimir("CELULAR        : " + x.getCelular());
        imprimir("");
        imprimir("COD. CURSO     : " + c.getCodCurso());
        imprimir("ASIGNATURA     : " + c.getAsignatura());
        imprimir("CICLO          : " + nombreCiclo(c.getCiclo()));
        imprimir("CREDITOS       : " + c.getCreditos());
        imprimir("HORAS          : " + c.getHoras());
    } catch (Exception ex) {
        mensaje("Error consultando retiro");
    }
}
	void listarCboCodigo() {
    cboCodigo.removeAllItems();
    try {
        for (Retiro r : ar.listar()) {
            cboCodigo.addItem(r.getNumRetiro());
        }
    } catch (Exception ex) {
        mensaje("Error cargando retiros");
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
	void imprimir(String s){
		txtResultado.append(s + "\n"); 
	}
	void mensaje(String s) {
		JOptionPane.showMessageDialog(this, s);
	}
	public int leerCodigo() {
		return Integer.parseInt(cboCodigo.getSelectedItem().toString());
	}
}

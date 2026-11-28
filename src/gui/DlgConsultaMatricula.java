package gui;

import arreglos.*;
import clases.*;
import servicios.ConsultasFacade;
import servicios.ConsultasFacade.FichaMatricula;
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
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgConsultaMatricula extends JDialog implements ActionListener {
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
			DlgConsultaMatricula dialog = new DlgConsultaMatricula();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgConsultaMatricula() {
		setBounds(100, 100, 499, 323);
		getContentPane().setLayout(null);
		
		lblNewLabel = new JLabel("N\u00FAmero de matr\u00EDcula:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(10, 15, 143, 15);
		getContentPane().add(lblNewLabel);
		
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
			Tema.aplicar(this, "Consulta de matr\u00edcula", "Ficha completa de una matr\u00edcula");
	}
	//declaracion global
MatriculaDao am = new MatriculaDao();               // solo para llenar el combo
ConsultasFacade consultas = new ConsultasFacade();  // FACADE: ficha completa en una llamada
	
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
			imprimir("Seleccione un numero de matricula");
		}
	}
	
	void imprimir(){
		imprimir("");
	}
	//metodo Listar
void listar() {
    try {
        FichaMatricula f = consultas.fichaMatricula(leerCodigo());
        Matricula m = f.getMatricula();
        Alumno x = f.getAlumno();
        Curso c = f.getCurso();

        imprimir("CODIGO        : " + m.getNumeroMatricula());
        imprimir("ESTADO        : " + f.getEstadoAlumno());
        imprimir("");
        imprimir("COD. ALUMNO   : " + x.getCodAlumno());
        imprimir("NOMBRES       : " + x.getNombres());
        imprimir("APELLIDOS     : " + x.getApellidos());
        imprimir("DNI           : " + x.getDni());
        imprimir("EDAD          : " + x.getEdad());
        imprimir("CELULAR       : " + x.getCelular());
        imprimir("");
        imprimir("COD. CURSO    : " + c.getCodCurso());
        imprimir("ASIGNATURA    : " + c.getAsignatura());
        imprimir("CICLO         : " + nombreCiclo(c.getCiclo()));
        imprimir("CREDITOS      : " + c.getCreditos());
        imprimir("HORAS         : " + c.getHoras());
    } catch (Exception ex) {
        mensaje("Error consultando matr\u00edcula");
    }
}
void listarCboCodigo() {
    cboCodigo.removeAllItems();
    try {
        for (Matricula m : am.listar()) {
            cboCodigo.addItem(m.getNumeroMatricula());
        }
    } catch (Exception ex) {
        mensaje("Error cargando matr\u00edculas");
    }
    cboCodigo.setSelectedIndex(-1);
}
	String nombreCiclo(int i) { return Ciclo.nombre(i); }
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

package gui;
import javax.swing.JDialog;
import mediador.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class FrmPrincipal extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnArchivo;
	private JMenu mnMantenimiento;
	private JMenu mnRegistro;
	private JMenu mnConsulta;
	private JMenu mnReporte;
	private JMenuItem mntmSalir;
	private JMenuItem mntmMantAlumno;
	private JMenuItem mntmMantCurso;
	private JMenuItem mntmRegRetiro;
	private JMenuItem mntmRegMatricula;
	private JMenuItem mntmConsAlumno;
	private JMenuItem mntmConsCurso;
	private JMenuItem mntmConsMatricula;
	private JMenuItem mntmConsRetiro;
	private JMenuItem mntmAlumMatPendiente;
	private JMenuItem mntmAlumMatVigente;
	private JMenuItem mntmAlumMatCurso;
        private PnlDashboard dashboard;
        private final Mediador mediador = new MediadorMatricula();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmPrincipal frame = new FrmPrincipal();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
        

	/**
	 * Create the frame.
	 */
	public FrmPrincipal() {
            
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 870, 700);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnArchivo = new JMenu("Archivo");
		mnArchivo.setMnemonic('A');
		menuBar.add(mnArchivo);
		
		mntmSalir = new JMenuItem("Salir");
		mntmSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmSalir.addActionListener(this);
		mnArchivo.add(mntmSalir);
		
		mnMantenimiento = new JMenu("Mantenimiento");
		mnMantenimiento.setMnemonic('M');
		menuBar.add(mnMantenimiento);
		
		mntmMantAlumno = new JMenuItem("Alumno");
		mntmMantAlumno.addActionListener(this);
		mnMantenimiento.add(mntmMantAlumno);
		
		mntmMantCurso = new JMenuItem("Curso");
		mntmMantCurso.addActionListener(this);
		mnMantenimiento.add(mntmMantCurso);
		
		mnRegistro = new JMenu("Registro");
		mnRegistro.setMnemonic('R');
		menuBar.add(mnRegistro);
		
		mntmRegMatricula = new JMenuItem("Matricula");
		mntmRegMatricula.addActionListener(this);
		mnRegistro.add(mntmRegMatricula);
		
		mntmRegRetiro = new JMenuItem("Retiro");
		mntmRegRetiro.addActionListener(this);
		mnRegistro.add(mntmRegRetiro);
		
		mnConsulta = new JMenu("Consulta");
		mnConsulta.setMnemonic('C');
		menuBar.add(mnConsulta);
		
		mntmConsAlumno = new JMenuItem("Alumno");
		mntmConsAlumno.addActionListener(this);
		mnConsulta.add(mntmConsAlumno);
		
		mntmConsCurso = new JMenuItem("Curso");
		mntmConsCurso.addActionListener(this);
		mnConsulta.add(mntmConsCurso);
		
		mntmConsMatricula = new JMenuItem("Matricula");
		mntmConsMatricula.addActionListener(this);
		mnConsulta.add(mntmConsMatricula);
		
		mntmConsRetiro = 
				new JMenuItem("Retiro");
		mntmConsRetiro.addActionListener(this);
		mnConsulta.add(mntmConsRetiro);
		
		mnReporte = new JMenu("Reporte");
		mnReporte.setMnemonic('p');
		menuBar.add(mnReporte);
		
		mntmAlumMatPendiente = new JMenuItem("Alumnos con matricula pendiente");
		mntmAlumMatPendiente.addActionListener(this);
		mnReporte.add(mntmAlumMatPendiente);
		
		mntmAlumMatVigente = new JMenuItem("Alumnos con matricula vigente");
		mntmAlumMatVigente.addActionListener(this);
		mnReporte.add(mntmAlumMatVigente);
		
		mntmAlumMatCurso = new JMenuItem("Alumnos matriculados por curso");
		mntmAlumMatCurso.addActionListener(this);
		mnReporte.add(mntmAlumMatCurso);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	        
dashboard = new PnlDashboard();
dashboard.setMediador(mediador);
mediador.registrar(dashboard);   // el dashboard escucha al Mediador
contentPane.add(dashboard, BorderLayout.CENTER);
        }
        
        

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmAlumMatCurso) {
			actionPerformedMntmAlumMatCurso(e);
		}
		if (e.getSource() == mntmAlumMatVigente) {
			actionPerformedMntmAlumMatVigente(e);
		}
		if (e.getSource() == mntmAlumMatPendiente) {
			actionPerformedMntmAlumMatPendiente(e);
		}
		if (e.getSource() == mntmConsRetiro) {
			actionPerformedMntmConsRetiro(e);
		}
		if (e.getSource() == mntmConsMatricula) {
			actionPerformedMntmConsMatricula(e);
		}
		if (e.getSource() == mntmConsCurso) {
			actionPerformedMntmConsCurso(e);
		}
		if (e.getSource() == mntmConsAlumno) {
			actionPerformedMntmConsAlumno(e);
		}
		if (e.getSource() == mntmRegRetiro) {
			actionPerformedMntmRegRetiro(e);
		}
		if (e.getSource() == mntmRegMatricula) {
			actionPerformedMntmRegMatricula(e);
		}
		if (e.getSource() == mntmMantCurso) {
			actionPerformedMntmMantCurso(e);
		}
		if (e.getSource() == mntmMantAlumno) {
			actionPerformedMntmMantAlumno(e);
		}
		if (e.getSource() == mntmSalir) {
			actionPerformedMntmSalir(e);
		}
	}
	protected void actionPerformedMntmSalir(ActionEvent e) {
		int valor = JOptionPane.showOptionDialog(null, "Estas seguro que deseas cerrar el programa?","Confirmar",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Si", "No"}, null);
		if (valor == 0) System.exit(0);
	}
protected void actionPerformedMntmMantAlumno(ActionEvent e)     { abrirColega(new DlgMantAlumno()); }
	protected void actionPerformedMntmMantCurso(ActionEvent e)      { abrirColega(new DlgMantCurso()); }
	protected void actionPerformedMntmRegMatricula(ActionEvent e)   { abrirColega(new DlgRegMatricula()); }
	protected void actionPerformedMntmRegRetiro(ActionEvent e)      { abrirColega(new DlgRegRetiro()); }
	protected void actionPerformedMntmConsAlumno(ActionEvent e)     { abrir(new DlgConsultaAlumno()); }
	protected void actionPerformedMntmConsCurso(ActionEvent e)      { abrir(new DlgConsultaCurso()); }
	protected void actionPerformedMntmConsMatricula(ActionEvent e)  { abrir(new DlgConsultaMatricula()); }
	protected void actionPerformedMntmConsRetiro(ActionEvent e)     { abrir(new DlgConsultaRetiro()); }
	protected void actionPerformedMntmAlumMatPendiente(ActionEvent e) { abrir(new DlgReporteAMP()); }
	protected void actionPerformedMntmAlumMatVigente(ActionEvent e)   { abrir(new DlgReporteAMV()); }
	protected void actionPerformedMntmAlumMatCurso(ActionEvent e)     { abrir(new DlgReporteAMC()); }

	/** Diálogo de solo lectura: se muestra y se libera. */
	private void abrir(JDialog dlg) {
		dlg.setModal(true);
		dlg.setLocationRelativeTo(this);
		dlg.setVisible(true);
		dlg.dispose();
	}

	/** Diálogo que participa en el Mediador: se registra al abrir y se da de baja al cerrar. */
	private <T extends JDialog & Colega> void abrirColega(T dlg) {
		dlg.setMediador(mediador);
		mediador.registrar(dlg);
		try {
			abrir(dlg);
		} finally {
			mediador.eliminar(dlg);
		}
	}
}

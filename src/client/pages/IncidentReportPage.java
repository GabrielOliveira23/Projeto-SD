package client.pages;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.User;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class IncidentReportPage extends JFrame {

	private JPanel contentPane;

	private User user;
	private HomePage home;
	private JTextField txtRodovia;
	private JTextField kmField;
	private JTextField dateField;
	private JComboBox<String> incidentCombo;

	public IncidentReportPage(User user, HomePage home) {
		super("Reportar Incidente");
		this.user = user;
		this.home = home;

		this.initComponents();
		this.setVisible(true);
	}

	private void initComponents() {
		this.setSize(390, 340);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JLabel lblRodovia = new JLabel("Rodovia");
		lblRodovia.setFont(new Font("Dialog", Font.BOLD, 16));
		lblRodovia.setBounds(20, 80, 69, 19);
		getContentPane().add(lblRodovia);
		
		JLabel lblKm = new JLabel("Km");
		lblKm.setFont(new Font("Dialog", Font.BOLD, 16));
		lblKm.setBounds(20, 120, 27, 19);
		getContentPane().add(lblKm);
		
		JLabel lblTipoDeIncidente = new JLabel("Tipo de Incidente");
		lblTipoDeIncidente.setFont(new Font("Dialog", Font.BOLD, 16));
		lblTipoDeIncidente.setBounds(20, 160, 154, 19);
		getContentPane().add(lblTipoDeIncidente);
		
		JLabel lblData = new JLabel("Data");
		lblData.setFont(new Font("Dialog", Font.BOLD, 16));
		lblData.setBounds(20, 200, 41, 19);
		getContentPane().add(lblData);
		
		txtRodovia = new JTextField();
		txtRodovia.setBounds(200, 77, 160, 25);
		getContentPane().add(txtRodovia);
		txtRodovia.setColumns(10);
		
		kmField = new JTextField();
		kmField.setColumns(10);
		kmField.setBounds(200, 117, 160, 24);
		getContentPane().add(kmField);
		
		dateField = new JTextField();
		dateField.setColumns(10);
		dateField.setBounds(200, 197, 160, 25);
		getContentPane().add(dateField);
		
		JLabel reportIncidentLabel = new JLabel("Reportar Incidente");
		reportIncidentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		reportIncidentLabel.setFont(new Font("URW Bookman L", Font.BOLD, 20));
		reportIncidentLabel.setBounds(0, 20, 390, 20);
		getContentPane().add(reportIncidentLabel);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(70, 245, 110, 35);
		getContentPane().add(btnCancelar);
		
		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(200, 245, 110, 35);
		getContentPane().add(btnConfirmar);
		
		incidentCombo = new JComboBox<String>();
		incidentCombo.setBounds(200, 157, 160, 25);
		getContentPane().add(incidentCombo);
	}
}

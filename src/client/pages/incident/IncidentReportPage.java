package client.pages.incident;

import javax.swing.JFrame;
import javax.swing.text.MaskFormatter;

import com.google.gson.JsonObject;

import client.pages.HomePage;
import config.ConnectionLogic;
import entities.User;
import utils.DataVerify;
import utils.IncidentTypeEnum;

import javax.swing.JLabel;
import java.awt.Font;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

public class IncidentReportPage extends JFrame {
	private User user;
	private HomePage home;

	private JTextField highwayField;
	private JTextField kmField;
	private JFormattedTextField dateField;
	private JFormattedTextField hourField;
	private JComboBox<String> incidentCombo;
	private MaskFormatter dateMask;
	private MaskFormatter hourMask;

	// IncidentTypeEnum.NEBLINA.getNumero();

	public IncidentReportPage(User user, HomePage home) {
		super("Reportar Incidente");
		this.user = user;
		this.home = home;

		try {
			this.dateMask = new MaskFormatter("##/##/####");
			this.hourMask = new MaskFormatter("##:##");
		} catch (ParseException e) {
			System.out.println("Erro de formatacao da data");
		}

		this.initComponents();
		this.setVisible(true);
	}

	private String getParsedDate(String date) {
		String[] dateSplit = date.split("/");
		return dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0] + " " + hourField.getText() + ":00";
	}

	private void confirmForm() {
		if (highwayField.getText().isEmpty()
				|| kmField.getText().isEmpty()
				|| dateField.getText().isEmpty()) {
			System.out.println("Preencha todos os campos!");
			return;
		} else if (Integer.parseInt(kmField.getText()) < 0) {
			System.out.println("Km invalido!");
			return;
		} else if (!DataVerify.hour(hourField.getText().split(":")[0])) {
			System.out.println("Hora invalida!");
			return;
		} else if (!DataVerify.minute(hourField.getText().split(":")[1])) {
			System.out.println("minuto invalida!");
			return;
		}
		
		JsonObject response = ConnectionLogic.reportIncident(
				user.getToken(), user.getId(),
				getParsedDate(dateField.getText()),
				highwayField.getText(),
				Integer.parseInt(kmField.getText()),
				IncidentTypeEnum.getEnum(incidentCombo.getSelectedItem().toString()));

		System.out.println("Resposta servidor: " + response);

		if (response.get("codigo").getAsInt() == 200) {
			System.out.println("Cadastrado com sucesso!");
		} else {
			System.out.println("Erro ao cadastrar");
			return;
		}

		this.home.setVisible(true);
		dispose();
	}

	private void addIncidents() {
		this.incidentCombo.addItem("Vento");
		this.incidentCombo.addItem("Chuva");
		this.incidentCombo.addItem("Neblina");
		this.incidentCombo.addItem("Neve");
		this.incidentCombo.addItem("Gelo na pista");
		this.incidentCombo.addItem("Granizo");
		this.incidentCombo.addItem("Transito parado");
		this.incidentCombo.addItem("Filas de transito");
		this.incidentCombo.addItem("Transito lento");
		this.incidentCombo.addItem("Acidente desconhecido");
		this.incidentCombo.addItem("Incidente desconhecido");
		this.incidentCombo.addItem("Trabalhos na estrada");
		this.incidentCombo.addItem("Via interditada");
		this.incidentCombo.addItem("Pista interditada");
	}

	private void initComponents() {
		this.setSize(390, 340);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblRodovia = new JLabel("Rodovia");
		lblRodovia.setFont(new Font("Dialog", Font.BOLD, 16));
		lblRodovia.setBounds(20, 80, 70, 20);
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
		lblData.setBounds(40, 200, 40, 19);
		getContentPane().add(lblData);

		highwayField = new JTextField();
		highwayField.setBounds(200, 77, 160, 25);
		getContentPane().add(highwayField);
		highwayField.setColumns(10);

		kmField = new JTextField();
		kmField.setColumns(10);
		kmField.setBounds(200, 117, 160, 24);
		getContentPane().add(kmField);

		dateField = new JFormattedTextField(dateMask);
		dateField.setHorizontalAlignment(SwingConstants.CENTER);
		dateField.setColumns(10);
		dateField.setBounds(90, 197, 80, 25);
		getContentPane().add(dateField);

		JLabel reportIncidentLabel = new JLabel("Reportar Incidente");
		reportIncidentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		reportIncidentLabel.setFont(new Font("URW Bookman L", Font.BOLD, 20));
		reportIncidentLabel.setBounds(0, 20, 374, 20);
		getContentPane().add(reportIncidentLabel);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(70, 245, 110, 35);
		btnCancelar.addActionListener(e -> {
			this.home.setVisible(true);
			dispose();
		});
		getContentPane().add(btnCancelar);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(200, 245, 110, 35);
		btnConfirmar.addActionListener(e -> confirmForm());
		getContentPane().add(btnConfirmar);

		incidentCombo = new JComboBox<String>();
		incidentCombo.setBounds(200, 157, 160, 25);
		this.addIncidents();
		getContentPane().add(incidentCombo);

		JLabel lblHora = new JLabel("Hora");
		lblHora.setFont(new Font("Dialog", Font.BOLD, 16));
		lblHora.setBounds(220, 200, 40, 19);
		getContentPane().add(lblHora);

		hourField = new JFormattedTextField(hourMask);
		hourField.setHorizontalAlignment(SwingConstants.CENTER);
		hourField.setColumns(10);
		hourField.setBounds(270, 197, 80, 25);
		getContentPane().add(hourField);
	}
}

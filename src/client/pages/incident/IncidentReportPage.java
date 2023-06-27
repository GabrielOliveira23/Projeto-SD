package client.pages.incident;

import javax.swing.JFrame;
import javax.swing.text.MaskFormatter;

import com.google.gson.JsonObject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import client.pages.HomePage;
import config.ClientLogic;
import entities.User;
import utils.DataVerify;
import utils.IncidentTypeEnum;

public class IncidentReportPage extends JFrame {
	private User user;
	private HomePage home;

	private JTextField kmField;
	private JFormattedTextField highwayField;
	private JFormattedTextField dateField;
	private JFormattedTextField hourField;
	private JComboBox<String> incidentTypeBox;
	private MaskFormatter dateMask;
	private MaskFormatter hourMask;
	private MaskFormatter highwayMask;

	public IncidentReportPage(User user, HomePage home) {
		super("Reportar Incidente");
		this.user = user;
		this.home = home;

		try {
			this.dateMask = new MaskFormatter("##/##/####");
			this.hourMask = new MaskFormatter("##:##");
			this.highwayMask = new MaskFormatter("UU-###");
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
		try {
			if (highwayField.getText().isEmpty()
					|| kmField.getText().isEmpty()
					|| dateField.getText().equals("  /  /    ")
					|| hourField.getText().equals("  :  "))
				throw new Exception("Preencha todos os campos!");
			else if (!kmField.getText().matches("[0-9]+")
					|| Integer.parseInt(kmField.getText()) < 0)
				throw new Exception("Km invalido!");
			else if (!DataVerify.hour(hourField.getText().split(":")[0]))
				throw new Exception("Hora invalida!");
			else if (!DataVerify.minute(hourField.getText().split(":")[1]))
				throw new Exception("minuto invalido!");

			JsonObject response = ClientLogic.reportIncident(
					user.getToken(), user.getId(),
					getParsedDate(dateField.getText()),
					highwayField.getText(),
					Integer.parseInt(kmField.getText()),
					IncidentTypeEnum.getEnum(incidentTypeBox.getSelectedItem().toString()));

			System.out.println("Resposta servidor: " + response);

			if (response.get("codigo").getAsInt() == 500)
				throw new Exception(response.get("mensagem").getAsString());
			else if (response.get("codigo").getAsInt() != 200)
				throw new Exception("Erro de codigo desconhecido!");

			System.out.println("Cadastrado com sucesso!");
			JOptionPane.showMessageDialog(null, "Incidente cadastrado com sucesso!");

			this.home.setVisible(true);
			dispose();
		} catch (Exception e) {
			System.out.println("Erro ao cadastrar incidente: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Erro ao cadastrar incidente: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void addIncidents() {
		this.incidentTypeBox.addItem("Vento");
		this.incidentTypeBox.addItem("Chuva");
		this.incidentTypeBox.addItem("Neblina");
		this.incidentTypeBox.addItem("Neve");
		this.incidentTypeBox.addItem("Gelo na pista");
		this.incidentTypeBox.addItem("Granizo");
		this.incidentTypeBox.addItem("Transito parado");
		this.incidentTypeBox.addItem("Filas de transito");
		this.incidentTypeBox.addItem("Transito lento");
		this.incidentTypeBox.addItem("Acidente desconhecido");
		this.incidentTypeBox.addItem("Incidente desconhecido");
		this.incidentTypeBox.addItem("Trabalhos na estrada");
		this.incidentTypeBox.addItem("Via interditada");
		this.incidentTypeBox.addItem("Pista interditada");
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
		lblData.setBounds(40, 200, 41, 19);
		getContentPane().add(lblData);

		highwayField = new JFormattedTextField(this.highwayMask);
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

		incidentTypeBox = new JComboBox<String>();
		incidentTypeBox.setBounds(200, 157, 160, 25);
		this.addIncidents();
		getContentPane().add(incidentTypeBox);

		JLabel lblHora = new JLabel("Hora");
		lblHora.setFont(new Font("Dialog", Font.BOLD, 16));
		lblHora.setBounds(220, 200, 41, 19);
		getContentPane().add(lblHora);

		hourField = new JFormattedTextField(hourMask);
		hourField.setHorizontalAlignment(SwingConstants.CENTER);
		hourField.setColumns(10);
		hourField.setBounds(270, 197, 80, 25);
		getContentPane().add(hourField);
	}
}

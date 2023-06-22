package client.pages.incident;

import javax.swing.JFrame;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import client.JsonClientTreatment;
import client.pages.HomePage;
import config.ClientLogic;
import entities.User;
import utils.GeneralFunctions;
import utils.IncidentTypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;

public class ListIncidentPage extends JFrame {
	private User userRepository;
	private HomePage homePage;
	private JFormattedTextField highwayField;
	private JTextField minKmField;
	private JTextField maxKmField;

	private MaskFormatter highwayMask;
	private JTable incidentTable;

	public ListIncidentPage(User userRepository, HomePage homePage) {
		super("Filtrar Incidentes");
		this.userRepository = userRepository;
		this.homePage = homePage;

		try {
			highwayMask = new MaskFormatter("UU-###");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.initComponents();
		this.setVisible(true);
	}

	private void getIncidents() {
		String faixaKm = "";

		if (!minKmField.getText().isEmpty() && !maxKmField.getText().isEmpty())
			faixaKm = minKmField.getText() + "-" + maxKmField.getText();

		if (highwayField.getText().isEmpty()) {
			System.out.println("Preencha o campo de rodovia!");
			return;
		}

		Date dataHoraAtual = new Date();
		String data = new SimpleDateFormat("yyyy-MM-dd").format(dataHoraAtual);
		data = data.concat(" " + new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual));

		int periodo = GeneralFunctions.getPeriod(data);

		JsonObject response = ClientLogic.getIncidents(
				userRepository.getToken(), userRepository.getId(),
				highwayField.getText(), data, faixaKm, periodo);
		System.out.println("Resposta do servidor: " + response);

		if (!JsonClientTreatment.responseTreatment(response))
			return;

		fillTable(response.get("lista_incidentes").getAsJsonArray());
	}

	private void fillTable(JsonArray list) {
		DefaultTableModel model = (DefaultTableModel) this.incidentTable.getModel();
		model.setRowCount(0);
		model.fireTableDataChanged();

		for (JsonElement item : list) {
			model.addRow(new Object[] {
					item.getAsJsonObject().get("id_incidente").getAsInt(),
					item.getAsJsonObject().get("rodovia").getAsString(),
					item.getAsJsonObject().get("km").getAsInt(),
					IncidentTypeEnum.getDescription(item.getAsJsonObject().get("tipo_incidente").getAsInt()),
					GeneralFunctions.getParsedDate(item.getAsJsonObject().get("data").getAsString())
			});
		}
	}

	private void initComponents() {
		this.setSize(800, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(50, 240, 110, 35);
		btnConfirmar.addActionListener(e -> getIncidents());
		getContentPane().add(btnConfirmar);

		JLabel lblTitle = new JLabel("Lista de Incidentes");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 20));
		lblTitle.setBounds(0, 20, 784, 20);
		getContentPane().add(lblTitle);

		JLabel lblRodovia = new JLabel("Rodovia");
		lblRodovia.setFont(new Font("Dialog", Font.BOLD, 16));
		lblRodovia.setBounds(20, 90, 80, 20);
		getContentPane().add(lblRodovia);

		highwayField = new JFormattedTextField(highwayMask);
		highwayField.setHorizontalAlignment(SwingConstants.CENTER);
		highwayField.setColumns(10);
		highwayField.setBounds(110, 91, 80, 25);
		getContentPane().add(highwayField);

		JLabel lblEntre = new JLabel("Km Mín");
		lblEntre.setFont(new Font("Dialog", Font.BOLD, 16));
		lblEntre.setBounds(20, 139, 70, 21);
		getContentPane().add(lblEntre);

		minKmField = new JTextField();
		minKmField.setHorizontalAlignment(SwingConstants.CENTER);
		minKmField.setColumns(10);
		minKmField.setBounds(110, 140, 80, 25);
		getContentPane().add(minKmField);

		maxKmField = new JTextField();
		maxKmField.setHorizontalAlignment(SwingConstants.CENTER);
		maxKmField.setColumns(10);
		maxKmField.setBounds(110, 190, 80, 25);
		getContentPane().add(maxKmField);

		JLabel lblKmMax = new JLabel("Km Max");
		lblKmMax.setFont(new Font("Dialog", Font.BOLD, 16));
		lblKmMax.setBounds(20, 195, 70, 21);
		getContentPane().add(lblKmMax);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(50, 300, 110, 35);
		btnCancelar.addActionListener(e -> {
			this.dispose();
			homePage.setVisible(true);
		});
		getContentPane().add(btnCancelar);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Incidentes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(210, 70, 550, 270);
		getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 23, 530, 236);
		panel.add(scrollPane);

		incidentTable = new JTable();
		incidentTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"ID", "Rodovia", "Km", "Tipo de Incidente", "Data"
				}));
		incidentTable.getColumnModel().getColumn(0).setPreferredWidth(35);
		incidentTable.getColumnModel().getColumn(1).setPreferredWidth(87);
		incidentTable.getColumnModel().getColumn(2).setPreferredWidth(65);
		incidentTable.getColumnModel().getColumn(3).setPreferredWidth(234);
		incidentTable.getColumnModel().getColumn(4).setPreferredWidth(109);
		scrollPane.setViewportView(incidentTable);
	}
}

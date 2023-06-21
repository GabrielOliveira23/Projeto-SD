package client.pages.incident;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import client.JsonClientTreatment;
import config.ConnectionLogic;
import entities.User;
import utils.GeneralFunctions;
import utils.IncidentTypeEnum;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;

public class MyIncidentsPage extends JFrame {
	private JTable incidentTable;

	private User userRepository;
	private JFrame previousPage;

	public MyIncidentsPage(User userRepository, JFrame home) {
		super("Minha Lista de Incidentes");
		this.userRepository = userRepository;
		this.previousPage = home;
		initComponents();
		this.getIncidents();
		this.setVisible(true);
	}

	public void getIncidents() {
		JsonObject response = ConnectionLogic.getUserIncidents(
				userRepository.getToken(), userRepository.getId());

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
			if (item.getAsJsonObject().has("id_incidente")) {
				model.addRow(new Object[] {
						item.getAsJsonObject().get("id_incidente").getAsInt(),
						item.getAsJsonObject().get("rodovia").getAsString(),
						item.getAsJsonObject().get("km").getAsInt(),
						IncidentTypeEnum.getDescription(item.getAsJsonObject().get("tipo_incidente").getAsInt()),
						GeneralFunctions.getParsedDate(item.getAsJsonObject().get("data").getAsString())
				});
			}
		}
	}

	private void removeIncident() {
		try {
			if (incidentTable.getSelectedRow() == -1)
				throw new Exception("Selecione um incidente para remover!");

			int id = (int) incidentTable.getValueAt(incidentTable.getSelectedRow(), 0);
			JsonObject response = ConnectionLogic.deleteIncident(
					userRepository.getToken(), userRepository.getId(), id);

			System.out.println("Resposta do servidor: " + response);

			if (!JsonClientTreatment.responseTreatment(response))
				return;

			this.getIncidents();
		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().isEmpty())
				e = new Exception("Erro ao remover incidente!");
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateIncident() {
		try {
			if (incidentTable.getSelectedRow() == -1)
				throw new Exception("Selecione um incidente para atualizar!");
			int id = (int) incidentTable.getValueAt(incidentTable.getSelectedRow(), 0);
			new IncidentUpdatePage(userRepository, this, id);
			this.dispose();
		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().isEmpty())
				e = new Exception("Erro ao atualizar incidente!");
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initComponents() {
		this.setSize(800, 450);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Incidentes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(224, 72, 550, 328);
		getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 23, 530, 294);
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

		JLabel lblTitle = new JLabel("Meus Incidentes");
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 20, 784, 20);
		getContentPane().add(lblTitle);

		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(e -> {
			updateIncident();
		});
		btnAtualizar.setBounds(55, 149, 110, 35);
		getContentPane().add(btnAtualizar);

		JButton btnRemover = new JButton("Remover");
		btnRemover.setBounds(55, 199, 110, 35);
		btnRemover.addActionListener(e -> {
			removeIncident();
		});
		getContentPane().add(btnRemover);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(55, 249, 110, 35);
		btnCancelar.addActionListener(e -> {
			this.dispose();
			this.previousPage.setVisible(true);
		});
		getContentPane().add(btnCancelar);
	}
}

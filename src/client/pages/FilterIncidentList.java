package client.pages;

import javax.swing.JFrame;

import com.google.gson.JsonObject;

import client.ConnectionLogic;
import entities.User;
import javax.swing.JButton;

public class FilterIncidentList extends JFrame {
	private User userRepository;
	private HomePage homePage;

	public FilterIncidentList(User userRepository, HomePage homePage) {
		super("Filtrar Incidentes");
		this.userRepository = userRepository;
		this.homePage = homePage;

		this.initComponents();
		this.setVisible(true);
	}

	private void getIncidents() {
		String rodovia = "BR-222";
		String data = "2023-05-23 00:00:00";
		String faixa_km = "0-100";
		int periodo = 1;

		JsonObject response = ConnectionLogic.getIncidents(
				userRepository.getToken(), userRepository.getId(),
				rodovia, data, faixa_km, periodo);
		System.out.println("Resposta do servidor: " + response);

		if (response.get("codigo").getAsInt() == 200) {
			System.out.println("Incidentes obtidos com sucesso!");
			response.get("lista").getAsJsonArray().forEach(incident -> {
				System.out.println("Rodovia: " + ((JsonObject) incident).get("rodovia").getAsString());
			});
		} else {
			System.out.println("Erro ao obter incidentes!");
			// this.lblError.setText(response.get("mensagem").getAsString());
			// this.lblError.setVisible(true);
			return;
		}
	}

	private void initComponents() {
		this.setSize(450, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(255, 170, 110, 35);
		btnConfirmar.addActionListener(e -> {
			getIncidents();
		});
		getContentPane().add(btnConfirmar);
	}

}

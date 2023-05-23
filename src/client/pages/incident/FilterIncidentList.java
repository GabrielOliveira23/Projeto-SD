package client.pages.incident;

import javax.swing.JFrame;

import com.google.gson.JsonObject;

import client.pages.HomePage;
import config.ConnectionLogic;
import entities.User;
import utils.GeneralFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;
import javax.swing.JTextField;

public class FilterIncidentList extends JFrame {
	private User userRepository;
	private HomePage homePage;
	private JFormattedTextField highwayField;
	private JTextField minKmField;
	private JTextField maxKmField;

	private MaskFormatter highwayMask;

	public FilterIncidentList(User userRepository, HomePage homePage) {
		super("Filtrar Incidentes");
		this.userRepository = userRepository;
		this.homePage = homePage;

		try {
			highwayMask = new MaskFormatter("UU-###");
			highwayMask.setPlaceholderCharacter('_');
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.initComponents();
		this.setVisible(true);
	}

	private void getIncidents() {
		String faixaKm = null;

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

		JsonObject response = ConnectionLogic.getIncidents(
				userRepository.getToken(), userRepository.getId(),
				highwayField.getText(), data, faixaKm, periodo);
		System.out.println("Resposta do servidor: " + response);

		if (response.get("codigo").getAsInt() == 200) {
			if (response.has("mensagem"))
				System.out.println(response.get("mensagem").getAsString());
			else {
				System.out.println("Incidentes obtidos com sucesso!");
				response.get("lista_incidentes").getAsJsonArray().forEach(incident -> {
					System.out.println("Rodovia: " + ((JsonObject) incident).get("rodovia").getAsString());
				});
			}
		} else {
			System.out.println("Erro ao obter incidentes!");
			return;
		}
	}

	private void initComponents() {
		this.setSize(350, 280);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(170, 180, 110, 35);
		btnConfirmar.addActionListener(e -> getIncidents());
		getContentPane().add(btnConfirmar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(40, 180, 110, 35);
		btnCancelar.addActionListener(e -> this.dispose());
		getContentPane().add(btnCancelar);

		JLabel lblEspecificarIncidente = new JLabel("Especificar Incidente");
		lblEspecificarIncidente.setHorizontalAlignment(SwingConstants.CENTER);
		lblEspecificarIncidente.setFont(new Font("Dialog", Font.BOLD, 20));
		lblEspecificarIncidente.setBounds(0, 20, 334, 20);
		getContentPane().add(lblEspecificarIncidente);

		JLabel lblRodovia = new JLabel("Rodovia");
		lblRodovia.setFont(new Font("Dialog", Font.BOLD, 16));
		lblRodovia.setBounds(20, 80, 70, 20);
		getContentPane().add(lblRodovia);

		highwayField = new JFormattedTextField(highwayMask);
		highwayField.setColumns(10);
		highwayField.setBounds(160, 81, 160, 25);
		getContentPane().add(highwayField);

		JLabel lblEntre = new JLabel("Entre km");
		lblEntre.setFont(new Font("Dialog", Font.BOLD, 16));
		lblEntre.setBounds(20, 120, 68, 21);
		getContentPane().add(lblEntre);

		minKmField = new JTextField();
		minKmField.setColumns(10);
		minKmField.setBounds(110, 117, 80, 25);
		getContentPane().add(minKmField);

		JLabel lblE = new JLabel("e");
		lblE.setFont(new Font("Dialog", Font.BOLD, 16));
		lblE.setBounds(210, 120, 10, 20);
		getContentPane().add(lblE);

		maxKmField = new JTextField();
		maxKmField.setColumns(10);
		maxKmField.setBounds(240, 117, 80, 25);
		getContentPane().add(maxKmField);
	}
}

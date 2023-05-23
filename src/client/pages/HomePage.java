package client.pages;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import config.ConnectionLogic;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JButton;

import entities.User;

public class HomePage extends JFrame {
	private JLabel lblError;
	private User userRepository;
	private HomePage homePage;

	public HomePage(User userRepository) {
		super("HomePage");
		this.userRepository = userRepository;
		this.homePage = this;

		this.initComponents();
		this.setVisible(true);
	}

	private void logout() {
		this.lblError.setVisible(false);
		JsonObject response = ConnectionLogic.logout(userRepository.getToken(), userRepository.getId());
		System.out.println("Resposta do servidor: " + response);

		if (response.get("codigo").getAsInt() == 200) {
			System.out.println("Logout realizado com sucesso!");
		} else {
			System.out.println("Erro ao realizar logout!");
			this.lblError.setText(response.get("mensagem").getAsString());
			this.lblError.setVisible(true);
			return;
		}

		this.userRepository = null;
		new LoginPage();
		this.dispose();
	}

	private void initComponents() {
		this.setSize(600, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel titleLabel = new JLabel("Home");
		titleLabel.setFont(new Font("URW Bookman L", Font.BOLD, 26));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(0, 11, 584, 38);
		getContentPane().add(titleLabel);

		JPanel panel = new JPanel();
		panel.setBounds(0, 60, 584, 237);
		getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnReport = new JButton("Reportar Incidentes");
		btnReport.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnReport.setBounds(350, 10, 150, 34);
		btnReport.addActionListener(e -> {
			this.lblError.setVisible(false);
			new IncidentReportPage(userRepository, homePage);
			setVisible(false);
		});
		panel.add(btnReport);

		JButton btnUpdateRegister = new JButton("Atualizar Cadastro");
		btnUpdateRegister.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnUpdateRegister.setBounds(93, 10, 150, 34);
		btnUpdateRegister.addActionListener(e -> {
			this.lblError.setVisible(false);
			new UpdateUserPage(userRepository, homePage);
			setVisible(false);
		});
		panel.add(btnUpdateRegister);

		JButton btnIncidentsList = new JButton("Lista de Incidentes");
		btnIncidentsList.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnIncidentsList.setBounds(350, 70, 150, 34);
		btnIncidentsList.addActionListener(e -> {
			this.lblError.setVisible(false);
			new FilterIncidentList(userRepository, homePage);
		});
		panel.add(btnIncidentsList);

		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(10, 327, 89, 23);
		btnLogout.addActionListener(e -> logout());
		getContentPane().add(btnLogout);

		lblError = new JLabel("New label");
		lblError.setBounds(109, 329, 285, 19);
		lblError.setVisible(false);
		getContentPane().add(lblError);
	}
}

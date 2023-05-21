package client.pages;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import client.ConnectionLogic;

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

		this.setSize(600, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.initComponents();
		this.setVisible(true);
	}

	private void logout() {
		JsonObject response = ConnectionLogic.logout(userRepository.getToken(), userRepository.getId());
		System.out.println("Resposta do servidor: " + response);

		if (response.get("codigo").getAsInt() == 200) {
			System.out.println("Logout realizado com sucesso!");
		} else {
			System.out.println("Erro ao realizar logout!");
			this.lblError.setText(response.get("mensagem").getAsString());
			this.lblError.setVisible(true);
		}

		this.userRepository = null;
		new LoginPage();
		this.dispose();
	}

	private void initComponents() {
		getContentPane().setLayout(null);

		JLabel lblHome = new JLabel("Home");
		lblHome.setFont(new Font("OCR A Extended", Font.BOLD, 26));
		lblHome.setHorizontalAlignment(SwingConstants.CENTER);
		lblHome.setBounds(0, 11, 584, 38);
		getContentPane().add(lblHome);

		JPanel panel = new JPanel();
		panel.setBounds(0, 60, 584, 56);
		getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnReport = new JButton("Reportar Incidentes");
		btnReport.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnReport.setBounds(350, 11, 150, 34);
		panel.add(btnReport);

		JButton btnUpdateRegister = new JButton("Atualizar Cadastro");
		btnUpdateRegister.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnUpdateRegister.setBounds(93, 11, 150, 34);
		btnUpdateRegister.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new UpdateUserPage(userRepository, homePage);
				setVisible(false);
			}
		});
		panel.add(btnUpdateRegister);

		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(10, 327, 89, 23);
		btnLogout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logout();
			}
		});
		getContentPane().add(btnLogout);

		lblError = new JLabel("New label");
		lblError.setBounds(109, 329, 285, 19);
		lblError.setVisible(false);
		getContentPane().add(lblError);
	}
}

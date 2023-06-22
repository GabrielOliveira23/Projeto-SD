package client.pages;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import client.pages.incident.ListIncidentPage;
import client.pages.incident.MyIncidentsPage;
import client.pages.incident.IncidentReportPage;
import client.pages.user.DeleteUserPage;
import client.pages.user.LoginPage;
import client.pages.user.UpdateUserPage;
import config.ClientLogic;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JButton;

import entities.User;

public class HomePage extends JFrame {
	private User userRepository;

	public HomePage(User userRepository) {
		super("Home Page");
		this.userRepository = userRepository;

		this.initComponents();
		this.setVisible(true);
	}

	private void logout() {
		JsonObject response = ClientLogic.logout(userRepository.getToken(), userRepository.getId());
		System.out.println("Resposta do servidor: " + response);

		if (response.get("codigo").getAsInt() == 200) {
			System.out.println("Logout realizado com sucesso!");
		} else if (response.get("codigo").getAsInt() == 500) {
			JOptionPane.showMessageDialog(null, response.get("mensagem").getAsString(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			JOptionPane.showMessageDialog(null, "Erro desconhecido", "Erro", JOptionPane.ERROR_MESSAGE);
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
			new IncidentReportPage(userRepository, this);
			setVisible(false);
		});
		panel.add(btnReport);

		JButton btnUpdateRegister = new JButton("Atualizar Cadastro");
		btnUpdateRegister.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnUpdateRegister.setBounds(93, 10, 150, 34);
		btnUpdateRegister.addActionListener(e -> {
			new UpdateUserPage(userRepository, this);
			setVisible(false);
		});
		panel.add(btnUpdateRegister);

		JButton btnIncidentsList = new JButton("Lista de Incidentes");
		btnIncidentsList.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnIncidentsList.setBounds(350, 70, 150, 34);
		btnIncidentsList.addActionListener(e -> {
			new ListIncidentPage(userRepository, this);
			setVisible(false);
		});
		panel.add(btnIncidentsList);

		JButton btnMeusIncidentes = new JButton("Meus Incidentes");
		btnMeusIncidentes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnMeusIncidentes.setBounds(93, 70, 150, 34);
		btnMeusIncidentes.addActionListener(e -> {
			new MyIncidentsPage(userRepository, this);
			setVisible(false);
		});
		panel.add(btnMeusIncidentes);

		JButton btnExcluirCadastro = new JButton("Excluir Cadastro");
		btnExcluirCadastro.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnExcluirCadastro.setBounds(219, 163, 150, 34);
		btnExcluirCadastro.addActionListener(e -> {
			new DeleteUserPage(userRepository, this);
			setVisible(false);
		});
		panel.add(btnExcluirCadastro);

		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(10, 327, 89, 23);
		btnLogout.addActionListener(e -> logout());
		getContentPane().add(btnLogout);
	}
}

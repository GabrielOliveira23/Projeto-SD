package client.pages.user;

import javax.swing.JFrame;
import javax.swing.JPasswordField;

import com.google.gson.JsonObject;

import client.pages.HomePage;
import config.ClientLogic;
import entities.User;
import utils.CaesarCrypt;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class UpdateUserPage extends JFrame {
	private User userRepository;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JTextField nameField;
	private JLabel lblError;

	public UpdateUserPage(User user, HomePage homePage) {
		super("Atualizar Cadastro");
		this.userRepository = user;

		this.initComponents(homePage);
		this.setVisible(true);
	}

	private void confirmForm(HomePage homePage) {
		if (emailField.getText().isEmpty() && passwordField.getPassword().length == 0
				&& nameField.getText().isEmpty()) {
			System.out.println("Preencha pelo menos um dos campos!");
			this.lblError.setText("Preencha pelo menos um dos campos!");
			this.lblError.setVisible(true);
			return;
		}

		userRepository.setName(nameField.getText());
		userRepository.setEmail(emailField.getText());
		userRepository.setPassword(CaesarCrypt.hashed(new String(passwordField.getPassword())));

		JsonObject response = ClientLogic.updateUser(userRepository);
		System.out.println("Resposta do servidor: " + response);

		try {
			if (response.get("codigo").getAsInt() == 200) {
				System.out.println("Atualizado com sucesso!");
				userRepository.setToken(response.get("token").getAsString());
			} else if (response.get("codigo").getAsInt() == 500) {
				System.out.println("Erro ao atualizar!");
				this.lblError.setText(response.get("mensagem").getAsString());
				this.lblError.setVisible(true);
				return;
			}
		} catch (Exception e) {
			System.out.println("Json enviado pelo servidor esta quebrado");
			this.lblError.setText("Json recebido invalido");
			this.lblError.setVisible(true);
			return;
		}

		homePage.setVisible(true);
		dispose();
	}

	public void initComponents(HomePage homePage) {
		this.setSize(440, 320);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblNovoEmail = new JLabel("Novo Email");
		lblNovoEmail.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNovoEmail.setBounds(30, 90, 101, 25);
		getContentPane().add(lblNovoEmail);

		JLabel lblNovaSenha = new JLabel("Nova Senha");
		lblNovaSenha.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNovaSenha.setBounds(30, 140, 106, 25);
		getContentPane().add(lblNovaSenha);

		emailField = new JTextField();
		emailField.setBounds(190, 90, 215, 25);
		getContentPane().add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(190, 140, 215, 25);
		getContentPane().add(passwordField);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(235, 220, 140, 40);
		btnConfirmar.addActionListener(e -> confirmForm(homePage));
		getContentPane().add(btnConfirmar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				homePage.setVisible(true);
				dispose();
			}
		});
		btnCancelar.setBounds(60, 220, 140, 40);
		getContentPane().add(btnCancelar);

		JLabel lblNovoNome = new JLabel("Novo Nome");
		lblNovoNome.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNovoNome.setBounds(30, 40, 116, 25);
		getContentPane().add(lblNovoNome);

		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(190, 40, 215, 25);
		getContentPane().add(nameField);
		
		lblError = new JLabel("Erro");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setBounds(0, 185, 430, 15);
		lblError.setVisible(false);
		getContentPane().add(lblError);
	}
}

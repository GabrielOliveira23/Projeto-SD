package client.pages.user;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import config.ClientLogic;
import utils.CaesarCrypt;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

public class RegisterPage extends JFrame {
	private LoginPage loginPage;
	private JTextField nameField;
	private JTextField emailField;
	private JPasswordField passwordField;

	public RegisterPage(LoginPage loginPage) {
		super("Cadastro");
		this.loginPage = loginPage;
		initComponents();
		this.setVisible(true);
	}

	private void confirmForm() {
		try {
			if (nameField.getText().isEmpty() || emailField.getText().isEmpty()
					|| passwordField.getPassword().length == 0)
				throw new Exception("Preencha todos os campos!");

			JsonObject response = ClientLogic.register(
					nameField.getText(),
					emailField.getText(),
					CaesarCrypt.hashed(new String(passwordField.getPassword())));
			System.out.println("Resposta do servidor: " + response);

			if (response.get("codigo").getAsInt() == 500)
				throw new Exception(response.get("mensagem").getAsString());
			else if (response.get("codigo").getAsInt() != 200)
				throw new Exception("Erro de codigo desconhecido");

			System.out.println("Cadastrado com sucesso!");
			JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
		} catch (Exception e) {
			System.out.println("Erro ao realizar cadastro: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Erro ao realizar cadastro: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.loginPage.setVisible(true);
		dispose();
	}

	private void initComponents() {
		this.setSize(440, 330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel registerLabel = new JLabel("Cadastro");
		registerLabel.setFont(new Font("URW Bookman L", Font.BOLD, 30));
		registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		registerLabel.setBounds(0, 10, 424, 45);
		getContentPane().add(registerLabel);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Dialog", Font.BOLD, 18));
		lblNome.setBounds(30, 72, 70, 15);
		getContentPane().add(lblNome);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Dialog", Font.BOLD, 18));
		lblEmail.setBounds(30, 122, 70, 15);
		getContentPane().add(lblEmail);

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Dialog", Font.BOLD, 18));
		lblSenha.setBounds(30, 172, 70, 15);
		getContentPane().add(lblSenha);

		nameField = new JTextField();
		nameField.setBounds(190, 72, 215, 25);
		getContentPane().add(nameField);
		nameField.setColumns(10);

		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(190, 122, 215, 25);
		getContentPane().add(emailField);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(190, 172, 215, 25);
		getContentPane().add(passwordField);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(60, 240, 140, 40);
		btnCancelar.addActionListener(e -> {
			loginPage.setVisible(true);
			dispose();
		});
		getContentPane().add(btnCancelar);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(240, 240, 140, 40);
		btnConfirmar.addActionListener(e -> confirmForm());
		getContentPane().add(btnConfirmar);
	}
}

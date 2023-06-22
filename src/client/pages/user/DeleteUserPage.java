package client.pages.user;

import javax.swing.JFrame;

import com.google.gson.JsonObject;

import config.ClientLogic;
import entities.User;
import utils.CaesarCrypt;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class DeleteUserPage extends JFrame {
	private JTextField emailField;
	private JPasswordField passwordField;

	private User userRepository;
	private JFrame previousPage;

	public DeleteUserPage(User userRepository, JFrame previousPage) {
		super("Deletar Usuario");
		this.userRepository = userRepository;
		this.previousPage = previousPage;

		this.initComponents();
		this.setVisible(true);
	}

	private void confirmForm() {
		try {
			if (emailField.getText().isEmpty()
					|| passwordField.getPassword().length == 0)
				throw new Exception("Preencha todos os campos!");

			userRepository.setEmail(emailField.getText());
			userRepository.setPassword(CaesarCrypt.hashed(new String(passwordField.getPassword())));

			JsonObject response = ClientLogic.deleteUser(userRepository);

			System.out.println("Resposta do servidor: " + response);

			if (response.get("codigo").getAsInt() == 200) {
				JOptionPane.showMessageDialog(null, "Usuario deletado com sucesso", "Sucesso",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (response.get("codigo").getAsInt() == 500) {
				throw new Exception(response.get("mensagem").getAsString());
			} else {
				throw new Exception();
			}

			new LoginPage();
			dispose();
		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().isEmpty())
				e = new Exception("Erro ao deletar usuario");
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initComponents() {
		setSize(440, 290);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel registerLabel = new JLabel("Confirmar Exclusão");
		registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		registerLabel.setFont(new Font("Dialog", Font.BOLD, 30));
		registerLabel.setBounds(0, 0, 424, 45);
		getContentPane().add(registerLabel);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Dialog", Font.BOLD, 18));
		lblEmail.setBounds(30, 71, 70, 15);
		getContentPane().add(lblEmail);

		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(180, 71, 215, 25);
		getContentPane().add(emailField);

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Dialog", Font.BOLD, 18));
		lblSenha.setBounds(30, 121, 70, 15);
		getContentPane().add(lblSenha);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(180, 121, 215, 25);
		getContentPane().add(passwordField);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(235, 182, 140, 40);
		btnConfirmar.addActionListener(e -> confirmForm());
		getContentPane().add(btnConfirmar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(55, 182, 140, 40);
		btnCancelar.addActionListener(e -> {
			previousPage.setVisible(true);
			dispose();
		});
		getContentPane().add(btnCancelar);
	}
}

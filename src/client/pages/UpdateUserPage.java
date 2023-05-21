package client.pages;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonObject;

import client.ConnectionLogic;
import entities.User;
import utils.CaesarCrypt;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UpdateUserPage extends JFrame {
	private User userRepository;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JTextField nameField;

	public UpdateUserPage(User user, HomePage homePage) {
		super("Atualizar Cadastro");
		this.userRepository = user;

		this.setSize(440, 320);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.initComponents(homePage);
		this.setVisible(true);
	}

	private void confirmForm(HomePage homePage) {
		if (emailField.getText().isEmpty() && passwordField.getPassword().length == 0 && nameField.getText().isEmpty()) {
			System.out.println("Preencha pelo menos um dos campos!");
			return;
		}

		userRepository.setName(nameField.getText());
		userRepository.setEmail(emailField.getText());
		userRepository.setPassword(CaesarCrypt.hashed(new String(passwordField.getPassword())));

		JsonObject response = ConnectionLogic.updateUser(userRepository);
		System.out.println("Resposta do servidor: " + response);

		if (response.get("codigo").getAsInt() == 200) {
			System.out.println("Atualizado com sucesso!");
		} else {
			System.out.println("Erro ao atualizar!");
		}

		homePage.setVisible(true);
		dispose();
	}

	public void initComponents(HomePage homePage) {

		JLabel lblNovoEmail = new JLabel("Novo Email");
		lblNovoEmail.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNovoEmail.setBounds(30, 140, 101, 25);
		getContentPane().add(lblNovoEmail);

		JLabel lblNovaSenha = new JLabel("Nova Senha");
		lblNovaSenha.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNovaSenha.setBounds(30, 90, 106, 25);
		getContentPane().add(lblNovaSenha);

		emailField = new JTextField();
		emailField.setBounds(190, 140, 215, 25);
		getContentPane().add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(190, 90, 215, 25);
		getContentPane().add(passwordField);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(235, 210, 140, 40);
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmForm(homePage);
			}
		});
		getContentPane().add(btnConfirmar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				homePage.setVisible(true);
				dispose();
			}
		});
		btnCancelar.setBounds(60, 210, 140, 40);
		getContentPane().add(btnCancelar);
		
		JLabel lblNovoNome = new JLabel("Novo Nome");
		lblNovoNome.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNovoNome.setBounds(30, 40, 116, 25);
		getContentPane().add(lblNovoNome);
		
		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(190, 40, 215, 25);
		getContentPane().add(nameField);
	}
}

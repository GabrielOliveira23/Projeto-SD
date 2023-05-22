package client.pages;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

public class RegisterPage extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField emailField;
	private JTextField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterPage frame = new RegisterPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegisterPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCadastro = new JLabel("Cadastro");
		lblCadastro.setFont(new Font("Dialog", Font.BOLD, 30));
		lblCadastro.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastro.setBounds(0, 10, 480, 45);
		contentPane.add(lblCadastro);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Dialog", Font.BOLD, 18));
		lblNome.setBounds(30, 90, 70, 15);
		contentPane.add(lblNome);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Dialog", Font.BOLD, 18));
		lblEmail.setBounds(30, 140, 70, 15);
		contentPane.add(lblEmail);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Dialog", Font.BOLD, 18));
		lblSenha.setBounds(30, 190, 70, 15);
		contentPane.add(lblSenha);
		
		nameField = new JTextField();
		nameField.setBounds(190, 90, 215, 25);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(190, 140, 215, 25);
		contentPane.add(emailField);
		
		passwordField = new JTextField();
		passwordField.setColumns(10);
		passwordField.setBounds(190, 190, 215, 25);
		contentPane.add(passwordField);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(60, 240, 140, 40);
		contentPane.add(btnCancelar);
		
		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(240, 240, 140, 40);
		contentPane.add(btnConfirmar);
	}
}

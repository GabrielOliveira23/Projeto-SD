package client.pages.user;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import client.pages.HomePage;
import config.ClientLogic;

import java.awt.Font;

import entities.User;

import utils.CaesarCrypt;

public class LoginPage extends JFrame {
    private JLabel emailLabel;
    private JTextField textField;
    private JButton submitButton;
    private JButton registerButton;
    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private User user;

    public LoginPage() {
        super("Login");
        this.user = new User();
        this.initComponents();
        this.setVisible(true);
    }

    private void openRegisterPage() {
        new RegisterPage(this);
        this.setVisible(false);
    }

    private void submitLogin() {
        try {
            if (textField.getText().isEmpty() || passwordField.getPassword().length == 0)
                throw new Exception("Preencha todos os campos!");

            String email = textField.getText();
            String password = CaesarCrypt.hashed(new String(passwordField.getPassword()));

            System.out.println("Email: " + email);
            System.out.println("Senha: " + password);

            JsonObject response = ClientLogic.login(email, password);
            System.out.println("Resposta do servidor: " + response);

            if (response.get("codigo").getAsInt() == 500)
                throw new Exception(response.get("mensagem").getAsString());
            else if (response.get("codigo").getAsInt() != 200)
                throw new Exception("Erro de codigo desconhecido!");

            System.out.println("Login realizado com sucesso!");
            user.setId(response.get("id_usuario").getAsInt());
            user.setToken(response.get("token").getAsString());

            new HomePage(user);
            this.dispose();
        } catch (Exception e) {
            System.out.println("Erro ao realizar login: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao realizar login: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        this.setSize(500, 360);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        emailLabel.setBounds(60, 90, 79, 27);
        getContentPane().add(emailLabel);

        textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBounds(160, 94, 250, 27);
        getContentPane().add(textField);

        passwordLabel = new JLabel("Senha");
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordLabel.setBounds(60, 153, 80, 25);
        getContentPane().add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 156, 250, 27);
        getContentPane().add(passwordField);

        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setBounds(0, 15, 484, 43);
        getContentPane().add(titleLabel);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("URW Bookman L", Font.BOLD, 40));

        submitButton = new JButton("Entrar");
        submitButton.setBounds(267, 231, 102, 37);
        submitButton.addActionListener(e -> submitLogin());
        getContentPane().add(submitButton);

        registerButton = new JButton("Cadastro");
        registerButton.addActionListener(e -> openRegisterPage());
        registerButton.setBounds(139, 231, 102, 37);
        getContentPane().add(registerButton);
    }
}

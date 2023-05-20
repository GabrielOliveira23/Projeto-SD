package client.pages;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import client.ConnectionLogic;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import utils.CaesarCrypt;

public class LoginPage extends JFrame {
    private JLabel emailLabel;
    private JTextField textField;
    private JButton submitButton;
    private JButton registerButton;
    private JLabel passwordLabel;
    private JPasswordField passwordField;

    public LoginPage(String serverIp, int port) {
        super("IncidentSOS");
        initComponents();
        this.setVisible(true);
        ConnectionLogic.connect(serverIp, port);
    }

    private void openRegisterPage() {

    }

    private void submitLogin() {
        String email = textField.getText();
        String password = CaesarCrypt.hashed(new String(passwordField.getPassword()));

        System.out.println("Email: " + email);
        System.out.println("Senha: " + password);

        JsonObject response = ConnectionLogic.login(email, password);
        
        if (response.get("codigo").getAsInt() == 200) {
            System.out.println("Login realizado com sucesso!");
            System.out.println("Token: " + response.get("token").getAsString());
            System.out.println("Id: " + response.get("id_usuario").getAsInt());
        } else {
            System.out.println("Erro ao realizar login!");
            System.out.println("Status: " + response.get("status").getAsInt());
            System.out.println("Mensagem: " + response.get("mensagem").getAsString());
        }
    }

    private void initComponents() {
        getContentPane().setLayout(null);

        emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        emailLabel.setBounds(70, 90, 47, 27);
        getContentPane().add(emailLabel);

        textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBounds(139, 90, 230, 27);
        getContentPane().add(textField);

        submitButton = new JButton("Entrar");
        submitButton.setBounds(267, 231, 102, 37);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitLogin();
            }
        });
        getContentPane().add(submitButton);

        registerButton = new JButton("Cadastro");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRegisterPage();
            }
        });
        registerButton.setBounds(139, 231, 102, 37);
        getContentPane().add(registerButton);

        passwordLabel = new JLabel("Senha");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordLabel.setBounds(70, 153, 48, 25);
        getContentPane().add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(139, 153, 230, 27);
        getContentPane().add(passwordField);

        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setBounds(0, 15, 484, 43);
        getContentPane().add(titleLabel);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("OCR A Extended", Font.BOLD, 40));

        this.setSize(500, 360);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

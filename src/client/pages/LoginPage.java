package client.pages;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import client.ConnectionLogic;
import entities.User;

import utils.CaesarCrypt;

public class LoginPage extends JFrame {
    private JLabel emailLabel;
    private JTextField textField;
    private JButton submitButton;
    private JButton registerButton;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel lblError;

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
        String email = textField.getText();
        String password = CaesarCrypt.hashed(new String(passwordField.getPassword()));

        System.out.println("Email: " + email);
        System.out.println("Senha: " + password);

        JsonObject response = ConnectionLogic.login(email, password);
        System.out.println("Resposta do servidor: " + response);

        if (response.get("codigo").getAsInt() == 200) {
            System.out.println("Login realizado com sucesso!");
            user.setId(response.get("id_usuario").getAsInt());
            user.setToken(response.get("token").getAsString());

            new HomePage(user);
            this.dispose();
        } else {
            System.out.println("Erro ao realizar login!");
            this.lblError.setText(response.get("mensagem").getAsString());
            this.lblError.setVisible(true);
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

        lblError = new JLabel("Erro");
        lblError.setFont(new Font("Dialog", Font.BOLD, 12));
        lblError.setVerticalAlignment(SwingConstants.TOP);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setBounds(139, 200, 230, 15);
        lblError.setVisible(false);
        getContentPane().add(lblError);

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
    }
}

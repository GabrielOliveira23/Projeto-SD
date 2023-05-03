

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonObject;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton submitButton;
    private JButton registerButton;

    private JLabel titleLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;

    private BorderLayout layout;

    private JPanel formPanel;
    private JPanel buttonPanel;
    private JPanel emailPanel;
    private JPanel passwordPanel;

    public LoginPage() {
        super("LOGIN");

        initComponents();
    }

    private void initComponents() {
        this.layout = new BorderLayout(20, 20);
        this.setLayout(this.layout);

        this.formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        this.emailField = new JTextField();
        this.passwordField = new JPasswordField();

        this.submitButton = new JButton("Submit");
        this.submitButton.addActionListener(e -> {

            System.out.println("Email: " + this.emailField.getText());
            System.out.println("Password: " + this.passwordField.getPassword().toString());
        });

        this.registerButton = new JButton("Register");

        this.titleLabel = new JLabel("Login");
        this.emailLabel = new JLabel("Email");
        this.passwordLabel = new JLabel("Senha");

        this.emailPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        this.passwordPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        this.buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        this.emailPanel.add(this.emailLabel);
        this.emailPanel.add(this.emailField);

        this.passwordPanel.add(this.passwordLabel);
        this.passwordPanel.add(this.passwordField);

        this.buttonPanel.add(this.submitButton);
        this.buttonPanel.add(this.registerButton);

        this.formPanel.add(this.emailPanel);
        this.formPanel.add(this.passwordPanel);
        this.formPanel.add(this.buttonPanel);
        this.formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        this.add(this.titleLabel, BorderLayout.NORTH);
        this.add(this.formPanel, BorderLayout.CENTER);

        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

package server.pages;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ServerLogPage extends JFrame {
    public ServerLogPage() {
        super("Log do Servidor");
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        setBounds(80, 100, 500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblLog = new JLabel("Log do Servidor");
        lblLog.setHorizontalAlignment(SwingConstants.CENTER);
        lblLog.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblLog.setBounds(10, 11, 464, 25);
        getContentPane().add(lblLog);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 47, 464, 403);
        getContentPane().add(scrollPane);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText("LOG DO SERVIDOR:\n");
        scrollPane.setViewportView(textArea);
    }
}

package server.pages;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

public class LoggedUsersPage extends JFrame {
    private JTable usersTable;
    private JPanel panel;
    private JFrame connectionPage;
    private Thread serverThread;

    public LoggedUsersPage(JFrame connectionPage, Thread serverThread) {
        super("Usuarios Logados");
        this.connectionPage = connectionPage;
        this.serverThread = serverThread;

        this.initComponents();
        this.setVisible(true);
    }

    private void ExitServer() {
        if (JOptionPane.showConfirmDialog(null, "Deseja encerrar o servidor?", "Encerrar Servidor",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            serverThread.stop();
            dispose();
            connectionPage.setVisible(true);
        }
    }

    private void initComponents() {
        this.setSize(800, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Lista de Incidentes");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 784, 20);
        getContentPane().add(lblTitle);

        panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Incidentes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(210, 70, 550, 270);
        getContentPane().add(panel);
        panel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 23, 530, 236);
        panel.add(scrollPane);

        usersTable = new JTable();
        usersTable.setModel(new DefaultTableModel(
                new Object[][] {
                        { "-1", "Teste", "teste@gmail.com" },
                },
                new String[] {
                        "UserId", "Nome", "Email"
                }));
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(95);
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(236);
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(290);
        scrollPane.setViewportView(usersTable);

        JButton btnVerLog = new JButton("Ver Log");
        btnVerLog.setBounds(50, 150, 110, 35);
        btnVerLog.addActionListener(e -> new ServerLogPage());
        getContentPane().add(btnVerLog);

        JButton btnEncerrar = new JButton("Encerrar");
        btnEncerrar.setBounds(50, 210, 110, 35);
        btnEncerrar.addActionListener(e -> ExitServer());
        getContentPane().add(btnEncerrar);
    }
}

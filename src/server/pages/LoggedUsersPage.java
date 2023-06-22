package server.pages;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import entities.User;

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

    private void fillTable(JsonArray list) {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.setRowCount(0);
        model.fireTableDataChanged();

        for (JsonElement item : list) {
            model.addRow(new Object[] {
                    item.getAsJsonObject().get("id_usuario").getAsInt(),
                    item.getAsJsonObject().get("nome").getAsString(),
                    item.getAsJsonObject().get("email").getAsString()
            });
        }
    }

    private void getLoggedUsers() {
        User user = new User();

        try {
            JsonObject response = user.getLoggedUsers();

            if (!response.has("codigo"))
                throw new Exception("Json Invalido");

            if (response.get("codigo").getAsInt() == 500)
                throw new Exception(response.get("message").getAsString());

            JsonArray lista = response.get("usuarios").getAsJsonArray();
            

            if (lista != null) {
                fillTable(lista);
            } else 
                throw new Exception("Erro ao buscar usuarios logados");
            
        } catch (Exception e) {
            if (e.getMessage() == null || e.getMessage().isEmpty())
                e = new Exception("Erro ao buscar usuarios logados");
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        this.setSize(800, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Usuários Logados");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 784, 30);
        getContentPane().add(lblTitle);

        panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Usuarios", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(210, 70, 550, 270);
        getContentPane().add(panel);
        panel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 23, 530, 236);
        panel.add(scrollPane);

        usersTable = new JTable();
        usersTable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "UserId", "Nome", "Email"
                }));
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(95);
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(236);
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(290);
        scrollPane.setViewportView(usersTable);

        JButton btnVerLog = new JButton("Ver Log");
        btnVerLog.setBounds(50, 190, 110, 35);
        btnVerLog.addActionListener(e -> new ServerLogPage());
        getContentPane().add(btnVerLog);

        JButton btnEncerrar = new JButton("Encerrar");
        btnEncerrar.setBounds(50, 250, 110, 35);
        btnEncerrar.addActionListener(e -> ExitServer());
        getContentPane().add(btnEncerrar);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(50, 130, 110, 35);
        btnAtualizar.addActionListener(e -> getLoggedUsers());
        getContentPane().add(btnAtualizar);
    }
}

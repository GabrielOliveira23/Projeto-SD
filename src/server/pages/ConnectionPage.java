package server.pages;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import server.config.Connection;

import java.awt.Font;

public class ConnectionPage extends JFrame {
	private JTextField txtPort;
	private Connection serverThread;

	public ConnectionPage() {
		super("Conexao");
		serverThread = new Connection();
		this.initComponents();
		this.setVisible(true);
	}

	private void confirmForm() {
		int port;
		try {
			if (txtPort.getText().isEmpty())
				port = 24001;
			else
				port = Integer.parseInt(txtPort.getText());

			if (port < 15000 || port > 65000)
				throw new Exception("Porta invalida!");
			else {
				Thread chamaServer = new Thread(() -> {
					try {
						serverThread.connect(port);
					} catch (Exception ex) {
						// Lidar com possíveis exceções lançadas durante a inicialização do servidor
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Erro ao iniciar o servidor: " + ex.getMessage(), "Erro",
								JOptionPane.ERROR_MESSAGE);
					}
				});
				chamaServer.start();
				new LoggedUsersPage(this, chamaServer);
				this.dispose();
			}
		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().equals(""))
				e = new Exception("Erro ao conectar com o servidor!");
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initComponents() {
		setSize(310, 310);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Server Connection");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 31, 294, 27);
		getContentPane().add(lblNewLabel);

		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(115, 100, 151, 25);
		getContentPane().add(txtPort);

		JLabel lblPort = new JLabel("Porta");
		lblPort.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblPort.setBounds(20, 100, 86, 25);
		getContentPane().add(lblPort);

		JButton btnConfirm = new JButton("Confirmar");
		btnConfirm.setBounds(70, 170, 160, 40);
		btnConfirm.addActionListener(e -> confirmForm());
		getContentPane().add(btnConfirm);
	}
}

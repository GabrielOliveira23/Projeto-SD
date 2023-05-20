package pages;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;

public class ConnectionPage extends JFrame {

	private JPanel contentPane;
	private JTextField txtServerIP;
	private JTextField txtPort;
	private JButton btnConfirm;
	private JLabel lblErro;

	public ConnectionPage() {
		this.setSize(300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.initComponents();
		this.setVisible(true);
	}

	private void confirmForm() {
		try {
			String serverIP = txtServerIP.getText();
			int port = Integer.parseInt(txtPort.getText());

			if (port < 15000 || port > 65000) {
				System.out.println("Dialog Box - Preencha todos os campos!");
				this.lblErro.setText("Porta invalida!");
				this.lblErro.setVisible(true);
			} else {
				this.lblErro.setVisible(false);
				
				if (serverIP.isEmpty())
					serverIP = "127.0.0.1";
				LoginPage loginPage = new LoginPage(serverIP, port);
				loginPage.setVisible(true);
				dispose();
			}
		} catch (NumberFormatException e) {
			System.out.println("Dialog Box - Preencha todos os campos!");
			this.lblErro.setText("Preencha a Porta!");
			this.lblErro.setVisible(true);
		} catch (Exception e) {
			System.out.println("Dialog Box - Dados invalidos!");
			this.lblErro.setText("Erro desconhecido!");
			this.lblErro.setVisible(true);
		}
	}

	private void initComponents() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServerIP = new JLabel("IP Servidor");
		lblServerIP.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblServerIP.setBounds(10, 54, 86, 25);
		contentPane.add(lblServerIP);

		txtServerIP = new JTextField();
		txtServerIP.setBounds(106, 54, 151, 25);
		contentPane.add(txtServerIP);
		txtServerIP.setColumns(10);

		JLabel lblPort = new JLabel("Porta");
		lblPort.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblPort.setBounds(10, 110, 86, 25);
		contentPane.add(lblPort);

		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(106, 110, 151, 25);
		contentPane.add(txtPort);

		btnConfirm = new JButton("Confirmar");
		btnConfirm.setBounds(62, 170, 160, 40);
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmForm();
			}
		});
		contentPane.add(btnConfirm);

		lblErro = new JLabel("Erro");
		lblErro.setVerticalAlignment(SwingConstants.TOP);
		lblErro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblErro.setHorizontalAlignment(SwingConstants.CENTER);
		lblErro.setBounds(0, 221, 284, 40);
		lblErro.setVisible(false);
		contentPane.add(lblErro);
	}
}

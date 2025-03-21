package client.pages;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import client.pages.user.LoginPage;
import config.ClientLogic;

public class ConnectionPage extends JFrame {

	private JTextField txtServerIP;
	private JTextField txtPort;
	private JButton btnConfirm;
	private JLabel lblErro;

	public ConnectionPage() {
		super("Conexao");
		this.initComponents();
		this.setVisible(true);
	}

	private void confirmForm() throws InterruptedException {
		String serverIp = txtServerIP.getText();
		int port, countTry = 0;

		try {
			port = Integer.parseInt(txtPort.getText());
		} catch (NumberFormatException e) {
			port = 24001;
		}

		if (port < 15000 || port > 65000) {
			System.out.println("Dialog Box - Preencha todos os campos!");
			this.lblErro.setText("Porta invalida!");
			this.lblErro.setVisible(true);
		} else {
			this.lblErro.setVisible(false);

			while (!ClientLogic.connect(serverIp, port)) {
				this.lblErro.setText("Servidor nao encontrado!");
				this.lblErro.setVisible(true);
				Thread.sleep(500);
				countTry++;

				if (countTry == 3) {
					JOptionPane.showMessageDialog(null, "Tente novamente mais tarde!", "Erro",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			new LoginPage();
			this.dispose();
		}

	}

	private void initComponents() {
		this.setSize(310, 310);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblServerIP = new JLabel("IP Servidor");
		lblServerIP.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblServerIP.setBounds(10, 54, 126, 25);
		getContentPane().add(lblServerIP);

		txtServerIP = new JTextField();
		txtServerIP.setBounds(134, 55, 151, 25);
		getContentPane().add(txtServerIP);
		txtServerIP.setColumns(10);

		JLabel lblPort = new JLabel("Porta");
		lblPort.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblPort.setBounds(10, 110, 86, 25);
		getContentPane().add(lblPort);

		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(134, 111, 151, 25);
		getContentPane().add(txtPort);

		btnConfirm = new JButton("Confirmar");
		btnConfirm.setBounds(70, 170, 160, 40);
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					confirmForm();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		getContentPane().add(btnConfirm);

		lblErro = new JLabel("Erro");
		lblErro.setVerticalAlignment(SwingConstants.TOP);
		lblErro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblErro.setHorizontalAlignment(SwingConstants.CENTER);
		lblErro.setBounds(0, 221, 284, 40);
		lblErro.setVisible(false);
		getContentPane().add(lblErro);
	}
}

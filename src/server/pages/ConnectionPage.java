package server.pages;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ConnectionPage extends JFrame {

	private JPanel contentPane;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionPage frame = new ConnectionPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConnectionPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(310, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Server Connection");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 31, 294, 27);
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(115, 100, 151, 25);
		contentPane.add(textField_1);
		
		JLabel lblPort = new JLabel("Porta");
		lblPort.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblPort.setBounds(20, 100, 86, 25);
		contentPane.add(lblPort);
		
		JButton btnConfirm = new JButton("Confirmar");
		btnConfirm.setBounds(70, 170, 160, 40);
		contentPane.add(btnConfirm);
	}
}

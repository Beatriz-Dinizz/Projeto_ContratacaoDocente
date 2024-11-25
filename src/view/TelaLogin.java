package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import controller.LoginController;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class TelaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textLoginUsuario;
	private JTextField textLoginSenha;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JButton btnAcessar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLogin frame = new TelaLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TelaLogin() {
		setTitle("Contratação de Docentes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 759, 566);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		textLoginUsuario = new JTextField();
		textLoginUsuario.setBounds(292, 281, 161, 25);
		contentPane.add(textLoginUsuario);
		textLoginUsuario.setColumns(10);

		textLoginSenha = new JTextField();
		textLoginSenha.setBounds(292, 336, 161, 25);
		contentPane.add(textLoginSenha);
		textLoginSenha.setColumns(10);

		JLabel lblUsuario = new JLabel("Usuário");
		lblUsuario.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblUsuario.setBounds(215, 283, 67, 19);
		contentPane.add(lblUsuario);

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblSenha.setBounds(215, 338, 67, 19);
		contentPane.add(lblSenha);

		JButton btnAcessar = new JButton("Acessar");
		btnAcessar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnAcessar.setBounds(323, 412, 97, 30);
		contentPane.add(btnAcessar);

		lblNewLabel = new JLabel("Sistema Contratação de Docentes");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 18));
		lblNewLabel.setBounds(235, 22, 281, 25);
		contentPane.add(lblNewLabel);

		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\beatr\\eclipse-workspace\\Projeto_ContratacaoDocente\\src\\imagens\\FATEC_ZONA_LESTE_LOGIN.png"));
		lblNewLabel_1.setBounds(215, 64, 320, 156);
		contentPane.add(lblNewLabel_1);

		btnAcessar.addActionListener(e -> {
			try {
				abrirSistema();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	private void abrirSistema() throws Exception {
		LoginController login = new LoginController(textLoginUsuario, textLoginSenha, btnAcessar);
		
		if (login.acessar()) {
			Telas telas = new Telas();
			telas.setVisible(true);
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Acesso inválido. Verifique se o usuário e senha digitados estão corretos.");
		}
	}
}

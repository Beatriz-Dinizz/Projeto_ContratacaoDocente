package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import model.Login;

public class LoginController implements ActionListener {
	private JTextField textLoginUsuario;
	private JTextField textLoginSenha;

	public LoginController(JTextField textLoginUsuario, JTextField textLoginSenha, JButton btnAcessar) {
		this.textLoginUsuario = textLoginUsuario;
		this.textLoginSenha = textLoginSenha;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals("Acessar")) {
			try {
				acessar();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean acessar() throws Exception {
		boolean valido = false;
		Login login = new Login();		
		
		login.usuario = textLoginUsuario.getText();
		login.senha = textLoginSenha.getText();
		
		if (!login.usuario.equals("") && !login.senha.equals("")) {	
			return validarAcesso(login);	
		}
		else {
			valido = false;
		}		
		return valido;
	}

	private boolean validarAcesso(Login login) {
		boolean acessoValido = false;
		String usuario = "admin";
		String senha = "fateczl";
		
		if (login.usuario.equals(usuario) && login.senha.equals(senha)) {
			acessoValido = true;
		} 
		return acessoValido;
	}	
}

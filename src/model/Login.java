package model;

public class Login {
	public String usuario;
	public String senha;
	
	@Override
	public String toString() {
		return usuario + ";" + senha;
	}
}

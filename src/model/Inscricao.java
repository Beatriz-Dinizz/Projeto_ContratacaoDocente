package model;

public class Inscricao {
	public String cpfProfessor;
	public String codigoDisciplina;
	public String codigoProcesso;
	
	@Override
	public String toString() {
		return cpfProfessor + ";" + codigoDisciplina + ";" + codigoProcesso;
	}
}

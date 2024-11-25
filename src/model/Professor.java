package model;

public class Professor {
	public String cpfProfessor;
	public String nomeProfessor;
	public String areaInteresse;
	public String pontos;
	
	@Override
	public String toString() {
		return cpfProfessor + ";" + nomeProfessor + ";" + areaInteresse + ";" + pontos;
	}
}

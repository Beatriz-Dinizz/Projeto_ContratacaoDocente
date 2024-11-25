package model;

public class ConsultaInscrito {
	public String cpfProfessor;
	public String nomeProfessor;
	public String areaInteresse;
	public String pontos;
	
	public int getPontos() {
        return Integer.parseInt(pontos);
    }

    @Override
    public String toString() {
        return cpfProfessor + ";" + nomeProfessor + ";" + areaInteresse + ";" + pontos;
    }
}

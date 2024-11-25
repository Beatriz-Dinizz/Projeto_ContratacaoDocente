package model;

public class Curso {
	public String codigoCurso;
	public String nomeCurso;
	public String areaConhecimento;
	
	@Override
	public String toString() {
		return codigoCurso + ";" + nomeCurso + ";" + areaConhecimento;
	}
}

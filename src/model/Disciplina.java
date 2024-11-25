package model;

public class Disciplina {
	public String codigoDisciplina;
	public String nomeDisciplina;
	public String diaSemana;
	public String horarioInicio;
	public String horasDiarias;
	public String codigoCurso;
	public String codigoProcesso;

	@Override
	public String toString() {
		return codigoDisciplina + ";" + nomeDisciplina + ";" + diaSemana + ";" + horarioInicio + ";" + horasDiarias + ";" + codigoCurso + ";" + codigoProcesso;
	}
}

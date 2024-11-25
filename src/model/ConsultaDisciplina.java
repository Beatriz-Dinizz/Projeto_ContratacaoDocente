package model;

import java.util.Objects;

public class ConsultaDisciplina {
	public String codigoDisciplina;
	public String nomeDisciplina;
	public String diaSemana;
	public String horarioInicio;
	public String horasDiarias;
	public String codigoCurso;
	public String codigoProcesso;	
	public String nomeCurso;
	public String areaConhecimento;
		
	@Override
	public String toString() {
		return "ConsultaDisciplina [codigoDisciplina=" + codigoDisciplina + ", nomeDisciplina=" + nomeDisciplina
				+ ", diaSemana=" + diaSemana + ", horarioInicio=" + horarioInicio + ", horasDiarias=" + horasDiarias
				+ ", codigoCurso=" + codigoCurso + ", codigoProcesso=" + codigoProcesso + ", nomeCurso=" + nomeCurso
				+ ", areaConhecimento=" + areaConhecimento + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsultaDisciplina other = (ConsultaDisciplina) obj;
		return Objects.equals(areaConhecimento, other.areaConhecimento)
				&& Objects.equals(codigoCurso, other.codigoCurso)
				&& Objects.equals(codigoDisciplina, other.codigoDisciplina)
				&& Objects.equals(codigoProcesso, other.codigoProcesso) && Objects.equals(diaSemana, other.diaSemana)
				&& Objects.equals(horarioInicio, other.horarioInicio)
				&& Objects.equals(horasDiarias, other.horasDiarias) && Objects.equals(nomeCurso, other.nomeCurso)
				&& Objects.equals(nomeDisciplina, other.nomeDisciplina);
	}	
	
    public int hashCode(String key) {
        int posicao = key.toLowerCase().charAt(0);
        posicao = posicao - 48; //ascii do zero
        
        return posicao;
    }
}

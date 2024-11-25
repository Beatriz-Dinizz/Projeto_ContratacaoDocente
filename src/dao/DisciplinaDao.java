package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import br.edu.fateczl.Lista;
import model.Disciplina;

public class DisciplinaDao {
	
	public Disciplina buscaCodigoDisciplina (String codigoDisciplina) throws Exception {
		Disciplina disciplina = new Disciplina();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");
		
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();
			
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				
				if (vetLinha[0].equals(disciplina.codigoDisciplina)) {
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];
					break;
				}				
				linha = buffer.readLine();
			}			
			buffer.close();
			isr.close();
			fis.close();
		}
		if (disciplina.codigoDisciplina == null) {
			disciplina = null;
		}
		return disciplina;
	}
	
	public void adicionarDisciplinaLista(Lista<Disciplina> disciplinas, Disciplina disciplina) throws Exception {
		if (disciplinas.size() == 0) {
			disciplinas.addFirst(disciplina);
		} else {
			disciplinas.addLast(disciplina);
		}
	}

	public void GravarDisciplina(File arq, String csvDisciplina, boolean existe) throws IOException {
		FileWriter fw = new FileWriter(arq, existe);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvDisciplina + "\r\n");
		pw.flush();
		pw.close();
		fw.close();
	}
}

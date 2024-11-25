package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import br.edu.fateczl.Lista;
import model.Professor;

public class ProfessorDao {
	
	public Professor buscaCpfProfessor(String cpfProfessor) throws Exception {
		Professor professor = new Professor();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Professor.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[0].equals(cpfProfessor)) {
					professor.cpfProfessor = vetLinha[0];
					professor.nomeProfessor = vetLinha[1];
					professor.areaInteresse = vetLinha[2];
					professor.pontos = vetLinha[3];
					break;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		if (professor.cpfProfessor == null) {
			professor = null;
		}
		return professor;
	}
	
	public void adicionarProfessorLista(Lista<Professor> professores, Professor professor) throws Exception {
		if (professores.size() == 0) {
			professores.addFirst(professor);
		} else {
			professores.addLast(professor);
		}
	}
	
	public void GravarProfessor(File arq, String csvProfessor, boolean existe) throws IOException{
		FileWriter fw = new FileWriter(arq, existe);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvProfessor + "\r\n");
		pw.flush();
		pw.close();
		fw.close();
	}
}

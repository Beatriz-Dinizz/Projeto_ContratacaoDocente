package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import br.edu.fateczl.Lista;
import model.Curso;

public class CursoDao {

	public Curso buscaCodigoCurso(String codigoCurso) throws Exception {
		Curso curso = new Curso();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Cursos.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[0].equals(codigoCurso)) {
					curso.codigoCurso = vetLinha[0];
					curso.nomeCurso = vetLinha[1];
					curso.areaConhecimento = vetLinha[2];
					break;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		if (curso.codigoCurso == null) {
			curso = null;
		}
		return curso;
	}
	
	public void adicionarCursoLista(Lista<Curso> cursos, Curso curso) throws Exception {
		if (cursos.size() == 0) {
			cursos.addFirst(curso);
		} else {
			cursos.addLast(curso);
		}
	}

	public void GravarCurso(File arq, String csvCurso, boolean existe) throws IOException {
		FileWriter fw = new FileWriter(arq, existe);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvCurso + "\r\n");
		pw.flush();
		pw.close();
		fw.close();
	}
}

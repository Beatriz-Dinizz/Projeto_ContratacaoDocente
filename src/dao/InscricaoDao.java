package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import br.edu.fateczl.Lista;
import model.Inscricao;

public class InscricaoDao {

	public Inscricao buscaCpfProfessor(String cpfProfessor) throws Exception {
		Inscricao inscricao = new Inscricao();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Inscricoes.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[0].equals(cpfProfessor)) {
					inscricao.cpfProfessor = vetLinha[0];
					inscricao.codigoDisciplina = vetLinha[1];
					inscricao.codigoProcesso = vetLinha[2];
					break;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		if (inscricao.cpfProfessor == null) {
			inscricao = null;
		}
		return inscricao;
	}

	public void adicionarInscricaoLista(Lista<Inscricao> inscricoes, Inscricao inscricao) throws Exception {
		if (inscricoes.size() == 0) {
			inscricoes.addFirst(inscricao);
		} else {
			inscricoes.addLast(inscricao);
		}
	}

	public void GravarInscricao(File arq, String csvInscricao, boolean existe) throws IOException {
		FileWriter fw = new FileWriter(arq, existe);
		PrintWriter pw = new PrintWriter(fw);
		pw.write(csvInscricao + "\r\n");
		pw.flush();
		pw.close();
		fw.close();
	}
}

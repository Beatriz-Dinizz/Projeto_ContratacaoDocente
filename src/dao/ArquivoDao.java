package dao;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import br.edu.fateczl.Lista;
import model.Inscricao;

public class ArquivoDao {

	public File criarArquivo(String nomeArquivo) throws Exception {
		try {
			String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocente";
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdir();
			}

			File arq = new File(path, nomeArquivo);

			if (!arq.exists()) {
				arq.createNewFile();
			}
			return arq;
		} catch (Exception e) {
			throw new Exception("Erro ao criar arquivo:" + e.getMessage());
		}
	}

	public File buscarArquivo(String nomeArquivo) throws Exception {
		try {
			String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocente";
			File arq = new File(path, nomeArquivo);

			return arq;
		} catch (Exception e) {
			throw new Exception("Erro ao buscar arquivo:" + e.getMessage());
		}
	}

	public void AtualizarArquivoInscricoes(Lista<Inscricao> inscricoesLista) throws Exception {
		try {
			File arq = buscarArquivo("Inscricoes.csv");
			FileWriter fw = new FileWriter(arq);
			PrintWriter pw = new PrintWriter(fw);

			for (int i = 0; i < inscricoesLista.size(); i++) {
				Inscricao inscricao = (Inscricao) inscricoesLista.get(i);
				pw.write(inscricao.cpfProfessor + ";" + inscricao.codigoDisciplina + ";" + inscricao.codigoProcesso
						+ "\n");
			}
			fw.close();
			pw.close();
		} catch (Exception e) {
			throw new Exception("Erro ao Atualizar arquivo Inscricoes.csv: " + e.getMessage());
		}
	}
}

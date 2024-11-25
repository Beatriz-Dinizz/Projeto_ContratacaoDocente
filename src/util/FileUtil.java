package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.fateczl.Lista;
import br.edu.fateczl.fila.Fila;
import dao.ArquivoDao;
import model.ConsultaInscrito;
import model.Disciplina;
import model.Inscricao;

public class FileUtil {

	public File criarArquivo(String nomeArquivo) {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocente";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, nomeArquivo);

		return arq;
	}

	public File buscarArquivo(String nomeArquivo) {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocente";
		File arq = new File(path, nomeArquivo);

		return arq;
	}

	public List<ConsultaInscrito> ordenarPorPontuacao(List<ConsultaInscrito> lista) {
		if (lista.size() <= 1) {
			return lista;
		}
		//Dividir a lista ao meio
		int meio = lista.size() / 2;
		List<ConsultaInscrito> esquerda = ordenarPorPontuacao(lista.subList(0, meio));
		List<ConsultaInscrito> direita = ordenarPorPontuacao(lista.subList(meio, lista.size()));

		//Mesclar as duas metades ordenadas
		return merge(esquerda, direita);
	}

	public List<ConsultaInscrito> merge(List<ConsultaInscrito> esquerda, List<ConsultaInscrito> direita) {
		List<ConsultaInscrito> resultado = new ArrayList<>();
		int i = 0, j = 0;

		//Comparar elementos de ambas as listas e adicionar o maior no resultado
		while (i < esquerda.size() && j < direita.size()) {
			if (esquerda.get(i).getPontos() >= direita.get(j).getPontos()) {
				resultado.add(esquerda.get(i));
				i++;
			} else {
				resultado.add(direita.get(j));
				j++;
			}
		}
		//Adicionar elementos restantes
		while (i < esquerda.size()) {
			resultado.add(esquerda.get(i));
			i++;
		}

		while (j < direita.size()) {
			resultado.add(direita.get(j));
			j++;
		}
		return resultado;
	}
	
	public Fila<Disciplina> listarDisciplinas(Fila<Disciplina> disciplinaFila) throws Exception{
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (!arq.exists()) {
			arq = arquivoDao.criarArquivo("Disciplinas.csv");
		}

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Disciplina disciplina = new Disciplina();
				disciplina.codigoDisciplina = vetLinha[0].trim();
				disciplina.nomeDisciplina = vetLinha[1].trim();
				disciplina.diaSemana = vetLinha[2].trim();
				disciplina.horarioInicio = vetLinha[3].trim();
				disciplina.horasDiarias = vetLinha[4].trim();
				disciplina.codigoCurso = vetLinha[5].trim();
				disciplina.codigoProcesso = vetLinha[6].trim();

				disciplinaFila.insert(disciplina);

				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar dados:" + e.getMessage());
		}
		
		return disciplinaFila;
	}
	
	public Lista<Disciplina> carregarDisciplinasLista(Lista<Disciplina> disciplinaLista) throws Exception{
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (!arq.exists()) {
			arq = arquivoDao.criarArquivo("Disciplinas.csv");
		}

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Disciplina disciplina = new Disciplina();
				disciplina.codigoDisciplina = vetLinha[0].trim();
				disciplina.nomeDisciplina = vetLinha[1].trim();
				disciplina.diaSemana = vetLinha[2].trim();
				disciplina.horarioInicio = vetLinha[3].trim();
				disciplina.horasDiarias = vetLinha[4].trim();
				disciplina.codigoCurso = vetLinha[5].trim();
				disciplina.codigoProcesso = vetLinha[6].trim();

				if (disciplinaLista.isEmpty()) {
					disciplinaLista.addFirst(disciplina);
				}
				else {
					disciplinaLista.addLast(disciplina);
				}

				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar dados:" + e.getMessage());
		}		
		return disciplinaLista;
	}
	
	public Lista<Inscricao> listarInscricoes(Lista<Inscricao> inscricoesLista) throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Inscricoes.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Inscricao inscricao = new Inscricao();
				inscricao.cpfProfessor = vetLinha[0];
				inscricao.codigoDisciplina = vetLinha[1];
				inscricao.codigoProcesso = vetLinha[2];

				if (inscricoesLista.isEmpty()) {
					inscricoesLista.addFirst(inscricao);
				} else {
					inscricoesLista.addLast(inscricao);
				}
				linha = buffer.readLine();
			}			
			buffer.close();
			isr.close();
			fis.close();
		}			
		return inscricoesLista;
	}

	public String LetrasMaiusculas(String texto) {
		texto = texto.toUpperCase();
		return texto;
	}
}

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import br.edu.fateczl.Lista;
import dao.ArquivoDao;
import model.ConsultaDisciplina;
import model.Disciplina;
import util.FileUtil;

public class ConsultaDisciplinaController implements ActionListener {
	private JTable tabelaConsultaDisciplina;
	@SuppressWarnings("rawtypes")
	Lista[] tabelaHashProcessosAtivos;

	public ConsultaDisciplinaController(JTable tabelaConsultaDisciplina) {
		this.tabelaConsultaDisciplina = tabelaConsultaDisciplina;
		tabelaHashProcessosAtivos = new Lista[10000];

		inicializarTabelaHash();
	}
	//Método que dispara o evento do botão "Listar"
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.contains("Listar")) {
			try {
				listar();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	//Método com a função de carregar as disiplinas, criar objetos para consulta e preencher a tabela hash e a JTable
	private void listar() throws Exception {
		FileUtil util = new FileUtil();
		Lista<Disciplina> disciplinaLista = new Lista<>();

		disciplinaLista = util.carregarDisciplinasLista(disciplinaLista);

		try {
			ArquivoDao arquivoDao = new ArquivoDao();

			File arqCurso = arquivoDao.buscarArquivo("Cursos.csv");

			if (!arqCurso.exists()) {
				arqCurso = arquivoDao.criarArquivo("Cursos.csv");
			}

			Lista<ConsultaDisciplina> cdLista = new Lista<ConsultaDisciplina>();

			for (int i = 0; i < disciplinaLista.size(); i++) {
				Disciplina disciplina = new Disciplina();
				ConsultaDisciplina cd = new ConsultaDisciplina();

				disciplina = disciplinaLista.get(i);
				cd.codigoDisciplina = disciplina.codigoDisciplina;
				cd.nomeDisciplina = disciplina.nomeDisciplina;
				cd.diaSemana = disciplina.diaSemana;
				cd.horarioInicio = disciplina.horarioInicio;
				cd.horasDiarias = disciplina.horasDiarias;
				cd.codigoCurso = disciplina.codigoCurso;
				cd.codigoProcesso = disciplina.codigoProcesso;

				if (cdLista.isEmpty()) {
					cdLista.addFirst(cd);
				} else {
					cdLista.addLast(cd);
				}
			}
			//Insere cada consulta na tabela hash pela posição calculada
			for (int i = 0; i < cdLista.size(); i++) {
				ConsultaDisciplina cd = new ConsultaDisciplina();
				cd = cdLista.get(i);

				adicionarCodigoCurso(cd);
			}
			PreencherJTableLista(tabelaHashProcessosAtivos);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar dados:" + e.getMessage());
		}
	}
	//Inicializa a tabela hash criando uma lista encadeada vazia para cada posição
	@SuppressWarnings("rawtypes")
	private void inicializarTabelaHash() {
		int tamanho = tabelaHashProcessosAtivos.length;
		for (int i = 0; i < tamanho; i++) {
			tabelaHashProcessosAtivos[i] = new Lista();
		}
	}

	@SuppressWarnings("unchecked")
	public void adicionarCodigoCurso(ConsultaDisciplina consultaDisciplina) {
		int posicao = consultaDisciplina.hashCode(consultaDisciplina.codigoDisciplina);
		tabelaHashProcessosAtivos[posicao].addFirst(consultaDisciplina);
	}
	//Preenche a JTable com os dados armazenados na tabela hash
	private void PreencherJTableLista(@SuppressWarnings("rawtypes") Lista[] tabelaHashProcessosAtivos) throws Exception {
		String[] nomeColunas = { "Codigo da Disciplina", "Nome", "Dia Ministrado", "Horário Inicío", "Horas Diárias",
				"Código do Curso", "Código do Processo" };
		Lista<String[]> linhas = new Lista<>();

		for (int i = 0; i < tabelaHashProcessosAtivos.length; i++) {
			if (!tabelaHashProcessosAtivos[i].isEmpty()) {
				for (int j = 0; j < tabelaHashProcessosAtivos[i].size(); j++) {
					ConsultaDisciplina disciplina = (ConsultaDisciplina) tabelaHashProcessosAtivos[i].get(j);

					String[] linha = new String[7];
					linha[0] = disciplina.codigoDisciplina;
					linha[1] = disciplina.nomeDisciplina;
					linha[2] = disciplina.diaSemana;
					linha[3] = disciplina.horarioInicio;
					linha[4] = disciplina.horasDiarias;
					linha[5] = disciplina.codigoCurso;
					linha[6] = disciplina.codigoProcesso;

					if (linhas.isEmpty()) {
						linhas.addFirst(linha);
					} else {
						linhas.addLast(linha);
					}
				}
			}
		}

		String[][] dados = new String[linhas.size()][];
		for (int i = 0; i < linhas.size(); i++) {
			dados[i] = linhas.get(i);
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabelaConsultaDisciplina.setModel(tableModel);
		tabelaConsultaDisciplina.getTableHeader().setVisible(true);
	}
}

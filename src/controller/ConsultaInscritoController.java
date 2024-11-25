package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import br.edu.fateczl.Lista;
import dao.ArquivoDao;
import model.ConsultaInscrito;
import model.Inscricao;
import util.FileUtil;

public class ConsultaInscritoController implements ActionListener {
	private JComboBox<String> cbNomeDisciplina;
	private JTable tabelaConsultaInscrito;

	public ConsultaInscritoController(JComboBox<String> cbNomeDisciplina, JTable tabelaConsultaInscrito) {
		this.cbNomeDisciplina = cbNomeDisciplina;
		this.tabelaConsultaInscrito = tabelaConsultaInscrito;

		try {
			cbNomeDisciplina.removeAllItems();
			carregarComboBox();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar dados.", "ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}
	//Carrega as disciplinas do arquivo "Disciplinas.csv" para o combobox
	private void carregarComboBox() throws Exception {		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();
			
			while (linha != null) {
				String[] vetLinha = linha.split(";");

				cbNomeDisciplina.addItem(vetLinha[1]);
				
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}		
	}
	//Método que dispara o evento do botão "Consultar"
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals("Consultar")) {
			try {
				consulta();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	//Realiza a consulta dos inscritos na disciplina selecionada e carrega dados dos arquivos e ordena os inscritos por pontuação
	private void consulta() throws Exception {
		DefaultTableModel tableModel = (DefaultTableModel) tabelaConsultaInscrito.getModel();
		tableModel.setRowCount(0);

		Lista<ConsultaInscrito> consultaInscritos = new Lista<ConsultaInscrito>();

		String disciplinaSelecionada = cbNomeDisciplina.getSelectedItem().toString();

		ArquivoDao arquivoDao = new ArquivoDao();
		String codigoProcesso = "";

		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[1].equals(disciplinaSelecionada)) {
					codigoProcesso = vetLinha[6];
					break;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}

		Lista<Inscricao> inscricaoLista = new Lista<Inscricao>();
		List<ConsultaInscrito> inscritos = new ArrayList<>();

		File arqInscricoes = arquivoDao.buscarArquivo("Inscricoes.csv");

		if (arqInscricoes.exists() && arqInscricoes.isFile()) {
			FileInputStream fis = new FileInputStream(arqInscricoes);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[2].equals(codigoProcesso)) {
					Inscricao inscricao = new Inscricao();
					inscricao.cpfProfessor = vetLinha[0];

					if (inscricaoLista.isEmpty()) {
						inscricaoLista.addFirst(inscricao);
					} else {
						inscricaoLista.addLast(inscricao);
					}
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}

		File arqProfessor = arquivoDao.buscarArquivo("Professor.csv");

		if (arqProfessor.exists() && arqProfessor.isFile()) {
			FileInputStream fis = new FileInputStream(arqProfessor);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			for (int i = 0; i < inscricaoLista.size(); i++) {
				Inscricao inscricao = inscricaoLista.get(i);

				while (linha != null) {
					String[] vetLinha = linha.split(";");

					if (vetLinha[0].equals(inscricao.cpfProfessor)) {
						ConsultaInscrito consultaInscrito = new ConsultaInscrito();

						consultaInscrito.cpfProfessor = vetLinha[0];
						consultaInscrito.nomeProfessor = vetLinha[1];
						consultaInscrito.areaInteresse = vetLinha[2];
						consultaInscrito.pontos = vetLinha[3];

						if (consultaInscritos.isEmpty()) {
							consultaInscritos.addFirst(consultaInscrito);
						} else {
							consultaInscritos.addLast(consultaInscrito);
						}
						break;
					}
					linha = buffer.readLine();
				}
			}
			buffer.close();
			isr.close();
			fis.close();
			
			FileUtil util = new FileUtil();
			
			for (int i = 0; i < consultaInscritos.size(); i++) {
				ConsultaInscrito inscrito = consultaInscritos.get(i);
				
				inscritos.add(inscrito);
			}				
			inscritos = util.ordenarPorPontuacao(inscritos);
		}
		PreencherJTableLista(inscritos);
	}
	
	private void PreencherJTableLista(List<ConsultaInscrito> inscritos) throws Exception {
        String[] nomeColunas = { "CPF", "Nome", "Área de Interesse", "Pontuação" };
        String[][] dados = new String[inscritos.size()][4]; 

        for (int i = 0; i < inscritos.size(); i++) {
            ConsultaInscrito consultaInscrito = inscritos.get(i);
            dados[i][0] = consultaInscrito.cpfProfessor;
            dados[i][1] = consultaInscrito.nomeProfessor;
            dados[i][2] = consultaInscrito.areaInteresse;
            dados[i][3] = String.valueOf(consultaInscrito.pontos); // Convertendo para String
        }
        DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
        tabelaConsultaInscrito.setModel(tableModel);
        tabelaConsultaInscrito.getTableHeader().setVisible(true);

        if (tabelaConsultaInscrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Não existem candidatos inscritos para essa disciplina.");
        }
	}
}

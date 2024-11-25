package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import br.edu.fateczl.Lista;
import br.edu.fateczl.fila.Fila;
import dao.ArquivoDao;
import dao.InscricaoDao;
import model.Disciplina;
import model.Inscricao;
import model.Professor;

public class InscricaoController implements ActionListener {

	private JTextField textInscricaoCpf;
	private JTextField textInscricaoCodigoDisciplina;
	private JTextField textInscricaoCodigoProcesso;
	private JTable tabelaInscricao;
	private JButton btnCadastrar;
	private JButton btnCancelar;
	private JButton btnSalvar;

	public InscricaoController(JTextField textInscricaoCpf, JTextField textInscricaoCodigoDisciplina,
			JTextField textInscricaoCodigoProcesso, JTable tabelaInscricao, JButton btnCadastrar, JButton btnCancelar,
			JButton btnSalvar) {
		this.textInscricaoCpf = textInscricaoCpf;
		this.textInscricaoCodigoDisciplina = textInscricaoCodigoDisciplina;
		this.textInscricaoCodigoProcesso = textInscricaoCodigoProcesso;
		this.tabelaInscricao = tabelaInscricao;
		this.btnCadastrar = btnCadastrar;
		this.btnCancelar = btnCancelar;
		this.btnSalvar = btnSalvar;

		try {
			listar();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao listar dados.", "ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals("Buscar")) {
			try {
				busca();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (cmd.equals("Cadastrar")) {
			try {
				cadastro();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (cmd.equals("Editar")) {
			editar();
		}
		if (cmd.equals("Excluir")) {
			try {
				excluir();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (cmd.contains("Listar")) {
			try {
				listar();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (cmd.equals("Salvar")) {
			try {
				salvar();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (cmd.equals("Cancelar")) {
			cancelar();
		}
	}

	public void busca() throws Exception {
		DefaultTableModel tableModel = (DefaultTableModel) tabelaInscricao.getModel();
		tableModel.setRowCount(0);

		Inscricao inscricao = new Inscricao();

		inscricao.cpfProfessor = textInscricaoCpf.getText();
		inscricao.codigoDisciplina = textInscricaoCodigoDisciplina.getText();
		inscricao.codigoProcesso = textInscricaoCodigoProcesso.getText();

		Fila<Inscricao> inscricoes = new Fila<Inscricao>();
		InscricaoDao inscricaoDao = new InscricaoDao();

		if (!inscricao.cpfProfessor.equals("")) {
			inscricao = inscricaoDao.buscaCpfProfessor(inscricao.cpfProfessor);
			if (inscricao != null) {
				inscricoes.insert(inscricao);
			}
		} else if (!inscricao.codigoDisciplina.isEmpty()) {
			inscricoes = buscaCodigoDisciplina(inscricao.codigoDisciplina);
		} else if (!inscricao.codigoProcesso.isEmpty()) {
			inscricoes = buscaCodigoProcesso(inscricao.codigoProcesso);
		} else {
			JOptionPane.showMessageDialog(null, "Preencha um campo", "ERRO", JOptionPane.ERROR_MESSAGE);
		}

		while (!inscricoes.isEmpty()) {
			Inscricao i = inscricoes.remove();
			tableModel
					.addRow(new Object[] { i.cpfProfessor, i.codigoDisciplina, i.codigoProcesso, "Editar", "Excluir" });
		}

		tabelaInscricao.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaInscricao.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaInscricao.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaInscricao.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaInscricao.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaInscricao.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		limparCampos();
	}

	private Fila<Inscricao> buscaCodigoDisciplina(String codigoDisciplina) throws Exception {
		Fila<Inscricao> inscricoes = new Fila<Inscricao>();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Inscricoes.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[1].equals(codigoDisciplina)) {
					Inscricao inscricao = new Inscricao();
					inscricao.cpfProfessor = vetLinha[0];
					inscricao.codigoDisciplina = vetLinha[1];
					inscricao.codigoProcesso = vetLinha[2];

					inscricoes.insert(inscricao);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return inscricoes;
	}

	private Fila<Inscricao> buscaCodigoProcesso(String codigoProcesso) throws Exception {
		Fila<Inscricao> inscricoes = new Fila<Inscricao>();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Inscricoes.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[2].equals(codigoProcesso)) {
					Inscricao inscricao = new Inscricao();
					inscricao.cpfProfessor = vetLinha[0];
					inscricao.codigoDisciplina = vetLinha[1];
					inscricao.codigoProcesso = vetLinha[2];

					inscricoes.insert(inscricao);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return inscricoes;
	}
	
	public void buscaDisciplina() throws Exception {
		String codigoDisciplina = textInscricaoCodigoDisciplina.getText();
		
		buscaCodigoProcessoDisciplina(codigoDisciplina);
	}
	
	private void buscaCodigoProcessoDisciplina(String codigoDisciplina) throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[0].equals(codigoDisciplina)) {
					textInscricaoCodigoProcesso.setText(vetLinha[6]);
					
					textInscricaoCodigoProcesso.setEnabled(false);
					break;
				}
				else {
					linha = buffer.readLine();
					textInscricaoCodigoProcesso.setText("Preencha um código de disciplina válido");
				}
				
			}
			buffer.close();
			isr.close();
			fis.close();
		}
	}

	private void cadastro() throws Exception {
		if (ValidarCadastro()) {
			String mensagem = ValidarCadastroExistente();

			if (mensagem == "") {
				Inscricao inscricao = new Inscricao();

				inscricao.cpfProfessor = textInscricaoCpf.getText();
				inscricao.codigoDisciplina = textInscricaoCodigoDisciplina.getText();
				inscricao.codigoProcesso = textInscricaoCodigoProcesso.getText();

				salvaInscricao(inscricao.toString());
				limparCampos();
				
				listar();

				JOptionPane.showMessageDialog(null, "Inscrição realizada com sucesso!");
			} else {
				JOptionPane.showMessageDialog(null, mensagem);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
		}
	}

	private boolean ValidarCadastro() throws Exception {
		boolean valido = true;

		if (textInscricaoCpf.getText().equals("") || textInscricaoCodigoDisciplina.getText().equals("")
				|| textInscricaoCodigoProcesso.getText().equals("")) {
			valido = false;
		}
		return valido;
	}

	private String ValidarCadastroExistente() throws Exception {
		//verifica se o novo cadastro existe na tabela de inscrição
		String mensagem = "";
		boolean valido = true;

		Lista<Inscricao> inscricaoLista = new Lista<Inscricao>();

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

				if (inscricaoLista.size() == 0) {
					inscricaoLista.addFirst(inscricao);
				} else {
					inscricaoLista.addLast(inscricao);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
		}
		String cpfInscricaoDigitado = textInscricaoCpf.getText();

		for (int i = 0; i < inscricaoLista.size(); i++) {
			Inscricao inscricao = new Inscricao();
			inscricao = inscricaoLista.get(i);

			if (inscricao.cpfProfessor.equals(cpfInscricaoDigitado)) {
				valido = false;
				break;
			}
		}

		if (!valido) {
			mensagem = "Já existe um professor com esse CPF cadastrado para essa inscrição.";
			
			return mensagem;
		}
		//Consultar tabela de professores para ver se o novo cadastro ja existe na tabela Professores
		valido = true;
		Fila<Professor> filaProfessor = listarProfessores();

		for (int i = 0; i < filaProfessor.size(); i++) {
			Professor professor = filaProfessor.get(i);

			if (textInscricaoCpf.getText().equals(professor.cpfProfessor)) {
				valido = false;
			}
		}
		if (valido) {
			mensagem = "CPF do professor não encontrado, por favor realize o cadastro na aba Gerenciar Professor.";
			
			return mensagem;
		}		
		//Consultar tabela de disciplina para ver se o novo cadastro ja existe na tabela Disciplinas
		valido = true;
		Fila<Disciplina> filaDisciplina = listarDisciplina();

		for (int i = 0; i < filaDisciplina.size(); i++) {
			Disciplina disciplina = filaDisciplina.get(i);

			if (textInscricaoCodigoDisciplina.getText().equals(disciplina.codigoDisciplina)) {
				valido = false;
			}
		}
		
		if (valido) {
			mensagem = "Disciplina não encontrada, por favor realize o cadastro na aba Gerenciar Disciplina.";
			
			return mensagem;
		}
		return mensagem;
	}
	
	private void editar() {
		int selectedRow = tabelaInscricao.getSelectedRow();

		if (selectedRow != -1) {
			DefaultTableModel model = (DefaultTableModel) tabelaInscricao.getModel();

			String cpfInscricao = (String) model.getValueAt(selectedRow, 0);
			String codigoDisciplinaInscricao = (String) model.getValueAt(selectedRow, 1);
			String codigoProcessoInscricao = (String) model.getValueAt(selectedRow, 2);

			textInscricaoCpf.setText(cpfInscricao);
			textInscricaoCodigoDisciplina.setText(codigoDisciplinaInscricao);
			textInscricaoCodigoProcesso.setText(codigoProcessoInscricao);

			btnCadastrar.setEnabled(false);
			btnCancelar.setEnabled(true);
			btnSalvar.setEnabled(true);

		} else {
			JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.");
		}
	}
	
	private void excluir() throws Exception {
		Inscricao inscricao = new Inscricao();

		inscricao.cpfProfessor = textInscricaoCpf.getText();

		int selectedRow = tabelaInscricao.getSelectedRow();

		DefaultTableModel model = (DefaultTableModel) tabelaInscricao.getModel();
		String cpfInscricaoOriginal = (String) model.getValueAt(selectedRow, 0);

		ExcluirInscricao(cpfInscricaoOriginal.toString());
		limparCampos();
	}
	
	private void ExcluirInscricao(String cpfProfessor) throws Exception {
		InscricaoDao inscricaoDao = new InscricaoDao();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Inscricoes.csv");

		if (!arq.exists()) {
			throw new IOException("Arquivo não encontrado para exclusão.");
		}

		Lista<Inscricao> inscricoes = new Lista<Inscricao>();
		boolean existe = false;

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				boolean excluirPorCpf = !cpfProfessor.isEmpty() && vetLinha[0].trim().equals(cpfProfessor);

				if (!(excluirPorCpf)) {
					Inscricao inscricao = new Inscricao();
					inscricao.cpfProfessor = vetLinha[0].trim();
					inscricao.codigoDisciplina = vetLinha[1].trim();
					inscricao.codigoProcesso = vetLinha[2].trim();

					inscricaoDao.adicionarInscricaoLista(inscricoes, inscricao);
				} else {
					existe = true;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao excluir inscrição: " + e.getMessage());
		}
		if (!existe) {
			throw new IOException("Inscrição não encontrada na base de dados.");
		}
		try (FileWriter fw = new FileWriter(arq); PrintWriter pw = new PrintWriter(fw)) {

			for (int i = 0; i < inscricoes.size(); i++) {
				Inscricao inscricao = (Inscricao) inscricoes.get(i);
				pw.write(inscricao.cpfProfessor + ";" + inscricao.codigoDisciplina + ";" + inscricao.codigoProcesso
						+ "\n");
			}
			fw.close();
			pw.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar inscricões na base: " + e.getMessage());
		}
		PreencherJTableLista(inscricoes);

		JOptionPane.showMessageDialog(null, "Inscrição excluída com sucesso!");
	}
	
	private void listar() throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Inscricoes.csv");

		if (!arq.exists()) {
			arq = arquivoDao.criarArquivo("Inscricoes.csv");
		}

		Fila<Inscricao> inscricaoFila = new Fila<>();

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Inscricao inscricao = new Inscricao();
				inscricao.cpfProfessor = vetLinha[0].trim();
				inscricao.codigoDisciplina = vetLinha[1].trim();
				inscricao.codigoProcesso = vetLinha[2].trim();

				inscricaoFila.insert(inscricao);

				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();

			PreencherJTableFila(inscricaoFila);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar inscrições:" + e.getMessage());
		}
	}

	private Fila<Professor> listarProfessores() throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Professor.csv");

		if (!arq.exists()) {
			throw new IOException("Arquivo não encontrado para Listagem.");
		}

		Fila<Professor> professorFila = new Fila<>();

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Professor professor = new Professor();
				professor.cpfProfessor = vetLinha[0].trim();
				professor.nomeProfessor = vetLinha[1].trim();
				professor.areaInteresse = vetLinha[2].trim();
				professor.pontos = vetLinha[3].trim();

				professorFila.insert(professor);

				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar professores:" + e.getMessage());
		}

		return professorFila;
	}
	
	private Fila<Disciplina> listarDisciplina() throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (!arq.exists()) {
			throw new IOException("Arquivo não encontrado para Listagem.");
		}

		Fila<Disciplina> disciplinaFila = new Fila<>();

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
			JOptionPane.showMessageDialog(null, "Falha ao listar Disciplinas:" + e.getMessage());
		}

		return disciplinaFila;
	}
	
	private void salvar() throws Exception {
		if (ValidarCadastro()) {
			
				Lista<Inscricao> inscricaoLista = new Lista<Inscricao>();

				InscricaoDao inscricaoDao = new InscricaoDao();
				ArquivoDao arquivoDao = new ArquivoDao();
				File arq = arquivoDao.buscarArquivo("Inscricoes.csv");

				boolean existe = false;

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

						inscricaoDao.adicionarInscricaoLista(inscricaoLista, inscricao);

						linha = buffer.readLine();
					}
					buffer.close();
					fis.close();
					isr.close();
				}

				int selectedRow = tabelaInscricao.getSelectedRow();

				try {
					if (selectedRow == -1) {
						JOptionPane.showMessageDialog(null, "Selecione uma linha para salvar as alterações.");
						return;
					}
					String novoCpfInscricao = textInscricaoCpf.getText();
					String novoCodigoDisciplinaInscricao = textInscricaoCodigoDisciplina.getText();
					String novoCodigoDisciplinaProcesso = textInscricaoCodigoProcesso.getText();

					DefaultTableModel model = (DefaultTableModel) tabelaInscricao.getModel();
					String cpfInscricaoOriginal = (String) model.getValueAt(selectedRow, 0);

					Inscricao inscricaoAtualizada = new Inscricao();
					inscricaoAtualizada.cpfProfessor = novoCpfInscricao;
					inscricaoAtualizada.codigoDisciplina = novoCodigoDisciplinaInscricao;
					inscricaoAtualizada.codigoProcesso = novoCodigoDisciplinaProcesso;

					boolean encontrado = false;
					for (int i = 0; i < inscricaoLista.size(); i++) {
						Inscricao inscricao = inscricaoLista.get(i);

						if (inscricao.cpfProfessor.equals(cpfInscricaoOriginal)) {
							inscricaoLista.remove(i);
							inscricaoLista.add(inscricaoAtualizada, i);
							encontrado = true;

							tabelaInscricao.setValueAt(inscricaoAtualizada.cpfProfessor, selectedRow, 0);
							tabelaInscricao.setValueAt(inscricaoAtualizada.codigoDisciplina, selectedRow, 1);
							tabelaInscricao.setValueAt(inscricaoAtualizada.codigoProcesso, selectedRow, 2);

							for (int j = 0; j < inscricaoLista.size(); j++) {
								Inscricao inscricaoFinal = inscricaoLista.get(j);

								FileWriter fw = new FileWriter(arq, existe);
								PrintWriter pw = new PrintWriter(fw);
								pw.write(inscricaoFinal.cpfProfessor + ";" + inscricaoFinal.codigoDisciplina + ";"
										+ inscricaoFinal.codigoProcesso + "\r\n");
								pw.flush();
								pw.close();
								fw.close();

								existe = true;
							}
							limparCampos();

							btnCadastrar.setEnabled(true);
							btnCancelar.setEnabled(false);
							btnSalvar.setEnabled(false);

							JOptionPane.showMessageDialog(null, "Inscrição editada com sucesso!");
							break;
						}
					}

					if (!encontrado) {
						System.out.println("Registro não encontrado no arquivo CSV.");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao salvar as alterações: " + e.getMessage());
				}
		} else {
			JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
		}
	}

	private void salvaInscricao(String csvInscricao) throws Exception {
		InscricaoDao inscricaoDao = new InscricaoDao();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.criarArquivo("Inscricoes.csv");

		boolean existe = false;

		if (arq.exists()) {
			existe = true;
		}
		inscricaoDao.GravarInscricao(arq, csvInscricao, existe);
	}	

	private void cancelar() {
		limparCampos();

		btnCadastrar.setEnabled(true);
		btnCancelar.setEnabled(false);
		btnSalvar.setEnabled(false);
	}

	private void limparCampos() {
		textInscricaoCpf.setText("");
		textInscricaoCodigoDisciplina.setText("");
		textInscricaoCodigoProcesso.setText("");
	}
	//Transformando células da JTable em botões visuais
	public class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = -5110218254660001249L;
		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value != null) {
				setText(value.toString());
			}
			return this;
		}
	}
	//Trasformando botões interativos
	public class ButtonEditor extends DefaultCellEditor {
		private static final long serialVersionUID = -4548884134242262264L;
		private String label;
		private JButton button;

		public ButtonEditor(JTextField textField, String label) {
			super(textField);
			this.label = label;
			button = new JButton();
			button.setOpaque(true);

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();

					if (label.equals("Editar")) {
						editar();
					} else if (label.equals("Excluir")) {
						try {
							excluir();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			button.setText(label);
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return new String(label);
		}
	}	

	private void PreencherJTableFila(Fila<Inscricao> inscricaoFila) throws Exception {
		String[] nomeColunas = { "CPF do Professor", "Código da Disciplina", "Código do Processo", "Editar",
				"Excluir" };
		String[][] dados = new String[inscricaoFila.size()][5];

		for (int i = 0; i < inscricaoFila.size(); i++) {
			Inscricao inscricao = inscricaoFila.get(i);
			dados[i][0] = inscricao.cpfProfessor;
			dados[i][1] = inscricao.codigoDisciplina;
			dados[i][2] = inscricao.codigoProcesso;
			dados[i][3] = "Editar";
			dados[i][4] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabelaInscricao.setModel(tableModel);

		tabelaInscricao.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaInscricao.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaInscricao.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaInscricao.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaInscricao.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaInscricao.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabelaInscricao.getTableHeader().setVisible(true);
	}

	private void PreencherJTableLista(Lista<Inscricao> inscricaoLista) throws Exception {
		String[] nomeColunas = { "CPF do Professor", "Código da Disciplina", "Código do Processo", "Editar", "Excluir" };
		String[][] dados = new String[inscricaoLista.size()][5];

		for (int i = 0; i < inscricaoLista.size(); i++) {
			Inscricao inscricao = inscricaoLista.get(i);
			dados[i][0] = inscricao.cpfProfessor;
			dados[i][1] = inscricao.codigoDisciplina;
			dados[i][2] = inscricao.codigoProcesso;
			dados[i][3] = "Editar";
			dados[i][4] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabelaInscricao.setModel(tableModel);

		tabelaInscricao.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaInscricao.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaInscricao.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaInscricao.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaInscricao.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaInscricao.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabelaInscricao.getTableHeader().setVisible(true);
	}
}

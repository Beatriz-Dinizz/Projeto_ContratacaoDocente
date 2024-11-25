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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import br.edu.fateczl.Lista;
import br.edu.fateczl.fila.Fila;
import dao.ArquivoDao;
import dao.CursoDao;
import model.Curso;
import util.FileUtil;

public class CursoController implements ActionListener {

	private JTextField textCursoCodigo;
	private JTextField textCursoNome;
	private JTextField textCursoArea;
	private JTable tabela;
	private JButton btnCadastrar;
	private JButton btnCancelar;
	private JButton btnSalvar;

	public CursoController(JTextField textCursoCodigo, JTextField textCursoNome, JTextField textCursoArea,
			JTable tabela, JButton btnCadastrar, JButton btnCancelar, JButton btnSalvar) throws Exception {
		this.textCursoCodigo = textCursoCodigo;
		this.textCursoNome = textCursoNome;
		this.textCursoArea = textCursoArea;
		this.tabela = tabela;
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

	private void busca() throws Exception {
		DefaultTableModel tableModel = (DefaultTableModel) tabela.getModel();
		tableModel.setRowCount(0);

		Curso curso = new Curso();

		curso.codigoCurso = textCursoCodigo.getText();
		curso.nomeCurso = textCursoNome.getText();
		curso.areaConhecimento = textCursoArea.getText();

		Fila<Curso> cursos = new Fila<Curso>();
		CursoDao cursoDao = new CursoDao();

		if (!curso.codigoCurso.equals("")) {
			curso = cursoDao.buscaCodigoCurso(curso.codigoCurso);
			if (curso != null) {
				cursos.insert(curso);
			}
		} else if (!curso.nomeCurso.equals("")) {
			curso = buscaNomeCurso(curso.nomeCurso);
			if (curso != null) {
				cursos.insert(curso);
			}
		} else if (!curso.areaConhecimento.isEmpty()) {
			cursos = buscaAreaConhecimento(curso.areaConhecimento);
		} else {
			JOptionPane.showMessageDialog(null, "Preencha um campo", "ERRO", JOptionPane.ERROR_MESSAGE);
		}

		while (!cursos.isEmpty()) {
			Curso c = cursos.remove();
			tableModel.addRow(new Object[] {c.codigoCurso, c.nomeCurso, c.areaConhecimento, "Editar", "Excluir"});
		}

		tabela.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabela.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabela.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabela.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabela.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabela.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		limparCampos();
	}

	private Curso buscaNomeCurso(String nomeCurso) throws Exception {
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

				if (vetLinha[1].equals(nomeCurso)) {
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
		if (curso.nomeCurso == null) {
			curso = null;
		}
		return curso;
	}

	private Fila<Curso> buscaAreaConhecimento(String areaConhecimento) throws Exception {
		Fila<Curso> cursos = new Fila<Curso>();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Cursos.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[2].equals(areaConhecimento)) {
					Curso curso = new Curso();
					curso.codigoCurso = vetLinha[0];
					curso.nomeCurso = vetLinha[1];
					curso.areaConhecimento = vetLinha[2];

					cursos.insert(curso);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return cursos;
	}

	private void cadastro() throws Exception {
		if (ValidarCadastro()) {
			if (validarCadastroExistente()) {
				Curso curso = new Curso();
				FileUtil util = new FileUtil();
				
				curso.codigoCurso = util.LetrasMaiusculas(textCursoCodigo.getText());
				curso.nomeCurso = util.LetrasMaiusculas(textCursoNome.getText());
				curso.areaConhecimento = util.LetrasMaiusculas(textCursoArea.getText());

				cadastraCurso(curso.toString());
				limparCampos();
				
				listar();

				JOptionPane.showMessageDialog(null, "Curso cadastrado com sucesso!");
			} else {
				JOptionPane.showMessageDialog(null, "Dados já existentes na base.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!");
		}
	}

	private boolean ValidarCadastro() throws Exception {
		boolean valido = true;

		if (textCursoCodigo.getText().equals("") || textCursoNome.getText().equals("")
				|| textCursoArea.getText().equals("")) {
			valido = false;
		}
		return valido;
	}

	private boolean validarCadastroExistente() throws Exception {
		boolean valido = true;

		Lista<Curso> cursoLista = new Lista<Curso>();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Cursos.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Curso curso = new Curso();
				curso.codigoCurso = vetLinha[0];
				curso.nomeCurso = vetLinha[1];
				curso.areaConhecimento = vetLinha[2];

				if (cursoLista.size() == 0) {
					cursoLista.addFirst(curso);
				} else {
					cursoLista.addLast(curso);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
		}
		String codigoDigitado = textCursoCodigo.getText();
		String nomeDigitado = textCursoNome.getText();

		for (int i = 0; i < cursoLista.size(); i++) {
			Curso curso = new Curso();
			curso = cursoLista.get(i);

			if (curso.codigoCurso.equals(codigoDigitado) || curso.nomeCurso.equals(nomeDigitado)) {
				valido = false;
				break;
			}
		}
		return valido;
	}

	private void cadastraCurso(String csvCurso) throws Exception {
		CursoDao cursoDao = new CursoDao();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.criarArquivo("Cursos.csv");

		boolean existe = false;

		if (arq.exists()) {
			existe = true;
		}
		cursoDao.GravarCurso(arq, csvCurso, existe);
	}

	private void editar() {

		int selectedRow = tabela.getSelectedRow();

		if (selectedRow != -1) {
			DefaultTableModel model = (DefaultTableModel) tabela.getModel();

			String codigoCurso = (String) model.getValueAt(selectedRow, 0);
			String nomeCurso = (String) model.getValueAt(selectedRow, 1);
			String areaConhecimento = (String) model.getValueAt(selectedRow, 2);

			textCursoCodigo.setText(codigoCurso);
			textCursoNome.setText(nomeCurso);
			textCursoArea.setText(areaConhecimento);

			btnCadastrar.setEnabled(false);
			btnCancelar.setEnabled(true);
			btnSalvar.setEnabled(true);
		} else {
			JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.");
		}
	}

	private void excluir() throws Exception {
		Curso curso = new Curso();

		curso.codigoCurso = textCursoCodigo.getText();

		int selectedRow = tabela.getSelectedRow();

		DefaultTableModel model = (DefaultTableModel) tabela.getModel();
		String codigoCursoOriginal = (String) model.getValueAt(selectedRow, 0);

		ExcluirCurso(codigoCursoOriginal.toString());
		limparCampos();
	}

	private void ExcluirCurso(String codigoCurso) throws Exception {
		CursoDao cursoDao = new CursoDao();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Cursos.csv");

		if (!arq.exists()) {
			throw new IOException("Arquivo não encontrado para exclusão.");
		}

		Lista<Curso> cursos = new Lista<>();
		boolean existe = false;

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				boolean excluirPorCodigo = !codigoCurso.isEmpty() && vetLinha[0].trim().equals(codigoCurso);

				if (!excluirPorCodigo) {
					Curso curso = new Curso();
					curso.codigoCurso = vetLinha[0].trim();
					curso.nomeCurso = vetLinha[1].trim();
					curso.areaConhecimento = vetLinha[2].trim();

					cursoDao.adicionarCursoLista(cursos, curso);
				} else {
					existe = true;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao excluir curso: " + e.getMessage());
		}
		if (!existe) {
			throw new IOException("Curso não encontrado com os critérios fornecidos.");
		}
		try {
			FileWriter fw = new FileWriter(arq);
			PrintWriter pw = new PrintWriter(fw);

			for (int i = 0; i < cursos.size(); i++) {
				Curso curso = (Curso) cursos.get(i);
				pw.write(curso.codigoCurso + ";" + curso.nomeCurso + ";" + curso.areaConhecimento + "\n");
			}
			fw.close();
			pw.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar cursos na base: " + e.getMessage());
		}
		PreencherJTableLista(cursos);

		JOptionPane.showMessageDialog(null, "Curso excluído com sucesso!");
	}

	private void listar() throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Cursos.csv");

		if (!arq.exists()) {
			arq = arquivoDao.criarArquivo("Cursos.csv");
		}

		Fila<Curso> cursoFila = new Fila<>();

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Curso curso = new Curso();
				curso.codigoCurso = vetLinha[0].trim();
				curso.nomeCurso = vetLinha[1].trim();
				curso.areaConhecimento = vetLinha[2].trim();

				cursoFila.insert(curso);

				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();

			PreencherJTableFila(cursoFila);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar cursos:" + e.getMessage());
		}
	}

	private void salvar() throws Exception {
		if (ValidarCadastro()) {
			Lista<Curso> cursoLista = new Lista<Curso>();

			CursoDao cursoDao = new CursoDao();
			ArquivoDao arquivoDao = new ArquivoDao();
			File arq = arquivoDao.buscarArquivo("Cursos.csv");

			boolean existe = false;

			if (arq.exists() && arq.isFile()) {
				FileInputStream fis = new FileInputStream(arq);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader buffer = new BufferedReader(isr);
				String linha = buffer.readLine();

				while (linha != null) {
					String[] vetLinha = linha.split(";");

					Curso curso = new Curso();
					curso.codigoCurso = vetLinha[0];
					curso.nomeCurso = vetLinha[1];
					curso.areaConhecimento = vetLinha[2];

					cursoDao.adicionarCursoLista(cursoLista, curso);

					linha = buffer.readLine();
				}
				buffer.close();
				fis.close();
				isr.close();
			}

			int selectedRow = tabela.getSelectedRow();

			try {
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Selecione uma linha para salvar as alterações.");
					return;
				}
				String novoCodigoCurso = textCursoCodigo.getText();
				String novoNomeCurso = textCursoNome.getText();
				String novaAreaConhecimento = textCursoArea.getText();

				DefaultTableModel model = (DefaultTableModel) tabela.getModel();
				String codigoCursoOriginal = (String) model.getValueAt(selectedRow, 0);

				Curso cursoAtualizado = new Curso();
				cursoAtualizado.codigoCurso = novoCodigoCurso;
				cursoAtualizado.nomeCurso = novoNomeCurso;
				cursoAtualizado.areaConhecimento = novaAreaConhecimento;

				boolean encontrado = false;
				for (int i = 0; i < cursoLista.size(); i++) {
					Curso curso = cursoLista.get(i);
					
					if (curso.codigoCurso.equals(codigoCursoOriginal)) {
						cursoLista.remove(i);
						cursoLista.add(cursoAtualizado, i);
						encontrado = true;

						tabela.setValueAt(cursoAtualizado.codigoCurso, selectedRow, 0);
						tabela.setValueAt(cursoAtualizado.nomeCurso, selectedRow, 1);
						tabela.setValueAt(cursoAtualizado.areaConhecimento, selectedRow, 2);

						for (int j = 0; j < cursoLista.size(); j++) {
							Curso cursoFinal = cursoLista.get(j);

							FileWriter fw = new FileWriter(arq, existe);
							PrintWriter pw = new PrintWriter(fw);
							pw.write(cursoFinal.codigoCurso + ";" + cursoFinal.nomeCurso + ";"
									+ cursoFinal.areaConhecimento + "\r\n");
							pw.flush();
							pw.close();
							fw.close();

							existe = true;
						}
						limparCampos();

						btnCadastrar.setEnabled(true);
						btnCancelar.setEnabled(false);
						btnSalvar.setEnabled(false);

						JOptionPane.showMessageDialog(null, "Dados do curso editados com sucesso!");
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
			JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!");
		}
	}
	
	private void cancelar() {
		limparCampos();

		btnCadastrar.setEnabled(true);
		btnCancelar.setEnabled(false);
		btnSalvar.setEnabled(false);
	}

	private void limparCampos() {
		textCursoCodigo.setText("");
		textCursoNome.setText("");
		textCursoArea.setText("");
	}
	//Transformando células da JTable em botões visuais
	public class ButtonRenderer extends JButton implements TableCellRenderer {		
		private static final long serialVersionUID = 5613302577680191689L;
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
		private static final long serialVersionUID = -1335394916278273633L;
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

	private void PreencherJTableFila(Fila<Curso> cursoFila) throws Exception {
		String[] nomeColunas = {"Codigo do Curso", "Nome do Curso", "Area de Conhecimento", "Editar", "Excluir" };
		String[][] dados = new String[cursoFila.size()][5];

		for (int i = 0; i < cursoFila.size(); i++) {
			Curso curso = cursoFila.get(i);
			dados[i][0] = curso.codigoCurso;
			dados[i][1] = curso.nomeCurso;
			dados[i][2] = curso.areaConhecimento;
			dados[i][3] = "Editar";
			dados[i][4] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabela.setModel(tableModel);

		tabela.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabela.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabela.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabela.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabela.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabela.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabela.getTableHeader().setVisible(true);
	}

	private void PreencherJTableLista(Lista<Curso> cursoLista) throws Exception {
		String[] nomeColunas = { "Codigo do Curso", "Nome", "Área de Conhecimento", "Editar", "Excluir" };
		String[][] dados = new String[cursoLista.size()][5];

		for (int i = 0; i < cursoLista.size(); i++) {
			Curso curso = cursoLista.get(i);
			dados[i][0] = curso.codigoCurso;
			dados[i][1] = curso.nomeCurso;
			dados[i][2] = curso.areaConhecimento;
			dados[i][3] = "Editar";
			dados[i][4] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabela.setModel(tableModel);

		tabela.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabela.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabela.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabela.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabela.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabela.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabela.getTableHeader().setVisible(true);
	}
}

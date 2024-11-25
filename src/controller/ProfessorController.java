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
import dao.ProfessorDao;
import model.Professor;

public class ProfessorController implements ActionListener {

	private JTextField textProfessorCpf;
	private JTextField textProfessorNome;
	private JTextField textProfessorArea;
	private JTextField textProfessorPontuacao;
	private JTable tabelaProfessor;
	private JButton btnCadastrar;
	private JButton btnCancelar;
	private JButton btnSalvar;

	public ProfessorController(JTextField textProfessorCpf, JTextField textProfessorNome, JTextField textProfessorArea,
			JTextField textProfessorPontuacao, JTable tabelaProfessor, JButton btnCadastrar, JButton btnCancelar,
			JButton btnSalvar) {
		this.textProfessorCpf = textProfessorCpf;
		this.textProfessorNome = textProfessorNome;
		this.textProfessorArea = textProfessorArea;
		this.textProfessorPontuacao = textProfessorPontuacao;
		this.tabelaProfessor = tabelaProfessor;
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
		DefaultTableModel tableModel = (DefaultTableModel) tabelaProfessor.getModel();
		tableModel.setRowCount(0);

		Professor professor = new Professor();

		professor.cpfProfessor = textProfessorCpf.getText();
		professor.nomeProfessor = textProfessorNome.getText();
		professor.areaInteresse = textProfessorArea.getText();
		professor.pontos = textProfessorPontuacao.getText();

		Fila<Professor> professores = new Fila<Professor>();
		ProfessorDao professorDao = new ProfessorDao();

		if (!professor.cpfProfessor.equals("")) {
			professor = professorDao.buscaCpfProfessor(professor.cpfProfessor);
			if (professor != null) {
				professores.insert(professor);
			}
		} else if (!professor.nomeProfessor.equals("")) {
			professor = buscaNomeProfessor(professor.nomeProfessor);
			if (professor != null) {
				professores.insert(professor);
			}
		} else if (!professor.areaInteresse.isEmpty()) {
			professores = buscaAreaInteresse(professor.areaInteresse);
		} else if (!professor.pontos.isEmpty()) {
			professores = buscaPontos(professor.pontos);
		} else {
			JOptionPane.showMessageDialog(null, "Preencha um campo", "ERRO", JOptionPane.ERROR_MESSAGE);
		}

		while (!professores.isEmpty()) {
			Professor p = professores.remove();
			tableModel.addRow(
					new Object[] { p.cpfProfessor, p.nomeProfessor, p.areaInteresse, p.pontos, "Editar", "Excluir" });
		}

		tabelaProfessor.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaProfessor.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaProfessor.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaProfessor.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaProfessor.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaProfessor.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		limparCampos();
	}

	private Professor buscaNomeProfessor(String nomeProfessor) throws Exception {
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

				if (vetLinha[1].equals(nomeProfessor)) {
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
		if (professor.nomeProfessor == null) {
			professor = null;
		}
		return professor;
	}

	private Fila<Professor> buscaAreaInteresse(String areaInteresse) throws Exception {
		Fila<Professor> professores = new Fila<Professor>();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Professor.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[2].equals(areaInteresse)) {
					Professor professor = new Professor();
					professor.cpfProfessor = vetLinha[0];
					professor.nomeProfessor = vetLinha[1];
					professor.areaInteresse = vetLinha[2];
					professor.pontos = vetLinha[3];

					professores.insert(professor);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return professores;
	}

	private Fila<Professor> buscaPontos(String pontos) throws Exception {
		Fila<Professor> professores = new Fila<Professor>();
		
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Professor.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[3].equals(pontos)) {
					Professor professor = new Professor();
					professor.cpfProfessor = vetLinha[0];
					professor.nomeProfessor = vetLinha[1];
					professor.areaInteresse = vetLinha[2];
					professor.pontos = vetLinha[3];

					professores.insert(professor);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return professores;
	}

	private void cadastro() throws Exception {
		if (ValidarCadastro()) {
			if (validarCadastroExistente()) {
				Professor professor = new Professor();

				professor.cpfProfessor = textProfessorCpf.getText();
				professor.nomeProfessor = textProfessorNome.getText();
				professor.areaInteresse = textProfessorArea.getText();
				professor.pontos = textProfessorPontuacao.getText();

				cadastraProfessor(professor.toString());
				limparCampos();
				
				listar();

				JOptionPane.showMessageDialog(null, "Professor cadastrado com sucesso!");
			} else {
				JOptionPane.showMessageDialog(null, "Dados já existentes na base.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!");
		}
	}

	private boolean ValidarCadastro() throws Exception {
		boolean valido = true;

		if (textProfessorCpf.getText().equals("") || textProfessorNome.getText().equals("")
				|| textProfessorArea.getText().equals("") || textProfessorPontuacao.getText().equals("")) {
			valido = false;
		}
		return valido;
	}

	private boolean validarCadastroExistente() throws Exception {
		boolean valido = true;

		Lista<Professor> professorLista = new Lista<Professor>();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Professor.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Professor professor = new Professor();
				professor.cpfProfessor = vetLinha[0];
				professor.nomeProfessor = vetLinha[1];
				professor.areaInteresse = vetLinha[2];
				professor.pontos = vetLinha[3];

				if (professorLista.size() == 0) {
					professorLista.addFirst(professor);
				} else {
					professorLista.addLast(professor);
				}

				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}

		String cpfDigitado = textProfessorCpf.getText();

		for (int i = 0; i < professorLista.size(); i++) {
			Professor professor = new Professor();
			professor = professorLista.get(i);

			if (professor.cpfProfessor.equals(cpfDigitado)) {
				valido = false;
				break;
			}
		}
		return valido;
	}

	private void cadastraProfessor(String csvProfessor) throws Exception {
		ProfessorDao professorDao = new ProfessorDao();
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.criarArquivo("Professor.csv");

		boolean existe = false;

		if (arq.exists()) {
			existe = true;
		}
		professorDao.GravarProfessor(arq, csvProfessor, existe);
	}

	private void editar() {

		int selectedRow = tabelaProfessor.getSelectedRow();

		if (selectedRow != -1) {
			DefaultTableModel model = (DefaultTableModel) tabelaProfessor.getModel();

			String cpfProfessor = (String) model.getValueAt(selectedRow, 0);
			String nomeProfessor = (String) model.getValueAt(selectedRow, 1);
			String areaInteresse = (String) model.getValueAt(selectedRow, 2);
			String pontos = (String) model.getValueAt(selectedRow, 3);

			textProfessorCpf.setText(cpfProfessor);
			textProfessorNome.setText(nomeProfessor);
			textProfessorArea.setText(areaInteresse);
			textProfessorPontuacao.setText(pontos);

			btnCadastrar.setEnabled(false);
			btnCancelar.setEnabled(true);
			btnSalvar.setEnabled(true);
		} else {
			JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.");
		}
	}

	private void excluir() throws Exception {
		Professor professor = new Professor();

		professor.cpfProfessor = textProfessorCpf.getText();

		int selectedRow = tabelaProfessor.getSelectedRow();

		DefaultTableModel model = (DefaultTableModel) tabelaProfessor.getModel();
		String cpfProfessorOriginal = (String) model.getValueAt(selectedRow, 0);

		ExcluirProfessor(cpfProfessorOriginal.toString());
		limparCampos();
	}

	private void ExcluirProfessor(String cpfProfessor) throws Exception {
		ProfessorDao professorDao = new ProfessorDao();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Professor.csv");

		if (!arq.exists()) {
			throw new IOException("Arquivo não encontrado para exclusão.");
		}

		Lista<Professor> professores = new Lista<Professor>();
		boolean existe = false;

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				boolean excluirPorCpf = !cpfProfessor.isEmpty() && vetLinha[0].trim().equals(cpfProfessor);

				if (!excluirPorCpf) {
					Professor professor = new Professor();
					professor.cpfProfessor = vetLinha[0].trim();
					professor.nomeProfessor = vetLinha[1].trim();
					professor.areaInteresse = vetLinha[2].trim();
					professor.pontos = vetLinha[3].trim();

					professorDao.adicionarProfessorLista(professores, professor);
				} else {
					existe = true;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao excluir dados do professor: " + e.getMessage());
		}
		if (!existe) {
			throw new IOException("Professor não encontrado com os critérios fornecidos.");
		}
		try {
			FileWriter fw = new FileWriter(arq);
			PrintWriter pw = new PrintWriter(fw);

			for (int i = 0; i < professores.size(); i++) {
				Professor professor = (Professor) professores.get(i);
				pw.write(professor.cpfProfessor + ";" + professor.nomeProfessor + ";" + professor.areaInteresse + ";"
						+ professor.pontos + "\n");
			}
			fw.close();
			pw.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar professores na base: " + e.getMessage());
		}
		PreencherJTableLista(professores);

		JOptionPane.showMessageDialog(null, "Dados do professor excluídos com sucesso");
	}

	private void listar() throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Professor.csv");

		if (!arq.exists()) {
			arq = arquivoDao.criarArquivo("Professor.csv");
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

			PreencherJTableFila(professorFila);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar professores:" + e.getMessage());
		}
	}

	private void salvar() throws Exception {
		if (ValidarCadastro()) {
			if (validarCadastroExistente()) {
				Lista<Professor> professorLista = new Lista<Professor>();

				ProfessorDao professorDao = new ProfessorDao();
				ArquivoDao arquivoDao = new ArquivoDao();
				File arq = arquivoDao.buscarArquivo("Professor.csv");

				boolean existe = false;

				if (arq.exists() && arq.isFile()) {
					FileInputStream fis = new FileInputStream(arq);
					InputStreamReader isr = new InputStreamReader(fis);
					BufferedReader buffer = new BufferedReader(isr);

					String linha = buffer.readLine();

					while (linha != null) {
						String[] vetLinha = linha.split(";");

						Professor professor = new Professor();
						professor.cpfProfessor = vetLinha[0];
						professor.nomeProfessor = vetLinha[1];
						professor.areaInteresse = vetLinha[2];
						professor.pontos = vetLinha[3];

						professorDao.adicionarProfessorLista(professorLista, professor);

						linha = buffer.readLine();
					}
					buffer.close();
					isr.close();
					fis.close();
				}

				int selectedRow = tabelaProfessor.getSelectedRow();

				try {
					if (selectedRow == -1) {
						JOptionPane.showMessageDialog(null, "Selecione uma linha para salvar as alterações.");
						return;
					}
					String novoCpfProfessor = textProfessorCpf.getText();
					String novoNomeProfessor = textProfessorNome.getText();
					String novaAreaInteresse = textProfessorArea.getText();
					String novaPontuacao = textProfessorPontuacao.getText();

					DefaultTableModel model = (DefaultTableModel) tabelaProfessor.getModel();
					String cpfProfessorOriginal = (String) model.getValueAt(selectedRow, 0);

					Professor professorAtualizado = new Professor();
					professorAtualizado.cpfProfessor = novoCpfProfessor;
					professorAtualizado.nomeProfessor = novoNomeProfessor;
					professorAtualizado.areaInteresse = novaAreaInteresse;
					professorAtualizado.pontos = novaPontuacao;

					boolean encontrado = false;
					for (int i = 0; i < professorLista.size(); i++) {
						Professor professor = professorLista.get(i);

						if (professor.cpfProfessor.equals(cpfProfessorOriginal)) {
							professorLista.remove(i);
							professorLista.add(professorAtualizado, i);
							encontrado = true;

							tabelaProfessor.setValueAt(professorAtualizado.cpfProfessor, selectedRow, 0);
							tabelaProfessor.setValueAt(professorAtualizado.nomeProfessor, selectedRow, 1);
							tabelaProfessor.setValueAt(professorAtualizado.areaInteresse, selectedRow, 2);
							tabelaProfessor.setValueAt(professorAtualizado.pontos, selectedRow, 3);

							for (int j = 0; j < professorLista.size(); j++) {
								Professor professorFinal = professorLista.get(j);

								FileWriter fw = new FileWriter(arq, existe);
								PrintWriter pw = new PrintWriter(fw);
								pw.write(professorFinal.cpfProfessor + ";" + professorFinal.nomeProfessor + ";"
										+ professorFinal.areaInteresse + ";" + professorFinal.pontos + "\r\n");
								pw.flush();
								pw.close();
								fw.close();

								existe = true;
							}
							limparCampos();

							btnCadastrar.setEnabled(true);
							btnCancelar.setEnabled(false);
							btnSalvar.setEnabled(false);

							JOptionPane.showMessageDialog(null, "Dados do professor editados com sucesso!");
							break;
						}
					}
					if (!encontrado) {
						JOptionPane.showMessageDialog(null, "Registro não encontrado no arquivo.");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao salvar as alterações: " + e.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(null, "Dados já existentes na base.");
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

	public void limparCampos() {
		textProfessorCpf.setText("");
		textProfessorNome.setText("");
		textProfessorArea.setText("");
		textProfessorPontuacao.setText("");
	}
	//Transformando células da JTable em botões visuais
	public class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = -9141765179938513131L;
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
		private static final long serialVersionUID = 8914723201078709772L;
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
						try {
							editar();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
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

	private void PreencherJTableFila(Fila<Professor> professorFila) throws Exception {
		String[] nomeColunas = { "CPF", "Nome", "Area de Interesse", "Pontuação", "Editar", "Excluir" };
		String[][] dados = new String[professorFila.size()][6];

		for (int i = 0; i < professorFila.size(); i++) {
			Professor professor = professorFila.get(i);
			dados[i][0] = professor.cpfProfessor;
			dados[i][1] = professor.nomeProfessor;
			dados[i][2] = professor.areaInteresse;
			dados[i][3] = professor.pontos;
			dados[i][4] = "Editar";
			dados[i][5] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabelaProfessor.setModel(tableModel);

		tabelaProfessor.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaProfessor.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaProfessor.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaProfessor.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaProfessor.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaProfessor.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabelaProfessor.getTableHeader().setVisible(true);
	}

	private void PreencherJTableLista(Lista<Professor> professorLista) throws Exception {
		String[] nomeColunas = { "CPF", "Nome", "Area de Interesse", "Pontuação", "Editar", "Excluir" };
		String[][] dados = new String[professorLista.size()][6];

		for (int i = 0; i < professorLista.size(); i++) {
			Professor professor = professorLista.get(i);
			dados[i][0] = professor.cpfProfessor;
			dados[i][1] = professor.nomeProfessor;
			dados[i][2] = professor.areaInteresse;
			dados[i][3] = professor.pontos;
			dados[i][4] = "Editar";
			dados[i][5] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabelaProfessor.setModel(tableModel);

		tabelaProfessor.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaProfessor.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaProfessor.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaProfessor.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaProfessor.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaProfessor.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabelaProfessor.getTableHeader().setVisible(true);
	}
}

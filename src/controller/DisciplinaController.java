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
import dao.DisciplinaDao;
import model.Curso;
import model.Disciplina;
import model.Inscricao;
import util.FileUtil;

public class DisciplinaController implements ActionListener {

	private JTextField textDisciplinaCodigo;
	private JTextField textDisciplinaNome;
	private JTextField textDisciplinaDia;
	private JTextField textDisciplinaHorario;
	private JTextField textDisciplinaHorasDiarias;
	private JTextField textDisciplinaCodigoCurso;
	private JTextField textDisciplinaCodigoProcesso;
	private JTable tabelaDisciplina;
	private JButton btnCadastrar;
	private JButton btnCancelar;
	private JButton btnSalvar;

	public DisciplinaController(JTextField textDisciplinaCodigo, JTextField textDisciplinaNome,
			JTextField textDisciplinaDia, JTextField textDisciplinaHorario, JTextField textDisciplinaHorasDiarias,
			JTextField textDisciplinaCodigoCurso, JTextField textDisciplinaCodigoProcesso, JTable tabelaDisciplina,
			JButton btnCadastrar, JButton btnCancelar, JButton btnSalvar) {
		this.textDisciplinaCodigo = textDisciplinaCodigo;
		this.textDisciplinaNome = textDisciplinaNome;
		this.textDisciplinaDia = textDisciplinaDia;
		this.textDisciplinaHorario = textDisciplinaHorario;
		this.textDisciplinaHorasDiarias = textDisciplinaHorasDiarias;
		this.textDisciplinaCodigoCurso = textDisciplinaCodigoCurso;
		this.textDisciplinaCodigoProcesso = textDisciplinaCodigoProcesso;
		this.tabelaDisciplina = tabelaDisciplina;
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
		DefaultTableModel tableModel = (DefaultTableModel) tabelaDisciplina.getModel();
		tableModel.setRowCount(0);

		Disciplina disciplina = new Disciplina();

		disciplina.codigoDisciplina = textDisciplinaCodigo.getText();
		disciplina.nomeDisciplina = textDisciplinaNome.getText();
		disciplina.diaSemana = textDisciplinaDia.getText();
		disciplina.horarioInicio = textDisciplinaHorario.getText();
		disciplina.horasDiarias = textDisciplinaHorasDiarias.getText();
		disciplina.codigoCurso = textDisciplinaCodigoCurso.getText();
		disciplina.codigoProcesso = textDisciplinaCodigoProcesso.getText();

		Fila<Disciplina> disciplinas = new Fila<Disciplina>();
		DisciplinaDao disciplinaDao = new DisciplinaDao();

		if (!disciplina.codigoDisciplina.equals("")) {
			disciplina = disciplinaDao.buscaCodigoDisciplina(disciplina.codigoDisciplina);
			if (disciplina != null) {
				disciplinas.insert(disciplina);
			}
		} else if (!disciplina.nomeDisciplina.equals("")) {
			disciplina = buscaNomeDisciplina(disciplina.nomeDisciplina);
			if (disciplina != null) {
				disciplinas.insert(disciplina);
			}
		} else if (!disciplina.diaSemana.isEmpty()) {
			disciplinas = buscaDiaSemana(disciplina.diaSemana);
		} else if (!disciplina.horarioInicio.isEmpty()) {
			disciplinas = buscaHorarioInicio(disciplina.horarioInicio);
		} else if (!disciplina.horasDiarias.isEmpty()) {
			disciplinas = buscaHorasDiarias(disciplina.horasDiarias);
		} else if (!disciplina.codigoCurso.equals("")) {
			disciplina = buscaCodigoCurso(disciplina.codigoCurso);
			if (disciplina != null) {
				disciplinas.insert(disciplina);
			}
		} else if (!disciplina.codigoProcesso.equals("")) {
			disciplina = buscaCodigoProcesso(disciplina.codigoProcesso);
			if (disciplina != null) {
				disciplinas.insert(disciplina);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Preencha um campo", "ERRO", JOptionPane.ERROR_MESSAGE);
		}

		while (!disciplinas.isEmpty()) {
			Disciplina d = disciplinas.remove();
			tableModel.addRow(new Object[] { d.codigoDisciplina, d.nomeDisciplina, d.diaSemana, d.horarioInicio,
					d.horasDiarias, d.codigoCurso, d.codigoProcesso, "Editar", "Excluir" });
		}

		tabelaDisciplina.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaDisciplina.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaDisciplina.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaDisciplina.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaDisciplina.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaDisciplina.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		limparCampos();
	}

	private Disciplina buscaNomeDisciplina(String nomeDisciplina) throws Exception {
		Disciplina disciplina = new Disciplina();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[1].equals(nomeDisciplina)) {
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];
					break;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		if (disciplina.nomeDisciplina == null) {
			disciplina = null;
		}
		return disciplina;
	}

	private Fila<Disciplina> buscaDiaSemana(String diaSemana) throws Exception {
		Fila<Disciplina> disciplinas = new Fila<Disciplina>();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[2].equals(diaSemana)) {
					Disciplina disciplina = new Disciplina();
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];

					disciplinas.insert(disciplina);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return disciplinas;
	}

	private Fila<Disciplina> buscaHorarioInicio(String horarioInicio) throws Exception {
		Fila<Disciplina> disciplinas = new Fila<Disciplina>();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[3].equals(horarioInicio)) {
					Disciplina disciplina = new Disciplina();
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];

					disciplinas.insert(disciplina);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return disciplinas;
	}

	private Fila<Disciplina> buscaHorasDiarias(String horasDiarias) throws Exception {
		Fila<Disciplina> disciplinas = new Fila<Disciplina>();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[4].equals(horasDiarias)) {
					Disciplina disciplina = new Disciplina();
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];

					disciplinas.insert(disciplina);
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return disciplinas;
	}

	private Disciplina buscaCodigoCurso(String codigoCurso) throws Exception {
		Disciplina disciplina = new Disciplina();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[5].equals(codigoCurso)) {
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];
					break;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}

		if (disciplina.codigoCurso == null) {
			disciplina = null;
		}
		return disciplina;
	}

	private Disciplina buscaCodigoProcesso(String codigoProcesso) throws Exception {
		Disciplina disciplina = new Disciplina();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);
			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				if (vetLinha[6].equals(codigoProcesso)) {
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];
					break;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		if (disciplina.codigoProcesso == null) {
			disciplina = null;
		}
		return disciplina;
	}

	private void cadastro() throws Exception {
		String mensagem = ValidarCadastro();

		if (mensagem == "") {
			if (validarCadastroExistente()) {
				Disciplina disciplina = new Disciplina();
				FileUtil util = new FileUtil();


				disciplina.codigoDisciplina = util.LetrasMaiusculas(textDisciplinaCodigo.getText());
				disciplina.nomeDisciplina = util.LetrasMaiusculas(textDisciplinaNome.getText());
				disciplina.diaSemana = util.LetrasMaiusculas(textDisciplinaDia.getText());
				disciplina.horarioInicio = util.LetrasMaiusculas(textDisciplinaHorario.getText());
				disciplina.horasDiarias = util.LetrasMaiusculas(textDisciplinaHorasDiarias.getText());
				disciplina.codigoCurso = util.LetrasMaiusculas(textDisciplinaCodigoCurso.getText());
				disciplina.codigoProcesso = util.LetrasMaiusculas(textDisciplinaCodigoProcesso.getText());

				cadastraDisciplina(disciplina.toString());
				limparCampos();

				listar();

				JOptionPane.showMessageDialog(null, "Disciplina cadastrada com sucesso!");

			} else {
				JOptionPane.showMessageDialog(null, "Dados já existentes na base.");
			}
		} else {
			JOptionPane.showMessageDialog(null, mensagem);
		}
	}

	private String ValidarCadastro() throws Exception {
		String mensagem = "";
		boolean valido = false;

		if (textDisciplinaCodigo.getText().equals("") || textDisciplinaNome.getText().equals("")
				|| textDisciplinaDia.getText().equals("") || textDisciplinaHorario.getText().equals("")
				|| textDisciplinaHorasDiarias.getText().equals("") || textDisciplinaCodigoCurso.getText().equals("")
				|| textDisciplinaCodigoProcesso.getText().equals("")) {
			mensagem = "Por favor, preencha todos os campos.";
		}
		Fila<Curso> filaCurso = listarCurso();

		for (int i = 0; i < filaCurso.size(); i++) {
			Curso curso = filaCurso.get(i);

			if (textDisciplinaCodigoCurso.getText().equals(curso.codigoCurso)) {
				valido = true;
			}
		}
		if (!valido) {
			mensagem = "Código do curso não encontrado, por favor verifique se está correto ou realize o cadastro do curso.";
		}

		return mensagem;
	}

	private boolean validarCadastroExistente() throws Exception {
		boolean valido = true;

		Lista<Disciplina> disciplinaLista = new Lista<Disciplina>();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();

			while (linha != null) {
				String[] vetLinha = linha.split(";");

				Disciplina disciplina = new Disciplina();
				disciplina.codigoDisciplina = vetLinha[0];
				disciplina.nomeDisciplina = vetLinha[1];
				disciplina.diaSemana = vetLinha[2];
				disciplina.horarioInicio = vetLinha[3];
				disciplina.horasDiarias = vetLinha[4];
				disciplina.codigoCurso = vetLinha[5];
				disciplina.codigoProcesso = vetLinha[6];

				if (disciplinaLista.size() == 0) {
					disciplinaLista.addFirst(disciplina);
				} else {
					disciplinaLista.addLast(disciplina);
				}

				linha = buffer.readLine();
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		String codigoDisciplinaDigitado = textDisciplinaCodigo.getText();
		String nomeDisciplinaDigitado = textDisciplinaNome.getText();
		/*
		 * String codigoCursoDigitado = textDisciplinaCodigoCurso.getText();
		 */ String codigoProcessoDigitado = textDisciplinaCodigoProcesso.getText();

		for (int i = 0; i < disciplinaLista.size(); i++) {
			Disciplina disciplina = new Disciplina();
			disciplina = disciplinaLista.get(i);

			if (disciplina.codigoDisciplina.equals(codigoDisciplinaDigitado)
					|| disciplina.nomeDisciplina.equals(nomeDisciplinaDigitado)
					/*
					 * || disciplina.codigoCurso.equals(codigoCursoDigitado)
					 */
					|| disciplina.codigoProcesso.equals(codigoProcessoDigitado)) {
				valido = false;
				break;
			}
		}
		return valido;
	}

	private void cadastraDisciplina(String csvDisciplina) throws Exception {
		DisciplinaDao disciplinaDao = new DisciplinaDao();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.criarArquivo("Disciplinas.csv");

		boolean existe = false;

		if (arq.exists()) {
			existe = true;
		}
		disciplinaDao.GravarDisciplina(arq, csvDisciplina, existe);
	}

	private void editar() {

		int selectedRow = tabelaDisciplina.getSelectedRow();

		if (selectedRow != -1) {
			DefaultTableModel model = (DefaultTableModel) tabelaDisciplina.getModel();

			String codigoDisciplina = (String) model.getValueAt(selectedRow, 0);
			String nomeDisciplina = (String) model.getValueAt(selectedRow, 1);
			String diaSemana = (String) model.getValueAt(selectedRow, 2);
			String horarioInicio = (String) model.getValueAt(selectedRow, 3);
			String horasDiarias = (String) model.getValueAt(selectedRow, 4);
			String codigoCurso = (String) model.getValueAt(selectedRow, 5);
			String codigoProcesso = (String) model.getValueAt(selectedRow, 6);

			textDisciplinaCodigo.setText(codigoDisciplina);
			textDisciplinaNome.setText(nomeDisciplina);
			textDisciplinaDia.setText(diaSemana);
			textDisciplinaHorario.setText(horarioInicio);
			textDisciplinaHorasDiarias.setText(horasDiarias);
			textDisciplinaCodigoCurso.setText(codigoCurso);
			textDisciplinaCodigoProcesso.setText(codigoProcesso);

			btnCadastrar.setEnabled(false);
			btnCancelar.setEnabled(true);
			btnSalvar.setEnabled(true);
		} else {
			JOptionPane.showMessageDialog(null, "Selecione uma linha para editar.");
		}
	}

	private void excluir() throws Exception {
		Disciplina disciplina = new Disciplina();

		disciplina.codigoDisciplina = textDisciplinaCodigo.getText();

		int selectedRow = tabelaDisciplina.getSelectedRow();

		DefaultTableModel model = (DefaultTableModel) tabelaDisciplina.getModel();
		String codigoDisciplinaOriginal = (String) model.getValueAt(selectedRow, 0);

		excluirDisciplina(codigoDisciplinaOriginal.toString());
		limparCampos();
	}

	private void excluirDisciplina(String codigoDisciplina) throws Exception {
		DisciplinaDao disciplinaDao = new DisciplinaDao();

		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (!arq.exists()) {
			throw new IOException("Arquivo não encontrado para exclusão.");
		}

		Lista<Disciplina> disciplinas = new Lista<Disciplina>();
		boolean existe = false;

		try {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffer = new BufferedReader(isr);

			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				boolean excluirPorCodigoDisciplina = !codigoDisciplina.isEmpty()
						&& vetLinha[0].trim().equals(codigoDisciplina);

				if (!(excluirPorCodigoDisciplina)) {
					Disciplina disciplina = new Disciplina();
					disciplina.codigoDisciplina = vetLinha[0].trim();
					disciplina.nomeDisciplina = vetLinha[1].trim();
					disciplina.diaSemana = vetLinha[2].trim();
					disciplina.horarioInicio = vetLinha[3].trim();
					disciplina.horasDiarias = vetLinha[4].trim();
					disciplina.codigoCurso = vetLinha[5].trim();
					disciplina.codigoProcesso = vetLinha[6].trim();

					disciplinaDao.adicionarDisciplinaLista(disciplinas, disciplina);
				} else {
					existe = true;
				}
				linha = buffer.readLine();
			}
			buffer.close();
			fis.close();
			isr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao excluir disciplina: " + e.getMessage());
		}
		if (!existe) {
			throw new IOException("Disciplina não encontrada com os critérios fornecidos.");
		}
		try (FileWriter fw = new FileWriter(arq); PrintWriter pw = new PrintWriter(fw)) {

			for (int i = 0; i < disciplinas.size(); i++) {
				Disciplina disciplina = (Disciplina) disciplinas.get(i);
				pw.write(disciplina.codigoDisciplina + ";" + disciplina.nomeDisciplina + ";" + disciplina.diaSemana
						+ disciplina.horarioInicio + ";" + disciplina.horasDiarias + disciplina.codigoCurso
						+ disciplina.codigoProcesso + "\n");
			}
			fw.close();
			pw.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao atualizar disciplinas na base: " + e.getMessage());
		}
		PreencherJTableLista(disciplinas);

		excluirInscricao(codigoDisciplina);

		JOptionPane.showMessageDialog(null, "Disciplina excluída com sucesso!");
	}

	private void excluirInscricao(String codigoDisciplina) throws Exception {
		Lista<Inscricao> inscricoesLista = new Lista<Inscricao>();
		FileUtil util = new FileUtil();
		ArquivoDao arquivoDao = new ArquivoDao();

		inscricoesLista = util.listarInscricoes(inscricoesLista);

		for (int i = 0; i < inscricoesLista.size(); i++) {
			Inscricao inscricao = new Inscricao();
			inscricao = inscricoesLista.get(i);

			if (codigoDisciplina.equals(inscricao.codigoDisciplina)) {
				inscricoesLista.remove(i);
				i = -1;
			}
		}

		arquivoDao.AtualizarArquivoInscricoes(inscricoesLista);
		
		inscricoesLista = util.listarInscricoes(inscricoesLista);
	}

	private void listar() throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

		if (!arq.exists()) {
			arq = arquivoDao.criarArquivo("Disciplinas.csv");
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

			PreencherJTableFila(disciplinaFila);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar disciplinas:" + e.getMessage());
		}
	}

	public Fila<Curso> listarCurso() throws Exception {
		ArquivoDao arquivoDao = new ArquivoDao();
		File arq = arquivoDao.buscarArquivo("Cursos.csv");

		if (!arq.exists()) {
			throw new IOException("Arquivo não encontrado para Listagem.");
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

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha ao listar cursos:" + e.getMessage());
		}
		return cursoFila;
	}

	private void salvar() throws Exception {
		String mensagem = ValidarCadastro();

		if (mensagem == "") {
			Lista<Disciplina> disciplinaLista = new Lista<Disciplina>();

			DisciplinaDao disciplinaDao = new DisciplinaDao();
			ArquivoDao arquivoDao = new ArquivoDao();
			File arq = arquivoDao.buscarArquivo("Disciplinas.csv");

			boolean existe = false;

			if (arq.exists() && arq.isFile()) {
				FileInputStream fis = new FileInputStream(arq);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader buffer = new BufferedReader(isr);
				String linha = buffer.readLine();

				while (linha != null) {
					String[] vetLinha = linha.split(";");

					Disciplina disciplina = new Disciplina();
					disciplina.codigoDisciplina = vetLinha[0];
					disciplina.nomeDisciplina = vetLinha[1];
					disciplina.diaSemana = vetLinha[2];
					disciplina.horarioInicio = vetLinha[3];
					disciplina.horasDiarias = vetLinha[4];
					disciplina.codigoCurso = vetLinha[5];
					disciplina.codigoProcesso = vetLinha[6];

					disciplinaDao.adicionarDisciplinaLista(disciplinaLista, disciplina);

					linha = buffer.readLine();
				}
				buffer.close();
				isr.close();
				fis.close();
			}

			int selectedRow = tabelaDisciplina.getSelectedRow();

			try {
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Selecione uma linha para salvar as alterações.");
					return;
				}
				String novoCodigoDisciplina = textDisciplinaCodigo.getText();
				String novoNomeDisciplina = textDisciplinaNome.getText();
				String novoDiaSemana = textDisciplinaDia.getText();
				String novoHorarioInicio = textDisciplinaHorario.getText();
				String novaHorasDiarias = textDisciplinaHorasDiarias.getText();
				String novoCodigoCurso = textDisciplinaCodigoCurso.getText();
				String novoCodigoProcesso = textDisciplinaCodigoProcesso.getText();

				DefaultTableModel model = (DefaultTableModel) tabelaDisciplina.getModel();
				String codigoDisciplinaOriginal = (String) model.getValueAt(selectedRow, 0);

				Disciplina disciplinaAtualizada = new Disciplina();
				disciplinaAtualizada.codigoDisciplina = novoCodigoDisciplina;
				disciplinaAtualizada.nomeDisciplina = novoNomeDisciplina;
				disciplinaAtualizada.diaSemana = novoDiaSemana;
				disciplinaAtualizada.horarioInicio = novoHorarioInicio;
				disciplinaAtualizada.horasDiarias = novaHorasDiarias;
				disciplinaAtualizada.codigoCurso = novoCodigoCurso;
				disciplinaAtualizada.codigoProcesso = novoCodigoProcesso;

				boolean encontrado = false;
				for (int i = 0; i < disciplinaLista.size(); i++) {
					Disciplina disciplina = disciplinaLista.get(i);

					if (disciplina.codigoDisciplina.equals(codigoDisciplinaOriginal)) {
						disciplinaLista.remove(i);
						disciplinaLista.add(disciplinaAtualizada, i);
						encontrado = true;

						tabelaDisciplina.setValueAt(disciplinaAtualizada.codigoDisciplina, selectedRow, 0);
						tabelaDisciplina.setValueAt(disciplinaAtualizada.nomeDisciplina, selectedRow, 1);
						tabelaDisciplina.setValueAt(disciplinaAtualizada.diaSemana, selectedRow, 2);
						tabelaDisciplina.setValueAt(disciplinaAtualizada.horarioInicio, selectedRow, 3);
						tabelaDisciplina.setValueAt(disciplinaAtualizada.horasDiarias, selectedRow, 4);
						tabelaDisciplina.setValueAt(disciplinaAtualizada.codigoCurso, selectedRow, 5);
						tabelaDisciplina.setValueAt(disciplinaAtualizada.codigoProcesso, selectedRow, 6);

						for (int j = 0; j < disciplinaLista.size(); j++) {
							Disciplina disciplinaFinal = disciplinaLista.get(j);

							FileWriter fw = new FileWriter(arq, existe);
							PrintWriter pw = new PrintWriter(fw);
							pw.write(disciplinaFinal.codigoDisciplina + ";" + disciplinaFinal.nomeDisciplina + ";"
									+ disciplinaFinal.diaSemana + ";" + disciplinaFinal.horarioInicio + ";"
									+ disciplinaFinal.horasDiarias + ";" + disciplinaFinal.codigoCurso + ";"
									+ disciplinaFinal.codigoProcesso + "\r\n");
							pw.flush();
							pw.close();
							fw.close();

							existe = true;
						}
						limparCampos();

						btnCadastrar.setEnabled(true);
						btnCancelar.setEnabled(false);
						btnSalvar.setEnabled(false);

						JOptionPane.showMessageDialog(null, "Dados da disciplina editados com sucesso!");
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
			JOptionPane.showMessageDialog(null, mensagem);
		}
	}

	private void cancelar() {
		limparCampos();

		btnCadastrar.setEnabled(true);
		btnCancelar.setEnabled(false);
		btnSalvar.setEnabled(false);
	}

	public void limparCampos() {
		textDisciplinaCodigo.setText("");
		textDisciplinaNome.setText("");
		textDisciplinaDia.setText("");
		textDisciplinaHorario.setText("");
		textDisciplinaHorasDiarias.setText("");
		textDisciplinaCodigoCurso.setText("");
		textDisciplinaCodigoProcesso.setText("");
	}
	//Transformando células da JTable em botões visuais
	public class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = -8281389585212758070L;

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
		private static final long serialVersionUID = 1139923165166564772L;
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

	private void PreencherJTableFila(Fila<Disciplina> disciplinaFila) throws Exception {
		String[] nomeColunas = { "Codigo da Disciplina", "Nome", "Dia Ministrado", "Horário Inicío",
				"Horas Diárias", "Código do Curso", "Código do Processo", "Editar", "Excluir" };
		String[][] dados = new String[disciplinaFila.size()][9];

		for (int i = 0; i < disciplinaFila.size(); i++) {
			Disciplina disciplina = disciplinaFila.get(i);
			dados[i][0] = disciplina.codigoDisciplina;
			dados[i][1] = disciplina.nomeDisciplina;
			dados[i][2] = disciplina.diaSemana;
			dados[i][3] = disciplina.horarioInicio;
			dados[i][4] = disciplina.horasDiarias;
			dados[i][5] = disciplina.codigoCurso;
			dados[i][6] = disciplina.codigoProcesso;
			dados[i][7] = "Editar";
			dados[i][8] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabelaDisciplina.setModel(tableModel);

		tabelaDisciplina.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaDisciplina.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaDisciplina.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaDisciplina.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaDisciplina.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaDisciplina.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabelaDisciplina.getTableHeader().setVisible(true);
	}

	private void PreencherJTableLista(Lista<Disciplina> disciplinaLista) throws Exception {
		String[] nomeColunas = { "Codigo da Disciplina", "Nome da Disciplina", "Dia da Semana", "Horário Inicío",
				"Horas Diárias", "Código do Curso", "Código do Processo", "Editar", "Excluir" };
		String[][] dados = new String[disciplinaLista.size()][9];

		for (int i = 0; i < disciplinaLista.size(); i++) {
			Disciplina disciplina = disciplinaLista.get(i);
			dados[i][0] = disciplina.codigoDisciplina;
			dados[i][1] = disciplina.nomeDisciplina;
			dados[i][2] = disciplina.diaSemana;
			dados[i][3] = disciplina.horarioInicio;
			dados[i][4] = disciplina.horasDiarias;
			dados[i][5] = disciplina.codigoCurso;
			dados[i][6] = disciplina.codigoProcesso;
			dados[i][7] = "Editar";
			dados[i][8] = "Excluir";
		}

		DefaultTableModel tableModel = new DefaultTableModel(dados, nomeColunas);
		tabelaDisciplina.setModel(tableModel);

		tabelaDisciplina.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		tabelaDisciplina.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), "Editar"));
		tabelaDisciplina.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
		tabelaDisciplina.getColumn("Excluir").setCellEditor(new ButtonEditor(new JTextField(), "Excluir"));

		DefaultCellEditor editorEditar = (DefaultCellEditor) tabelaDisciplina.getColumn("Editar").getCellEditor();
		editorEditar.setClickCountToStart(1);

		DefaultCellEditor editorExcluir = (DefaultCellEditor) tabelaDisciplina.getColumn("Excluir").getCellEditor();
		editorExcluir.setClickCountToStart(1);

		tabelaDisciplina.getTableHeader().setVisible(true);
	}
}
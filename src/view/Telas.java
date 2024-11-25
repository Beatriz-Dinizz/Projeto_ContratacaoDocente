package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;
import controller.ConsultaDisciplinaController;
import controller.ConsultaInscritoController;
import controller.CursoController;
import controller.DisciplinaController;
import controller.InscricaoController;
import controller.ProfessorController;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.text.NumberFormat;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class Telas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textDisciplinaCodigo, textDisciplinaNome, textDisciplinaDia, textDisciplinaHorario, textDisciplinaHorasDiarias;
	private JTextField textDisciplinaCodigoCurso;
	private JTextField textDisciplinaCodigoProcesso;
	private JTextField textCursoCodigo;
	private JTextField textCursoNome;
	private JTextField textCursoArea;
	private JTextField textProfessorCpf;
	private JTextField textProfessorNome;
	private JTextField textProfessorArea;
	private JTextField textProfessorPontuacao;
	private JTextField textInscricaoCpf;
	private JTextField textInscricaoCodigoDisciplina;
	private JTextField textInscricaoCodigoProcesso;
	private JTable table;
	private JTable tableDisciplina;
	private JTable tableProfessor;
	private JTable tabelaInscricao;
	private JTable tabelaConsultaInscrito;
	private JTable tabelaConsultaDisciplina;

	public Telas() throws Exception {
		setTitle("Contratação de Docentes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 964, 732);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setForeground(Color.DARK_GRAY);
		tabbedPane.setBounds(196, 0, 754, 700);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel tabCurso = new JPanel();
		tabbedPane.addTab("Curso", null, tabCurso, "Cadastrar Curso");
		tabCurso.setLayout(null);
		tabCurso.setVisible(false);
		
		JPanel tabConsultaInscritos = new JPanel();
		tabbedPane.addTab("Consultar Inscritos", null, tabConsultaInscritos, "Consultar Inscritos");
		tabConsultaInscritos.setLayout(null);
		tabConsultaInscritos.setVisible(false);
		
		JPanel tabConsultaDisciplinas = new JPanel();
		tabbedPane.addTab("Processos Ativos", null, tabConsultaDisciplinas, "Processos Ativos");
		tabConsultaDisciplinas.setLayout(null);
		tabConsultaDisciplinas.setVisible(false);

		JLabel lblCursoCodigo = new JLabel("Código do Curso");
		lblCursoCodigo.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblCursoCodigo.setBounds(37, 50, 140, 23);
		tabCurso.add(lblCursoCodigo);

		JLabel lblCursoNome = new JLabel("Nome do Curso");
		lblCursoNome.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblCursoNome.setBounds(37, 105, 140, 23);
		tabCurso.add(lblCursoNome);

		JLabel lblCursoArea = new JLabel("Área de Conhecimento");
		lblCursoArea.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblCursoArea.setBounds(37, 164, 163, 23);
		tabCurso.add(lblCursoArea);

		textCursoCodigo = new JTextField();
		textCursoCodigo.setFont(new Font("Georgia", Font.PLAIN, 14));
		textCursoCodigo.setColumns(10);
		textCursoCodigo.setBounds(210, 50, 96, 22);
		tabCurso.add(textCursoCodigo);

		textCursoNome = new JTextField();
		textCursoNome.setFont(new Font("Georgia", Font.PLAIN, 14));
		textCursoNome.setColumns(10);
		textCursoNome.setBounds(210, 105, 392, 22);
		tabCurso.add(textCursoNome);

		textCursoArea = new JTextField();
		textCursoArea.setFont(new Font("Georgia", Font.PLAIN, 14));
		textCursoArea.setColumns(10);
		textCursoArea.setBounds(210, 164, 392, 22);
		tabCurso.add(textCursoArea);

		JButton btnCursoBuscar = new JButton("Buscar");
		btnCursoBuscar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnCursoBuscar.setBounds(210, 247, 106, 23);
		tabCurso.add(btnCursoBuscar);

		JButton btnCursoCadastrar = new JButton("Cadastrar");
		btnCursoCadastrar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnCursoCadastrar.setBounds(37, 247, 106, 23);
		tabCurso.add(btnCursoCadastrar);

		JButton btnCursoSalvar = new JButton("Salvar");
		btnCursoSalvar.setEnabled(false);
		btnCursoSalvar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnCursoSalvar.setBounds(379, 247, 106, 23);
		tabCurso.add(btnCursoSalvar);

		JButton btnCursoCancelar = new JButton("Cancelar");
		btnCursoCancelar.setEnabled(false);
		btnCursoCancelar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnCursoCancelar.setBounds(559, 247, 106, 23);
		tabCurso.add(btnCursoCancelar);

		table = new JTable();

		JPanel tabDisciplina = new JPanel();
		tabDisciplina.setForeground(Color.DARK_GRAY);
		tabbedPane.addTab("Disciplina", null, tabDisciplina, "Gerenciar Disciplina");
		tabDisciplina.setLayout(null);

		JLabel lblDisciplinaCodigo = new JLabel("Código da Disciplina");
		lblDisciplinaCodigo.setBounds(38, 23, 140, 23);
		lblDisciplinaCodigo.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(lblDisciplinaCodigo);

		JLabel lblDisciplinaNome = new JLabel("Nome da Disciplina");
		lblDisciplinaNome.setBounds(38, 67, 140, 23);
		lblDisciplinaNome.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(lblDisciplinaNome);

		JLabel lblDisciplinaDia = new JLabel("Dia Ministrado");
		lblDisciplinaDia.setBounds(38, 109, 140, 23);
		lblDisciplinaDia.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(lblDisciplinaDia);

		JLabel lblDisciplinaHorario = new JLabel("Horário Início");
		lblDisciplinaHorario.setBounds(38, 149, 140, 23);
		lblDisciplinaHorario.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(lblDisciplinaHorario);

		JLabel lblDisciplinaHorasDiarias = new JLabel("Horas Diárias");
		lblDisciplinaHorasDiarias.setBounds(38, 194, 140, 23);
		lblDisciplinaHorasDiarias.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(lblDisciplinaHorasDiarias);

		JLabel lblDisciplinaCodigoCurso = new JLabel("Código do Curso");
		lblDisciplinaCodigoCurso.setBounds(38, 235, 140, 23);
		lblDisciplinaCodigoCurso.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(lblDisciplinaCodigoCurso);

		JLabel lblDisciplinaCodigoProcesso = new JLabel("Código do Processo");
		lblDisciplinaCodigoProcesso.setBounds(38, 277, 140, 23);
		lblDisciplinaCodigoProcesso.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(lblDisciplinaCodigoProcesso);

		textDisciplinaNome = new JTextField();
		textDisciplinaNome.setBounds(208, 67, 380, 22);
		textDisciplinaNome.setFont(new Font("Georgia", Font.PLAIN, 14));
		textDisciplinaNome.setColumns(10);
		tabDisciplina.add(textDisciplinaNome);

		textDisciplinaDia = new JTextField();
		textDisciplinaDia.setBounds(208, 109, 240, 22);
		textDisciplinaDia.setFont(new Font("Georgia", Font.PLAIN, 14));
		textDisciplinaDia.setColumns(10);
		tabDisciplina.add(textDisciplinaDia);

		textDisciplinaHorario = new JTextField();
		textDisciplinaHorario.setBounds(208, 149, 140, 22);
		textDisciplinaHorario.setFont(new Font("Georgia", Font.PLAIN, 14));
		textDisciplinaHorario.setColumns(10);
		tabDisciplina.add(textDisciplinaHorario);

		textDisciplinaHorasDiarias = new JTextField();
		textDisciplinaHorasDiarias.setBounds(208, 194, 120, 22);
		textDisciplinaHorasDiarias.setFont(new Font("Georgia", Font.PLAIN, 14));
		textDisciplinaHorasDiarias.setColumns(10);
		tabDisciplina.add(textDisciplinaHorasDiarias);

		textDisciplinaCodigoCurso = new JTextField();
		textDisciplinaCodigoCurso.setBounds(208, 235, 96, 22);
		textDisciplinaCodigoCurso.setFont(new Font("Georgia", Font.PLAIN, 14));
		textDisciplinaCodigoCurso.setColumns(10);
		tabDisciplina.add(textDisciplinaCodigoCurso);

		textDisciplinaCodigoProcesso = new JTextField();
		textDisciplinaCodigoProcesso.setBounds(208, 277, 96, 22);
		textDisciplinaCodigoProcesso.setFont(new Font("Georgia", Font.PLAIN, 14));
		textDisciplinaCodigoProcesso.setColumns(10);
		tabDisciplina.add(textDisciplinaCodigoProcesso);

		JButton btnDisciplinaBuscar = new JButton("Buscar");
		btnDisciplinaBuscar.setBounds(241, 346, 106, 23);
		btnDisciplinaBuscar.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(btnDisciplinaBuscar);

		JButton btnDisciplinaCadastrar = new JButton("Cadastrar");
		btnDisciplinaCadastrar.setBounds(70, 346, 106, 23);
		btnDisciplinaCadastrar.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(btnDisciplinaCadastrar);

		JButton btnDisciplinaSalvar = new JButton("Salvar");
		btnDisciplinaSalvar.setEnabled(false);
		btnDisciplinaSalvar.setBounds(404, 346, 106, 23);
		btnDisciplinaSalvar.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(btnDisciplinaSalvar);

		JButton btnDisciplinaCancelar = new JButton("Cancelar");
		btnDisciplinaCancelar.setEnabled(false);
		btnDisciplinaCancelar.setBounds(566, 346, 106, 23);
		btnDisciplinaCancelar.setFont(new Font("Georgia", Font.PLAIN, 14));
		tabDisciplina.add(btnDisciplinaCancelar);

		tableDisciplina = new JTable();

		JScrollPane scrollPaneDisciplina = new JScrollPane(tableDisciplina);
		scrollPaneDisciplina.setBounds(10, 431, 729, 210);
		tabDisciplina.add(scrollPaneDisciplina);

		JButton btnDisciplinaListar = new JButton("Listar disciplinas cadastradas");
		btnDisciplinaListar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnDisciplinaListar.setBounds(10, 398, 231, 23);
		tabDisciplina.add(btnDisciplinaListar);

		JPanel tabInscricao = new JPanel();
		tabbedPane.addTab("Inscrição", null, tabInscricao, "Gerenciar Inscrição");
		tabInscricao.setLayout(null);

		JLabel lblInscricaoCpf = new JLabel("CPF do Professor");
		lblInscricaoCpf.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblInscricaoCpf.setBounds(30, 45, 140, 23);
		tabInscricao.add(lblInscricaoCpf);

		JLabel lblInscricaoCodigoDisciplina = new JLabel("Código da Disciplina");
		lblInscricaoCodigoDisciplina.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblInscricaoCodigoDisciplina.setBounds(30, 92, 140, 23);
		tabInscricao.add(lblInscricaoCodigoDisciplina);

		JLabel lblInscricaoCodigoProcesso = new JLabel("Código do Processo");
		lblInscricaoCodigoProcesso.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblInscricaoCodigoProcesso.setBounds(30, 142, 140, 23);
		tabInscricao.add(lblInscricaoCodigoProcesso);

		textInscricaoCpf = new JTextField();
		textInscricaoCpf.setFont(new Font("Georgia", Font.PLAIN, 14));
		textInscricaoCpf.setColumns(10);
		textInscricaoCpf.setBounds(185, 45, 209, 22);
		tabInscricao.add(textInscricaoCpf);

		textInscricaoCodigoDisciplina = new JTextField();
		textInscricaoCodigoDisciplina.setFont(new Font("Georgia", Font.PLAIN, 14));
		textInscricaoCodigoDisciplina.setColumns(10);
		textInscricaoCodigoDisciplina.setBounds(185, 95, 96, 22);
		tabInscricao.add(textInscricaoCodigoDisciplina);

		textInscricaoCodigoProcesso = new JTextField();
		textInscricaoCodigoProcesso.setText("Preencha um código de disciplina válido");
		textInscricaoCodigoProcesso.setEnabled(false);
		textInscricaoCodigoProcesso.setToolTipText("Preencha um código de disciplina válido");
		textInscricaoCodigoProcesso.setFont(new Font("Georgia", Font.PLAIN, 14));
		textInscricaoCodigoProcesso.setColumns(10);
		textInscricaoCodigoProcesso.setBounds(185, 145, 263, 22);
		tabInscricao.add(textInscricaoCodigoProcesso);

		JButton btnInscricaoBuscar = new JButton("Buscar");
		btnInscricaoBuscar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnInscricaoBuscar.setBounds(227, 244, 106, 23);
		tabInscricao.add(btnInscricaoBuscar);

		JButton btnInscricaoCadastrar = new JButton("Cadastrar");
		btnInscricaoCadastrar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnInscricaoCadastrar.setBounds(49, 244, 106, 23);
		tabInscricao.add(btnInscricaoCadastrar);

		JButton btnInscricaoSalvar = new JButton("Salvar");
		btnInscricaoSalvar.setEnabled(false);
		btnInscricaoSalvar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnInscricaoSalvar.setBounds(402, 244, 106, 23);
		tabInscricao.add(btnInscricaoSalvar);

		JButton btnInscricaoCancelar = new JButton("Cancelar");
		btnInscricaoCancelar.setEnabled(false);
		btnInscricaoCancelar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnInscricaoCancelar.setBounds(576, 244, 106, 23);
		tabInscricao.add(btnInscricaoCancelar);

		tabelaInscricao = new JTable();
		tabelaInscricao.setBounds(0, 0, 1, 1);
		tabInscricao.add(tabelaInscricao);

		JScrollPane scrollPaneInscricao = new JScrollPane(tabelaInscricao);
		scrollPaneInscricao.setBounds(10, 340, 729, 285);
		tabInscricao.add(scrollPaneInscricao);

		JPanel tabProfessor = new JPanel();
		tabbedPane.addTab("Professor", null, tabProfessor, "Cadastrar Professor");
		tabProfessor.setLayout(null);

		JLabel lblProfessorCpf = new JLabel("CPF do Professor");
		lblProfessorCpf.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblProfessorCpf.setBounds(41, 57, 140, 23);
		tabProfessor.add(lblProfessorCpf);

		JLabel lblProfessorNome = new JLabel("Nome do Professor");
		lblProfessorNome.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblProfessorNome.setBounds(41, 103, 140, 23);
		tabProfessor.add(lblProfessorNome);

		JLabel lblProfessorArea = new JLabel("Área de Interesse");
		lblProfessorArea.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblProfessorArea.setBounds(41, 149, 140, 23);
		tabProfessor.add(lblProfessorArea);

		JLabel lblProfessorPontuacao = new JLabel("Pontuação");
		lblProfessorPontuacao.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblProfessorPontuacao.setBounds(41, 197, 140, 23);
		tabProfessor.add(lblProfessorPontuacao);

		textProfessorCpf = new JTextField();
		textProfessorCpf.setFont(new Font("Georgia", Font.PLAIN, 14));
		textProfessorCpf.setColumns(10);
		textProfessorCpf.setBounds(180, 57, 178, 22);
		tabProfessor.add(textProfessorCpf);

		textProfessorNome = new JTextField();
		textProfessorNome.setFont(new Font("Georgia", Font.PLAIN, 14));
		textProfessorNome.setColumns(10);
		textProfessorNome.setBounds(180, 103, 420, 22);
		tabProfessor.add(textProfessorNome);

		textProfessorArea = new JTextField();
		textProfessorArea.setFont(new Font("Georgia", Font.PLAIN, 14));
		textProfessorArea.setColumns(10);
		textProfessorArea.setBounds(180, 149, 386, 22);
		tabProfessor.add(textProfessorArea);

		textProfessorPontuacao = new JTextField();
		textProfessorPontuacao.setFont(new Font("Georgia", Font.PLAIN, 14));
		textProfessorPontuacao.setColumns(10);
		textProfessorPontuacao.setBounds(180, 197, 96, 22);
		tabProfessor.add(textProfessorPontuacao);

		JButton btnProfessorBuscar = new JButton("Buscar");
		btnProfessorBuscar.setToolTipText("Buscar por CPF ou nome");
		btnProfessorBuscar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnProfessorBuscar.setBounds(227, 279, 106, 23);
		tabProfessor.add(btnProfessorBuscar);

		JButton btnProfessorCadastrar = new JButton("Cadastrar");
		btnProfessorCadastrar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnProfessorCadastrar.setBounds(41, 279, 106, 23);
		tabProfessor.add(btnProfessorCadastrar);

		JButton btnProfessorSalvar = new JButton("Salvar");
		btnProfessorSalvar.setEnabled(false);
		btnProfessorSalvar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnProfessorSalvar.setBounds(408, 279, 106, 23);
		tabProfessor.add(btnProfessorSalvar);

		JButton btnProfessorCancelar = new JButton("Cancelar");
		btnProfessorCancelar.setEnabled(false);
		btnProfessorCancelar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnProfessorCancelar.setBounds(586, 279, 106, 23);
		tabProfessor.add(btnProfessorCancelar);

		JButton btnProfessorListar = new JButton("Listar professores cadastrados");
		btnProfessorListar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnProfessorListar.setBounds(10, 338, 245, 23);
		tabProfessor.add(btnProfessorListar);

		tableProfessor = new JTable();

		JScrollPane scrollPaneProfessor = new JScrollPane(tableProfessor);
		scrollPaneProfessor.setBounds(10, 371, 729, 272);
		tabProfessor.add(scrollPaneProfessor);

		NumberFormat numberFormat = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(numberFormat);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(999);
		formatter.setAllowsInvalid(false);

		textDisciplinaCodigo = new JTextField();
		textDisciplinaCodigo.setBounds(208, 25, 96, 22);
		textDisciplinaCodigo.setFont(new Font("Georgia", Font.PLAIN, 14));
		textDisciplinaCodigo.setColumns(10);
		tabDisciplina.add(textDisciplinaCodigo);

		JButton btnCursoListar = new JButton("Listar cursos cadastrados");
		btnCursoListar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnCursoListar.setBounds(10, 313, 211, 23);
		tabCurso.add(btnCursoListar);
		
		tabelaConsultaInscrito = new JTable();
		
		JScrollPane scrollPaneConsultaInscrito = new JScrollPane(tabelaConsultaInscrito);
		scrollPaneConsultaInscrito.setBounds(10, 239, 729, 402);
		tabConsultaInscritos.add(scrollPaneConsultaInscrito);
		
		tabelaConsultaDisciplina = new JTable();
		
		JScrollPane scrollPaneConsultaDisciplina = new JScrollPane(tabelaConsultaDisciplina);
		scrollPaneConsultaDisciplina.setBounds(10, 102, 729, 539);
		tabConsultaDisciplinas.add(scrollPaneConsultaDisciplina);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnConsultar.setBounds(317, 167, 106, 23);
		tabConsultaInscritos.add(btnConsultar);
		
		JComboBox<String> cbNomeDisciplina = new JComboBox<String>();
		cbNomeDisciplina.setBounds(199, 97, 293, 21);
		tabConsultaInscritos.add(cbNomeDisciplina);
		
		JLabel lblNomeDaDisciplina = new JLabel("Nome da Disciplina:");
		lblNomeDaDisciplina.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblNomeDaDisciplina.setBounds(60, 95, 140, 23);
		tabConsultaInscritos.add(lblNomeDaDisciplina);
		
		JLabel lblConsultar = new JLabel("CONSULTAR INSCRITOS POR DISCIPLINA");
		lblConsultar.setFont(new Font("Georgia", Font.PLAIN, 14));
		lblConsultar.setBounds(199, 29, 307, 23);
		tabConsultaInscritos.add(lblConsultar);

		JButton btnListarCurso = new JButton("Listar Processos Ativos");
		btnListarCurso.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnListarCurso.setBounds(233, 69, 270, 23);
		tabConsultaDisciplinas.add(btnListarCurso);

		CursoController cCont = new CursoController(textCursoCodigo, textCursoNome, textCursoArea, table,
				btnCursoCadastrar, btnCursoCancelar, btnCursoSalvar);
		ProfessorController pCont = new ProfessorController(textProfessorCpf, textProfessorNome, textProfessorArea,
				textProfessorPontuacao, tableProfessor, btnProfessorCadastrar, btnProfessorCancelar,
				btnProfessorSalvar);
		DisciplinaController dCont = new DisciplinaController(textDisciplinaCodigo, textDisciplinaNome,
				textDisciplinaDia, textDisciplinaHorario, textDisciplinaHorasDiarias, textDisciplinaCodigoCurso,
				textDisciplinaCodigoProcesso, tableDisciplina, btnDisciplinaCadastrar, btnDisciplinaCancelar,
				btnDisciplinaSalvar);
		ConsultaInscritoController ciCont = new ConsultaInscritoController(cbNomeDisciplina, tabelaConsultaInscrito);
		ConsultaDisciplinaController cdCont = new ConsultaDisciplinaController(tabelaConsultaDisciplina);

		btnCursoCadastrar.addActionListener(cCont);
		btnCursoBuscar.addActionListener(cCont);
		btnCursoCancelar.addActionListener(cCont);
		btnCursoListar.addActionListener(cCont);
		btnCursoSalvar.addActionListener(cCont);

		JScrollPane scrollPaneCurso = new JScrollPane(table);
		scrollPaneCurso.setBounds(10, 346, 729, 295);
		tabCurso.add(scrollPaneCurso, BorderLayout.CENTER);

		btnProfessorCadastrar.addActionListener(pCont);
		btnProfessorBuscar.addActionListener(pCont);
		btnProfessorCancelar.addActionListener(pCont);
		btnProfessorListar.addActionListener(pCont);
		btnProfessorSalvar.addActionListener(pCont);
		
		btnConsultar.addActionListener(ciCont);
		btnListarCurso.addActionListener(cdCont);

		InscricaoController iCont = new InscricaoController(textInscricaoCpf, textInscricaoCodigoDisciplina,
				textInscricaoCodigoProcesso, tabelaInscricao, btnInscricaoCadastrar, btnInscricaoCancelar,
				btnInscricaoSalvar);

		JButton btnInscricaoListar = new JButton("Listar inscrições");
		btnInscricaoListar.setFont(new Font("Georgia", Font.PLAIN, 14));
		btnInscricaoListar.setBounds(10, 307, 140, 23);
		tabInscricao.add(btnInscricaoListar);

		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.DARK_GRAY);
		menuPanel.setBounds(5, 173, 192, 527);
		menuPanel.setVisible(true);

		JButton btnInicio = new JButton("Início");
		btnInicio.setFont(new Font("Georgia", Font.BOLD, 12));
		btnInicio.setBackground(new Color(255, 102, 102));
		btnInicio.setBounds(0, 0, 190, 50);
		JButton btnAbaCurso = new JButton("Gerenciar Curso");
		btnAbaCurso.setBounds(0, 50, 190, 50);
		btnAbaCurso.setFont(new Font("Georgia", Font.BOLD, 12));
		JButton btnAbaDisciplina = new JButton("Gerenciar Disciplina");
		btnAbaDisciplina.setBounds(0, 100, 190, 50);
		btnAbaDisciplina.setFont(new Font("Georgia", Font.BOLD, 12));
		JButton btnAbaProfessor = new JButton("Gerenciar Professor");
		btnAbaProfessor.setBounds(0, 150, 190, 50);
		btnAbaProfessor.setFont(new Font("Georgia", Font.BOLD, 12));

		JButton btnAbaInscricao = new JButton("Gerenciar Inscrição");
		btnAbaInscricao.setBounds(0, 199, 190, 50);
		menuPanel.add(btnAbaInscricao);
		btnAbaInscricao.setFont(new Font("Georgia", Font.BOLD, 12));

		JButton btnAbaConsultarInscritos = new JButton("Consultar Inscritos");
		btnAbaConsultarInscritos.setBounds(0, 250, 190, 50);
		menuPanel.add(btnAbaConsultarInscritos);
		btnAbaConsultarInscritos.setFont(new Font("Georgia", Font.BOLD, 12));

		JButton btnAbaConsultarDisciplinas = new JButton("Processos Ativos");
		btnAbaConsultarDisciplinas.setBounds(0, 300, 190, 50);
		menuPanel.add(btnAbaConsultarDisciplinas);
		btnAbaConsultarDisciplinas.setFont(new Font("Georgia", Font.BOLD, 12));

		btnInicio.setBackground(Color.DARK_GRAY);
		btnAbaCurso.setBackground(Color.DARK_GRAY);
		btnAbaDisciplina.setBackground(Color.DARK_GRAY);
		btnAbaProfessor.setBackground(Color.DARK_GRAY);
		btnAbaInscricao.setBackground(Color.DARK_GRAY);
		btnAbaConsultarInscritos.setBackground(Color.DARK_GRAY);
		btnAbaConsultarDisciplinas.setBackground(Color.DARK_GRAY);

		btnInicio.setForeground(Color.WHITE);
		btnAbaCurso.setForeground(Color.WHITE);
		btnAbaDisciplina.setForeground(Color.WHITE);
		btnAbaProfessor.setForeground(Color.WHITE);
		btnAbaInscricao.setForeground(Color.WHITE);
		btnAbaConsultarInscritos.setForeground(Color.WHITE);
		btnAbaConsultarDisciplinas.setForeground(Color.WHITE);

		tabbedPane.remove(tabCurso);
		tabbedPane.remove(tabDisciplina);
		tabbedPane.remove(tabProfessor);
		tabbedPane.remove(tabInscricao);
		tabbedPane.remove(tabConsultaInscritos);
		tabbedPane.remove(tabConsultaDisciplinas);

		contentPane.add(menuPanel, BorderLayout.WEST);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\beatr\\eclipse-workspace\\Projeto_ContratacaoDocente\\src\\imagens\\FATEC_ZONA_LESTE.png"));
		lblNewLabel.setBounds(5, 6, 187, 168);
		contentPane.add(lblNewLabel);

		btnAbaCurso.addActionListener(e -> abrirAba(tabbedPane, tabCurso, "Curso"));
		btnAbaDisciplina.addActionListener(e -> abrirAba(tabbedPane, tabDisciplina, "Disciplina"));
		btnAbaProfessor.addActionListener(e -> abrirAba(tabbedPane, tabProfessor, "Professor"));
		btnAbaInscricao.addActionListener(e -> abrirAba(tabbedPane, tabInscricao, "Inscrição"));
		btnAbaConsultarInscritos.addActionListener(e -> abrirAba(tabbedPane, tabConsultaInscritos, "Consultar Inscritos"));
		btnAbaConsultarDisciplinas.addActionListener(e -> abrirAba(tabbedPane, tabConsultaDisciplinas, "Processos Ativos"));

		menuPanel.setLayout(null);
		menuPanel.add(btnInicio);
		menuPanel.add(btnAbaCurso);
		menuPanel.add(btnAbaDisciplina);
		menuPanel.add(btnAbaProfessor);
		menuPanel.add(btnAbaConsultarInscritos);

		btnInicio.addActionListener(e -> {
			int numeroAbas = tabbedPane.getTabCount();

			for (int i = numeroAbas - 1; i >= 0; i--) {

				if (i >= 0) {
					tabbedPane.removeTabAt(i);

					numeroAbas = tabbedPane.getTabCount();
				}
			}
		});
		
		btnAbaConsultarInscritos.addActionListener(e -> {
			ConsultaInscritoController consultaInscritoController = new ConsultaInscritoController(cbNomeDisciplina, tabelaConsultaInscrito);
			btnAbaConsultarInscritos.addActionListener(consultaInscritoController);
		});
		
		btnDisciplinaCadastrar.addActionListener(dCont);
		btnDisciplinaBuscar.addActionListener(dCont);
		btnDisciplinaCancelar.addActionListener(dCont);
		btnDisciplinaListar.addActionListener(dCont);
		btnDisciplinaSalvar.addActionListener(dCont);

		btnInscricaoCadastrar.addActionListener(iCont);
		btnInscricaoBuscar.addActionListener(iCont);
		btnInscricaoCancelar.addActionListener(iCont);
		btnInscricaoListar.addActionListener(iCont);
		btnInscricaoSalvar.addActionListener(iCont);

        textInscricaoCodigoDisciplina.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
					iCont.buscaDisciplina();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
					iCont.buscaDisciplina();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
					iCont.buscaDisciplina();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });        
		revalidate();
		repaint();

	}
	
	private static void abrirAba(JTabbedPane tabbedPane, JPanel tab, String nome) {
		tabbedPane.addTab(nome, tab);

		int indexAba = tabbedPane.indexOfTab(nome);
		tabbedPane.setSelectedIndex(indexAba);
	}
}

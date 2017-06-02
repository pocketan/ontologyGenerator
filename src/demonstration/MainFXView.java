package demonstration;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class MainFXView extends Application implements Observer{
	/*** Model ***/
	private InputTextModel iptModel;
	private OntologyModel ontModel;

	/*** Controller ***/
	private MainController controller;

	/*** View ***/
	/** Whole part **/
	private JSplitPane splitpane;

	/* InputText parts */
	private JPanel iptPanel;
	private JScrollPane iptScrollpane;
	private JTextArea iptTextArea;
	private JButton generateBt, importBt;
	/* Ontology parts */
	private JPanel ontPanel;
	private JScrollPane ontScrollpane;
	private JTable ontTable;
	/* Document parts */
	private JPanel docPanel;
	private JScrollPane docScrollpane;
	private JEditorPane editorpane;

	/* コンストラクタ
	public MainFXView(MainController ctrl) {
		//super("OntologyGenerator");
		this.controller = ctrl;

		// controllerにこのviewインスタンスを持たせる
		controller.setMainFXView(this);

		// Modelを参照のために保持する
		iptModel = controller.getI_model();
		ontModel = controller.getO_model();
		// modelのオブザーバーにこのviewを追加
		iptModel.addObserver(this);
	}
	*/
	@Override
	public void init() {
		//this.controller = ctrl;
		// controllerにこのviewインスタンスを持たせる
		controller.setMainFXView(this);
		// Modelを参照のために保持する
		iptModel = controller.getIptModel();
		ontModel = controller.getOntModel();
		// modelのオブザーバーにこのviewを追加
		iptModel.addObserver(this);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {

		designWholeFrame(primaryStage);
	}
	private void designWholeFrame(Stage stage) {
		//setSize(1200,800);
		/*
		setExtendedState(JFrame.MAXIMIZED_BOTH);		// サイズは画面全体
		setLocationRelativeTo(null);					// フレームを中央に表示
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// ウインドウを閉じたら終了
		setLayout(new GridLayout(2, 1));
		 */
		designInputFrame(stage);
		designOutputFrame(stage);
	}

	private void designInputFrame(Stage stage) {
		JPanel pn_menu = new JPanel();
		pn_menu.setLayout(new BoxLayout(pn_menu, BoxLayout.LINE_AXIS));	// 配置順を左から右に
	    importBt = new JButton("インポート");
	    importBt.addActionListener(controller.getImportTextAction());
		pn_menu.add(importBt);
		pn_menu.add(new JLabel("設定"));
		pn_menu.add(Box.createGlue());	// 可変長の隙間を挿入
		pn_menu.add(new JLabel("クリア"));

	    iptTextArea = new JTextArea();
	    iptTextArea.setLineWrap(true);
	    iptScrollpane = new JScrollPane(iptTextArea);

	    iptPanel = new JPanel(new BorderLayout());
	    iptPanel.add(pn_menu, BorderLayout.NORTH);
	    iptPanel.add(iptScrollpane, BorderLayout.CENTER);

	    //add(iptPanel);
	}
	private void designOutputFrame(Stage stage) {
		JPanel pn_menu = new JPanel();
		pn_menu.setLayout(new BoxLayout(pn_menu, BoxLayout.LINE_AXIS));	// 配置順を左から右に
	    generateBt = new JButton("オントロジー構築");
	    generateBt.addActionListener(controller.getGenerateAction());
	    pn_menu.add(Box.createGlue());	// 可変長の隙間を挿入
		pn_menu.add(generateBt);
		pn_menu.add(Box.createGlue());	// 可変長の隙間を挿入
		pn_menu.add(new JLabel("クリア"));

		ontTable = new JTable(ontModel);
		ontScrollpane = new JScrollPane(ontTable);

		ontPanel = new JPanel(new BorderLayout());
		ontPanel.add(pn_menu, BorderLayout.NORTH);
		ontPanel.add(ontScrollpane, BorderLayout.CENTER);

		//add(ontPanel);
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	public void setInputTextModel(InputTextModel i_model) {
		this.iptModel = i_model;
	}
	public void setOntologyModel(OntologyModel o_model) {
		this.ontModel = o_model;
	}
	public void setModels(InputTextModel i_model, OntologyModel o_model) {
		setInputTextModel(i_model);
		setOntologyModel(o_model);
	}

	public JButton getGenerateBt() {
		return generateBt;
	}
	public JTextArea getIptTextArea() {
		return iptTextArea;
	}
	public JButton getImportBt() {
		return importBt;
	}
}
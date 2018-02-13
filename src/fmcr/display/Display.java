package fmcr.display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import fmcr.factory.CodeAnalysisFactory;
import fmcr.main.Client;


/**
 * A class representing FMCR window 
 * For example:
 * <pre>
 *    Display display = new Display();
 *    display.setVisible(true);
 * </pre>
 * 
 * @author  Inah Omoronyia
 * @version 1.0
 */
public class Display extends JFrame{
	private static final long serialVersionUID = 1L;
	/**
	 * Creates new form Display
	 */
	public Display() {
		initComponents();
	}


	private void initComponents() {
		this.setResizable(false);
		this.setTitle("FMCR Source Code Analyser");
		jToolBar1 = new javax.swing.JToolBar();

		button1 = new JButton("Load Source Repository", Client.repositoryIcon);
		button2 = new JButton("Analyse",Client.analysisIcon);


		jTabbedPane1 = new javax.swing.JTabbedPane();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jToolBar1.setRollover(true);


		button1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Client.getDisplay().readLocalRepository();
			}
		});
		jToolBar1.add(button1);


		button2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				analyseProgram();
			}
		});
		jToolBar1.add(button2);

		jToolBar1.add(Box.createHorizontalStrut(700)); 
		jToolBar1.add(Box.createHorizontalGlue());

		createSourceCodeEditorPane();
		jScrollPane1 = new javax.swing.JScrollPane(sourcecodePanel);
		
		createLogTextPane();

		jTabbedPane1.addTab("Fields", createFieldAnalysisPage());
		jTabbedPane1.addTab("Methods", createMethodAnalysisPage());
		jTabbedPane1.addTab("Classes", createClassAnalysisPage());
		jTabbedPane1.addTab("Logs", logTextPane);
		jTabbedPane1.setSelectedIndex(3);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1065, Short.MAX_VALUE)
								.addComponent(jScrollPane1))
						.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
						.addContainerGap())
				);

		pack();
	}                      

	public RSyntaxTextArea textArea;
	JPanel sourcecodePanel;
	public String selectedLine= null;
	private void createSourceCodeEditorPane() {
		sourcecodePanel = new JPanel(new BorderLayout());

		textArea = new RSyntaxTextArea(20, 60);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		sourcecodePanel.add(sp);

		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1) {
					return;
				}
				if (e.getClickCount() != 2) {
					return;
				}

				int offset = textArea.viewToModel(e.getPoint());

				try {
					int rowStart = Utilities.getRowStart(textArea, offset);
					int rowEnd = Utilities.getRowEnd(textArea, offset);
					selectedLine = textArea.getText().substring(rowStart, rowEnd);
					selectedLine = selectedLine.replace("{", "");
					analyseProgram();

				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	private void clearSourceStatements() {
		textArea.setText("");
	}
	public void addSourceStatement(String statement) {
		textArea.append(statement+"\n");
	}
	
	
	/**
	 * Reads a .java file for program analysis
	 */
	public void readLocalRepository() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select Source File ");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".java", "java", "java");
		chooser.setFileFilter(filter);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			Thread queryThread2 = new Thread() {
				public void run() {		
					BufferedReader buff = null;

					clearSourceStatements();

					String[] s = chooser.getSelectedFile().toString().split(Pattern.quote("."));
					if(!s[s.length-1].equals("java")) {
						return;
					}

					try {	
						buff = new BufferedReader(new FileReader(chooser.getSelectedFile()));
						String str;
						while ((str = buff.readLine()) != null) {
							addSourceStatement(str);

						}
					}
					catch (IOException e) {
						System.err.print(e);
					} 

					faView = null;
					maView = null;
					caView = null;					
				}
			};
			queryThread2.start();				
		}        
	} 



	/**
     * Executes program analysis
     * @param button press event (ActionEvent).
     */
	private void analyseProgram() {
		Thread queryThread2 = new Thread() {
			public void run() {
				clearLogPane(logTextPane);
				jTabbedPane1.setSelectedIndex(0);

				Client.loadCompilationUnit();
				updateFieldAnalysisPage();
				updateMethodAnalysisPage();
				updateClassAnalysisPage();
				new CodeAnalysisFactory().displayReports(faView,maView,caView);
				
			}
		};
		queryThread2.start();
	}

	private JPanel infopc = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static JPanel classAnalysisPanel;
	private static final JLabel cblabel = new JLabel("<html><font size='3'>Inappropriate Access Level</font></html>");
	private static final JLabel bclabel = new JLabel("<html><font size='3'>Unused Parameter</font></html>");
	private static final JLabel cnlabelc = new JLabel("<html><font size='3'>Missing Documentation</font></html>");

	public JComponent createClassAnalysisPage(){
		classAnalysisPanel = new JPanel(new BorderLayout(0,0));	

		jTabbedPane1.revalidate();
		jTabbedPane1.repaint();

		return classAnalysisPanel;
	}	

	ClassAnalysisView caView =null;
	public void updateClassAnalysisPage(){
		classAnalysisPanel.removeAll();				
		caView = new ClassAnalysisView();

		cblabel.setIcon(Client.fBugIcon);
		bclabel.setIcon(Client.mBugIcon);
		cnlabelc.setIcon(Client.docBugIcon);
		infopc.add(cblabel);
		infopc.add(bclabel);
		infopc.add(cnlabelc);

		classAnalysisPanel.add(infopc,BorderLayout.SOUTH);
		classAnalysisPanel.add(caView,BorderLayout.CENTER);
		
	}

	private JPanel infopm = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static JPanel methodAnalysisPanel;
	private static final JLabel bmlabel = new JLabel("<html><font size='3'>Unused Parameter</font></html>");
	private static final JLabel cnlabelm = new JLabel("<html><font size='3'>Missing Documentation</font></html>");

	public JComponent createMethodAnalysisPage(){
		methodAnalysisPanel = new JPanel(new BorderLayout(0,0));	

		jTabbedPane1.revalidate();
		jTabbedPane1.repaint();

		return methodAnalysisPanel;
	}

	MethodAnalysisView maView = null;
	public void updateMethodAnalysisPage(){
		methodAnalysisPanel.removeAll();				
		maView = new MethodAnalysisView();

		bmlabel.setIcon(Client.mBugIcon);
		cnlabelm.setIcon(Client.docBugIcon);
		infopm.add(bmlabel);
		infopm.add(cnlabelm);
		methodAnalysisPanel.add(infopm,BorderLayout.SOUTH);
		methodAnalysisPanel.add(maView,BorderLayout.CENTER);	
		
	}

	private JPanel infopf = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static final JLabel fblabel = new JLabel("<html><font size='3'>Inappropriate Access Level</font></html>");
	private static final JLabel cnlabelf = new JLabel("<html><font size='3'>Missing Documentation</font></html>");
	private static JPanel fieldAnalysisPanel;
	public JComponent createFieldAnalysisPage(){
		fieldAnalysisPanel = new JPanel(new BorderLayout(0,0));	

		jTabbedPane1.revalidate();
		jTabbedPane1.repaint();

		return fieldAnalysisPanel;
	}

	public JPanel getFieldAnalysisPanel() {
		return fieldAnalysisPanel;
	}


	FieldAnalysisView faView =null;
	public void updateFieldAnalysisPage(){
		fieldAnalysisPanel.removeAll();

		faView = new FieldAnalysisView();

		fblabel.setIcon(Client.fBugIcon);
		cnlabelf.setIcon(Client.docBugIcon);
		infopf.add(fblabel);
		infopf.add(cnlabelf);
		fieldAnalysisPanel.add(infopf,BorderLayout.SOUTH);
		fieldAnalysisPanel.add(faView,BorderLayout.CENTER);
		
	}

	private static JTextPane logTextPane;
	private StyleSheet styleSheet = new StyleSheet();
	private HTMLDocument htmlDocument;
	private HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
	JPopupMenu clearpop;
	/**
	 * program analysis logging
	 */
	public void createLogTextPane(){

		styleSheet.addRule("ul.tree, ul.tree ul {list-style: none; margin: 0; padding: 0; } ");
		styleSheet.addRule("ul.tree ul { margin-left: 10px; }");
		styleSheet.addRule("ul.tree li { margin: 0; padding: 0 7px; line-height: 20px; color: #369; font-weight: bold; border-left:1px solid rgb(100,100,100);}");
		styleSheet.addRule("ul.tree li:last-child { border-left:none; }");
		styleSheet.addRule("ul.tree li:before {position:relative; top:-0.3em; height:1em; width:12px; color:white; border-bottom:1px solid rgb(100,100,100); content:''; display:inline-block; left:-7px; }");
		styleSheet.addRule("ul.tree li:last-child:before { border-left:1px solid rgb(100,100,100); }");

		Font font = new Font("Verdana", Font.PLAIN, 10);
		String bodyRule = "body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }";
		styleSheet.addRule(bodyRule);

		htmlEditorKit.setStyleSheet(styleSheet);
		htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
		try {
			htmlEditorKit.insertHTML(htmlDocument, htmlDocument.getLength(),"", 0, 0, null);
		} catch (BadLocationException | IOException e1) {
			e1.printStackTrace();
		}

		logTextPane = new JTextPane();
		logTextPane.setEditorKit(htmlEditorKit);
		logTextPane.setDocument(htmlDocument);

		try {

			Container contentPane = getContentPane();
			contentPane.add(logTextPane, BorderLayout.CENTER);
			super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		clearpop = new JPopupMenu();
		JMenuItem clearmi = new JMenuItem("Clear");
		clearmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearLogPane(logTextPane);
			}

		}); 

		clearpop.add(clearmi);
		PopupMenuListener pml = new PopupMenuListener();
		logTextPane.addMouseListener(pml);
		logTextPane.setEditable(false);
	}

	class PopupMenuListener extends MouseAdapter {
		public void mousePressed(MouseEvent me) {
			showPopup(me);
		}

		public void mouseReleased(MouseEvent me) {
			showPopup(me);
		}

		private void showPopup(MouseEvent me) {
			if (me.isPopupTrigger()) {
				clearpop.show(me.getComponent(),
						me.getX(), me.getY());
			}
		}
	}

	public static void clearLogPane(JTextPane tp){
		tp.setText("");
	}

	/**
	 * program error log update
	 */
	public  void appendToPane(String msg, boolean isError){
		if(isError){
			msg = "<html><font color='red'>"+msg+"</font><br></html>";
		}
		else{
			msg = "<html>"+msg+"<br></html>";
		}

		try {
			htmlEditorKit.insertHTML(htmlDocument, htmlDocument.getLength(), msg, 0, 0, null);	
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}

		int len = logTextPane.getDocument().getLength();
		logTextPane.setCaretPosition(len);
		logTextPane.replaceSelection(msg);	

		logTextPane.revalidate();
		logTextPane.repaint();
		jTabbedPane1.setSelectedIndex(3);
	}

	/**
     * program analysis log update
     */
	public void updateLogPage(final String text, final boolean isError){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				appendToPane(text, isError);

			}
		});	
	}


	private JButton button1;
	private JButton button2;
	private JScrollPane jScrollPane1;
	private JTabbedPane jTabbedPane1;
	private JToolBar jToolBar1;
}


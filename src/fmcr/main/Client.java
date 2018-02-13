package fmcr.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.ImageIcon;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import fmcr.display.Display;
import fmcr.factory.CodeAnalysisFactory;
import fmcr.visitors.CodeVisitor;
import fmcr.visitors.InitVisitor;


/**
 * This class contains FMCR main method and utility functions. The key functions include:
 * <pre>
 * (1) Uses JavaParser to create create an AST
 * </pre> 
 * <pre>
 * (2) Initialises different Visitors
 * </pre> 
 * <pre>
 * (3) Injects the visitor into the AST
 * </pre> 
 * @author  Inah Omoronyia
 * @version 1.0
 */
public class Client {
	
	private static CompilationUnit cu;
	
	private static Display display;
	
	public static final ImageIcon mBugIcon = new ImageIcon("img/ladybird.png");
	public static final ImageIcon fBugIcon = new ImageIcon("img/bug2.png");
	public static final ImageIcon docBugIcon = new ImageIcon("img/reddot.png");
	public static final ImageIcon repositoryIcon = new ImageIcon("img/download.png");
	public static final ImageIcon analysisIcon = new ImageIcon("img/analyse.png");

    public static final ImageIcon blankIcon = new ImageIcon();
        
	//code (class) summary
	public static double totalNoFields =0;
	public static double nonDocumentedFields = 0;
	public static double noAccessLevelBugs = 0;

	public static double totalNoMethods = 0;
	public static double nonDocumentedMethods = 0;
	public static double noUnusedParameterBug = 0;	
			
	public static String className;
	public static String packageName;
	public static boolean isEnum;
	public static boolean isInterface;
	public static boolean isinner;
	public static String innerClassName;
	
	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				display = new Display();
				display.setVisible(true);
			}
		});
	}
	
	public static boolean loadCompilationUnit() {
		try {
			String code = getDisplay().textArea.getText();
			cu = JavaParser.parse(code );
			if(cu == null) {
				return false;
			}
			return true;
		}
		catch(ParseProblemException e) {
			List<Problem> problems = e.getProblems();
			try {
				String firstProblemdesc = problems.get(0).getMessage();
				String firstProblemBegin = problems.get(0).getLocation().get().toRange().get().begin.toString();
				String firstProblemEnd = problems.get(0).getLocation().get().toRange().get().end.toString();
				Client.getDisplay().updateLogPage(firstProblemdesc +":"+firstProblemBegin+"-"+firstProblemEnd, true);
				return false;
			} catch (Exception e1) {
				return false;
			}
		}

	}
	
	public static void loadCompilationUnit(InputStream inputstream) throws IOException, ParseProblemException{
		if(inputstream == null) {
			return;
		}
		cu = JavaParser.parse(inputstream );
	}
	
	public  static void doCodeAnalysis(CodeAnalysisFactory caf){
		if(Client.getCompilationUnit() !=null) {
			
			className = null;
			packageName = null;
			isEnum = false;
			isInterface = false;
			
			VoidVisitor<Void> pcVisitor = new InitVisitor();
			VoidVisitor<Void> codeVisitor = new CodeVisitor(caf);
			
			pcVisitor.visit(Client.getCompilationUnit(), null);
			if(className ==null) {
				Client.getDisplay().updateLogPage("UNPURSABLE SOURCE NULL", true);
			}
			else {
				codeVisitor.visit(Client.getCompilationUnit(), null);
			}
		}			
	}
	
	public static CompilationUnit getCompilationUnit() {
		return cu;
	}
		
	public static Display getDisplay() {
		return display;
	}

	public static void setDisplay(Display display) {
		Client.display = display;
	}

	public static void resetClassAtts() {
		totalNoFields =0;
		nonDocumentedFields = 0;
		noAccessLevelBugs = 0;

		totalNoMethods = 0;
		nonDocumentedMethods = 0;
		noUnusedParameterBug = 0;	
	}
}

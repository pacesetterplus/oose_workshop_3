package fmcr.visitors;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.swing.plaf.SeparatorUI;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import fmcr.factory.CodeAnalysisFactory;
import fmcr.main.Client;
import fmcr.util.MParameter;
import fmcr.util.Report;
import fmcr.util.ReportTag;
import fmcr.util.Variable;

/**
 * A visitor that traverse an AST to extract its program properties
 *
 * @author <Yusuf Ogunjobi, 1009097o>
 * @version 1.0
 * 
 * @see fmcr.visitors.InitVisitor
 */
public class CodeVisitor extends VoidVisitorAdapter<Void> {
	CodeAnalysisFactory caf;
	boolean used = false;// flag for used parameter
	BlockStmt blockStatement;

	public CodeVisitor(CodeAnalysisFactory caf) {
		this.caf = caf;
	}

	/**
	 * Workshop 3: Visitors Design Pattern with JavaParser (Q1)
	 */
	@Override
	public void visit(FieldDeclaration fd, Void arg) {
		super.visit(fd, arg); // Do not delete
		Report fr = new Report(ReportTag.FIELD);// Do not delete

		/*
		 * Q1a: Populate fr with all the field variables (0.5 mark).
		 */
		// forEach will not be executed if list is empty.
		fd.getVariables().forEach(variable -> {
			fr.addVariable(new Variable(variable.getNameAsString(), variable.getType().toString()));
		});

		/*
		 * Q1b: Populate fr with all the field modifiers (0.5 mark).
		 */
		fd.getModifiers().forEach(modifier -> {
			fr.addModifier(modifier.asString());
		});

		/*
		 * Q1c: Retrieve the line position of the field in the source code, then use the
		 * result to call setLine() method on fr (1 mark).
		 */

		Optional<Range> range = fd.getRange();// the range of characters that this node covers
		if (range.isPresent()) {
			fr.setLine(range.get().begin.line);
		}

		/*
		 * Q1d: Call setInappropriateAccessLevel() method in fr. The method should take
		 * boolean value true only when the field is non-private and non-final,
		 * otherwise a parameter value should be false (1 mark).
		 */
		
		EnumSet<Modifier> modSet = fd.getModifiers();
		if (!(modSet.contains(Modifier.FINAL) || modSet.contains(Modifier.PRIVATE))) {
			fr.setInappropriateAccessLevel(true);
		 //System.out.println(true);
		} else {
			fr.setInappropriateAccessLevel(false);

		}

		/*
		 * Q1e: Call setDocumented() method in fr. The method should take boolean value
		 * true only when the field has a Javadoc comment, otherwise a parameter value
		 * should be false (1 mark).
		 */
		if (fd.getJavadoc().isPresent()) {
			fr.setDocumented(true);
		} else {
			fr.setDocumented(false);
		}

		caf.updateFieldsTable(fr);// Do not delete

	}

	/**
	 * Workshop 3: Visitors Design Pattern with JavaParser (Q2)
	 */
	@Override
	public void visit(MethodDeclaration md, Void arg) {
		super.visit(md, arg);// Do not delete
		Report mr = new Report(ReportTag.METHOD);// Do not delete

		/*
		 * Q2a: Call setMethodName() method in mr. The method should take as parameter
		 * the name of the method declaration (0.5 mark).
		 */
		mr.setMethodName(md.getName().asString());

		/*
		 * Q2b: Call setDocumented() method in mr. The method should take boolean value
		 * true only when the method declaration has a Javadoc comment, otherwise a
		 * parameter value should be false (1 mark).
		 */
		if (md.getJavadoc().isPresent()) {
			mr.setDocumented(true);
		} else {
			mr.setDocumented(false);
		}
		/*
		 * Q2c: Populate mr with all the method's modifiers (0.5 mark).
		 */
		md.getModifiers().forEach(modifier -> {
			mr.addModifier(modifier.asString());
		});

		/*
		 * Q2d:Populate mr with all the method's parameters (0.5 mark)
		 */
		md.getParameters().forEach(parameter -> {
			mr.addParameter(new MParameter(parameter.getNameAsString(), parameter.getType().asString()));
		});

		/*
		 * Q2e: Whenever a method declaration has a body, call the setUnusedParameter()
		 * method in mr. It should take boolean value true only when the method
		 * declaration has a parameter that is unused in its body. Otherwise the value
		 * should be false (2 mark).
		 */
		md.getBody().ifPresent(body -> {
			blockStatement = md.getBody().get();
		});
		if (!blockStatement.isEmpty()) {// method has a body
			ArrayList<Node> leaves = new ArrayList<Node>();
			retrieveleaveNodes(blockStatement, leaves);
			md.getParameters().forEach(parameter -> {
				leaves.forEach(leave -> {
					//check if leave is a SimpleName object
					//and check it equals the SimpleName of parameter.
					if (leave instanceof SimpleName) {
						if (parameter.getName().equals(leave)) {
							used = true;
						}
					}
				});
				if (used) {
					mr.setUnusedParameter(false);
				} else {
					mr.setUnusedParameter(true);
				}
			});
		} else {// no body, abstract method
			mr.setUnusedParameter(true);
		}

		/*
		 * Q2f: Call setReturnType() method in mr. The method should take as parameter
		 * the return type of the method declaration (0.5 mark).
		 */
		mr.setReturnType(md.getType().asString());

		/*
		 * Q2g: Retrieve the start and end position of the declared method. Use the
		 * results to call setStartLine() and setSize() on mr respectively. The size of
		 * a method is the difference between the start and end position) (1 mark).
		 */
		md.getRange().ifPresent(range -> {
			int startLine = range.begin.line;
			int endLine = range.end.line;

			mr.setStartLine(startLine);
			mr.setSize(endLine - startLine + 1);// is adding one needed.
		});

		caf.updateMethodsTable(mr);// Do not Delete
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
		super.visit(cid, null);
		Client.isinner = cid.isInnerClass();
		if (Client.isinner) {
			Client.innerClassName = cid.getNameAsString();// +"[INNER]";
		}
	}

	/**
	 * 
	 * @param node
	 *            - parent
	 * @param leaves
	 *            - children This function uses recursion to retrieve the leaves
	 *            from a parent node
	 */
	public void retrieveleaveNodes(Node node, ArrayList<Node> leaves) {
		List<Node> children = node.getChildNodes();
		if (children.isEmpty()) {
			leaves.add(node);
		} else {
			children.forEach(c1 -> {
				retrieveleaveNodes(c1, leaves);
			});
		}
	}
}

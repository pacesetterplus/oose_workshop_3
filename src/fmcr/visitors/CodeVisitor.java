package fmcr.visitors;

import java.util.ArrayList;
import java.util.List;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import fmcr.factory.CodeAnalysisFactory;
import fmcr.main.Client;
import fmcr.util.Report;
import fmcr.util.ReportTag;

/**
 * A visitor that traverse an AST to extract its program properties
 *
 * @author <Yusuf Ogunjobi, 1009097>
 * @version 1.0
 * 
 * @see     fmcr.visitors.InitVisitor
 */
public class CodeVisitor extends VoidVisitorAdapter<Void>{
	CodeAnalysisFactory caf;

	public CodeVisitor(CodeAnalysisFactory caf) {
		this.caf = caf;
	}
	
	/**
	 * Workshop 3: Visitors Design Pattern with JavaParser (Q1)
	 */
	@Override
	public void visit(FieldDeclaration fd, Void arg) {
		super.visit(fd, arg); //Do not delete
		Report fr = new Report(ReportTag.FIELD);//Do not delete
		
		
		/*
		 * Q1a: Populate fr with all the field variables (0.5 mark).
		 */

		
		
		/*
		 * Q1b: Populate fr with all the field modifiers (0.5 mark).
		 */

		
		
		/*
		 * Q1c: Retrieve the line position of the field in the source code, 
		 * then use the result to call setLine() method on fr (1 mark).
		 */

		
		
		/*
		 * Q1d: Call setInappropriateAccessLevel() method in fr. The method 
		 * should take boolean value true only when the field is non-private
		 * and non-final, otherwise a parameter value should be false (1 mark).
		 */

		
		
		/*
		 * Q1e: Call setDocumented() method in fr. The method should take boolean 
		 * value  true only when the field has a Javadoc comment, otherwise a
		 * parameter value should be false (1 mark).
		 */

		
		
		
		
		caf.updateFieldsTable(fr);//Do not delete
	}
	
	/**
	 * Workshop 3: Visitors Design Pattern with JavaParser (Q2)
	 */
	@Override		
	public void visit(MethodDeclaration md, Void arg) {
		super.visit(md, arg);//Do not delete
		Report mr = new Report(ReportTag.METHOD);//Do not delete
		

		/*
		 * Q2a: Call setMethodName() method in mr. The method should
		 * take as parameter the name of the method declaration (0.5 mark).
		 */
		
		
		/*
		 * Q2b: Call setDocumented() method in mr. The method should 
		 * take boolean value  true only when the method declaration has
		 * a Javadoc comment, otherwise a parameter value should be false (1 mark).
		 */

		
		/*
		 * Q2c: Populate mr with all the method's modifiers (0.5 mark).
		 */

		

		/*
		 * Q2d:Populate mr with all the method's parameters (0.5 mark)
		 */
		
		
		
		/*
		 * Q2e: Whenever a method declaration has a body, call the setUnusedParameter()
		 * method in mr. It should take boolean value true only when the method declaration
		 * has a parameter that is unused in its body. Otherwise the value should be false (2 mark).
		 */

		
		/*
		 * Q2f: Call setReturnType() method in mr. The method should take as parameter the
		 * return type of the method declaration (0.5 mark).
		 */


		
		
		/*
		 * Q2g: Retrieve the start and end position of the declared method. Use the results 
		 * to call setStartLine() and setSize() on mr respectively. The size of a method is
		 * the difference between the start and end position) (1 mark).
		 */

		
		
		
		
		caf.updateMethodsTable(mr);//Do not Delete
	}
	
	
	@Override
	public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
		super.visit(cid, null);
		Client.isinner = cid.isInnerClass();
		if(Client.isinner) {
			Client.innerClassName = cid.getNameAsString();//+"[INNER]";
		}			
	}
	
	/**
	 * 
	 * @param node - parent 
	 * @param leaves - children
	 * This function uses recursion to retrieve the leaves from a parent node
	 */
	public void retrieveleaveNodes(Node node, ArrayList<Node> leaves) {
		List<Node> children = node.getChildNodes();		
		if(children.isEmpty()) {
			leaves.add(node);
		}
		else {
			children.forEach(c1 ->{
				retrieveleaveNodes(c1,leaves);
			});
		}
	}	
}

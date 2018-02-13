package fmcr.visitors;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import fmcr.main.Client;

/**
 * The initial visitor that traverse an AST to determine 
 * the className, packageName and establish if its an interface or enum class
 *
 * @author  Inah Omoronyia
 * @version 1.0
 * 
 * @see     fmcr.visitors.CodeVisitor
 */

public class InitVisitor extends VoidVisitorAdapter<Void>{
	
	@Override
	public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
		super.visit(cid, null);
		String c_temp = cid.getNameAsString();
		if(c_temp != null && c_temp.length() >0) {
			Client.className = cid.getNameAsString();
			if(cid.isInterface()) {
				Client.isInterface = true;
			}
		}		
	}
	
	@Override
	public void visit(EnumDeclaration ed, Void arg) {
		super.visit(ed, arg);
//		className = ed.getNameAsString()+"[ENUM]";
		Client.className = ed.getNameAsString();//+"[ENUM]";
		Client.isEnum = true;
	}
	
	@Override
	public void visit(PackageDeclaration pd, Void arg) {
		super.visit(pd, arg);
		Client.packageName = pd.getNameAsString();
	}
}

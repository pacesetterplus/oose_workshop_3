package fmcr.factory;

import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.Optional;

import com.github.javaparser.Range;

import fmcr.display.ClassAnalysisView;
import fmcr.display.FieldAnalysisView;
import fmcr.display.MethodAnalysisView;
import fmcr.main.Client;
import fmcr.util.Report;

/**
 * Populates the FieldAnalysisView, MethodAnalysisView and ClassAnalysisView  with the outcome 
 * of field method and class analysis results respectively.
 *
 * @author  Inah Omoronyia
 * @version 1.0
 * @see     fmcr.display.FieldAnalysisView
 * @see     fmcr.display.MethodAnalysisView
 * @see     fmcr.display.ClassAnalysisView
 */
public class CodeAnalysisFactory {

	FieldAnalysisView fv;
	MethodAnalysisView mv;
	ClassAnalysisView cv;
	
	public void displayReports(final FieldAnalysisView fv, final MethodAnalysisView mv, final ClassAnalysisView cv){
		this.fv = fv;
		this.mv = mv;
		this.cv = cv;
		Client.doCodeAnalysis(this);
		codeSummary();
	}
	
	public void updateFieldsTable(Report r) {
		
		//update the class and package name of r
		if(Client.className ==null) {
			System.out.println("UNPURSABLE SOURCE");
		}
		
		if(Client.packageName !=null) {
			r.setPackageName(Client.packageName);			
		}
		
		if(Client.isinner) {
			r.setClassName(Client.className+"."+Client.innerClassName);	
		}
		else {
			r.setClassName(Client.className);
		}
		
		//update field, documented and access level counters
		Client.totalNoFields = Client.totalNoFields +1;
		if(!r.isDocumented()) {
			Client.nonDocumentedFields = Client.nonDocumentedFields+1;
		}
		if(r.hasInappropriateAccessLevel()) {
			Client.noAccessLevelBugs = Client.noAccessLevelBugs+1;
		}


		//include labels depending on report properties
		boolean accessLevelBug = r.hasInappropriateAccessLevel();
		int line = r.getLine();

		if(accessLevelBug) {
			if(r.isDocumented()) {
				fv.model.addRow(new Object[]{Client.fBugIcon,Client.blankIcon, r, line});
			}
			else {
				fv.model.addRow(new Object[]{Client.fBugIcon,Client.docBugIcon, r, line});
			}
		}
		else {
			if(r.isDocumented()) {
				fv.model.addRow(new Object[]{Client.blankIcon, Client.blankIcon, r, line});
			}
			else {
				fv.model.addRow(new Object[]{Client.blankIcon, Client.docBugIcon, r, line});
			}						
		}

		fv.model.fireTableDataChanged();
		Rectangle cellBounds1 = fv.table.getCellRect(fv.table.getRowCount() - 1, 0, true);
		fv.table.scrollRectToVisible(cellBounds1);

		fv.repaint();
		fv.updateUI();
	}
	
	public void updateMethodsTable(Report r) {
		
		//update the class and package name of r
		if(Client.packageName !=null) {
			r.setPackageName(Client.packageName);			
		}
		if(Client.className ==null) {
			System.out.println("UNPURSABLE SOURCE");
		}
		if(Client.isinner) {
			r.setClassName(Client.className+"."+Client.innerClassName);	
		}
		else {
			r.setClassName(Client.className);
		}
		
		//update method, documented and access level counters
		Client.totalNoMethods = Client.totalNoMethods+1;
		if(!r.isDocumented()) {
			Client.nonDocumentedMethods = Client.nonDocumentedMethods+1;
		}
		if(r.hasUnusedParameter()) {
			Client.noUnusedParameterBug = Client.noUnusedParameterBug+1;
		}
				
		//include labels depending on report properties
		if(r.hasUnusedParameter()) {
			if(r.isDocumented()) {
				mv.model.addRow(new Object[]{Client.mBugIcon,Client.blankIcon, r,  r.getStartLine(), r.getSize()});
			}
			else {
				mv.model.addRow(new Object[]{Client.mBugIcon, Client.docBugIcon, r, r.getStartLine(), r.getSize()});
			}
		}
		else {
			if(r.isDocumented()) {
				mv.model.addRow(new Object[]{Client.blankIcon, Client.blankIcon,r, r.getStartLine(),r.getSize()});
			}
			else {
				mv.model.addRow(new Object[]{Client.blankIcon,Client.docBugIcon,r, r.getStartLine(), r.getSize()});
			}
		}

		mv.model.fireTableDataChanged();
		Rectangle cellBounds2 = mv.table.getCellRect(mv.table.getRowCount() - 1, 0, true);
		mv.table.scrollRectToVisible(cellBounds2);

		mv.repaint();
		mv.updateUI();
	}
	
	

	private void codeSummary() {
		double ratioNonDocumentedNodes =0;
		if((Client.nonDocumentedFields +Client.nonDocumentedMethods)>0) {
			ratioNonDocumentedNodes= ((Client.nonDocumentedFields +Client.nonDocumentedMethods)/(Client.totalNoFields+Client.totalNoMethods))*100;
		}
		double ratioAccessLevelBugs =0;
		if(Client.totalNoFields>0) {
			ratioAccessLevelBugs = (Client.noAccessLevelBugs/Client.totalNoFields)*100;
		}
		double ratioUnusedParameterBug=0;
		if(Client.totalNoMethods>0) {
			ratioUnusedParameterBug = (Client.noUnusedParameterBug/Client.totalNoMethods)*100;
		}

		double codeQty =0;
		codeQty = ((100-ratioNonDocumentedNodes)+(100-ratioAccessLevelBugs)+(100-ratioUnusedParameterBug))/3;

		Optional<Range> range = Client.getCompilationUnit().getRange();
		int begin = range.get().begin.line;
		int end = range.get().end.line;
		int size = end-begin;

		DecimalFormat df = new DecimalFormat("#.##"); 
		String header = null;
		header=	Client.className;

		if(Client.isEnum) {
			cv.model.addRow(new Object[]{header+"[ENUM]",df.format(ratioNonDocumentedNodes), df.format(ratioAccessLevelBugs), df.format(ratioUnusedParameterBug) , df.format(codeQty), size});
		}
		else if(Client.isInterface) {
			cv.model.addRow(new Object[]{header+"[INTERFACE]",df.format(ratioNonDocumentedNodes), df.format(ratioAccessLevelBugs), df.format(ratioUnusedParameterBug) , df.format(codeQty), size});
		}
		else {
			cv.model.addRow(new Object[]{header,df.format(ratioNonDocumentedNodes), df.format(ratioAccessLevelBugs), df.format(ratioUnusedParameterBug) , df.format(codeQty), size});
		}

		cv.model.fireTableDataChanged();
		Rectangle cellBounds = cv.table.getCellRect(cv.table.getRowCount() - 1, 0, true);
		cv.table.scrollRectToVisible(cellBounds);

		cv.repaint();
		cv.updateUI();
		
		Client.resetClassAtts();
	}
	
}

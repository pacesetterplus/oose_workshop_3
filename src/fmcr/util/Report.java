package fmcr.util;

import java.util.ArrayList;

/**
 * A data structure used to hold relevant properties mined from a visited node in an AST. 
 * A FIELD tag is used to identify when the visited node is a (or contained in a) field 
 * A METHOD tag is used to identify when visited node is a (or contained in a method. 
 * For example:
 * <pre>
 *  Report r = new Report(ReportTag.FIELD);
 * </pre>
 *
 * @author  Inah Omoronyia
 * @version 1.0
 * @see     fmcr.util.ReportTag
 */
public class Report {	
	
	//tag
	private ReportTag tag;
	
	//generic attributes
	private ArrayList<String> modifiers; //Method modifiers
	private boolean documented;
	
	//class attributes
	private String packageName = "";
	private String className;
	
	//field attributes
	private String fieldName;
	private ArrayList<Variable> variables; //Field variables
	private boolean inappropriateAccessLevel;
	private int line; 
	
	//method attributes
	private String methodName;
	private ArrayList<MParameter> parameters; //Method parameters
	private boolean unusedParameter;
	private String returnType;
	private int startLine;
	private int size; //Difference between the start and end line of the method
		
	//call graph attributes
	private String sourceClass ="";
	private String sourceMethod ="";
	private String sourceMethodArguments ="";
	
	private String targetClass ="";
	private String targetMethod ="";
	private String targetMethodArguments ="";
	
	
	public Report(ReportTag tag) {
		this.tag = tag;
		modifiers = new ArrayList<String>();
		variables = new ArrayList<Variable>();
		parameters = new ArrayList<MParameter>();
	}
	
	
	public ReportTag getTag() {
		return tag;
	}
	public void setTag(ReportTag tag) {
		this.tag = tag;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public ArrayList<String> getModifiers() {
		return modifiers;
	}
	public void addModifier(String modifier) {
		modifiers.add(modifier);
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public boolean hasInappropriateAccessLevel() {
		return inappropriateAccessLevel;
	}
	public void setInappropriateAccessLevel(boolean inappropriateAccessLevel) {
		this.inappropriateAccessLevel = inappropriateAccessLevel;
	}
	public ArrayList<Variable> getVariables() {
		return variables;
	}
	public void addVariable(Variable v) {
		variables.add(v);
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}

	public boolean isDocumented() {
		return documented;
	}

	public void setDocumented(boolean documented) {
		this.documented = documented;
	}
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public ArrayList<MParameter> getParameters() {
		return parameters;
	}
	public void addParameter(MParameter p) {
		parameters.add(p);
	}
	public boolean hasUnusedParameter() {
		return unusedParameter;
	}
	public void setUnusedParameter(boolean unusedParameter) {
		this.unusedParameter = unusedParameter;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	
	
	public String getSourceClass() {
		return sourceClass;
	}


	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}


	public String getSourceMethod() {
		return sourceMethod;
	}


	public void setSourceMethod(String sourceMethod) {
		this.sourceMethod = sourceMethod;
	}


	public String getSourceMethodArguments() {
		return sourceMethodArguments;
	}


	public void setSourceMethodArguments(String sourceMethodArguments) {
		this.sourceMethodArguments = sourceMethodArguments;
	}


	public String getTargetClass() {
		return targetClass;
	}


	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}


	public String getTargetMethod() {
		return targetMethod;
	}


	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}


	public String getTargetMethodArguments() {
		return targetMethodArguments;
	}


	public void setTargetMethodArguments(String targetMethodArguments) {
		this.targetMethodArguments = targetMethodArguments;
	}


	@Override
	public String toString() {
		if(tag == null) {
			return "[empty report tag]";
		}
		else if(tag == ReportTag.FIELD) {
			String variables_h = "";
			for(int i=0;i<variables.size();i++) {
				Variable v = variables.get(i);
				variables_h = variables_h+v;
				if(i < variables.size()-1) {
					variables_h = variables_h+", ";
				}
			}
			
			String modifiers_h = "";
			for(String m:modifiers) {
				modifiers_h = modifiers_h + m+" ";
			};
			
			return modifiers_h + variables_h;
		}
		else if(tag == ReportTag.METHOD) {
			String modifiers_h = "";
			for(String m:modifiers) {
				modifiers_h = modifiers_h + m+" ";
			};
			String mparameters_h = "";
			for(int i=0;i<parameters.size();i++) {
				MParameter p = parameters.get(i);
				mparameters_h = mparameters_h+p;
				if(i < parameters.size()-1) {
					mparameters_h = mparameters_h+", ";
				}
			}
			String s = methodName+"("+mparameters_h+")|";
			if(modifiers_h !=null && modifiers_h.length()>0) {
				s =s +modifiers_h+" ";
			}
			s =s +returnType;
			return s;

		}
		else {
			return "[invalid report tag]";
		}
	}
	
}

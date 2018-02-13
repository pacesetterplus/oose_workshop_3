package fmcr.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import fmcr.main.Client;

/**
 * A class that is used for the tabular display of the outcome of class analysis.
 *
 * @author  Inah Omoronyia
 * @version 1.0
 * @see     fmcr.display.RepositoryAnalysisView
 * @see     fmcr.display.FieldAnalysisView
 * @see     fmcr.display.MethodAnalysisView
 * @see	    fmcr.factory.CodeAnalysisFactory
 */
public class ClassAnalysisView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public JTable table;
	public ClassReportModel model;
	
	/**
     * Panel that displays the results of class analysis
     * 
     * @see fmcr.MethodAnalysisView#MethodAnalysisView()
     * @see fmcr.FieldAnalysisView#FieldAnalysisView()
     * @see fmcr.RepositoryAnalysisView#RepositoryAnalysisView()
     */
	public ClassAnalysisView() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0,0));
				
		model = new ClassReportModel();						
		model.setRowCount(0);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -8305142193885321738L;
			@Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table,value, isSelected, hasFocus, row, column);
					c.setBackground(row%2==0 ? Color.LIGHT_GRAY : new Color(226,225,213));                        
	            return c;
	        };
	    });
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
		
		table.setFont(new Font("Verdana", Font.PLAIN, 10));
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 10));
		
        String headers[] = {"Class","", "", "","CodeQty", "LOC" };

        Border headerBorder = UIManager.getBorder("TableHeader.cellBorder");

        JLabel docBugLabel = new JLabel(headers[1], Client.docBugIcon, JLabel.CENTER);
        docBugLabel.setBorder(headerBorder);
        JLabel fBugLabel = new JLabel(headers[2], Client.fBugIcon, JLabel.CENTER);
        fBugLabel.setBorder(headerBorder);
        JLabel mBugLabel = new JLabel(headers[3], Client.mBugIcon, JLabel.CENTER);
        mBugLabel.setBorder(headerBorder);

        TableCellRenderer renderer = new JComponentTableCellRenderer();

        TableColumnModel columnModel = table.getColumnModel();

        TableColumn column0 = columnModel.getColumn(0);
        column0.setMaxWidth(900);

        TableColumn column1 = columnModel.getColumn(1);
        column1.setHeaderRenderer(renderer);
        column1.setHeaderValue(docBugLabel);
        column1.setMaxWidth(70);
        
        TableColumn column2 = columnModel.getColumn(2);
        column2.setHeaderRenderer(renderer);
        column2.setHeaderValue(fBugLabel);
        column2.setMaxWidth(80);

        TableColumn column3 = columnModel.getColumn(3);
        column3.setHeaderRenderer(renderer);
        column3.setHeaderValue(mBugLabel);
        column3.setMaxWidth(70);
        
        TableColumn column4 = columnModel.getColumn(4);
        column4.setMaxWidth(70);

        TableColumn column5 = columnModel.getColumn(5);
        column5.setMaxWidth(70);
	      
		JPanel panelaw = new JPanel(new BorderLayout(0,0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.getViewport().add(table);
		panelaw.add(scrollPane_1, BorderLayout.CENTER);
		add(panelaw, BorderLayout.CENTER);
		
	}
	
	class JComponentTableCellRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			return (JComponent) value;
		}
	}
		
	 public class ClassReportModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

			public ClassReportModel() {
		      super(new Object[]{"Class",Client.fBugIcon, Client.mBugIcon,Client.docBugIcon,"CodeQty", "LOC"}, 0);
		    }
					
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
			
		    @SuppressWarnings("rawtypes")
			@Override
		    public Class<?> getColumnClass(int columnIndex) {
		      Class clazz = String.class;
		      switch (columnIndex) {
		      	case 0:
			      clazz = String.class;
			      break;
		      	case 1:
			      clazz = Double.class;
			      break;
			    case 2:
		          clazz = Double.class;
		          break;		        
		        case 3:
				      clazz = Double.class;
				      break;		
		        case 4:
				      clazz = Double.class;
				      break;	
		        case 5:
				      clazz = Integer.class;
				      break;	
		      }
		      return clazz;
		    }

	 }
}

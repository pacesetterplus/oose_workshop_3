package fmcr.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * A class that is used for the tabular display of the outcome of field analysis.
 *
 * @author  Inah Omoronyia
 * @version 1.0
 * @see     fmcr.display.RepositoryAnalysisView
 * @see     fmcr.display.MethodAnalysisView
 * @see     fmcr.display.ClassAnalysisView
 * @see		fmcr.factory.CodeAnalysisFactory
 */
public class FieldAnalysisView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public JTable table;
	public FieldReportModel model;
	
	/**
     * Panel that displays the results of fields analysis
     * 
     * @see fmcr.MethodAnalysisView#MethodAnalysisView()
     * @see fmcr.ClassAnalysisView#ClassAnalysisView()
     * @see fmcr.RepositoryAnalysisView#RepositoryAnalysisView()
     */
	public FieldAnalysisView() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0,0));
				
		model = new FieldReportModel();						
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
		table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		
		table.setFont(new Font("Verdana", Font.PLAIN, 10));
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 10));
		
		TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0)
                column.setMaxWidth(20);
            if (i == 1)
                column.setMaxWidth(20);
            if (i == 2) 
                column.setMaxWidth(1110);
            if (i == 3)
                column.setMaxWidth(30);
        }
				
		JPanel panelaw = new JPanel(new BorderLayout(0,0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.getViewport().add(table);
		panelaw.add(scrollPane_1, BorderLayout.CENTER);
		add(panelaw, BorderLayout.CENTER);
		
	}
		
	 public class FieldReportModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

			public FieldReportModel() {
		      super(new Object[]{" "," ","Variables", "Line"}, 0);
		    }
						
		    @SuppressWarnings("rawtypes")
			@Override
		    public Class<?> getColumnClass(int columnIndex) {
		      Class clazz = String.class;
		      switch (columnIndex) {
		      	case 0:
			      clazz = ImageIcon.class;
			      break;
			    case 1:
		          clazz = ImageIcon.class;
		          break;
		        case 2:
		          clazz = String.class;
		          break;
		        case 3:
				      clazz = Integer.class;
				      break;		        
		      }
		      return clazz;
		    }

	 }
}

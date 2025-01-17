package bsu.rfe.java.group8.yanuchok.var8A;

import java.awt.Color; 
import java.awt.Component; 
import java.awt.FlowLayout; 
import java.text.DecimalFormat; 
import java.text.DecimalFormatSymbols; 
import java.text.NumberFormat; 
import javax.swing.JLabel; 
import javax.swing.JPanel; 
import javax.swing.JTable; 
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRender implements TableCellRenderer {
	
	private JPanel panel = new JPanel();
	private JLabel label = new JLabel();
	
	private String needle = null;
	
	private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
	
	public GornerTableCellRender() {
		formatter.setMaximumFractionDigits(5);
		formatter.setGroupingUsed(false);
		
		DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
		dottedDouble.setDecimalSeparator('.');
		
		formatter.setDecimalFormatSymbols(dottedDouble);
		
		panel.add(label);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		String formattedDouble = null;
		    formattedDouble= formatter.format(value);
			label.setText(formattedDouble);
		if (col == 0 && needle != null && needle.equals(formattedDouble)) {
			panel.setBackground(Color.RED);
		} else {
			panel.setBackground(Color.WHITE);
		}
		
		return panel;
	}
	
	public void setNeedle(String needle) {
		this.needle = needle;
	}
}


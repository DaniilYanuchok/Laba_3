package bsu.rfe.java.group8.yanuchok.var8A;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat; 
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
	
	private Double from;
	private Double to;
	private Double step;
	private Double[] coefficients;
	
	public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
		this.from = from;
		this.to = to;
		this.step = step;
		this.coefficients = coefficients;
	}
	
	public Double getFrom() {
		return from;
	}
	
	public Double getTo() {
		return to; 
	}
	
	public Double getStep() {
		return step;
	}
	
	public int getColumnCount() {
		return 3;
	}
	
	public int getRowCount() {
		return (int) (Math.ceil((to-from)/step)) +1;
	}
	
	public Object getValueAt(int row, int col) {
		
		double x = from + step*row;
		if (col == 0) {
			return x;
		} 
		if (col == 1 ) {
			Double result = coefficients[0];
			for (int i = 1; i < coefficients.length; i++)
				result = result * x + coefficients[i];
			
			return result;
		}
		else {
			Double result = coefficients[0];
			for (int i = 1; i < coefficients.length; i++)
				result = result * x + coefficients[i];
			
			DecimalFormat newformatter = (DecimalFormat) NumberFormat.getInstance();
			newformatter.setMaximumFractionDigits(5);
			newformatter.setGroupingUsed(false);
			DecimalFormatSymbols newdottedDouble = newformatter.getDecimalFormatSymbols();
			newdottedDouble.setDecimalSeparator('.');
			newformatter.setDecimalFormatSymbols(newdottedDouble);
			String StrResult = newformatter.format(result);
			String[] SplitStrResult = StrResult.split("[.]");
			
			if (SplitStrResult.length == 1) {
				return false;
			}
			else {
				int IntResult = Integer.parseInt(SplitStrResult[1]);
				
				return (IntResult % 2 == 0) ? false : true;
			} 
		}
	}
	
	public String getColumnName(int col) {
		switch(col) {
		case 0:
			return "Значение Х";
		case 1:
			return "Значение многочлена";
		default:
			return "Дробная часть нечетная";
		}
	}
	
	public Class<?> getColumnClass(int col) {
			if (col == 1 || col == 0)
				return Double.class;
			else
				return Boolean.class;
	}
}


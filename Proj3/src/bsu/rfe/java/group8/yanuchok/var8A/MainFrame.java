package bsu.rfe.java.group8.yanuchok.var8A;

import java.awt.BorderLayout; 
import java.awt.Color; 
import java.awt.Dimension; 
import java.awt.Toolkit; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.io.DataOutputStream; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileOutputStream; 
import java.io.PrintStream; 
import javax.swing.AbstractAction; 
import javax.swing.Action; 
import javax.swing.BorderFactory; 
import javax.swing.Box; 
import javax.swing.JButton; 
import javax.swing.JFileChooser; 
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JMenu; 
import javax.swing.JMenuBar; 
import javax.swing.JMenuItem; 
import javax.swing.JOptionPane; 
import javax.swing.JPanel; 
import javax.swing.JScrollPane; 
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	
	private Double[] coefficients;
	
	private JFileChooser fileChooser = null;
	
	private JMenuItem saveToTextMenuItem;
	private JMenuItem saveToGraphicsMenuItem;
	private JMenuItem searchValueMenuItem;
	private JMenuItem aboutProgramMenuItem;
	
	private JTextField textFieldFrom; 
	private JTextField textFieldTo; 
	private JTextField textFieldStep;
	
	private Box hBoxResult;
	
	private GornerTableCellRender renderer = new GornerTableCellRender();
	
	private GornerTableModel data;
	
	public MainFrame(Double[] coefficients) {
		super("Табулирование многочлена на отрезке по схеме Горнер");
		this.coefficients = coefficients;
		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("Файл");
		menuBar.add(fileMenu);
		JMenu tableMenu = new JMenu("Таблица");
		menuBar.add(tableMenu);
		JMenu caseMenu = new JMenu("Справка");
		menuBar.add(caseMenu);
		
		Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
			public void actionPerformed (ActionEvent event) {
				if (fileChooser == null) {
					fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
				}
				
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
					saveToTextFile(fileChooser.getSelectedFile());
			}
		};
		
		saveToTextMenuItem = fileMenu.add(saveToTextAction);
		saveToTextMenuItem.setEnabled(false);
		
		Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") { 
				   public void actionPerformed(ActionEvent event) { 
				    if (fileChooser==null) { 
				     fileChooser = new JFileChooser(); 
				     fileChooser.setCurrentDirectory(new File(".")); 
				    } 
				    
				    if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION); 
    
				     saveToGraphicsFile(fileChooser.getSelectedFile()); 
				   } 
				  };
				  
		saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
		saveToGraphicsMenuItem.setEnabled(false);
		
		Action searchValueAction = new AbstractAction("Найти значение X") {
			public void actionPerformed (ActionEvent event) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
				renderer.setNeedle(value);
				getContentPane().repaint();
			}
		};
		
		searchValueMenuItem = tableMenu.add(searchValueAction);
		searchValueMenuItem.setEnabled(false);
		
		Action aboutProgramAction = new AbstractAction("О программе") {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(MainFrame.this, "Author: Yanuchok Daniil 8 group");
			}
		};
		
		aboutProgramMenuItem = caseMenu.add(aboutProgramAction);
		aboutProgramMenuItem.setEnabled(true);
		
		JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
		textFieldFrom = new JTextField("0.0", 10);  
		textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());   
		JLabel labelForTo = new JLabel("до:");   
		textFieldTo = new JTextField("1.0", 10);   
		textFieldTo.setMaximumSize(textFieldTo.getPreferredSize()); 
		JLabel labelForStep = new JLabel("с шагом:");  
		textFieldStep = new JTextField("0.1", 10);    
		textFieldStep.setMaximumSize(textFieldStep.getPreferredSize()); 
		
		Box hboxRange = Box.createHorizontalBox();
		hboxRange.setBorder(BorderFactory.createBevelBorder(1));
		hboxRange.add(Box.createHorizontalGlue()); 
		hboxRange.add(labelForFrom);
		hboxRange.add(Box.createHorizontalStrut(10));
		hboxRange.add(textFieldFrom);  
		hboxRange.add(Box.createHorizontalStrut(20));
		hboxRange.add(labelForTo);  
		hboxRange.add(Box.createHorizontalStrut(10));   
		hboxRange.add(textFieldTo);  
		hboxRange.add(Box.createHorizontalStrut(20));   
		hboxRange.add(labelForStep);  
		hboxRange.add(Box.createHorizontalStrut(10));   
		hboxRange.add(textFieldStep);
		hboxRange.add(Box.createHorizontalGlue());
        hboxRange.setPreferredSize(new Dimension(new Double(hboxRange.getMaximumSize().getWidth()).intValue(), new Double(hboxRange.getMinimumSize().getHeight()).intValue()*2));
		getContentPane().add(hboxRange, BorderLayout.NORTH);
		
		JButton buttonCalc = new JButton("Вычислить");
		  buttonCalc.addActionListener(new ActionListener() {
			  public void  actionPerformed(ActionEvent ev) {
				  try {
					  Double from = Double.parseDouble(textFieldFrom.getText());
					  Double to = Double.parseDouble(textFieldTo.getText());
					  Double step = Double.parseDouble(textFieldStep.getText());
					  data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
					  JTable table = new JTable(data);
					  table.setDefaultRenderer(Double.class, renderer);
					  table.setRowHeight(30);
					  hBoxResult.removeAll();
					  hBoxResult.add(new JScrollPane (table));
					  getContentPane().validate();
					  saveToTextMenuItem.setEnabled(true);
					  saveToGraphicsMenuItem.setEnabled(true);
					  searchValueMenuItem.setEnabled(true);
				  } catch (NumberFormatException ex) {
					  JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
				  }
			  }
		  });
		  
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
        		textFieldFrom.setText("0.0");
        		textFieldTo.setText("1.0");
        		textFieldStep.setText("0.1");
        		hBoxResult.removeAll();
        		hBoxResult.add(new JPanel());
        		saveToTextMenuItem.setEnabled(false);
        		saveToGraphicsMenuItem.setEnabled(false);
        		searchValueMenuItem.setEnabled(false);
        		getContentPane().validate();
        	}
        });
        
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension(new Double(hboxButtons.getMaximumSize().getWidth()).intValue(), new Double(hboxButtons.getMinimumSize().getHeight()).intValue()*2));
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER); 
	}
	
	protected void saveToGraphicsFile(File selectedFile) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
			for (int i = 0; i<data.getRowCount(); i++) {
				out.writeDouble((Double)data.getValueAt(i,0));
				out.writeDouble((Double)data.getValueAt(i,1));
			}
			out.close();
		} catch (Exception e) {
			
		}
	}
	
	protected void saveToTextFile(File selectedFile) {
		try {
			PrintStream out = new PrintStream(selectedFile);
			out.println("Результаты табулирования многочлена по схеме Горнера");
			out.print("Многочлен: ");
			for (int i=0; i<coefficients.length; i++) {
				out.print(coefficients[i] + "*X^" + (coefficients.length-i-1));
				if (i!=coefficients.length-1)
					out.print(" + ");
			}
			out.println("");
			out.println("Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " + data.getStep());
			out.println("====================================================");
			for (int i = 0; i<data.getRowCount(); i++) {
				out.println("Значение в точке " + data.getValueAt(i,0) + " равно " + data.getValueAt(i,1));
			}
			out.close();
		} catch (FileNotFoundException e) {
			
		}
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
			System.exit(-1);
		}
		Double[] coefficients = new Double[args.length];
		int i = 0;
		try {
			for (String arg: args) {
				coefficients[i++] = Double.parseDouble(arg);
			}
		} catch (NumberFormatException ex) {
			System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
			System.exit(-2);
		}
		MainFrame frame = new MainFrame(coefficients);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

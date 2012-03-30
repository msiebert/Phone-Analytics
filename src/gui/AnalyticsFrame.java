package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import analytics.PhoneAnalytics;

public class AnalyticsFrame extends JFrame implements ActionListener {
	
	//this will let the GUI send messages back to the controller
	private PhoneAnalytics analytics;
	
	private static final long serialVersionUID = 1L;

	private final int HEIGHT = 400;
	private final int WIDTH = 600;
	private final int BORDER_HEIGHT = 30;
	private final int BORDER_WIDTH = 30;
	
	private final int TITLE_FONT_SIZE = 24;
	private final int HEADER_FONT_SIZE = 18;
	private static final int LINK_FONT_SIZE = 14;
	private final int SMALL_HEADER_FONT_SIZE = 10;
	
	private Color textColor = new Color(4, 2, 78);
	
	//button declarations
	private JButton initOrg;
	private JButton initCalls;
	private JButton runAnalysis;
	
	//text field declarations
	private JTextField year;
	private JTextField month;
	private JTextField transferDate;
	
	//the error label
	private JLabel error;
	
	//loading label
	private JLabel loading;
	
	//file chooser
	JFileChooser fileChooser;
	
	public AnalyticsFrame(PhoneAnalytics analytics) {
		super("Phone Analytics");
		
		this.analytics = analytics;
		
		setSize(WIDTH, HEIGHT);
		
		//set the background color of the GUI
		Container container = getContentPane();
		container.setBackground(new Color(90, 105, 156));
		
		//make the side panels for a border
		JPanel north = new JPanel();
		north.setBackground(new Color(90, 105, 156));
		north.setSize(WIDTH, BORDER_HEIGHT);
		add(north, BorderLayout.NORTH);
		JPanel south = new JPanel();
		south.setBackground(new Color(90, 105, 156));
		south.setSize(WIDTH, BORDER_HEIGHT);
		add(south, BorderLayout.SOUTH);
		JPanel west = new JPanel();
		west.setBackground(new Color(90, 105, 156));
		west.setSize(BORDER_WIDTH, HEIGHT);
		add(west, BorderLayout.WEST);
		JPanel east = new JPanel();
		east.setBackground(new Color(90, 105, 156));
		east.setSize(BORDER_WIDTH, HEIGHT);
		add(east, BorderLayout.EAST);
		
		//add the middle panel to the GUI
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		content.setBackground(Color.white);
		content.setSize(WIDTH - (BORDER_WIDTH * 2), HEIGHT - (BORDER_HEIGHT *2));
		content.setBorder(BorderFactory.createLineBorder (Color.gray, 4));
		add(content, BorderLayout.CENTER);
		
		//add the title to the panel
		JLabel title = new JLabel("Phone Analytics", JLabel.CENTER);
		Font titleFont = new Font(title.getFont().getName(), Font.BOLD, TITLE_FONT_SIZE);
		title.setFont(titleFont);
		title.setForeground(textColor);
		content.add(title, BorderLayout.NORTH);
		
		//create a panel to hold the menu
		JPanel menu = new JPanel();
		menu.setPreferredSize(new Dimension((WIDTH / 2) - BORDER_WIDTH, HEIGHT - BORDER_HEIGHT));
		menu.setMinimumSize(menu.getPreferredSize());
		menu.setMaximumSize(menu.getPreferredSize());
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.setBackground(content.getBackground());
		menu.setBorder(new EmptyBorder(0, 20, 0, 0));
		content.add(menu, BorderLayout.WEST);
		
		//display the initialization menu
		menu.add(makeHeader("Initialize System"));
		JPanel initMenu = new JPanel();
		initMenu.setBorder(new EmptyBorder(0, 20, 0, 0));
		initMenu.setBackground(content.getBackground());
		initMenu.setLayout(new BoxLayout(initMenu, BoxLayout.Y_AXIS));
		initOrg = makeLink("Load Mission Organization");
		initMenu.add(initOrg);
		initMenu.add(makeSmallHeader("Enter Year of Calls (yyyy format):"));
		year = makeTextField(4);
		initMenu.add(year);
		initMenu.add(makeSmallHeader("Enter Month of Calls (mm format):"));
		month = makeTextField(2);
		initMenu.add(month);
		initMenu.add(makeSmallHeader("Enter Date of Last Move Day (yyyy/mm/dd format):"));
		transferDate = makeTextField(10);
		initMenu.add(transferDate);
		initCalls = makeLink("Load Call List");
		initMenu.add(initCalls);
		menu.add(initMenu);
		
		
		//display the 'run analytics' menu
		menu.add(makeHeader("Analytics"));
		runAnalysis = makeLink("Run Phone Analytics");
		menu.add(runAnalysis);
		
		//create a FileChooser to be used to choose files for mission organization and calls
		fileChooser = new JFileChooser();
		
		//create a cell phone image to be displayed on the right side of the screen
		ImageIcon phoneIcon = new ImageIcon("phone.gif");
		JLabel phonePicture = new JLabel("", phoneIcon, JLabel.CENTER);
		phonePicture.setPreferredSize(new Dimension((WIDTH / 2) - BORDER_WIDTH, HEIGHT - BORDER_HEIGHT));
		content.add(phonePicture, BorderLayout.EAST);
		
		//create a JLabel that can contain error messages
		error = new JLabel("", JLabel.CENTER);
		error.setForeground(Color.red);
		content.add(error, BorderLayout.SOUTH);
		
		//create a cell phone image to be displayed on the right side of the screen
		ImageIcon loadingIcon = new ImageIcon("loading.gif");
		loading = new JLabel("", loadingIcon, JLabel.CENTER);
		loading.setVisible(false);
		content.add(loading, BorderLayout.CENTER); 

		//display the page
		setVisible(true);
	}
	
	/*
	 * Sets the text of the error message
	 * PARAMETER: String text - the text to put into the error message
	 * */
	public void setError(String text) {
		error.setText("<html><p>" + text + "</p></html>");
	}
	
	/*
	 * Formats the text passed in to be a header
	 * PARAMETER: String text - the text to formatted into a header
	 * RETURN VALUE: JLabel - a formatted JLabel
	 * */
	private JLabel makeHeader(String text) {
		JLabel header = new JLabel(text);
		Font headerFont = new Font(header.getFont().getName(), Font.BOLD, HEADER_FONT_SIZE);
		header.setFont(headerFont);
		header.setForeground(textColor);
		return header;
	}
	
	/*
	 * Formats the text passed in to be a smaller header
	 * PARAMETER: String text - the text to be formatted into a small header
	 * RETURN VALUE: JLabel - the formatted JLabel
	 * */
	private JLabel makeSmallHeader(String text) {
		JLabel header = new JLabel("<html><p>" + text + "</p></html>");
		Font headerFont = new Font(header.getFont().getName(), Font.ITALIC, SMALL_HEADER_FONT_SIZE);
		header.setFont(headerFont);
		header.setForeground(textColor);
		//header.setBorder(new EmptyBorder(10, -5, 10, 0));
		return header;
	}
	
	/*
	 * Formats a textfield
	 * PARAMETER: int columnCount - the number of columns there should be
	 * RETURN VALUE: JTextField - the formatted JTextField
	 * */
	private JTextField makeTextField(int columnCount) {
		JTextField field = new JTextField(columnCount);
		return field;
	}
	
	/*
	 * Formats the text passed in to be a link
	 * PARAMETER: String text - the text to be formatted as a link
	 * RETURN VALUE: JButton - a formatted JButton
	 * */
	private JButton makeLink(String text) {
		JButton link = new JButton(text);
		
		//do some font formatting
		Font linkFont = new Font(link.getFont().getName(), Font.PLAIN, LINK_FONT_SIZE);
		link.setFont(linkFont);
		link.setForeground(textColor);
		
		//remove the button look
		link.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		//make the link look clickable when the cursor goes over it
		link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		//let this frame's Action Listener listen to the link
		link.addActionListener(this);
				
		return link;
	}

	public void actionPerformed(final ActionEvent e) {
		
		loading.setVisible(true);
		setError("");
		
		//create a new thread to handle the action so that we can still show the loading image
		new Thread() {  
	        public void run() { 
	        	Object source = e.getSource();

	        	//take different actions based on which button was the one that got clicked
	        	if (source.equals(initOrg)) {
	        		int returnVal = fileChooser.showOpenDialog(AnalyticsFrame.this);

	                if (returnVal == JFileChooser.APPROVE_OPTION) {
	                	File file = fileChooser.getSelectedFile();
	                	if (analytics.initOrganization(file.getAbsolutePath()))
	                		((JButton) source).setText("Mission Organization...Loaded");
	                	else
	                		((JButton) source).setText("Mission Organization...Failed");
	                }
	        	}
	        	else if (source.equals(initCalls)) {
	        		String yearText = year.getText();
	        		String monthText = month.getText();
	        		String transferText = transferDate.getText();
	        		
	        		//do simple tests for length of the strings
	        		if (yearText == null || yearText.length() != 4) {
	        			setError("Please enter the year in 'yyyy' format.");
	        			loading.setVisible(false);
	        			return;
	        		}
	        		if (monthText == null || monthText.length() != 2) {
	        			setError("Please enter the month in 'mm' format.");
	        			loading.setVisible(false);
	        			return;
	        		}
	        		if (transferText == null || transferText.length() != 10) {
	        			setError("Please enter the transfer date in 'yyyy/mm/dd' format.");
	        			loading.setVisible(false);
	        			return;
	        		}
	        		
	        		int returnVal = fileChooser.showOpenDialog(AnalyticsFrame.this);

	               if (returnVal == JFileChooser.APPROVE_OPTION) {
	                	File file = fileChooser.getSelectedFile();
	                	
	                	if (analytics.initCallList(file.getAbsolutePath(), yearText, monthText, transferText))
	                		((JButton) source).setText("Call List...Loaded");
	                	else
	                		((JButton) source).setText("Call List...Failed");
	                }
	        	}
	        	else if (source.equals(runAnalysis)) {
	        		if (analytics.runAnalysis())
						((JButton) source).setText("Run Analysis...Complete");
					else 
						((JButton) source).setText("Run Analysis...Failed");
	        	}

	        	loading.setVisible(false); 
	        }  
	    }.start();  
		
	}

}

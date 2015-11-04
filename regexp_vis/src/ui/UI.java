package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @author pg272
 */
public class UI extends JFrame {

	/**
	 * Constructor for objects of class UI
	 */
	public UI() {
		super("Regular Language Visualiser");
		setMinimumSize(new Dimension(1200, 700));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel panelParent = new JPanel();
		JPanel panelEast = new JPanel();
		JPanel panelSouth = new JPanel();
		panelEast.setPreferredSize(new Dimension(200, 200));
		panelSouth.setPreferredSize(new Dimension(800, 100));
		panelParent.setBackground(new Color(1, 221, 25));
		panelEast.setBackground(new Color(255, 0, 255));
		panelSouth.setBackground(new Color(220, 20, 60));
		panelParent.setLayout(new BorderLayout());
		panelParent.add(panelEast, BorderLayout.EAST);
		panelParent.add(panelSouth, BorderLayout.SOUTH);

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);

		add(menuBar, BorderLayout.NORTH);
		add(panelParent);
		pack();
	}

	public static void main(String args[]) {
		JFrame frame = new UI();
		frame.setVisible(true);
	}
	
}

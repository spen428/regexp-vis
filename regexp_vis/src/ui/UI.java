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

	private static final int JFRAME_WIDTH_MIN_PX = 1200;
	private static final int JFRAME_HEIGHT_MIN_PX = 700;
	private static final String DEFAULT_JFRAME_TITLE = "Regular Language Visualiser";

	public UI() {
		this(DEFAULT_JFRAME_TITLE);
	}

	/**
	 * Constructor for objects of class UI
	 * 
	 * @param title
	 *            The title of the window.
	 */
	public UI(String title) {
		super(title);

		setMinimumSize(new Dimension(JFRAME_WIDTH_MIN_PX, JFRAME_HEIGHT_MIN_PX));
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

}

package test.ui;

import javax.swing.JFrame;

/**
 * 
 * @author sp611
 *
 */
public class UITest extends JFrame {

	private static final int JFRAME_WIDTH_PX = 450;
	private static final int JFRAME_HEIGHT_PX = 450;

	public UITest() {
		super("Regular Language Visualiser - User Interface Demo");
	}

	public static void main(String[] args) {
		UITest frame = new UITest();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(JFRAME_WIDTH_PX, JFRAME_HEIGHT_PX);
		frame.setVisible(true);
	}

}

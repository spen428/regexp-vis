package test.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import ui.GraphPanel;

/**
 * Demonstrates the capabilities of JGraphX.
 * 
 * @author sp611
 * 
 */
public class GraphicsTest extends KeyAdapter implements KeyListener {

	private static final int JFRAME_WIDTH_PX = 450;
	private static final int JFRAME_HEIGHT_PX = 450;
	private final JFrame frame;
	private final GraphPanel graphPanel;

	public GraphicsTest() {
		frame = new JFrame("Regular Language Visualiser - "
				+ "Graph Layout Demo");
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(JFRAME_WIDTH_PX, JFRAME_HEIGHT_PX);
		graphPanel = new GraphPanel();
		graphPanel.addKeyListener(this);
		frame.getContentPane().add(graphPanel);
		frame.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char keyChar = e.getKeyChar();
		switch (keyChar) {
		default:
		case 'a':
			graphPanel.setVertexLayout(0);
			break;
		case 's':
			graphPanel.setVertexLayout(1);
			break;
		case 'd':
			graphPanel.setVertexLayout(2);
			break;
		}
	}

	public static void main(String[] args) {
		new GraphicsTest();
	}

}

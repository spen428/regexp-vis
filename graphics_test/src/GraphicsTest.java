/**
 * Getting to grips with the SWT framework.
 * 
 * @author sp611
 *
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GraphicsTest {

	public static final int CANVAS_WIDTH_PX = 800;
	public static final int CANVAS_HEIGHT_PX = 600;
	public static final String VERSION = "0.01a";

	private final Shell shell;
	private final Canvas canvas;

	public GraphicsTest(Display display) {
		this.shell = new Shell(display);
		this.canvas = new Canvas(shell, SWT.NONE);

		canvas.setSize(CANVAS_WIDTH_PX, CANVAS_HEIGHT_PX);
		canvas.setBackground(new Color(display, new RGB(0, 200, 200)));

		shell.setText("Regular Language Visualiser v" + VERSION);
		shell.pack();
		centerShell();
		shell.open();
	}

	private void centerShell() {
		Rectangle b1 = shell.getDisplay().getBounds();
		Rectangle b2 = shell.getBounds();
		shell.setLocation((b1.width - b2.width) / 2,
				(b1.height - b2.height) / 2);
	}

	public Shell getShell() {
		return shell;
	}

}

import org.eclipse.swt.widgets.Display;

/**
 * @author sp611
 */
public class Main {

	public static void main(String args[]) {
		Display display = new Display();
		GraphicsTest ui = new GraphicsTest(display);

		while (!ui.getShell().isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}

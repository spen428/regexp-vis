package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import model.BasicRegexp;
import model.InvalidRegexpException;

/**
 * @author pg272, sp611
 */
public class UI extends JFrame implements KeyListener {

    private static final int JFRAME_WIDTH_MIN_PX = 1200;
    private static final int JFRAME_HEIGHT_MIN_PX = 700;
    private static final String DEFAULT_JFRAME_TITLE = "Regular Language "
            + "Visualiser";
    private static final String LONG_REGEX = "(a|b)*cd+(((a*)*bcf)djka)+";

    private final GraphPanel graphPanel;
    private final JTextField regexpInput;

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
        this.graphPanel = new GraphPanel();

        setMinimumSize(new Dimension(JFRAME_WIDTH_MIN_PX, JFRAME_HEIGHT_MIN_PX));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /* Components */
        this.regexpInput = new JTextField();
        this.regexpInput.setText(LONG_REGEX);
        this.regexpInput.addKeyListener(this);

        /* Panels */
        JPanel panelParent = new JPanel();
        JPanel panelEast = new JPanel();
        JPanel panelSouth = new JPanel();

        panelEast.setPreferredSize(new Dimension(200, 200));
        panelEast.setBackground(new Color(255, 0, 255));

        panelSouth.setPreferredSize(new Dimension(800, 100));
        panelSouth.setBackground(new Color(220, 20, 60));
        panelSouth.add(this.regexpInput);

        panelParent.setBackground(new Color(1, 221, 25));
        panelParent.setLayout(new BorderLayout());
        panelParent.add(this.graphPanel, BorderLayout.CENTER);
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

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == this.regexpInput) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                String regexp = this.regexpInput.getText();
                System.out.printf("Entered: %s%n", regexp);
                try {
                    BasicRegexp re = BasicRegexp.parseRegexp(regexp);
                    BasicRegexp.debugPrintBasicRegexp(0, re);
                    this.graphPanel.resetGraph(re);
                } catch (InvalidRegexpException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}

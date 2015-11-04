package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
/**
 * Write a description of class UI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class UI
{

    /**
     * Constructor for objects of class UI
     */
    public UI(){
       
    }
    
    public static void main(String args[]){
    final JFrame frame = new JFrame("Regular language vis");
        frame.setMinimumSize(new Dimension(1200, 700));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JPanel panelN = new JPanel();
        JPanel panelE = new JPanel();
        JPanel panelS = new JPanel();
        panelE.setPreferredSize(new Dimension(200,200));
        panelS.setPreferredSize(new Dimension(800, 100));
        panel.setBackground(new Color(1,221,25));
        panelE.setBackground(new Color(255,0,255));
        panelS.setBackground(new Color(220,20,60));
        panel.setLayout( new BorderLayout());
        panel.add(panelN, BorderLayout.NORTH);
        panel.add(panelE, BorderLayout.EAST);
        panel.add(panelS, BorderLayout.SOUTH);
        frame.add(panel);
        
        JMenuBar menuBar = new JMenuBar();
    panelN.add(menuBar);
   
    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);
    JMenu editMenu = new JMenu("Edit");
    menuBar.add(editMenu);
        
        frame.pack();
        frame.setVisible(true);
   
    }
}


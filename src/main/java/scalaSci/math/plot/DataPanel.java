package scalaSci.math.plot;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import scalaSci.math.io.*;
import scalaSci.math.plot.components.*;

public abstract class DataPanel extends JPanel implements ComponentListener, FilePrintable, ClipBoardPrintable, StringPrintable {
    protected DataToolBar toolBar;
    protected JScrollPane scrollPane;
    public static int[] dimension = new int[] { 300, 300 };

    public DataPanel() {
        setLayout(new BorderLayout());
        initToolBar();
        init();
        }

    protected void initToolBar() {
        toolBar = new DataToolBar(this);
        add(toolBar, BorderLayout.NORTH);
        toolBar.setFloatable(false);
    }

    protected void initSize() {
        scrollPane.setSize(dimension[0], dimension[1]); 
   }

    protected void init() {
	addComponentListener(this);
    }

    public void update() {
	// this.remove(scrollPane);
	toWindow();
	repaint();
    }
    protected abstract void toWindow();

    public abstract void toClipBoard();

    public abstract void toASCIIFile(File file);

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
		/*
		 * dimension = new int[] { (int) (this.getSize().getWidth()), (int)
		 * (this.getSize().getHeight()) };
		 */
        initSize();
    }

    public void componentShown(ComponentEvent e) {
    }

}
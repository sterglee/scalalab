package scalaSci.math.plot.components;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import scalaSci.math.plot.*;
import scalaSci.math.plot.canvas.*;

public class LegendPanel extends JPanel implements ComponentListener {
    PlotCanvas plotCanvas;
    PlotPanel plotPanel;
    LinkedList<Legend> legends;
    public static int INVISIBLE = -1;
    public static int VERTICAL = 0;
    public static int HORIZONTAL = 1;
    int orientation;
    private int maxHeight;
    private int maxWidth;
    JPanel container;
    private int inset = 5;

    public LegendPanel(PlotPanel _plotPanel, int _orientation) {
        plotPanel = _plotPanel;  // the plot panel where the legend belongs
        plotCanvas = plotPanel.plotCanvas;  // the corresponding canvas
        plotCanvas.attachLegend(this);  // attach the legend to the panel
        orientation = _orientation;

        container = new JPanel();  // create a container to place the legend
        container.setBackground(plotCanvas.getBackground());
        container.setLayout(new GridLayout(1, 1, inset, inset));
        container.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1),null));  // a black border line around the legend

        updateLegends();
        setBackground(plotCanvas.getBackground());
        addComponentListener(this);
        setLayout(new GridBagLayout());
        add(container);
    }
// t=inc(0, 0.01, 20); x=sin(0.56*t); figure(); plot(t,x)

    public void updateLegends() {
        if (orientation != INVISIBLE) {
            container.removeAll();
            maxHeight = 1;
            maxWidth = 1;

            legends = new LinkedList<Legend>();
            for (scalaSci.math.plot.plots.Plot plot : plotCanvas.getPlots()) {
                Legend l = new Legend(plot);
                legends.add(l);
                maxWidth = (int) Math.max(maxWidth, l.getPreferredSize().getWidth());
                maxHeight = (int) Math.max(maxHeight, l.getPreferredSize().getHeight());

                container.add(l);
			
            }

        updateSize();
			//repaint();
        }
    }

    private void updateSize() {
        if (orientation == VERTICAL) {
            int nh = 1;
            if (maxHeight < plotCanvas.getHeight())
               nh = plotCanvas.getHeight() / (maxHeight + inset);
            int nw = 1 + legends.size() / nh;

            ((GridLayout) (container.getLayout())).setColumns(nw);
            ((GridLayout) (container.getLayout())).setRows(1 + legends.size() / nw);
            container.setPreferredSize(new Dimension((maxWidth + inset) * nw, (maxHeight + inset) * (1 + legends.size() / nw)));
    } 
        else if (orientation == HORIZONTAL) {
            int nw = 1;
            if (maxWidth < plotCanvas.getWidth())
               nw = plotCanvas.getWidth() / (maxWidth + inset);
            int nh = 1 + legends.size() / nw;

            ((GridLayout) (container.getLayout())).setRows(nh); 
            ((GridLayout) (container.getLayout())).setColumns(1 + legends.size() / nh);
            container.setPreferredSize(new Dimension((maxWidth + inset) * (1 + legends.size() / nh), (maxHeight + inset) * nh));
      }
    container.updateUI();
    }

    public void note(int i) {
        if (orientation != INVISIBLE) {
            legends.get(i).setBackground(PlotCanvas.NOTE_COLOR);
            legends.get(i).name.setForeground(plotPanel.getBackground());
          }
    }

    public void nonote(int i) {
        if (orientation != INVISIBLE) {
            legends.get(i).setBackground(plotPanel.getBackground());
            legends.get(i).name.setForeground(PlotCanvas.NOTE_COLOR);
        }
    }

    public void componentResized(ComponentEvent e) {
        //System.out.println("LegendPanel.componentResized");
        //System.out.println("PlotCanvas : "+plotCanvas.panelSize[0]+" x "+plotCanvas.panelSize[1]);
         if (orientation != INVISIBLE) {
            updateSize();
        }
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
	}

    public class Legend extends JPanel {
        JPanel color;
        JLabel name;
        scalaSci.math.plot.plots.Plot plot;
        public Legend(scalaSci.math.plot.plots.Plot p) {
	plot = p;
	setLayout(new BorderLayout(2, 2));
                  color = new JPanel();
	name = new JLabel();

	setBackground(Color.WHITE);
	update();

	add(color, BorderLayout.WEST);
	add(name, BorderLayout.CENTER);
	
        name.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
	if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
                        if (plotCanvas.allowEdit && e.getClickCount() > 1)
                        	editText();
                        if (plotCanvas.allowNote && e.getClickCount() <= 1)
		note_nonote();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
	});

	color.addMouseListener(new MouseListener() {
		public void mouseClicked(MouseEvent e) {
	if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
                        if (plotCanvas.allowEdit && e.getClickCount() > 1)
                            editColor();
                        if (plotCanvas.allowNote && e.getClickCount() <= 1)
                            note_nonote();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
	});
}

    public void editText() {
	String name1 = JOptionPane.showInputDialog(plotCanvas, "Choose name", plot.getName());
	if (name1 != null) {
                    plot.setName(name1);
	update();
	updateLegends();
	}
    }

    public void editColor() {
	Color c = JColorChooser.showDialog(plotCanvas, "Choose plot color", plot.getColor());
	if (c != null) {
                    plot.setColor(c);
	update();
	plotCanvas.repaint();
	}
    }

    public void update() {
        int size = name.getFont().getSize();
        color.setSize(new Dimension(size, size));
        color.setPreferredSize(new Dimension(size, size));
	// TODO change legend when plot is invisible
	/*if (!plot.visible)
	color.setBackground(Color.LIGHT_GRAY);
			else*/
        color.setBackground(plot.getColor());
			/*if (!plot.visible) {
				name.setFont(name.getFont().deriveFont(Font.ITALIC));
				name.setForeground(Color.LIGHT_GRAY);
			}*/
        name.setText(plot.getName());
        repaint();
    }

    public void note_nonote() {
	plot.noted = !plot.noted;
	plotCanvas.repaint();
        }

    }

}

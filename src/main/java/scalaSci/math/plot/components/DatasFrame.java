package scalaSci.math.plot.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import scalaSci.math.plot.*;
import scalaSci.math.plot.canvas.*;

public class DatasFrame extends JFrame {
    private PlotCanvas plotCanvas;
    private LegendPanel legend;
    public JTabbedPane panels;

    public DatasFrame(PlotCanvas p, LegendPanel l) {
        super("Data");
        plotCanvas = p;
        legend = l;
        JPanel panel = new JPanel();
    
        panels = new JTabbedPane();
    // add a tab for each plot object
        for (scalaSci.math.plot.plots.Plot plot: plotCanvas.getPlots()) 
            panels.add(new DataPanel(plot), plot.getName());   // init the panel with the plot object

        panel.add(panels);
        setContentPane(panel);
        pack();
        setVisible(true);
    }

    public class DataPanel extends JPanel {
        MatrixTablePanel XY;
        JCheckBox visible;
        JButton color;
        JPanel plottoolspanel;

        scalaSci.math.plot.plots.Plot plot;
        DatasFrame dframe;

        public DataPanel(scalaSci.math.plot.plots.Plot _plot) {
            plot = _plot;
            visible = new JCheckBox("visible");
            visible.setSelected(plot.getVisible());
            color = new JButton();
            color.setBackground(plot.getColor());
            XY = new MatrixTablePanel( plotCanvas.reverseMapedData( plot.getData()));

            visible.addChangeListener(new ChangeListener() {
	public void stateChanged(ChangeEvent e) {
                        if (visible.isSelected())
                            plot.setVisible(true);
	else
                            plot.setVisible(false);
            plotCanvas.repaint();
	}
        });
            color.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
                    Color c = JColorChooser.showDialog(plotCanvas, "Choose plot color", plot.getColor());
                    color.setBackground(c);
                    plot.setColor(c);
                    legend.updateLegends();
                    plotCanvas.linkedLegendPanel.repaint();
                    plotCanvas.repaint();
	}
        });

        this.setLayout(new BorderLayout());
        plottoolspanel = new JPanel();
        plottoolspanel.add(visible);
        plottoolspanel.add(color);
        this.add(plottoolspanel, BorderLayout.NORTH);
        this.add(XY, BorderLayout.CENTER);
        }
    }
}

package graph;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

public class ScatterPlotControls extends Panel implements ActionListener {
  protected Button zoomHome, zoomForward, zoomBackward;
  protected ScatterPlot graph;
  protected TextField xStartField, xEndField;
  protected TextField yStartField, yEndField;
  protected TextField xTickField, yTickField;
  protected TextField xToleranceField, yToleranceField;
  protected Button applyButton;
  protected Label graphPointLabel, graphPointXYLabel;

  public ScatterPlotControls(ScatterPlot graph) {
    this.graph = graph;
      
    FramedArea panel1 = new FramedArea();
    panel1.setLayout(new BorderLayout(1000, 0)); 
    Panel panel2 = new Panel();
    panel2.setLayout(new GridLayout(0,4));

    panel2.add(new Label("X Start"));
    xStartField = new TextField();
    xStartField.setForeground(Color.red);
    panel2.add(xStartField);

    panel2.add(new Label("Y Start"));
    yStartField = new TextField();
    yStartField.setForeground(Color.red);
    panel2.add(yStartField);

    panel2.add(new Label("X Axis End"));
    xEndField = new TextField();
    xEndField.setForeground(Color.red);
    panel2.add(xEndField);

    panel2.add(new Label("Y Axis End"));
    yEndField = new TextField();
    yEndField.setForeground(Color.red);
    panel2.add(yEndField);

    panel2.add(new Label("X Tickmarks"));
    xTickField = new TextField();
    xTickField.setForeground(Color.red);
    panel2.add(xTickField);

    panel2.add(new Label("Y Tickmarks"));
    yTickField = new TextField();
    yTickField.setForeground(Color.red);
    panel2.add(yTickField);

    panel2.add(new Label("X Tolerance"));
    xToleranceField = new TextField(graph.formatdouble(graph.getXTolerance()));
    xToleranceField.setForeground(Color.red);
    panel2.add(xToleranceField);

    panel2.add(new Label("Y Tolerance"));
    yToleranceField = new TextField(graph.formatdouble(graph.getYTolerance()));
    yToleranceField.setForeground(Color.red);
    panel2.add(yToleranceField);

    panel1.add("North", panel2);
    Panel panel = new Panel();
    panel.add(applyButton = new Button("Apply"));
    panel1.add("South", panel);
    add("West", panel1);

    FramedArea panel3 = new FramedArea();
    panel3.setLayout(new GridLayout(4,1));
    panel3.add(new Label("Zoom", Label.CENTER));
    zoomHome = new Button("Home");
    zoomHome.addActionListener(this);
    panel3.add(zoomHome);
    zoomForward = new Button("Forward");
    zoomForward.addActionListener(this);
    panel3.add(zoomForward);
    zoomBackward = new Button("Backward");
    zoomBackward.addActionListener(this);
    panel3.add(zoomBackward);

    Panel panel4 = new FramedArea();
    panel4.setLayout(new GridLayout(3, 1));
    panel4.add(new Label("Selected Point:", Label.CENTER));
    graphPointLabel = new Label("No Point Selected", Label.CENTER);
    graphPointLabel.setForeground(Color.red);
    panel4.add(graphPointLabel);
    graphPointXYLabel = new Label();
    graphPointXYLabel.setForeground(Color.red);
    panel4.add(graphPointXYLabel);

    Panel panel5 = new Panel();
    panel5.setLayout(new GridLayout(2, 1));
    panel5.add(panel4);
    panel5.add(panel3);
    
    add("East", panel5);
    //    add(panel4);
    //    add("East", panel3);
    applyButton.addActionListener(this);
  }

  public void addNotify() {
    super.addNotify();
    updateValues();
  }
  
  public void updateValues() {
    xStartField.setText(graph.formatdouble(graph.getXStart()));
    xEndField.setText(graph.formatdouble(graph.getXEnd()));
    yStartField.setText(graph.formatdouble(graph.getYStart()));
    yEndField.setText(graph.formatdouble(graph.getYEnd()));

    xTickField.setText(graph.formatdouble(graph.getXTicks()));
    yTickField.setText(graph.formatdouble(graph.getYTicks()));
  }

  protected void setSelectedPoint(GraphPoint p) {
    graphPointLabel.setText(p.getLabel() + ": ");
    graphPointXYLabel.setText(p.getXPos() + ", " + p.getYPos());
  }


  public void actionPerformed(ActionEvent e) {

    if(e.getSource() == zoomForward) {
      graph.zoomForward();
    } else if (e.getSource() == zoomBackward) {
      graph.zoomBackward();
    } else if (e.getSource() == zoomHome) {
      graph.zoomHome();
    } else if (e.getSource() == applyButton) {
      double xStart, xEnd, yStart, yEnd;

      try {
          xStart = Double.parseDouble(xStartField.getText());
      } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in X Start")).show(); 	
	return;
      }

      try {
 	xEnd = Double.parseDouble(xEndField.getText());
      } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in X End")).show(); 	
	return;
      }

      try {
 	yStart = Double.parseDouble(yStartField.getText());
	 } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in Y Start")).show(); 	
	return;
      }

      try {
 	yEnd = Double.parseDouble(yEndField.getText());
	  } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in Y End")).show(); 	
	return;
      }

      try {
 	double f = Double.parseDouble(xTickField.getText());
	graph.setXTicks(f);
      } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in X TickMark")).show(); 	
	return;
      }

      try {
 	double f = Double.parseDouble(yTickField.getText());
	graph.setYTicks(f);
      } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in Y TickMark")).show(); 	
	return;
      }

      try {
 	double f = Double.parseDouble(xToleranceField.getText());
      } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in X Tolerance")).show(); 	
	return;
      }

      try {
 	double f = Double.parseDouble(yToleranceField.getText());
      } catch (NumberFormatException n) {
	(new PromptDialog((Frame) getParent(), true, "format error in Y Tolerance")).show(); 	
	return;
      }


      double[] coords = new double[4];
      coords[0] = xStart;
      coords[1] = xEnd;
      coords[2] = yStart;
      coords[3] = yEnd;
      graph.zoomForward(coords);
    }
  }
}



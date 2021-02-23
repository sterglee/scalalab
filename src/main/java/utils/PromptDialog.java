package  utils;
import java.awt.*;

import  utils.*;

public class PromptDialog extends Dialog {
  void PromptDialog_WindowClosing(java.awt.event.WindowEvent event) {
    setVisible(false);
    dispose();
  }

  void okButton_Clicked(java.awt.event.ActionEvent event) {
    setVisible(false);
  }

  private void setsizes(){
    lblWidth = metrics.stringWidth(myPrompt + "   ");
    okWidth  = 2 * metrics.stringWidth("OK"); 
    textHeight = metrics.getMaxAscent() + metrics.getLeading() +
      metrics.getMaxDescent();
  }    
  
  public PromptDialog(Frame parent, boolean modal, String prompt) {
    super(parent, modal);
    myFont = new Font("Dialog", Font.BOLD, 14);
    metrics = getFontMetrics(myFont);
    myPrompt = prompt;
    setsizes();
  }

  public void addNotify() {
    super.addNotify();

    setLayout(null);
    setSize(getInsets().left + getInsets().right + lblWidth + 30,
	    getInsets().top + getInsets().bottom + 6 * textHeight);
    lbl = new Label(myPrompt);
    lbl.setBounds(getInsets().left + 10,
		  getInsets().top + textHeight,lblWidth,textHeight);
    okButton = new java.awt.Button("OK");
    okButton.setBounds(getInsets().left + (int)(lblWidth - okWidth)/2 + 10,
		       getInsets().top + 3 * textHeight,
		       okWidth,(int) (1.5 * textHeight));
    add(lbl); add(okButton);
    setResizable(false);
	
    Window lWindow = new Window();
    addWindowListener(lWindow);
    Action lAction = new Action();
    okButton.addActionListener(lAction);
    locateCenter();
  }

  // locate to the center of my parent's dimensions
  //
  protected void locateCenter() {
    Rectangle bounds = getParent().getBounds();
    Rectangle abounds = getBounds();
    
    setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
		bounds.y + (bounds.height - abounds.height)/2);
  }

  public synchronized void setVisible(boolean b) {
    Rectangle bounds = getParent().getBounds();
    Rectangle abounds = getBounds();
    setLocation((bounds.width - abounds.width)/3, (bounds.height - abounds.height)/2);
    super.setVisible(b);
  }
    
  int lblWidth, okWidth, textHeight;
  Font myFont;
  FontMetrics metrics;
  String myPrompt;
	
  java.awt.Button okButton;
  Dialog parent;
  Label lbl;
	
  class Window extends java.awt.event.WindowAdapter {
    public void windowClosing(java.awt.event.WindowEvent event) {
      Object object = event.getSource();
      if (object == PromptDialog.this)
	PromptDialog_WindowClosing(event);
    }
  }

  class Action implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent event) {
      Object object = event.getSource();
      if (object == okButton) okButton_Clicked(event);
    }
  }
}

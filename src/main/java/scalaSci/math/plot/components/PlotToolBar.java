package scalaSci.math.plot.components;

import com.lowagie.text.DocumentException;
import java.awt.event.*;
import java.io.*;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import scalaSci.math.plot.*;
import scalaSci.math.plot.canvas.*;


public class PlotToolBar extends JToolBar {
    protected ButtonGroup buttonGroup;
    protected JToggleButton buttonCenter;
    protected JToggleButton buttonEdit;
    protected JToggleButton buttonZoom;
    protected JToggleButton buttonRotate;
    protected JToggleButton buttonViewCoords;
    protected JButton buttonSetScales;
    protected JButton buttonDatas;
    protected JButton buttonSavePNGFile;
    protected JButton buttonSaveJPGFile;
    protected JButton buttonSaveBMPFile;
    protected JButton buttonSaveEPSFile;
    protected JButton buttonSavePDFFile;
    protected JButton buttonReset;
    protected JButton buttonSetFigProps;
    private boolean denySaveSecurity;
    private JFileChooser pngFileChooser;
    private JFileChooser jpgFileChooser;
    private JFileChooser bmpFileChooser;
    private JFileChooser epsFileChooser;
    private JFileChooser pdfFileChooser;
	/** the currently selected PlotPanel */
    private PlotCanvas plotCanvas;
    private PlotPanel plotPanel;

    public PlotToolBar(PlotPanel pp) {
        plotPanel = pp;
        plotCanvas = pp.plotCanvas;
         try {
            pngFileChooser = new JFileChooser();
            pngFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".png");
	}

                public String getDescription() {
                    return "Portable Network Graphic file";
	}
            });
        
            jpgFileChooser = new JFileChooser();
            jpgFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".jpg");
	}

                public String getDescription() {
                    return "JPEG  Graphic file";
	}
            });
        
         bmpFileChooser = new JFileChooser();
         bmpFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".bmp");
	}

                public String getDescription() {
                    return "BMP Graphic file";
	}
            });
         
         epsFileChooser = new JFileChooser();
         epsFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".eps");
	}

                public String getDescription() {
                    return "EPSGraphic file";
	}
            });
        
          pdfFileChooser = new JFileChooser();
          pdfFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".pdf");
	}

                public String getDescription() {
                    return "PDF file";
	}
            });
   
        } catch (AccessControlException ace) {
    denySaveSecurity = true;
    }

    buttonGroup = new ButtonGroup();
    
    buttonCenter = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/center.png")));
    buttonCenter.setToolTipText("Center axes");
    buttonCenter.setSelected(plotCanvas.ActionMode == PlotCanvas.TRANSLATION);

    buttonZoom = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/zoom.png")));
    buttonZoom.setToolTipText("Zoom");
    buttonZoom.setSelected(plotCanvas.ActionMode == PlotCanvas.ZOOM);
    buttonEdit = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/edit.png")));
    buttonEdit.setToolTipText("Edit mode");

    buttonViewCoords = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/position.png")));
    buttonViewCoords.setToolTipText("Highlight coordinates / Highlight plot");
    buttonSetScales = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/scale.png")));
    buttonSetScales.setToolTipText("Set scales");

    buttonDatas = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/data.png")));
    buttonDatas.setToolTipText("Get datas");

    buttonSavePNGFile = new JButton("PNG");
    buttonSavePNGFile.setToolTipText("Save graphics in a .PNG File");

    buttonSaveJPGFile = new JButton("JPG");
    buttonSavePNGFile.setToolTipText("Save graphics in a .PNG File");

    buttonSaveBMPFile = new JButton("BMP");
    buttonSaveBMPFile.setToolTipText("Save graphics in a .BMP File");

    buttonSaveEPSFile = new JButton("EPS");
    buttonSaveEPSFile.setToolTipText("Save graphics in an EPS File");

    buttonSavePDFFile = new JButton("PDF");
    buttonSavePDFFile.setToolTipText("Save graphics in a .PDF File");

    buttonReset = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/back.png")));
    buttonReset.setToolTipText("Reset zoom & axes");

    buttonSetFigProps = new JButton(new ImageIcon(PlotPanel.class.getResource("icons/edit.png")));
    buttonSetFigProps.setToolTipText("Edit Figure properties");
                                        
    buttonEdit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	plotCanvas.ActionMode = PlotCanvas.EDIT;
	}
    });

    buttonZoom.setSelected(true);
        buttonZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	plotCanvas.ActionMode = PlotCanvas.ZOOM;
	}
    });

    buttonCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	plotCanvas.ActionMode = PlotCanvas.TRANSLATION;
	}
    });

    buttonViewCoords.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	plotCanvas.setNoteCoords(buttonViewCoords.isSelected());
	}
    });

    buttonSetScales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	plotCanvas.displaySetScalesFrame();
	}
    });

    buttonDatas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	plotCanvas.displayDatasFrame();
	}
    });

    buttonSavePNGFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	choosePNGFile();
	}
    });
    
    buttonSaveJPGFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	chooseJPGFile();
	}
    });
    
    
    buttonSaveBMPFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	chooseBMPFile();
	}
    });
    
    buttonSaveEPSFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	chooseEPSFile();
	}
    });

    
    
    buttonSavePDFFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	choosePDFFile();
	}
    });
    
    buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	plotCanvas.resetBase();
	}
    });

     buttonSetFigProps.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                  plotCanvas.configCanvas();
                    }
     });
                        
    buttonGroup.add(buttonCenter);
    buttonGroup.add(buttonZoom);
    add(buttonCenter, null);
    add(buttonZoom, null);
    add(buttonReset, null);
    add(buttonViewCoords, null);
    add(buttonSetScales, null);
    add(buttonEdit, null);
    add(buttonSavePNGFile, null);
    add(buttonSaveJPGFile, null);
    add(buttonSaveBMPFile, null);
    add(buttonSaveEPSFile, null);
    add(buttonSavePDFFile, null);
    add(buttonDatas, null);
    add(buttonSetFigProps, null);
                
    if (!denySaveSecurity) {
        pngFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGraphicFile();
                }
            });
	} else {
        buttonSavePNGFile.setEnabled(false);
	}

    if (!denySaveSecurity) {
        jpgFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveJPGGraphicFile();
                }
            });
	} else {
        buttonSaveJPGFile.setEnabled(false);
	}
    
    
    if (!denySaveSecurity) {
        bmpFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveBMPGraphicFile();
                }
            });
	} else {
        buttonSaveBMPFile.setEnabled(false);
	}
        
    if (!denySaveSecurity) {
        epsFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEPSGraphicFile();
                }
            });
	} else {
        buttonSaveEPSFile.setEnabled(false);
	}

    
    if (!denySaveSecurity) {
        pdfFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savePDFGraphicFile();
                }
            });
	} else {
        buttonSavePDFFile.setEnabled(false);
	}

    
    
    
    
    
    
    buttonEdit.setEnabled(plotCanvas.getEditable());
        buttonViewCoords.setEnabled(plotCanvas.getNotable());

	// allow mixed (2D/3D) plots managed by one toolbar
        if (plotCanvas instanceof Plot3DCanvas) {
	if (buttonRotate == null) {
    buttonRotate = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/rotation.png")));
    buttonRotate.setToolTipText("Rotate axes");

      buttonRotate.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	plotCanvas.ActionMode = Plot3DCanvas.ROTATION;
	}
        });
    buttonGroup.add(buttonRotate);
    add(buttonRotate, null, 2);
    buttonRotate.setSelected(plotCanvas.ActionMode == Plot3DCanvas.ROTATION);
	} else {
        buttonRotate.setEnabled(true);
	}
    } else {
	if (buttonRotate != null) {
        // no removal/disabling just disable
            if (plotCanvas.ActionMode == Plot3DCanvas.ROTATION) {
	plotCanvas.ActionMode = PlotCanvas.ZOOM;
            }
	buttonRotate.setEnabled(false);
	}
    }
}

void choosePNGFile() {
    pngFileChooser.showSaveDialog(this);
}

void chooseJPGFile() {
    jpgFileChooser.showSaveDialog(this);
}

void chooseBMPFile() {
    bmpFileChooser.showSaveDialog(this);
}

void chooseEPSFile() {
    epsFileChooser.showSaveDialog(this);
}

void choosePDFFile() {
    pdfFileChooser.showSaveDialog(this);
}





        



void saveGraphicFile() {
    java.io.File file = pngFileChooser.getSelectedFile();
    try {
        plotPanel.toGraphicFile(file);
    } catch (IOException e) {
    JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }


void saveJPGGraphicFile() {
    java.io.File file = jpgFileChooser.getSelectedFile();
    try {
        plotPanel.toGraphicJPGFile(file);
    } catch (IOException e) {
    JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

void saveBMPGraphicFile() {
    java.io.File file = bmpFileChooser.getSelectedFile();
    try {
        plotPanel.toGraphicBMPFile(file);
    } catch (IOException e) {
    JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }



void saveEPSGraphicFile() {
    java.io.File file = epsFileChooser.getSelectedFile();
    try {
        plotPanel.toEPSGraphicFile(file, plotPanel.plotCanvas.getWidth(), plotPanel.plotCanvas.getHeight());
    } catch (IOException e) {
    JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }


void savePDFGraphicFile() {
    java.io.File file = pdfFileChooser.getSelectedFile();
    try {
            plotPanel.toPDFGraphicFile(file, plotPanel.plotCanvas.getWidth(), plotPanel.plotCanvas.getHeight());
        
    } catch (IOException e) {
    JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }
}
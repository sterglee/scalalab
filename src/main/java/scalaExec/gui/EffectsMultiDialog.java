
package scalaExec.gui; 

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class EffectsMultiDialog extends JComponent  {
    private static final int BLUR_SIZE = 3;
    private BufferedImage image;
    private float alpha = 0.5f;
    public JPanel topPanel;
    public JPanel [][] subPanels;
    public String titleString;
    public int resizeZone = 50;  // size of the resize zone in pixels
    public int figureId;
    int rows, cols;
    
    public EffectsMultiDialog(JFrame frame, JPanel  [][]_subPanels, String _titleString, int currentFigTableIndex) {
        subPanels = _subPanels;
        cols =  subPanels.length;
        rows = subPanels[0].length;
        
        titleString = _titleString;
       
        figureId = currentFigTableIndex;
        
        Container contentPane = frame.getRootPane();
       image = GraphicsUtilities.createCompatibleTranslucentImage(contentPane.getWidth() + 2 * (int) BLUR_SIZE, 
                contentPane.getHeight() + 2 * (int) BLUR_SIZE);
        Graphics2D g2 = image.createGraphics();
        g2.translate(BLUR_SIZE, BLUR_SIZE);
        contentPane.paint(g2);
        g2.translate(-BLUR_SIZE, -BLUR_SIZE);
        g2.dispose();
        
        image = changeImageWidth(image, image.getWidth() / 2);
        ConvolveOp gaussianFilter = getGaussianBlurFilter(BLUR_SIZE, true);
        image = gaussianFilter.filter(image, null);
        gaussianFilter = getGaussianBlurFilter(BLUR_SIZE, false);
        image = gaussianFilter.filter(image, null);
        ColorTintFilter colorMixFilter = new ColorTintFilter(Color.WHITE, 0.4f);
        image = colorMixFilter.filter(image, null);
        image = changeImageWidth(image, image.getWidth() * 2);

        setBorder(new DropShadowBorder(Color.BLACK, 0, 11, .2f, 16,  false, true, true, true)); 
        setLayout(new BorderLayout());
        initComponents();
        
    }
    
    private void initComponents() {
           TitleBar titleBar =  new TitleBar(titleString);
           int sx=0, sy=0;
        add(titleBar, BorderLayout.NORTH);
        topPanel  = new JPanel(new GridLayout(rows, cols));
        topPanel.setOpaque(false);
        for (int r=0; r<rows; r++)  {
            for (int c=0; c<cols; c++) {
        subPanels[r][c].setOpaque(false);
        subPanels[r][c].setBorder(BorderFactory.createEmptyBorder(16, 2, 16, 2));
        EffectsDialog fd = new EffectsDialog(GlobalValues.scalalabMainFrame, subPanels[r][c],  "", 20);
        topPanel.add(fd);
        sx = fd.getSize().width; sy=fd.getSize().height;
            }
        }
        topPanel.setBorder(BorderFactory.createEmptyBorder(16, 2, 16, 2));
     
        add(topPanel);
        setSize(1000, 1000);
        
    }

    
    public static BufferedImage changeImageWidth(BufferedImage image, int width) {
        float ratio = (float) image.getWidth() / (float) image.getHeight();
        int height = (int) (width / ratio);
        
        BufferedImage temp = new BufferedImage(width, height,
                image.getType());
        Graphics2D g2 = temp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
        g2.dispose();

        return temp;
    }
    
    public static ConvolveOp getGaussianBlurFilter(int radius,
            boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }
        
        int size = radius * 2 + 1;
        float[] data = new float[size];
        
        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;
        
        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }
        
        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }        
        
        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    public void setTitle(String title) {
        TitleBar titleBar =  new TitleBar(titleString);
        add(titleBar, BorderLayout.NORTH);
    }

    public void  close() {
            EffectsMultiDialog.this.setVisible(false);
        }
        
        public void dispose() {
            EffectsMultiDialog.this.setVisible(false);
        }
        
    
    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        setupGraphics((Graphics2D) g);

        Point location = getLocation();
        location.x = (int) (-location.x - BLUR_SIZE);
        location.y = (int) (-location.y - BLUR_SIZE);

        Insets insets = getInsets();
        Shape oldClip = g.getClip();
        g.setClip(insets.left, insets.top,
                  getWidth() - insets.left - insets.right,
                  getHeight() - insets.top - insets.bottom);
        g.drawImage(image, location.x, location.y, null);
        g.setClip(oldClip);
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    private static void setupGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
       
        Toolkit tk = Toolkit.getDefaultToolkit();
        Map desktopHints = (Map) (tk.getDesktopProperty("awt.font.desktophints"));
        if (desktopHints != null) {
            g2.addRenderingHints(desktopHints);
        }
    }

    private class TitleBar extends JComponent {
        private String title;

        private TitleBar(String title) {
            this.title = title;
            setName("vistaTitleBar");
            setFont(new Font("Dialog", Font.BOLD, 12));
            setLayout(new GridBagLayout());

            JButton button = new JButton();
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setBorder(null);
            button.setIconTextGap(0);
            button.setVerticalAlignment(SwingConstants.TOP);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setIcon(new ImageIcon(getClass().getResource("close-title-bar.png")));
            button.setRolloverIcon(new ImageIcon(getClass().getResource("close-title-bar-rollover.png")));
            button.setOpaque(false);
            button.setName("CloseButton");
            add(Box.createVerticalStrut(24),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                                       GridBagConstraints.LINE_START,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(0, 0, 0, 0), 0, 0));
            add(button, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.FIRST_LINE_END,
                                               GridBagConstraints.VERTICAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    EffectsMultiDialog.this.setVisible(false);
                }
            });

            Locator locator = new Locator();
            addMouseListener(locator);
            addMouseMotionListener(locator);
        }

        @Override
        public boolean isOpaque() {
            return false;
        }

        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            setupGraphics(g2);

            Paint oldPaint = g2.getPaint();

            float rgb[] = new Color(0xe9efff).getRGBColorComponents(null);

            g2.setPaint(new GradientPaint(0.0f, 0.0f,
                                          new Color(rgb[0], rgb[1], rgb[2], 0.2f * getAlpha()),
                                          0.0f, getHeight(),
                                          new Color(rgb[0], rgb[1], rgb[2], 0.8f * getAlpha())));
            g2.fillRect(0, 0, getWidth(), getHeight());
            drawText(g2, 3, 0.8f);

            g2.setPaint(oldPaint);

            g2.setColor(Color.GREEN);
            g2.fillRect(getWidth()-resizeZone,  0,  getWidth(), getHeight());
            
            g2.setColor(new Color(rgb[0], rgb[1], rgb[2], 0.6f * getAlpha()));
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            
        }
        
        private void drawText(Graphics2D g2, int size, float opacity) {
            Composite oldComposite = g2.getComposite();
            float preAlpha = 1.0f;
            if (oldComposite instanceof AlphaComposite &&
                ((AlphaComposite) oldComposite).getRule() == AlphaComposite.SRC_OVER) {
                preAlpha = ((AlphaComposite) oldComposite).getAlpha();
            }

            g2.setFont(getFont());
            FontMetrics metrics = g2.getFontMetrics();
            int ascent = metrics.getAscent();
            int heightDiff = (metrics.getHeight() - ascent) / 2;

            g2.setColor(Color.BLACK);

            double tx = 2.0;
            double ty = 2.0 + heightDiff - size;
            g2.translate(tx, ty);

            for (int i = -size; i <= size; i++) {
                for (int j = -size; j <= size; j++) {
                    double distance = i * i + j * j;
                    float alpha = opacity;
                    if (distance > 0.0d) {
                        alpha = (float) (1.0f / ((distance * size) * opacity));
                    }
                    alpha *= preAlpha;
                    if (alpha > 1.0f) {
                        alpha = 1.0f;
                    }
                    g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
                    g2.drawString(title, i + size, j + size + ascent);
                }
            }

            g2.setComposite(oldComposite);
            g2.setColor(Color.WHITE);
            g2.drawString(title, size, size + ascent);

            g2.translate(-tx, -ty);
        }
    }

    private class Locator extends MouseAdapter {
        private Point startPoint;

        @Override
        public void mousePressed(MouseEvent e) {
            startPoint = e.getPoint();
            SwingUtilities.convertPointToScreen(startPoint, (Component) e.getSource());
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            EffectsMultiDialog.this.setCursor(Cursor.getDefaultCursor());
            
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            boolean  moveOp = true;
            Point point = e.getPoint();
            SwingUtilities.convertPointToScreen(point, (Component) e.getSource());
            int sx = startPoint.x;
            int sy = startPoint.y;
            Point location = EffectsMultiDialog.this.getLocation();
            int w = getSize().width;
            int h = getSize().height;
            int ex = location.x+w;
            int distance_x = point.x - startPoint.x;
            int distance_y = point.y - startPoint.y;

            int dx = 20;
                if ( (e.getModifiersEx() & e.CTRL_DOWN_MASK)!=0)
           {
                int nx = w+distance_x;
                int ny = h+distance_y;
               
            
                setSize(nx, ny );
                remove(topPanel);
                
                int subFigSizeX = (int) ((double) nx/ (double)rows);
                int subFigSizeY = (int)((double)ny/(double)cols);
                
                   TitleBar titleBar =  new TitleBar(titleString);
                  add(titleBar, BorderLayout.NORTH);
                  topPanel.removeAll();
     
            topPanel.setOpaque(false);
        for (int r=0; r<rows; r++)  {
            for (int c=0; c<cols; c++) {
        subPanels[r][c].setOpaque(false);
        subPanels[r][c].setBorder(BorderFactory.createEmptyBorder(16, 2, 16, 2));
        EffectsDialog fd = new EffectsDialog(GlobalValues.scalalabMainFrame, subPanels[r][c],  "", 1);
        fd.setSize(subFigSizeX, subFigSizeY);
        topPanel.add(fd);
            }
        }
        topPanel.setBorder(BorderFactory.createEmptyBorder(16, 2, 16, 2));
      
        add(topPanel);
        
        moveOp = false;
            }
  
      EffectsMultiDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            Point oldLocation = (Point) location.clone();
                  
            if (moveOp== true)  {
                 location.x += distance_x;
                 location.y += distance_y;
                 EffectsMultiDialog.this.setLocation(location);
        }

            Rectangle clip = new Rectangle(oldLocation.x, oldLocation.y, EffectsMultiDialog.this.getWidth(), EffectsMultiDialog.this.getHeight());
            clip.intersects(new Rectangle(location.x, location.y, EffectsMultiDialog.this.getWidth(), EffectsMultiDialog.this.getHeight()));

            EffectsMultiDialog.this.getParent().repaint(clip.x, clip.y, clip.width, clip.height);
            
            startPoint = point;
        
            
        }
            
    }

}

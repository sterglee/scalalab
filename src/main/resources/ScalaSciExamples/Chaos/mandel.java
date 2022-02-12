package ScalaSciExamples.Chaos;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;
import javax.swing.*;

public class mandel extends JFrame implements WindowListener {
    public  mandel() {
        addWindowListener(this);
        setTitle("Mandelbrot Set");
        setSize(256, 256);
    }

    public void paint (Graphics g) {
        Image img;
        int w= 256; int h= 256;

        int [] pix = new int[w*h];
        int index = 0;
        int iter;
        double a, b;
        double p, q, psq, qsq, pnew, qnew;

        for (int y=0; y<h; y++) {
            b = ((double)(y-128))/64;
              for (int x=0; x<w; x++)
            {
                a = ((double)(x-128))/64;
                p=q=0.0;
                iter = 0;
                while (iter < 32) {
                    psq = p*p; qsq = q*q;
                    if (psq+qsq >= 4.0) break;
                    pnew = psq - qsq + a;
                    qnew = 2*p*q + b;
                    p = pnew;
                    q = qnew;
                    iter++;
                }
                if (iter == 32) {
                    pix[index] = 255 << 24 | 255;
                 }
                index++;
            }
        }
        img = createImage(new MemoryImageSource(w, h, pix, 0, w));
        g.drawImage(img, 0, 0, null);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        {System.exit(0); }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

public static void main(String [] args) {
    JFrame f = new mandel();
    f.setVisible(true);
    }
}
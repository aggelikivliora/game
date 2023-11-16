package ce326.hw3;

import javax.swing.JButton;
import java.awt.*;
import java.awt.geom.Ellipse2D;
@SuppressWarnings("serial")
public class myButton extends JButton{
    private boolean mouseOver = false;
    private boolean mousePressed = false;

    public myButton(){
        super();
        setBackground(Color.lightGray);
        setFocusable(false);
        //sxhma: kuklos anti gia oval
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
        //adeio gia na mporw na kanw paintbackground
        setContentAreaFilled(false);		
    }
    @Override
    public void paintComponent(Graphics g){
        if (getModel().isArmed()) {
            g.setColor(Color.gray);
        } else {
            g.setColor(getBackground());
        }
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
 
        super.paintComponent(g);
    }
    Shape shape;
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.darkGray);
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }
    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }
}

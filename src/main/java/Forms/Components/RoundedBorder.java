
package Forms.Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;

/**
 *
 * @author VIEN
 */

public class RoundedBorder implements Border {

    private final int radius; // bán kính bo góc
    private Color color ; // màu 
    
    public RoundedBorder(int radius , Color color) {
        this.radius = radius;
        this.color = color ; 
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5);
    }

    @Override
    public boolean isBorderOpaque() {
        return false; // Không để vẽ nền vuông
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // viền mượt mà kh răng cưa 
        g2d.setColor(color); // Màu viền
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2d.dispose();
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
}


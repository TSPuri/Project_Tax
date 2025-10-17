

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PieChartPanel extends JPanel {
    private List<PieSlice> slices;
    private String title;
    
    public PieChartPanel(String title) {
        this.title = title;
        this.slices = new ArrayList<>();
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }
    
    public void addSlice(String label, double value, Color color) {
        slices.add(new PieSlice(label, value, color));
        repaint();
    }
    
    public void clearSlices() {
        slices.clear();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // คำนวณขนาดและตำแหน่งของกราฟวงกลม
        int diameter = Math.min(width - 100, height - 100);
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2 + 20;
        
        // วาดหัวข้อ
        g2d.setFont(new Font("Tahoma", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, 20);
        
        if (slices.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.drawString("ไม่มีข้อมูล", width / 2 - 30, height / 2);
            return;
        }
        
        // คำนวณค่าทั้งหมด
        double total = 0;
        for (PieSlice slice : slices) {
            total += slice.value;
        }
        
        if (total == 0) return;
        
        // วาดกราฟวงกลม
        double currentAngle = 0;
        for (PieSlice slice : slices) {
            double angle = (slice.value / total) * 360;
            
            g2d.setColor(slice.color);
            g2d.fillArc(x, y, diameter, diameter, (int) currentAngle, (int) angle);
            
            // วาดขอบ
            g2d.setColor(Color.BLACK);
            g2d.drawArc(x, y, diameter, diameter, (int) currentAngle, (int) angle);
            
            currentAngle += angle;
        }
        
        // วาด Legend
        drawLegend(g2d, x + diameter + 20, y);
    }
    
    private void drawLegend(Graphics2D g2d, int x, int y) {
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight() + 5;
        
        for (int i = 0; i < slices.size(); i++) {
            PieSlice slice = slices.get(i);
            
            // วาดสีใน legend
            g2d.setColor(slice.color);
            g2d.fillRect(x, y + i * lineHeight, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y + i * lineHeight, 15, 15);
            
            // วาดข้อความ
            g2d.setColor(Color.BLACK);
            String label = slice.label + " (" + String.format("%.1f", slice.value) + ")";
            g2d.drawString(label, x + 20, y + i * lineHeight + 12);
        }
    }
    
    private static class PieSlice {
        String label;
        double value;
        Color color;
        
        PieSlice(String label, double value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }
    }
}

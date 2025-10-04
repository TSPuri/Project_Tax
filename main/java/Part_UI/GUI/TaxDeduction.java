package Part_UI.GUI;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TaxDeduction extends JFrame {

    public TaxDeduction() {
        setTitle("คำนวณค่าลดหย่อนภาษี");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 400);
        setLocationRelativeTo(null);

        // ===== ปุ่มย้อนกลับ (มุมซ้ายบน) =====
        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setBackground(new Color(230, 230, 230));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);

        // ===== กล่องซ้าย: ครอบครัว =====
        JPanel familyPanel = createBoxPanel("ครอบครัว");
        familyPanel.add(labelWithUnder("สถานะสมรส :", new JComboBox<>(new String[]{"โสด","สมรส","หย่า"})));
        familyPanel.add(labelWithUnder("ค่าลดหย่อนบิดา-มารดา :", checkGroup("บิดา","มารดา")));
        familyPanel.add(labelWithUnder("ผู้พิการ/ทุพพลภาพ :", checkGroup("ผู้พิการ","ญาติ")));

        // ===== กล่องกลาง: กองทุน/ประกันสังคม =====
        JPanel fundPanel = createBoxPanel("กองทุนและประกันสังคม");
        fundPanel.add(labelWithUnder("กองทุนสำรองเลี้ยงชีพ (บาท) :", new JTextField(12)));
        fundPanel.add(labelWithUnder("เงินประกันสังคม (บาท) :", new JTextField(12)));
        fundPanel.add(labelWithUnder("ดอกเบี้ยซื้อที่อยู่อาศัย (บาท) :", new JTextField(12)));

        // ===== กล่องขวา: ประกัน/กองทุนอื่นๆ =====
        JPanel insurancePanel = createBoxPanel("ประกันและกองทุนอื่นๆ");
        insurancePanel.add(labelWithUnder("เบี้ยประกันชีวิต (บาท) :", new JTextField(12)));
        insurancePanel.add(labelWithUnder("เบี้ยประกันสุขภาพ (บาท) :", new JTextField(12)));
        insurancePanel.add(labelWithUnder("กองทุนอื่นๆ (บาท) :", new JTextField(12)));

        // ===== วางสามกล่องเรียงแนวนอน =====
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        centerPanel.add(familyPanel);
        centerPanel.add(fundPanel);
        centerPanel.add(insurancePanel);

        // ===== ปุ่มล่าง =====
        JButton clearBtn = new JButton("ล้างข้อมูล");
        clearBtn.setBackground(Color.YELLOW);       // ปุ่มเหลือง
        JButton calcBtn  = new JButton("คำนวณ");
        calcBtn.setBackground(new Color(70, 130, 180)); // น้ำเงิน (SteelBlue)
        calcBtn.setForeground(Color.BLACK);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.add(clearBtn);
        buttonPanel.add(calcBtn);

        // ===== จัด Layout ของ JFrame =====
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /** helper: กล่องใหญ่มีเส้นขอบและหัวเรื่อง */
    private JPanel createBoxPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        // เส้นขอบ + padding
        panel.setBorder(BorderFactory.createTitledBorder(
                  
                BorderFactory.createLineBorder(Color.GRAY)   // เส้นรอบนอก
                        
                ,
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
    
        
    
        return panel;
    }
    

    

    /** helper: label อยู่บน + component ใต้ */
    private JPanel labelWithUnder(String labelText, JComponent comp) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(comp);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return panel;
    }

    /** helper: กลุ่ม checkbox บิดา/มารดา ฯลฯ */
    private JPanel checkGroup(String... names) {
        JPanel group = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        for (String n : names) group.add(new JCheckBox(n));
        return group;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaxDeduction::new);
    }
}

package Part_UI.GUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class TaxDeduction extends JFrame {

    public TaxDeduction() {
        setTitle("กรอกข้อมูลเพื่อคำนวณภาษี");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(210, 230, 240)); // ฟ้าอ่อนพื้นหลัง
        setLayout(new BorderLayout());

        // ===== แถบบน: ปุ่มย้อนกลับซ้าย + หัวข้อกลางเป๊ะ + ช่องว่างขวาถ่วงน้ำหนัก =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // ปุ่มย้อนกลับ
        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        backButton.setBackground(new Color(200, 220, 210));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorder(new RoundedBorder(15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // panel ซ้าย: ปุ่มย้อนกลับ
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(backButton);

        // panel กลาง: หัวข้อ
        JLabel titleLabel = new JLabel("กรอกข้อมูลเพื่อคำนวณภาษี", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel centerPanelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        centerPanelTop.setOpaque(false);
        centerPanelTop.add(titleLabel);

        // panel ขวา: กล่องว่างถ่วง layout
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(leftPanel.getPreferredSize());

        // รวมสามส่วนเข้าด้วยกัน
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerPanelTop, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ===== กล่องซ้าย: ครอบครัว =====
        JPanel familyPanel = createRoundedPanel("ครอบครัว");
        familyPanel.add(labelWithUnder("สถานะสมรส :", new JComboBox<>(new String[]{"โสด", "สมรส", "หย่า"})));
        familyPanel.add(labelWithUnder("ค่าลดหย่อนบิดา-มารดา :", checkGroup("บิดา", "มารดา")));
        familyPanel.add(labelWithUnder("ลดหย่อนผู้พิการหรือทุพพลภาพ :", checkGroup("บิดา/มารดา", "ญาติ (เช่น พี่, น้อง ฯลฯ)")));
        familyPanel.add(labelWithUnder("จำนวนบุตร :", new JTextField(12))); 
        // ===== กล่องกลาง: กองทุน/ประกันสังคม =====
        JPanel fundPanel = createRoundedPanel("กองทุน และประกันสังคม");
        fundPanel.add(labelWithUnder("ค่าลดหย่อนกองทุนสำรองเลี้ยงชีพ (บาท) :", new JTextField(12)));
        fundPanel.add(labelWithUnder("เงินประกันสังคม (บาท) :", new JTextField(12)));
        fundPanel.add(labelWithUnder("ดอกเบี้ยซื้อที่อยู่อาศัย (บาท) :", new JTextField(12)));

        // ===== กล่องขวา: ประกัน/กองทุนอื่นๆ =====
        JPanel insurancePanel = createRoundedPanel("ประกันและกองทุนอื่นๆ");
        insurancePanel.add(labelWithUnder("เบี้ยประกันชีวิต (บาท) :", new JTextField(12)));
        insurancePanel.add(labelWithUnder("เบี้ยประกันสุขภาพ (บาท) :", new JTextField(12)));
        insurancePanel.add(labelWithUnder("กองทุนอื่นๆ (บาท) :", new JTextField(12)));

        // ===== รวมสามกล่องกลาง =====
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(familyPanel);
        centerPanel.add(fundPanel);
        centerPanel.add(insurancePanel);

        // ห่อด้วย panel ใหญ่เพื่อดันลงกลาง
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerPanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);

        // ===== ปุ่มล่าง =====
        JButton clearBtn = new JButton("ล้างข้อมูล");
        clearBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        clearBtn.setBackground(new Color(255, 200, 0));
        clearBtn.setFocusPainted(false);
        clearBtn.setBorder(new RoundedBorder(12));

        JButton calcBtn = new JButton("คำนวณ");
        calcBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        calcBtn.setBackground(new Color(30, 60, 120));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFocusPainted(false);
        calcBtn.setBorder(new RoundedBorder(12));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(clearBtn);
        buttonPanel.add(calcBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Event ปุ่มย้อนกลับ =====
        backButton.addActionListener(e -> {
            this.dispose();
            new IncomeForm().setVisible(true);
        });

        setVisible(true);
    }

    // ===== กล่องโค้ง =====
    private JPanel createRoundedPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new TitledBorder(null, title, TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Tahoma", Font.BOLD, 16))
        ));
        panel.setPreferredSize(new Dimension(270, 300));
        return panel;
    }

    // ===== Label + Component ใต้ =====
    private JPanel labelWithUnder(String labelText, JComponent comp) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 10, 3, 10);

        JLabel label = new JLabel("<html>" + labelText + "</html>");
        label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        panel.add(label, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(3, 10, 8, 10);
        panel.add(comp, gbc);

        return panel;
    }

    // ===== กลุ่ม checkbox =====
    private JPanel checkGroup(String... names) {
        JPanel group = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        group.setOpaque(false);
        for (String n : names) group.add(new JCheckBox(n));
        return group;
    }

    // ===== ขอบโค้งปุ่ม =====
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int r) { radius = r; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}

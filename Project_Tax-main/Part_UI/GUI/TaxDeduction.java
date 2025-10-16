
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import Part_Service.TaxCalculation;

public class TaxDeduction extends JFrame {

    private final Font thaiFont = new Font("Tahoma", Font.PLAIN, 15);
    private JComboBox<String> maritalBox;
    private JCheckBox fatherBox, motherBox, disParentBox, disRelBox;
    private JTextField providentField, socialField, homeLoanField, lifeIns, healthIns, otherFund;
    private JPanel childListPanel;

    private final String userId, year;
    private final double income, bonus, other;

    // ✅ Constructor ใหม่: รับค่ารายได้จากหน้า Income.java
    public TaxDeduction(String userId, String year, double income, double bonus, double other) {
        this.userId = userId;
        this.year = year;
        this.income = income;
        this.bonus = bonus;
        this.other = other;
        initUI();
    }

    private void initUI() {
        setTitle("กรอกข้อมูลเพื่อลดหย่อนภาษี");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 850);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(210, 230, 240));
        setLayout(new BorderLayout());

        // ===== แถบบน =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(thaiFont);
        backButton.setBackground(new Color(200, 220, 210));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorder(new RoundedBorder(15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            new Income(userId).setVisible(true);
        });

        JLabel titleLabel = new JLabel("กรอกข้อมูลเพื่อลดหย่อนภาษี", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));

        JPanel topCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        topCenter.setOpaque(false);
        topCenter.add(titleLabel);

        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topLeft.setOpaque(false);
        topLeft.add(backButton);

        topPanel.add(topLeft, BorderLayout.WEST);
        topPanel.add(topCenter, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ===== กล่องซ้าย: ครอบครัว =====
        JPanel familyPanel = createRoundedPanel("ครอบครัว");
        maritalBox = comboBox(new String[]{"โสด", "สมรส", "หย่า"});
        familyPanel.add(labelWithComponent("สถานะสมรส", maritalBox));

        fatherBox = new JCheckBox("บิดา");
        motherBox = new JCheckBox("มารดา");
        fatherBox.setFont(thaiFont);
        motherBox.setFont(thaiFont);

        JPanel parentGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parentGroup.setOpaque(false);
        parentGroup.add(fatherBox);
        parentGroup.add(motherBox);
        familyPanel.add(labelWithComponent("ลดหย่อนบิดา-มารดา", parentGroup));

        disParentBox = new JCheckBox("บิดา/มารดา");
        disRelBox = new JCheckBox("ญาติ (พี่, น้อง ฯลฯ)");
        disParentBox.setFont(thaiFont);
        disRelBox.setFont(thaiFont);
        JPanel disGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        disGroup.setOpaque(false);
        disGroup.add(disParentBox);
        disGroup.add(disRelBox);
        familyPanel.add(labelWithComponent("ลดหย่อนผู้พิการ/ทุพพลภาพ", disGroup));

        // ===== ส่วนกรอกข้อมูลบุตร =====
        childListPanel = new JPanel();
        childListPanel.setLayout(new BoxLayout(childListPanel, BoxLayout.Y_AXIS));
        childListPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(childListPanel);
        scrollPane.setPreferredSize(new Dimension(250, 180));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton addChildBtn = new JButton("+ เพิ่มบุตร");
        addChildBtn.setFont(thaiFont);
        addChildBtn.setBackground(new Color(180, 220, 250));
        addChildBtn.addActionListener(e -> {
            JPanel newChild = createChildPanel(childListPanel);
            childListPanel.add(newChild);
            childListPanel.revalidate();
            childListPanel.repaint();
        });

        familyPanel.add(labelWithComponent("ข้อมูลบุตร", scrollPane));
        familyPanel.add(addChildBtn);

        // ===== กล่องกลาง =====
        JPanel fundPanel = createRoundedPanel("กองทุนและประกันสังคม");
        providentField = textFieldNumbersOnly(10);
        socialField = textFieldNumbersOnly(10);
        homeLoanField = textFieldNumbersOnly(10);
        fundPanel.add(labelWithComponent("กองทุนสำรองเลี้ยงชีพ (บาท)", providentField));
        fundPanel.add(labelWithComponent("ประกันสังคม (บาท)", socialField));
        fundPanel.add(labelWithComponent("ดอกเบี้ยที่อยู่อาศัย (บาท)", homeLoanField));

        // ===== กล่องขวา =====
        JPanel insurancePanel = createRoundedPanel("ประกันและกองทุนอื่น ๆ");
        lifeIns = textFieldNumbersOnly(10);
        healthIns = textFieldNumbersOnly(10);
        otherFund = textFieldNumbersOnly(10);
        insurancePanel.add(labelWithComponent("เบี้ยประกันชีวิต (บาท)", lifeIns));
        insurancePanel.add(labelWithComponent("เบี้ยประกันสุขภาพ (บาท)", healthIns));
        insurancePanel.add(labelWithComponent("กองทุนอื่น ๆ (บาท)", otherFund));

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(familyPanel);
        centerPanel.add(fundPanel);
        centerPanel.add(insurancePanel);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerPanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);

        // ===== ปุ่มล่าง =====
        JButton clearBtn = new JButton("ล้างข้อมูล");
        clearBtn.setFont(thaiFont);
        clearBtn.setBackground(new Color(255, 200, 0));
        clearBtn.setFocusPainted(false);
        clearBtn.setBorder(new RoundedBorder(12));

        JButton calcBtn = new JButton("คำนวณ");
        calcBtn.setFont(thaiFont);
        calcBtn.setBackground(new Color(30, 60, 120));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFocusPainted(false);
        calcBtn.setBorder(new RoundedBorder(12));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(clearBtn);
        buttonPanel.add(calcBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== ปุ่มล้าง =====
        clearBtn.addActionListener(e -> clearFields(getContentPane()));

        // ===== ปุ่มคำนวณ =====
        calcBtn.addActionListener(e -> calculateTax());

        setVisible(true);
    }

    // ===== ฟังก์ชันคำนวณภาษี =====
    private void calculateTax() {
        try {
            double totalIncome = income + bonus + other; // ✅ ใช้ค่ารายได้จาก Income.java
            double provident = parseDouble(providentField.getText());
            double social = parseDouble(socialField.getText());
            double homeLoan = parseDouble(homeLoanField.getText());
            double life = parseDouble(lifeIns.getText());
            double health = parseDouble(healthIns.getText());
            double otherFundVal = parseDouble(otherFund.getText());

            // ===== รวมข้อมูลบุตร =====
            int totalChildDeduction = 0;
            for (Component comp : childListPanel.getComponents()) {
                if (comp instanceof JPanel panel) {
                    String name = "";
                    int birthYear = 0;
                    boolean studying = false, adopted = false;

                    for (Component c : panel.getComponents()) {
                        if (c instanceof JTextField tf) {
                            if (tf.getColumns() == 8) name = tf.getText().trim();
                            else if (tf.getColumns() == 5 && !tf.getText().trim().isEmpty())
                                birthYear = Integer.parseInt(tf.getText().trim());
                        } else if (c instanceof JCheckBox cb) {
                            if (cb.getText().contains("ศึกษา")) studying = cb.isSelected();
                            if (cb.getText().contains("บุญธรรม")) adopted = cb.isSelected();
                        }
                    }

                    if (!name.isEmpty() && birthYear > 0) {
                        int age = 2568 - birthYear;
                        if (!adopted) totalChildDeduction += (age <= 20 || studying) ? 60000 : 30000;
                        else totalChildDeduction += (age <= 20 || studying) ? 30000 : 0;
                    }
                }
            }

            // ===== เรียกใช้คลาสคำนวณภาษีหลัก =====
            double totalTax = TaxCalculation.calculateTaxFromForm(
                    totalIncome, provident, social, homeLoan, life, health, otherFundVal, totalChildDeduction
            );

            JOptionPane.showMessageDialog(this,
                    "คำนวณภาษีสำเร็จ!\n\n" +
                            "รายได้รวม: " + String.format("%,.2f", totalIncome) + " บาท\n" +
                            "ค่าลดหย่อนบุตร: " + String.format("%,d", totalChildDeduction) + " บาท\n" +
                            "ภาษีที่ต้องชำระ: " + String.format("%,.2f", totalTax) + " บาท",
                    "ผลลัพธ์ภาษี", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "เกิดข้อผิดพลาดในการคำนวณ กรุณากรอกข้อมูลให้ครบถ้วน",
                    "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

 }

    // ===== Helper methods =====
    private double parseDouble(String s) {
        try { return Double.parseDouble(s.trim()); }
        catch (Exception e) { return 0; }
    }

    private void clearFields(Component comp) {
        if (comp instanceof JTextField) ((JTextField) comp).setText("");
        else if (comp instanceof JComboBox) ((JComboBox<?>) comp).setSelectedIndex(0);
        else if (comp instanceof JCheckBox) ((JCheckBox) comp).setSelected(false);
        else if (comp instanceof Container)
            for (Component child : ((Container) comp).getComponents()) clearFields(child);
    }

    private JComboBox<String> comboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(thaiFont);
        return box;
    }

    private JTextField textFieldNumbersOnly(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(thaiFont);
        tf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        return tf;
    }

    private JPanel createRoundedPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new TitledBorder(null, title, TitledBorder.CENTER, TitledBorder.TOP, thaiFont)
        ));
        panel.setPreferredSize(new Dimension(320, 680));
        return panel;
    }

    private JPanel labelWithComponent(String labelText, JComponent comp) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 10, 3, 10);

        JLabel label = new JLabel(labelText);
        label.setFont(thaiFont);
        panel.add(label, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(3, 10, 8, 10);
        panel.add(comp, gbc);

        return panel;
    }

    private JPanel createChildPanel(JPanel container) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
        panel.setOpaque(false);

        JTextField nameField = new JTextField(8);
        nameField.setFont(thaiFont);
        JTextField birthYearField = textFieldNumbersOnly(5);

        JCheckBox studyingBox = new JCheckBox("กำลังศึกษาอยู่");
        studyingBox.setFont(thaiFont);
        JCheckBox adoptedBox = new JCheckBox("บุตรบุญธรรม");
        adoptedBox.setFont(thaiFont);

        JButton removeBtn = new JButton("– ลบ");
        removeBtn.setFont(thaiFont);
        removeBtn.setBackground(new Color(255, 180, 180));
        removeBtn.addActionListener(e -> {
            container.remove(panel);
            container.revalidate();
            container.repaint();
        });

        panel.add(new JLabel("ชื่อ:"));
        panel.add(nameField);
        panel.add(new JLabel("ปีเกิด:"));
        panel.add(birthYearField);
        panel.add(studyingBox);
        panel.add(adoptedBox);
        panel.add(removeBtn);

        return panel;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        RoundedBorder(int r) { radius = r; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
        }
    }
}

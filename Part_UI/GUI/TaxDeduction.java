
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import Part_Service.TaxCalculation;

public class TaxDeduction extends JFrame {

    private final Font thaiFont = new Font("Tahoma", Font.PLAIN, 15);
    private JComboBox<String> maritalBox;
    private JCheckBox fatherBox, motherBox, disParentBox, disRelBox;
    private JTextField childField, providentField, socialField, homeLoanField, lifeIns, healthIns, otherFund;
    private String userId;
    private String year;

    public TaxDeduction(String userId, String year) {
        this.userId = userId;
        this.year = year;

        setTitle("กรอกข้อมูลเพื่อลดหย่อนภาษี");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
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

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(backButton);

        JLabel titleLabel = new JLabel("กรอกข้อมูลเพื่อลดหย่อนภาษี", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));

        JPanel centerTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        centerTop.setOpaque(false);
        centerTop.add(titleLabel);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(leftPanel.getPreferredSize());

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerTop, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ===== กล่องซ้าย: ครอบครัว =====
        JPanel familyPanel = createRoundedPanel("ครอบครัว");
        maritalBox = comboBox(new String[]{"โสด", "สมรส", "หย่า"});
        familyPanel.add(labelWithComponent("สถานะสมรส", maritalBox));

        JPanel parentGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        parentGroup.setOpaque(false);
        fatherBox = new JCheckBox("บิดา");
        motherBox = new JCheckBox("มารดา");
        fatherBox.setFont(thaiFont);
        motherBox.setFont(thaiFont);
        parentGroup.add(fatherBox);
        parentGroup.add(motherBox);
        familyPanel.add(labelWithComponent("ลดหย่อนบิดา-มารดา", parentGroup));

        JPanel disGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        disGroup.setOpaque(false);
        disParentBox = new JCheckBox("บิดา/มารดา");
        disRelBox = new JCheckBox("ญาติ (เช่น พี่, น้อง ฯลฯ)");
        disParentBox.setFont(thaiFont);
        disRelBox.setFont(thaiFont);
        disGroup.add(disParentBox);
        disGroup.add(disRelBox);
        familyPanel.add(labelWithComponent("ลดหย่อนผู้พิการ/ทุพพลภาพ", disGroup));

        JPanel childPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        childPanel.setOpaque(false);
        childField = new JTextField(5);
        childField.setFont(thaiFont);
        JLabel unitLabel = new JLabel("คน");
        unitLabel.setFont(thaiFont);
        childPanel.add(childField);
        childPanel.add(unitLabel);
        familyPanel.add(labelWithComponent("จำนวนบุตร", childPanel));

        // ===== กล่องกลาง =====
        JPanel fundPanel = createRoundedPanel("กองทุนและประกันสังคม");
        providentField = textField();
        socialField = textField();
        homeLoanField = textField();
        fundPanel.add(labelWithComponent("กองทุนสำรองเลี้ยงชีพ (บาท)", providentField));
        fundPanel.add(labelWithComponent("ประกันสังคม (บาท)", socialField));
        fundPanel.add(labelWithComponent("ดอกเบี้ยที่อยู่อาศัย (บาท)", homeLoanField));

        // ===== กล่องขวา =====
        JPanel insurancePanel = createRoundedPanel("ประกันและกองทุนอื่น ๆ");
        lifeIns = textField();
        healthIns = textField();
        otherFund = textField();
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

        JButton saveBtn = new JButton("คำนวณ");
        saveBtn.setFont(thaiFont);
        saveBtn.setBackground(new Color(30, 60, 120));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(new RoundedBorder(12));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(clearBtn);
        buttonPanel.add(saveBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ปุ่มย้อนกลับ 
        backButton.addActionListener(e -> {
            dispose();
            new Income(userId).setVisible(true);
        });

        // ปุ่มล้าง 
        clearBtn.addActionListener(e -> clearFields(getContentPane()));

        //  ปุ่มคำนวณ 
        saveBtn.addActionListener(e -> {
            
            //  Path ไฟล์ 
            File file = new File("Part_Data/user_data.csv");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this,
                    "ไม่พบไฟล์ข้อมูลรายได้ กรุณาบันทึกในหน้า Income ก่อน",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            System.out.println("กำลังอ่านไฟล์จาก: " + file.getAbsolutePath());
            System.out.println("userId = " + userId + " | ปีภาษี = " + year);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean updated = false;
            boolean headerWritten = false;
        
            while ((line = br.readLine()) != null) {
                    // ข้าม header
                if (line.trim().isEmpty() || line.startsWith("UserID")) {
                    if (!headerWritten) {
                        sb.append(line).append("\n");
                        headerWritten = true;
                    }
                    continue;
                }
        
                    // ตรวจว่าเป็นบรรทัดของ userId + year หรือไม่
                if (line.startsWith(userId + "," + year + ",")) {
                    System.out.println("พบข้อมูลของผู้ใช้ในไฟล์: " + line);
        
                    int father = fatherBox.isSelected() ? 30000 : 0;
                    int mother = motherBox.isSelected() ? 30000 : 0;
                    int disParent = disParentBox.isSelected() ? 60000 : 0;
                    int disRel = disRelBox.isSelected() ? 600000 : 0;
                    int child = 0;
                    try {
                        child = Integer.parseInt(childField.getText().trim()) * 30000;
                    } catch (Exception ignored) {}
        
                    String[] data = line.split(",", -1);
        
                        // เขียนบรรทัดใหม่ (อัปเดตข้อมูลลดหย่อน)
                    line = data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," +
                    maritalBox.getSelectedItem().toString() + "," +
                     father + "," + mother + "," + disParent + "," + disRel + "," +
                    child + "," + providentField.getText().trim() + "," +
                    socialField.getText().trim() + "," +
                    homeLoanField.getText().trim() + "," +
                     lifeIns.getText().trim() + "," +
                    healthIns.getText().trim() + "," +
                    otherFund.getText().trim();
        
                    updated = true;
                    System.out.println("อัปเดตข้อมูลใหม่: " + line);
                }
                sb.append(line).append("\n");
                }
        
                // เขียนกลับลงไฟล์ (เฉพาะเมื่อเจอข้อมูลจริง) 
                if (updated) {
                    try (FileWriter fw = new FileWriter(file, false)) {
                        fw.write(sb.toString());
                    }
                    dispose();
                    new Result(userId).setVisible(true);;
                    System.out.println("บันทึกข้อมูลใหม่เรียบร้อย: " + file.getAbsolutePath());
        
                    // เรียกคำนวณภาษี 
                    try {
                        Part_Service.TaxCalculation.calculateTax(userId, year);
                        File taxFile = new File("taxhistory.csv");
                        if (taxFile.exists()) {
                            System.out.println("บันทึกผลภาษีลงไฟล์ taxhistory.csv แล้ว");
                        } else {
                            System.out.println("ไม่พบไฟล์ taxhistory.csv หลังคำนวณ อาจมี path ไม่ตรง");
                        }
        
                        JOptionPane.showMessageDialog(this,
                            "คำนวณภาษีเสร็จสิ้น!\nผลลัพธ์ถูกบันทึกใน taxhistory.csv",
                            "สำเร็จ", JOptionPane.INFORMATION_MESSAGE);
        
                    } catch (Exception ex) {
                        System.err.println("เกิดข้อผิดพลาดในการคำนวณภาษี:");
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                            "เกิดข้อผิดพลาดในการคำนวณภาษี",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
        
                } else {
                    JOptionPane.showMessageDialog(this,
                        "ไม่พบข้อมูลรายได้ของปีนี้ โปรดกรอกในหน้า Income ก่อน",
                        "Error", JOptionPane.WARNING_MESSAGE);
                    System.out.println("ไม่พบข้อมูลของ userId=" + userId + " ปี=" + year + " ในไฟล์ " + file.getName());
                }
        
            } catch (IOException ex) {
                System.err.println("เกิดข้อผิดพลาดในการอ่าน/เขียนไฟล์:");
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "เกิดข้อผิดพลาดในการบันทึกข้อมูล",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
           
        });
        
        setVisible(true);
    }

    public TaxDeduction() {
        //TODO Auto-generated constructor stub
    }

    //  ฟังก์ชันช่วย 
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

    private JTextField textField() {
        JTextField tf = new JTextField(10);
        tf.setFont(thaiFont);
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
        panel.setPreferredSize(new Dimension(320, 360));
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

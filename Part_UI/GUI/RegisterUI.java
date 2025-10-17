//package Part_UI.GUI.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

import Part_Service.PasswordValidator;

public class RegisterUI extends JFrame {

    public RegisterUI() {
        setTitle("ลงทะเบียนเข้าใช้งาน");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font thaiFontBold = new Font("Tahoma", Font.BOLD, 16);
        Font thaiFontNormal = new Font("Tahoma", Font.PLAIN, 15);

        // กำหนดฟอนต์ของ JOptionPane
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 14));

        // Container หลัก
        JPanel container = new JPanel();
        container.setBackground(new Color(246, 249, 255));
        container.setLayout(new GridLayout(1, 2, 20, 0));
        container.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ----- ฝั่งซ้าย -----
        JPanel left = new JPanel(new GridLayout(3, 1, 0, 15));
        left.setOpaque(false);

        String[][] boxes = {
                {"เลขประจำตัวผู้เสียภาษี", "เลขประจำตัวผู้เสียภาษีคือเลขบัตรประชาชน 13 หลัก"},
                {"เข้าสู่ระบบ", "กรอกเลขประจำตัวและรหัสผ่าน สำหรับผู้ที่เคยใช้งานแล้ว"},
                {"ลงทะเบียน", "สำหรับผู้ใช้ใหม่ กรอกข้อมูลเพื่อลงทะเบียนเข้าใช้งาน"}
        };

        for (String[] b : boxes) {
            JPanel box = new JPanel(new BorderLayout());
            box.setBackground(Color.WHITE);
            box.setBorder(new RoundedBorder(12));

            JLabel title = new JLabel(b[0], SwingConstants.CENTER);
            title.setOpaque(true);
            title.setBackground(new Color(255, 193, 7));
            title.setFont(thaiFontBold);
            title.setBorder(new EmptyBorder(8, 0, 8, 0));

            JLabel desc = new JLabel("<html><div style='text-align:center;'>" + b[1] + "</div></html>", SwingConstants.CENTER);
            desc.setOpaque(true);
            desc.setBackground(new Color(25, 118, 210));
            desc.setForeground(Color.WHITE);
            desc.setFont(thaiFontNormal);
            desc.setBorder(new EmptyBorder(8, 8, 8, 8));

            box.add(title, BorderLayout.NORTH);
            box.add(desc, BorderLayout.CENTER);
            left.add(box);
        }

        // ----- ฝั่งขวา -----
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(232, 241, 251));
        right.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel header = new JLabel("ลงทะเบียนเข้าใช้งาน", SwingConstants.CENTER);
        header.setFont(thaiFontBold);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        WebTextField taxIdField = new WebTextField("เลขประจำตัวผู้เสียภาษี", thaiFontNormal);
        WebTextField firstNameField = new WebTextField("ชื่อ", thaiFontNormal);
        WebTextField lastNameField = new WebTextField("นามสกุล", thaiFontNormal);
        WebPasswordField passwordField = new WebPasswordField("รหัสผ่าน", thaiFontNormal);
        WebPasswordField confirmField = new WebPasswordField("ยืนยันรหัสผ่าน", thaiFontNormal);

        ((AbstractDocument) taxIdField.getDocument()).setDocumentFilter(new DigitFilter());
        ((AbstractDocument) firstNameField.getDocument()).setDocumentFilter(new AlphaFilter());
        ((AbstractDocument) lastNameField.getDocument()).setDocumentFilter(new AlphaFilter());

        taxIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // ป้ายแสดงความปลอดภัยของรหัสผ่าน
        JLabel strengthLabel = new JLabel("กรุณากรอกรหัสผ่าน");
        strengthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        strengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        strengthLabel.setFont(thaiFontNormal);
        strengthLabel.setForeground(Color.GRAY);

        // ตรวจสอบรหัสแบบเรียลไทม์
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { check(); }
            public void removeUpdate(DocumentEvent e) { check(); }
            public void changedUpdate(DocumentEvent e) { check(); }

            private void check() {
                String pass = new String(passwordField.getPassword());
                String result = PasswordValidator.checkStrength(pass);
                strengthLabel.setText(result);

                if (result.contains("อ่อนมาก")) strengthLabel.setForeground(Color.RED);
                else if (result.contains("กลาง")) strengthLabel.setForeground(new Color(255, 140, 0));
                else if (result.contains("ค่อนข้าง")) strengthLabel.setForeground(new Color(34, 139, 34));
                else if (result.contains("สูงสุด")) strengthLabel.setForeground(new Color(0, 128, 0));
                else strengthLabel.setForeground(Color.GRAY);
            }
        });

        // ===== ปุ่ม =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton backBtn = new JButton("ย้อนกลับ");
        WebButtonStyle.apply(backBtn, thaiFontNormal, new Color(13, 43, 97), new Color(19, 61, 132));
        backBtn.setPreferredSize(new Dimension(150, 40));

        JButton registerBtn = new JButton("ลงทะเบียน");
        WebButtonStyle.apply(registerBtn, thaiFontNormal, new Color(13, 43, 97), new Color(19, 61, 132));
        registerBtn.setPreferredSize(new Dimension(150, 40));

        btnPanel.add(backBtn);
        btnPanel.add(registerBtn);

        // === ปุ่มย้อนกลับ ===
        backBtn.addActionListener(e -> {
            this.dispose();
            new Login();
        });

        // === ปุ่มลงทะเบียน ===
        registerBtn.addActionListener(e -> {
            String id = taxIdField.getText().trim();
            String fname = firstNameField.getText().trim();
            String lname = lastNameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (id.length() != 13) {
                JOptionPane.showMessageDialog(this, "เลขประจำตัวผู้เสียภาษีต้องมี 13 หลัก", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (fname.isEmpty() || lname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกชื่อและนามสกุล", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!PasswordValidator.isStrongEnough(pass)) {
                JOptionPane.showMessageDialog(this,
                        "รหัสผ่านไม่ปลอดภัยพอ (ต้องมี ≥8 ตัว มีตัวพิมพ์ใหญ่ และตัวเลข)",
                        "รหัสผ่านไม่ปลอดภัย", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "รหัสผ่านไม่ตรงกัน", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

             File file = new File("Part_Data/users.csv");
                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line;
                    while ((line = br.readLine()) != null) {
                            if (line.startsWith("ID:")) {
                                String existingID = line.split(":", 2)[1].trim();
                            if (existingID.equals(id)) {
                                JOptionPane.showMessageDialog(this, 
                                "ผู้ใช้นี้ได้ลงทะเบียนแล้ว", 
                                "ลงทะเบียนซ้ำ", 
                                JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            }
                    }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, 
                    "เกิดข้อผิดพลาดในการตรวจสอบข้อมูล", 
                      "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
        }

            try {
                File dataDir = new File("Part_Data");
                if (!dataDir.exists()) dataDir.mkdirs();

                FileWriter fw = new FileWriter("Part_Data/users.csv", true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("ID: " + id + "\n");
                bw.write("ชื่อ: " + fname + "\n");
                bw.write("นามสกุล: " + lname + "\n");
                bw.write("รหัสผ่าน: " + pass + "\n");
                bw.write("-------------------------\n");
                bw.newLine();
                bw.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการบันทึกไฟล์", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.dispose();
            new Login();
        });

        // Layout ฝั่งขวา
        right.add(header);
        right.add(taxIdField);
        right.add(Box.createVerticalStrut(10));
        right.add(firstNameField);
        right.add(Box.createVerticalStrut(10));
        right.add(lastNameField);
        right.add(Box.createVerticalStrut(10));
        right.add(passwordField);
        right.add(Box.createVerticalStrut(5));
        right.add(confirmField);
        right.add(strengthLabel);
        right.add(Box.createVerticalStrut(15));
        right.add(btnPanel);

        container.add(left);
        container.add(right);
        add(container);

        setPreferredSize(new Dimension(1000, 450));
        setMinimumSize(new Dimension(1000, 450));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ==== คลาสเสริม ====

    static class WebTextField extends JTextField {
        private String placeholder;
        private boolean hover = false;

        public WebTextField(String placeholder, Font font) {
            this.placeholder = placeholder;
            setFont(font);
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(235, 245, 255));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setColor(isFocusOwner() ? new Color(25, 118, 210) : hover ? new Color(100, 149, 237) : Color.GRAY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                g2.setColor(Color.GRAY);
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 5, getHeight() / 2 + g2.getFont().getSize() / 2 - 2);
            }
            g2.dispose();
        }
    }

    static class WebPasswordField extends JPasswordField {
        private String placeholder;
        private boolean hover = false;

        public WebPasswordField(String placeholder, Font font) {
            this.placeholder = placeholder;
            setFont(font);
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(235, 245, 255));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setColor(isFocusOwner() ? new Color(25, 118, 210) : hover ? new Color(100, 149, 237) : Color.GRAY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            super.paintComponent(g);
            if (getPassword().length == 0 && !isFocusOwner()) {
                g2.setColor(Color.GRAY);
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 5, getHeight() / 2 + g2.getFont().getSize() / 2 - 2);
            }
            g2.dispose();
        }
    }

    static class WebButtonStyle {
        public static void apply(JButton b, Font font, Color normal, Color hover) {
            b.setFont(font);
            b.setFocusPainted(false);
            b.setForeground(Color.WHITE);
            b.setBackground(normal);
            b.setOpaque(false);
            b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
                public void mouseExited(MouseEvent e) { b.setBackground(normal); }
            });
            b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(b.getBackground());
                    g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30);
                    super.paint(g2, c);
                    g2.dispose();
                }
            });
        }
    }

    static class RoundedBorder extends javax.swing.border.LineBorder {
        public RoundedBorder(int radius) {
            super(Color.LIGHT_GRAY, 1, true);
        }
    }

    static class DigitFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d+")) super.insertString(fb, offset, string, attr);
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d+")) super.replace(fb, offset, length, text, attrs);
        }
    }

    static class AlphaFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[a-zA-Zก-๙\\s]+")) super.insertString(fb, offset, string, attr);
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[a-zA-Zก-๙\\s]+")) super.replace(fb, offset, length, text, attrs);
        }
    }

    public static void main(String[] args) {
        new RegisterUI();
    }
}
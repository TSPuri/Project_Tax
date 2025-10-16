//package GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Password extends JFrame {

    public Password() {
        setTitle("Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font thaiFontBold = new Font("Tahoma", Font.BOLD, 16);
        Font thaiFontNormal = new Font("Tahoma", Font.PLAIN, 15);

        JPanel container = new JPanel();
        container.setBackground(new Color(246, 249, 255));
        container.setLayout(new GridLayout(1, 2, 20, 0));
        container.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ----- ซ้าย -----
        JPanel left = new JPanel(new GridLayout(3, 1, 0, 15));
        left.setOpaque(false);

        String[][] boxes = {
                {"ID", "เลขประจำตัวผู้เสียภาษีคือเลขบัตรประชาชน 13 หลัก"},
                {"เข้าสู่ระบบ", "กรอกเลขประจำตัวและรหัสผ่าน คือคนที่เคยเข้ามาใช้งานแล้ว"},
                {"ลงทะเบียน", "คนที่ยังไม่เคยใช้งาน กดลงทะเบียน กรอกเลขบัตรประชาชน ชื่อ-นามสกุล และรหัสผ่าน"}
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

        // ----- ขวา -----
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(232, 241, 251));
        right.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel header = new JLabel("เปลี่ยนรหัสผ่าน", SwingConstants.CENTER);
        header.setFont(thaiFontBold);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        WebTextField taxIdField = new WebTextField("ID", thaiFontNormal);
        WebTextField firstNameField = new WebTextField("ชื่อ", thaiFontNormal);
        WebTextField lastNameField = new WebTextField("นามสกุล", thaiFontNormal);
        WebPasswordField passwordField = new WebPasswordField("รหัสผ่านใหม่", thaiFontNormal);
        WebPasswordField confirmField = new WebPasswordField("ยืนยันรหัสผ่าน", thaiFontNormal);

        taxIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton changeBtn = new JButton("เปลี่ยนรหัสผ่าน");
        WebButtonStyle.apply(changeBtn, thaiFontNormal, new Color(13, 43, 97), new Color(19, 61, 132));
        changeBtn.setPreferredSize(new Dimension(150, 40));
        btnPanel.add(changeBtn);

        changeBtn.addActionListener(e -> {
            String id = taxIdField.getText().trim();
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            String newPass = new String(passwordField.getPassword());
            String confirmPass = new String(confirmField.getPassword());

            if (id.isEmpty() || first.isEmpty() || last.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกข้อมูลให้ครบ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPass.length() < 8) {
                JOptionPane.showMessageDialog(this, "รหัสผ่านต้องมีอย่างน้อย 8 ตัวอักษร", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "รหัสผ่านไม่ตรงกัน", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File file = new File("users.csv");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "ไม่พบไฟล์ผู้ใช้", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                List<String> lines = new ArrayList<>();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                br.close();

                boolean found = false;
                String currentID = null, currentFirst = null, currentLast = null;

                for (int i = 0; i < lines.size(); i++) {
                    line = lines.get(i).trim();

                    if (line.startsWith("ID:")) {
                        currentID = line.split(":", 2)[1].trim();
                    } else if (line.startsWith("ชื่อ:")) {
                        currentFirst = line.split(":", 2)[1].trim();
                    } else if (line.startsWith("นามสกุล:")) {
                        currentLast = line.split(":", 2)[1].trim();
                    } else if (line.startsWith("สกุล:")) {
                        currentLast = line.split(":", 2)[1].trim();
                    } else if (line.startsWith("รหัสผ่าน:")) {
                        if (currentID != null && currentFirst != null && currentLast != null) {
                            if (currentID.equals(id) && currentFirst.equals(first) && currentLast.equals(last)) {
                                lines.set(i, "รหัสผ่าน: " + newPass);
                                found = true;
                                break;
                            }
                        }
                    }
                }

                if (found) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    for (String l : lines) {
                        bw.write(l);
                        bw.newLine();
                    }
                    bw.flush();
                    bw.close();
                    JOptionPane.showMessageDialog(this, "เปลี่ยนรหัสผ่านสำเร็จ", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    new Login();
                } else {
                    JOptionPane.showMessageDialog(this, "ไม่พบผู้ใช้ที่ตรงกับข้อมูล", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดขณะบันทึกไฟล์", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        right.add(header);
        right.add(taxIdField);
        right.add(Box.createVerticalStrut(10));
        right.add(firstNameField);
        right.add(Box.createVerticalStrut(10));
        right.add(lastNameField);
        right.add(Box.createVerticalStrut(10));
        right.add(passwordField);
        right.add(Box.createVerticalStrut(10));
        right.add(confirmField);
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

    // ===== Custom TextField =====
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
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e) { repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(235, 245, 255));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            if (isFocusOwner()) g2.setColor(new Color(25, 118, 210));
            else if (hover) g2.setColor(new Color(100, 149, 237));
            else g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                g2.setColor(Color.GRAY);
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left+5, getHeight()/2 + g2.getFont().getSize()/2 -2);
            }
            g2.dispose();
        }
    }

    // ===== Custom PasswordField =====
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
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e) { repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(235, 245, 255));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            if (isFocusOwner()) g2.setColor(new Color(25, 118, 210));
            else if (hover) g2.setColor(new Color(100, 149, 237));
            else g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            super.paintComponent(g);
            if (getPassword().length == 0 && !isFocusOwner()) {
                g2.setColor(Color.GRAY);
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left+5, getHeight()/2 + g2.getFont().getSize()/2 -2);
            }
            g2.dispose();
        }
    }

    // ===== Custom Button Style =====
    static class WebButtonStyle {
        public static void apply(JButton b, Font font, Color normal, Color hover) {
            b.setFont(font);
            b.setFocusPainted(false);
            b.setForeground(Color.WHITE);
            b.setBackground(normal);
            b.setOpaque(false);
            b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setPreferredSize(new Dimension(120, 40));
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

    // ===== Rounded Border =====
    static class RoundedBorder extends javax.swing.border.LineBorder {
        public RoundedBorder(int radius) { super(Color.LIGHT_GRAY, 1, true); }
    }

    
}

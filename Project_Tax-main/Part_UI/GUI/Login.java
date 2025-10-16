//package Part_UI.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Login extends JFrame {

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font thaiFontBold = new Font("Tahoma", Font.BOLD, 16);
        Font thaiFontNormal = new Font("Tahoma", Font.PLAIN, 15);
        Font thaiFontSmall = new Font("Tahoma", Font.PLAIN, 13);

        JPanel container = new JPanel();
        container.setBackground(new Color(246, 249, 255));
        container.setLayout(new GridLayout(1, 2, 20, 0));
        container.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ===== Left Panel =====
        JPanel left = new JPanel(new GridLayout(3, 1, 0, 15));
        left.setOpaque(false);

        String[][] boxes = {
                {"ID", "เลขประจำตัวผู้เสียภาษีคือเลขบัตรประชาชน 13 หลัก"},
                {"เข้าสู่ระบบ", "กรอกเลขประจำตัวและรหัสผ่าน คือคนที่เคยเข้ามาใช้งานแล้ว"},
                {"ลงทะเบียน", "คนที่ยังไม่เคยใช้งาน กดลงทะเบียน"}
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

        // ===== Right Panel =====
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(232, 241, 251));
        right.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel header = new JLabel("เข้าสู่ระบบ", SwingConstants.CENTER);
        header.setFont(thaiFontBold);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        WebTextField ID = new WebTextField("ID", thaiFontNormal);
        WebPasswordField password = new WebPasswordField("รหัสผ่าน", thaiFontNormal);
        ID.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel forgotPanel = new JPanel(new BorderLayout());
        forgotPanel.setOpaque(false);
        JLabel forgot = new JLabel("ลืมรหัสผ่าน");
        forgot.setForeground(new Color(25, 118, 210));
        forgot.setFont(thaiFontSmall);
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPanel.add(forgot, BorderLayout.EAST);
        forgotPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();          
                new Password();     
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forgot.setForeground(new Color(19, 61, 132));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgot.setForeground(new Color(25, 118, 210));
            }
        });

        JPanel agreePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        agreePanel.setOpaque(false);
        JCheckBox agree = new JCheckBox("ตกลงยอมรับเงื่อนไขการใช้งาน และนโยบายความเป็นส่วนตัว");
        agree.setFont(thaiFontNormal);
        agreePanel.add(agree);

        JButton loginBtn = new JButton("เข้าสู่ระบบ");
        JButton registerBtn = new JButton("ลงทะเบียน");
        WebButtonStyle.apply(loginBtn, thaiFontNormal, new Color(13, 43, 97), new Color(19, 61, 132));
        WebButtonStyle.apply(registerBtn, thaiFontNormal, new Color(13, 43, 97), new Color(19, 61, 132));

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterUI(); 
        });
        
        loginBtn.addActionListener(e -> {
            String inputID = ID.getText().trim();
            String inputPass = new String(password.getPassword());

            if (inputID.isEmpty() || inputPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "กรุณากรอก ID และรหัสผ่าน", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!agree.isSelected()) {
                JOptionPane.showMessageDialog(this, "คุณต้องยอมรับเงื่อนไขก่อนเข้าสู่ระบบ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // โหมด admin พิเศษ
            if (inputID.equals("admin111") && inputPass.equals("AdminPass")) {
                dispose();
                new AdminPanelUI();
                return;
            }

            // โหลด users.csv
            Map<String, String> userMap = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
                String line;
                String currentID = null;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("ID: ")) {
                        currentID = line.substring(4).trim();
                    } else if (line.startsWith("รหัสผ่าน: ") && currentID != null) {
                        String pass = line.substring(9).trim();
                        userMap.put(currentID, pass);
                        currentID = null;
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "ไม่สามารถอ่านไฟล์ผู้ใช้ได้", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userMap.containsKey(inputID) && userMap.get(inputID).equals(inputPass)) {
                dispose();
                new Income(inputID).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "ID หรือ รหัสผ่านไม่ถูกต้อง", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);

        right.add(header);
        right.add(ID);
        right.add(Box.createVerticalStrut(10));
        right.add(password);
        right.add(forgotPanel);
        right.add(agreePanel);
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

      
       
       
       
       
       
       
        //ทำเล่่นๆ
            password.addActionListener(e -> {
            String passInput = new String(password.getPassword());
            if (passInput.equalsIgnoreCase("devmode")) {
                new DevModeWindow(); // เปิดโหมดลับด้วยคำว่า devmode
            }
        });
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
        public RoundedBorder(int radius) {
            super(Color.LIGHT_GRAY, 1, true);
        }
    }
}

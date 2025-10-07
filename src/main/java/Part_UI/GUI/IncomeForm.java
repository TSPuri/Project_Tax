package Part_UI.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;

public class IncomeForm extends JFrame {

    public IncomeForm() {
        setTitle("กรอกข้อมูลเพื่อคำนวณภาษี");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(210, 230, 240));
        setLayout(new BorderLayout());

        // ===== แถบบนซ้าย: ปุ่มย้อนกลับ =====
        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        backButton.setBackground(new Color(230, 230, 230));
        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeft.setOpaque(false);
        topLeft.add(backButton);
        add(topLeft, BorderLayout.NORTH);

        // ===== ศูนย์กลาง: หัวข้อใหญ่ + กล่องฟอร์ม =====
        JPanel centerCol = new JPanel();
        centerCol.setOpaque(false);
        centerCol.setLayout(new BoxLayout(centerCol, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("กรอกข้อมูลเพื่อคำนวณภาษี", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleWrap.setOpaque(false);
        titleWrap.add(titleLabel);

        // กล่องฟอร์มสีขาว
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 20));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(40, 80, 40, 80));
        formPanel.setPreferredSize(new Dimension(650, 320));

        Font labelFont = new Font("Tahoma", Font.BOLD, 15);
        Font fieldFont = new Font("Tahoma", Font.PLAIN, 15);

        JLabel yearLabel   = new JLabel("ปีที่คำนวณภาษี"); yearLabel.setFont(labelFont);
        JLabel salaryLabel = new JLabel("เงินเดือน (บาท)"); salaryLabel.setFont(labelFont);
        JLabel bonusLabel  = new JLabel("โบนัสต่อปี (บาท)"); bonusLabel.setFont(labelFont);
        JLabel otherLabel  = new JLabel("รายได้อื่นๆ เช่น ฟรีแลนซ์ต่อปี (บาท)"); otherLabel.setFont(labelFont);

        JTextField yearField   = new JTextField();   yearField.setFont(fieldFont);
        JTextField salaryField = new JTextField();   salaryField.setFont(fieldFont);
        JTextField bonusField  = new JTextField();   bonusField.setFont(fieldFont);
        JTextField otherField  = new JTextField();   otherField.setFont(fieldFont);

        // 🔹 เพิ่มส่วนนี้: บังคับกรอกได้เฉพาะตัวเลข
        ((AbstractDocument) yearField.getDocument()).setDocumentFilter(new NumericFilter(4)); // จำกัดแค่ตัวเลข 4 หลัก
        ((AbstractDocument) salaryField.getDocument()).setDocumentFilter(new NumericFilter(-1)); // -1 หมายถึงไม่จำกัดความยาว
        ((AbstractDocument) bonusField.getDocument()).setDocumentFilter(new NumericFilter(-1));
        ((AbstractDocument) otherField.getDocument()).setDocumentFilter(new NumericFilter(-1));

        formPanel.add(yearLabel);   formPanel.add(yearField);
        formPanel.add(salaryLabel); formPanel.add(salaryField);
        formPanel.add(bonusLabel);  formPanel.add(bonusField);
        formPanel.add(otherLabel);  formPanel.add(otherField);

        JPanel formWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formWrap.setOpaque(false);
        formWrap.add(formPanel);

        centerCol.add(Box.createVerticalStrut(10));
        centerCol.add(titleWrap);
        centerCol.add(Box.createVerticalStrut(10));
        centerCol.add(formWrap);
        add(centerCol, BorderLayout.CENTER);

        // ===== ปุ่มด้านล่าง =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        bottom.setOpaque(false);

        JButton resetButton = new JButton("ล้างข้อมูล");
        resetButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        resetButton.setBackground(new Color(255, 204, 0));

        JButton nextButton = new JButton("ถัดไป");
        nextButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        nextButton.setBackground(new Color(10, 20, 60));
        nextButton.setForeground(Color.BLACK);

        bottom.add(resetButton);
        bottom.add(nextButton);
        add(bottom, BorderLayout.SOUTH);

        // ===== Events =====
        resetButton.addActionListener(e -> {
            yearField.setText("");
            salaryField.setText("");
            bonusField.setText("");
            otherField.setText("");
        });
        nextButton.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "ข้อมูลถูกบันทึกแล้ว!", "Success", JOptionPane.INFORMATION_MESSAGE)
        );

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IncomeForm::new);
    }
}


class NumericFilter extends DocumentFilter {
    private int maxLength;

    public NumericFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        if (string.isEmpty() || string.matches("[0-9]+")) { // อนุญาตให้เป็นค่าว่างด้วย
            if (maxLength < 0 || fb.getDocument().getLength() + string.length() <= maxLength)
                super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        if (text.isEmpty() || text.matches("[0-9]+")) { // อนุญาตให้ล้างค่าได้
            if (maxLength < 0 || fb.getDocument().getLength() - length + text.length() <= maxLength)
                super.replace(fb, offset, length, text, attrs);
        }
    }
}
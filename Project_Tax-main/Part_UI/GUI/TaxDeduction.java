// TaxDeduction.java
import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TaxDeduction extends JFrame {

    private final Font thaiFont = new Font("Tahoma", Font.PLAIN, 15);
    private JComboBox<String> maritalBox;
    private JCheckBox fatherBox, motherBox, disParentBox, disRelBox;
    private JTextField providentField, socialField, homeLoanField, lifeIns, healthIns, otherFund;
    private JPanel childListPanel;

    private final String userId, year;
    private final double income, bonus, other;

    public TaxDeduction(String userId, String year, double income, double bonus, double other) {
        this.userId = userId; this.year = year; this.income = income; this.bonus = bonus; this.other = other;
        initUI();
    }

    private void initUI() {
        setTitle("กรอกข้อมูลเพื่อลดหย่อนภาษี");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 850);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(210, 230, 240));
        setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout()); topPanel.setOpaque(false);
        JButton backButton = new JButton("ย้อนกลับ"); backButton.setFont(thaiFont);

         backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == backButton) {
                    dispose();
                    new Income().setVisible(true);;
                }
            }
            
         });
  
        backButton.setBackground(new Color(200, 220, 210));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel titleLabel = new JLabel("กรอกข้อมูลเพื่อลดหย่อนภาษี", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        JPanel topCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15)); topCenter.setOpaque(false);
        topCenter.add(titleLabel);
        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10)); topLeft.setOpaque(false); topLeft.add(backButton);
        topPanel.add(topLeft, BorderLayout.WEST); topPanel.add(topCenter, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Family Panel
        JPanel familyPanel = createRoundedPanel("ครอบครัว");
        maritalBox = comboBox(new String[]{"โสด","สมรส","หย่า"});
        familyPanel.add(labelWithComponent("สถานะสมรส", maritalBox));
        fatherBox = new JCheckBox("บิดา"); fatherBox.setFont(thaiFont);
        motherBox = new JCheckBox("มารดา"); motherBox.setFont(thaiFont);
        JPanel parentGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3)); parentGroup.setOpaque(false);
        parentGroup.add(fatherBox); parentGroup.add(motherBox);
        familyPanel.add(labelWithComponent("ลดหย่อนบิดา-มารดา", parentGroup));
        disParentBox = new JCheckBox("บิดา/มารดาผู้พิการ"); disParentBox.setFont(thaiFont);
        disRelBox = new JCheckBox("ญาติผู้พิการ"); disRelBox.setFont(thaiFont);
        JPanel disGroup = new JPanel(new FlowLayout(FlowLayout.LEFT,10,3)); disGroup.setOpaque(false);
        disGroup.add(disParentBox); disGroup.add(disRelBox);
        familyPanel.add(labelWithComponent("ลดหย่อนผู้พิการ/ทุพพลภาพ", disGroup));

        // Child Panel
        childListPanel = new JPanel(); childListPanel.setLayout(new BoxLayout(childListPanel, BoxLayout.Y_AXIS)); childListPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(childListPanel);
        scrollPane.setPreferredSize(new Dimension(250, 180));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton addChildBtn = new JButton("+ เพิ่มบุตร"); addChildBtn.setFont(thaiFont); addChildBtn.setBackground(new Color(180,220,250));
        addChildBtn.addActionListener(e -> { childListPanel.add(createChildPanel(childListPanel)); childListPanel.revalidate(); childListPanel.repaint(); });
        familyPanel.add(labelWithComponent("ข้อมูลบุตร", scrollPane));
        familyPanel.add(addChildBtn);

        // Fund Panel
        JPanel fundPanel = createRoundedPanel("กองทุนและประกันสังคม");
        providentField = textFieldNumbersOnly(10); socialField = textFieldNumbersOnly(10); homeLoanField = textFieldNumbersOnly(10);
        fundPanel.add(labelWithComponent("กองทุนสำรองเลี้ยงชีพ (บาท)", providentField));
        fundPanel.add(labelWithComponent("ประกันสังคม (บาท)", socialField));
        fundPanel.add(labelWithComponent("ดอกเบี้ยที่อยู่อาศัย (บาท)", homeLoanField));

        // Insurance Panel
        JPanel insurancePanel = createRoundedPanel("ประกันและกองทุนอื่น ๆ");
        lifeIns = textFieldNumbersOnly(10); healthIns = textFieldNumbersOnly(10); otherFund = textFieldNumbersOnly(10);
        insurancePanel.add(labelWithComponent("เบี้ยประกันชีวิต (บาท)", lifeIns));
        insurancePanel.add(labelWithComponent("เบี้ยประกันสุขภาพ (บาท)", healthIns));
        insurancePanel.add(labelWithComponent("กองทุนอื่น ๆ (บาท)", otherFund));

        JPanel centerPanel = new JPanel(new GridLayout(1,3,20,0)); centerPanel.setOpaque(false);
        centerPanel.add(familyPanel); centerPanel.add(fundPanel); centerPanel.add(insurancePanel);
        JPanel centerWrapper = new JPanel(new GridBagLayout()); centerWrapper.setOpaque(false); centerWrapper.add(centerPanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);

        JButton clearBtn = new JButton("ล้างข้อมูล"); clearBtn.setFont(thaiFont); clearBtn.setBackground(new Color(255,200,0));
        JButton calcBtn = new JButton("คำนวณ"); calcBtn.setFont(thaiFont); calcBtn.setBackground(new Color(30,60,120)); calcBtn.setForeground(Color.WHITE);
        clearBtn.addActionListener(e -> clearFields(getContentPane()));
        calcBtn.addActionListener(e -> calculateTaxAndSaveCSV());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,50,15)); buttonPanel.setOpaque(false);
        buttonPanel.add(clearBtn); buttonPanel.add(calcBtn); add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void calculateTaxAndSaveCSV() {
        try {
            double totalIncome = (income+other)*12 + bonus;
            double provident = parseDoubleSafe(providentField.getText());
            double social = parseDoubleSafe(socialField.getText());
            double homeLoan = parseDoubleSafe(homeLoanField.getText());
            double life = parseDoubleSafe(lifeIns.getText());
            double health = parseDoubleSafe(healthIns.getText());
            double otherFundVal = parseDoubleSafe(otherFund.getText());

            int childDeduction = 0;
            Component[] childs = childListPanel.getComponents();
            for (Component comp : childs) {
                if (!(comp instanceof JPanel)) continue;
                JPanel panel = (JPanel) comp;
                String name=""; int birthYear=0; boolean studying=false, adopted=false;
                Component[] comps = panel.getComponents();
                for (Component c : comps) {
                    if (c instanceof JTextField tf) {
                        if (tf.getColumns()==8) name=tf.getText().trim();
                        if (tf.getColumns()==5 && !tf.getText().trim().isEmpty()) birthYear = parseIntSafe(tf.getText().trim());
                    } else if (c instanceof JCheckBox cb) {
                        if (cb.getText().contains("ศึกษา")) studying=cb.isSelected();
                        if (cb.getText().contains("บุญธรรม")) adopted=cb.isSelected();
                    }
                }
                if (!name.isEmpty() && birthYear>0) {
                    int age=2568-birthYear;
                    if (!adopted) childDeduction += (age<=20 || studying)?60000:30000;
                    else childDeduction += (age<=20 || studying)?30000:0;
                }
            }

            // คำนวณค่าลดหย่อนรวม
            double totalDeduction = provident + social + homeLoan + life + health + otherFundVal;
            totalDeduction += fatherBox.isSelected()?30000:0;
            totalDeduction += motherBox.isSelected()?30000:0;
            totalDeduction += disParentBox.isSelected()?60000:0;
            totalDeduction += disRelBox.isSelected()?60000:0;
            totalDeduction += childDeduction;

            double netIncome = Math.max(totalIncome - totalDeduction,0);
            double tax = Part_Data.TaxCalculation.calculateTax(netIncome, Integer.parseInt(year));

            // บันทึก user_data.csv
            File userFile = new File("Part_Data/user_data.csv");
            File tempFile = new File("Part_Data/temp.csv");
            boolean exist = userFile.exists();
            try (BufferedReader br = exist? new BufferedReader(new FileReader(userFile)) : null;
                 PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                if (!exist) pw.println("UserID,Year,Income,OtherIncome,Bonus,Status,Father,Mother,DisParent,DisRel,ChildDeduction,Provident,Social,HomeLoan,LifeIns,HealthIns,OtherFund");
                else {
                    String line; boolean updated=false;
                    while ((line=br.readLine())!=null) {
                        if (line.startsWith(userId+","+year)) { // แทนที่บรรทัดเก่า
                            pw.println(userId+","+year+","+income+","+other+","+bonus+","+maritalBox.getSelectedItem()+","+
                                    (fatherBox.isSelected()?30000:0)+","+(motherBox.isSelected()?30000:0)+","+
                                    (disParentBox.isSelected()?60000:0)+","+(disRelBox.isSelected()?60000:0)+","+
                                    childDeduction+","+ (int)provident+","+ (int)social+","+ (int)homeLoan+","+
                                    (int)life+","+ (int)health+","+ (int)otherFundVal);
                            updated=true;
                        } else pw.println(line);
                    }
                    if (!updated) pw.println(userId+","+year+","+income+","+other+","+bonus+","+maritalBox.getSelectedItem()+","+
                            (fatherBox.isSelected()?30000:0)+","+(motherBox.isSelected()?30000:0)+","+
                            (disParentBox.isSelected()?60000:0)+","+(disRelBox.isSelected()?60000:0)+","+
                            childDeduction+","+ (int)provident+","+ (int)social+","+ (int)homeLoan+","+
                            (int)life+","+ (int)health+","+ (int)otherFundVal);
                }
            }
            userFile.delete(); tempFile.renameTo(userFile);

            // บันทึก taxhistory.csv แบบเรียง
            File taxFile = new File("Part_Data/taxhistory.csv");
            File tempTax = new File("Part_Data/temp_tax.csv");
            exist = taxFile.exists();
            String[][] buffer = new String[1000][6]; 
            int index=0;
            if (!exist) buffer[index++] = new String[]{"UserID","Year","รายได้รวม","ค่าลดหย่อน","รายได้สุทธิ","ภาษีที่ต้องจ่าย"};
            else try (BufferedReader br = new BufferedReader(new FileReader(taxFile))) {
                String line; while ((line=br.readLine())!=null && index<1000) buffer[index++] = line.split(",");
            }
            // แทนที่/เพิ่มข้อมูลใหม่
            boolean found=false;
            for (int i=1;i<index;i++) {
                if (buffer[i][0].equals(userId) && buffer[i][1].equals(year)) {
                    buffer[i] = new String[]{userId,year,String.valueOf(totalIncome),String.valueOf((int)totalDeduction),String.valueOf((int)netIncome),String.valueOf((int)tax)};
                    found=true; break;
                }
            }
            

            if (!found && index<1000) buffer[index++] = new String[]{userId,year,String.valueOf(totalIncome),String.valueOf((int)totalDeduction),String.valueOf((int)netIncome),String.valueOf((int)tax)};
            // sort
            for (int i=1;i<index-1;i++) for (int j=i+1;j<index;j++) {
                int cmp = buffer[i][0].compareTo(buffer[j][0]);
                if (cmp>0 || (cmp==0 && Integer.parseInt(buffer[i][1])>Integer.parseInt(buffer[j][1]))) {
                    String[] tmp=buffer[i]; buffer[i]=buffer[j]; buffer[j]=tmp;
                }
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(tempTax))) {
                for (int i=0;i<index;i++) pw.println(String.join(",",buffer[i]));
            }
            taxFile.delete(); tempTax.renameTo(taxFile);

            JOptionPane.showMessageDialog(this,
                    "บันทึกและคำนวณสำเร็จ!\nรายได้รวม: "+String.format("%,.2f",totalIncome)+
                            " บาท\nค่าลดหย่อนบุตร: "+childDeduction+
                            " บาท\nภาษีที่ต้องชำระ: "+String.format("%,.2f",tax)+" บาท",
                    "ผลลัพธ์", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose(); // ปิดหน้าปัจจุบัน
                    new Result(userId).setVisible(true);
        } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this,"เกิดข้อผิดพลาด","Error",JOptionPane.ERROR_MESSAGE);}
    }

    private double parseDoubleSafe(String s) { try { return s==null||s.isEmpty()?0:Double.parseDouble(s.trim()); } catch(Exception e){return 0;} }
    private int parseIntSafe(String s) { try { return s==null||s.isEmpty()?0:Integer.parseInt(s.trim()); } catch(Exception e){return 0;} }

    private void clearFields(Component comp){
        if(comp instanceof JTextField) ((JTextField)comp).setText("");
        else if(comp instanceof JComboBox) ((JComboBox<?>)comp).setSelectedIndex(0);
        else if(comp instanceof JCheckBox) ((JCheckBox)comp).setSelected(false);
        else if(comp instanceof Container) for(Component c:((Container)comp).getComponents()) clearFields(c);
    }

    private JComboBox<String> comboBox(String[] items){ JComboBox<String> box = new JComboBox<>(items); box.setFont(thaiFont); return box; }
    private JTextField textFieldNumbersOnly(int cols){ JTextField tf = new JTextField(cols); tf.setFont(thaiFont);
        tf.addKeyListener(new KeyAdapter(){ public void keyTyped(KeyEvent e){ if(!Character.isDigit(e.getKeyChar())) e.consume();}});
        return tf;
    }
    private JPanel createRoundedPanel(String title){ JPanel panel = new JPanel(); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10,10,10,10),new TitledBorder(null,title,TitledBorder.CENTER,TitledBorder.TOP,thaiFont)));
        panel.setPreferredSize(new Dimension(320,680)); return panel;
    }
    private JPanel labelWithComponent(String labelText, JComponent comp){ JPanel panel = new JPanel(new GridBagLayout()); panel.setOpaque(false);
        GridBagConstraints gbc=new GridBagConstraints(); gbc.gridx=0; gbc.gridy=0; gbc.anchor=GridBagConstraints.WEST; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1.0; gbc.insets=new Insets(5,10,3,10);
        JLabel label=new JLabel(labelText); label.setFont(thaiFont); panel.add(label,gbc);
        gbc.gridy++; gbc.insets=new Insets(3,10,8,10); panel.add(comp,gbc); return panel;
    }
    private JPanel createChildPanel(JPanel container){
        JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,3)); panel.setOpaque(false);
        JTextField nameField=new JTextField(8); nameField.setFont(thaiFont);
        JTextField birthYearField=textFieldNumbersOnly(5);
        JCheckBox studyingBox=new JCheckBox("กำลังศึกษาอยู่"); studyingBox.setFont(thaiFont);
        JCheckBox adoptedBox=new JCheckBox("บุตรบุญธรรม"); adoptedBox.setFont(thaiFont);
        JButton removeBtn=new JButton("– ลบ"); removeBtn.setFont(thaiFont); removeBtn.setBackground(new Color(255,180,180));
        removeBtn.addActionListener(e->{container.remove(panel); container.revalidate(); container.repaint();});
        panel.add(new JLabel("ชื่อ:")); panel.add(nameField);
        panel.add(new JLabel("ปีเกิด:")); panel.add(birthYearField);
        panel.add(studyingBox); panel.add(adoptedBox); panel.add(removeBtn);
        return panel;
    }

    public static void main(String[] args){
        new TaxDeduction("1234567890000","2568",50000,20000,30000);
    }
}

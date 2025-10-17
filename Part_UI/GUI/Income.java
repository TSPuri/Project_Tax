import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

/**
 * Income Form - สำหรับกรอกข้อมูลรายได้
 * ผู้พัฒนา: Pitchanan
 */
public class Income extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Income.class.getName());
    private String userId; // เพิ่มไว้สำหรับบันทึก user

    public Income(String userId) {
        this.userId = userId;
        initComponents();
        inputyear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                restrictToNumbers(evt);
            }
        });

        inputsalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                restrictToNumbers(evt);
            }
        });

        inputBonus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                restrictToNumbers(evt);
            }
        });

        AnotherIncom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                restrictToNumbers(evt);
            }
        });
    }

    public Income() {
        this.userId = "defaultUser"; // fallback ถ้าไม่ได้มาจากหน้า Login
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        inputyear = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        inputsalary = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        inputBonus = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        AnotherIncom = new javax.swing.JTextField();
        NextButton = new javax.swing.JButton();
        ClearData = new javax.swing.JButton();
        Backbutton = new javax.swing.JButton();
        explaneLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Income");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 224, 231));

        jLabel1.setFont(new java.awt.Font("TH SarabunPSK", 0, 24));
        jLabel1.setText("ปีที่คำนวณภาษี");

        jLabel3.setFont(new java.awt.Font("TH SarabunPSK", 0, 24));
        jLabel3.setText("เงินเดือน (บาท)");

        jLabel4.setFont(new java.awt.Font("TH SarabunPSK", 0, 24));
        jLabel4.setText("โบนัส (บาท)");

        jLabel5.setFont(new java.awt.Font("TH SarabunPSK", 0, 24));
        jLabel5.setText("รายได้อื่นๆ เช่น การขายของออนไลน์, รับจ้างฟรีแลนซ์ (บาท)");

        NextButton.setBackground(new java.awt.Color(153, 255, 153));
        NextButton.setFont(new java.awt.Font("TH SarabunPSK", 0, 24));
        NextButton.setText("ถัดไป");
        NextButton.addActionListener(this::NextButtonActionPerformed);

        NextButton.addActionListener(e -> {
            String year = inputyear.getText().trim();
            String salary = inputsalary.getText().trim();
            String bonus = inputBonus.getText().trim();
            String another = AnotherIncom.getText().trim();

            if (year.isEmpty() || salary.isEmpty() || bonus.isEmpty() || another.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "กรุณากรอกข้อมูลให้ครบทุกช่องก่อนดำเนินการต่อ",
                    "คำเตือน",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // >>>> เพิ่มฟังก์ชันบันทึกข้อมูล CSV <<<<
            saveIncomeData(year, salary, bonus, another);

            dispose();
            new TaxDeduction(userId, year).setVisible(true);
        });

        ClearData.setBackground(new java.awt.Color(255, 255, 153));
        ClearData.setFont(new java.awt.Font("TH SarabunPSK", 0, 24));
        ClearData.setText("ล้างข้อมูล");
        ClearData.addActionListener(this::ClearDataActionPerformed);

        Backbutton.setFont(new java.awt.Font("TH SarabunPSK", 0, 18));
        Backbutton.setText("ย้อนกลับ");
        Backbutton.addActionListener(this::BackbuttonActionPerformed);
        Backbutton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        explaneLabel.setFont(new java.awt.Font("TH SarabunPSK", 0, 24));
        explaneLabel.setText("กรอกข้อมูลเพื่อคำนวณภาษี");

        inputyear.addActionListener(this::inputyearActionPerformed);
        inputBonus.addActionListener(this::inputBonusActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(129, 129, 129)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel4))
                            .addGap(224, 224, 224)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel3)))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(150, 150, 150)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(inputBonus, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(inputyear, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(ClearData, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(78, 78, 78)
                                    .addComponent(inputsalary, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(79, 79, 79)
                                    .addComponent(AnotherIncom, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(105, 105, 105)
                                    .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addContainerGap(81, Short.MAX_VALUE))
        );

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel3))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputyear, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputsalary, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputBonus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AnotherIncom, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(113, 113, 113)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ClearData, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(NextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(Backbutton)
                    .addGap(286, 286, 286)
                    .addComponent(explaneLabel)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
        );

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Backbutton)
                        .addComponent(explaneLabel))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void restrictToNumbers(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }

    private void saveIncomeData(String year, String salary, String bonus, String another) {
    String fileName = "user_data.csv";
    File file = new File(fileName);

    List<String> lines = new ArrayList<>();
    boolean found = false;

    // อ่านข้อมูลทั้งหมดก่อน
    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // ถ้าเป็น header
                if (parts[0].equals("UserID")) {
                    lines.add(line);
                    continue;
                }

                // ถ้าเจอ userId + year เดิม → เขียนทับ
                if (parts.length >= 17 && parts[0].equals(userId) && parts[1].equals(year)) {
                    String newLine = String.join(",",
                        userId, year, salary, bonus, another, "โสด", "-", "-", "-", "-", "0",
                        "0", "0", "0", "0", "0", "0"
                    );
                    lines.add(newLine);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการอ่านไฟล์", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // ถ้าไม่เจอเลย → เพิ่มบรรทัดใหม่
    if (!found) {
        // ถ้าไฟล์ยังไม่มี header ให้เพิ่มก่อน
        if (lines.isEmpty()) {
            lines.add("UserID,Year,ประเภทงาน,เงินเดือน,โบนัส,สถานะสมรส,บิดา,มารดา,ผู้พิการบิดา,ผู้พิการญาติ,จำนวนบุตร,กองทุนสำรองเลี้ยงชีพ,ประกันสังคม,ดอกเบี้ยที่อยู่อาศัย,เบี้ยประกันชีวิต,เบี้ยประกันสุขภาพ,กองทุนอื่นๆ");
        }
        String newLine = String.join(",",
            userId, year, "รายได้", salary, bonus, "โสด", "-", "-", "-", "-", "0",
            "0", "0", "0", "0", "0", "0"
        );
        lines.add(newLine);
    }

    // เขียนกลับไปทั้งไฟล์
    try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
        for (String l : lines) {
            pw.println(l);
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการเขียนไฟล์", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
    }
}


    private void BackbuttonActionPerformed(java.awt.event.ActionEvent evt) {}
    private void inputBonusActionPerformed(java.awt.event.ActionEvent evt) {}
    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt) {}
    private void ClearDataActionPerformed(java.awt.event.ActionEvent evt) {
        inputyear.setText("");
        inputsalary.setText("");
        inputBonus.setText("");
        AnotherIncom.setText("");
    }
    private void inputyearActionPerformed(java.awt.event.ActionEvent evt) {}

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new Income("defaultUser").setVisible(true));
    }

    private javax.swing.JTextField AnotherIncom;
    private javax.swing.JButton Backbutton;
    private javax.swing.JButton ClearData;
    private javax.swing.JButton NextButton;
    private javax.swing.JLabel explaneLabel;
    private javax.swing.JTextField inputBonus;
    private javax.swing.JTextField inputsalary;
    private javax.swing.JTextField inputyear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
}

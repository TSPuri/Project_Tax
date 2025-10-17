
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Final extends JFrame implements ActionListener,KeyListener{
    JLabel p,u,usernameLengthLabel, passwordLengthLabel;
    JTextField us, ps;
    JComboBox<String> prcombo;
    JRadioButton m, fm;
    public Final(){
        JFrame f = new JFrame("Login");//ข้อความ
        Container cp = f.getContentPane();//กล่อง
        cp.setLayout(null);

        u=new JLabel("USER: ");
        u.setBounds(86,5,100,30);
        us = new JTextField(); 
        us.setBounds(136,5,300,30);
         
        p=new JLabel("PASSWORD: ");
        p.setBounds(50,55,100,30);
        ps = new JTextField();
        ps.setBounds(136,55,300,30);
        // JLabel สำหรับแสดงจำนวนตัวอักษร
        usernameLengthLabel = new JLabel("Username Length: 0");
        usernameLengthLabel.setBounds(136, 35, 200, 30);

        passwordLengthLabel = new JLabel("Password Length: 0");
        passwordLengthLabel.setBounds(136, 85, 200, 30);

 
        JLabel r = new JLabel("Province: ");
        r.setBounds(66,100,100,30);
        prcombo = province_to_combobox();//เลือกปท
        prcombo.setBounds(136,100,300,30);
                
        JLabel s = new JLabel("sex: ");
        s.setBounds(86,150,100,30);  
         m = new JRadioButton("Male");//เลือกกลม
        m.setBounds(156,150,100,30);
         fm = new JRadioButton("Female");
        fm.setBounds(256,150,100,30);
            
        ButtonGroup group = new ButtonGroup();//เลือกได้แค่อันเดียว
        group.add(m);
        group.add(fm);
        
        JButton ok=new JButton("OK");//ปุ่ม
        ok.setBounds(150,200,80,30);
        JButton clean =new JButton("clean");
        clean.setBounds(250,200,80,30);
        ok.addActionListener(this);
        clean.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            us.setText("");//clearuser
            ps.setText("");// Clearpassword
            group.clearSelection();//Clearsex
            prcombo.setSelectedItem("Bangkok");//clearcombo
        }
    });
    us.addKeyListener(this);
    ps.addKeyListener(this);
        
        cp.add(u);
        cp.add(us);
        cp.add(p);
        cp.add(ps);
        cp.add(r);
        cp.add(prcombo);
        cp.add(s);
        cp.add(m);
        cp.add(fm);
        cp.add(ok);
        cp.add(clean);
        
        
                f.setSize(500,400);//ปรากฎjf
                f.setVisible(true);//copyทั้ง3บรรทัด
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        
            public  JComboBox<String> province_to_combobox() {//เปิด/linkไฟล์
                JComboBox<String> tmp = new JComboBox<>();//copyเลย
     File f = null;//ติดแดงใช้ตัวช่วยแล้วใส่ทั้งก้อนลงไป
     FileReader fr = null;
     BufferedReader br = null;
     try {
         f = new File("./File/Thailand_PRV.csv");
         fr = new FileReader(f);
         br = new BufferedReader(fr);
         String s;
         br.readLine();//ข้ามหัวข้อไฟล์
         while ((s = br.readLine()) != null) {
             String arr[] = s.split(",");
             tmp.addItem(arr[1]);  
         }
         tmp.setSelectedItem("Bangkok");
     } catch (Exception e) {
         System.out.println(e);
     } finally {
         try {
             if (br != null) br.close();
             if (fr != null) fr.close();
         } catch (Exception e) {
             System.out.println(e);
         }
     }
     return tmp;
   }//เปิด/linkบรรทัดสุดท้าย

   boolean error;
   public void actionPerformed(ActionEvent e) {
       if (e.getActionCommand().equals("OK")) {
           String username = us.getText();  // รับค่า USER
           String password = ps.getText();  // รับค่า PASSWORD
           String province = (String) prcombo.getSelectedItem();  // รับค่าจังหวัด
           String sex = "";
            if (m.isSelected()) {
                sex = "Male";  // ถ้าเลือกเพศชาย
            } else if (fm.isSelected()) {
                sex = "Female";  // ถ้าเลือกเพศหญิง
            }

           // แสดงข้อมูลในคอนโซล
           System.out.println("User: " + username);
           System.out.println("Password: " + password);
           System.out.println("Province: " + province);
           System.out.println("Sex: " + sex);

           // เขียนข้อมูลลงในไฟล์
           try (BufferedWriter bw = new BufferedWriter(new FileWriter("./File/Text.txt", true))) 
           {
               bw.write("Username: " + username + "\n");
               bw.write("Password: " + password + "\n");
               bw.write("Province: " + province + "\n");
               bw.write("Sex: " + sex + "\n\n");
           } catch (IOException ex) {
               System.out.println(ex);
           }
           Popup("fainal");}
              }//จบบัยทึกลงไฟล์
           
           public void keyTyped(KeyEvent e) {
    if (e.getSource() == ps) {
        // อนุญาตให้พิมพ์เฉพาะตัวเลขในช่อง password
        if (!Character.isDigit(e.getKeyChar())) {
            e.consume(); // หยุดการพิมพ์หากไม่ใช่ตัวเลข
        }
    } else if (e.getSource() == us) {
        // อนุญาตให้พิมพ์เฉพาะตัวอักษรและช่องว่างในช่อง username
        if (!(Character.isAlphabetic(e.getKeyChar()) || e.getKeyChar() == ' ')) {
            e.consume(); // หยุดการพิมพ์หากไม่ใช่ตัวอักษรหรือช่องว่าง
        }
        else if (e.getSource() == us) {
            // อนุญาตให้พิมพ์เฉพาะตัวอักษรตัวใหญ่และช่องว่างในช่อง username
            char typedChar = e.getKeyChar();
            if (!(Character.isAlphabetic(typedChar) || typedChar == ' ')) {
                e.consume(); // หยุดการพิมพ์หากไม่ใช่ตัวอักษรหรือช่องว่าง
            } else {
                // แปลงตัวอักษรที่พิมพ์ให้เป็นตัวใหญ่
                char upperCaseChar = Character.toUpperCase(typedChar);
                us.setText(us.getText() + upperCaseChar); // เพิ่มตัวอักษรตัวใหญ่ที่พิมพ์เข้าไป
                e.consume(); // หยุดการพิมพ์ตัวอักษรเดิมที่เป็นตัวเล็ก
            }
        }
    }

    }


@Override
public void keyPressed(KeyEvent e) {

}

@Override
public void keyReleased(KeyEvent e) {
     if (e.getSource() == us) {// นับจำนวนตัวอักษรในช่อง username
        int length = us.getText().length();
        System.out.println("Username length: " + length); 

    } else if (e.getSource() == ps) {// นับจำนวนตัวอักษรในช่อง password
        int length = ps.getText().length();
        System.out.println("Password length: " + length); 
    }
}


private void Popup(String message) {
    // แสดงหน้าต่างข้อความที่มีปุ่ม "OK"
    JOptionPane.showMessageDialog(this, message, "ข้อความ", JOptionPane.INFORMATION_MESSAGE);
}
}

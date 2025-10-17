package Lib;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;

public class Result extends JFrame {
    public Result() {
        setTitle("Result");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        // ฟอนต์
        Font thaiFontBold = new Font("Tahoma", 0, 24);

        // JPanel ใหญ่
        JPanel bg = new JPanel();
        bg.setBackground(new Color(246, 249, 255));
        bg.setLayout(null);
        bg.setBounds(0, 0, 1200, 800); // สำคัญมาก    

        // หัว
        JLabel Texthead = new JLabel("Calculation Results");
        Texthead.setFont(new Font("Tahoma", Font.PLAIN, 32));
        Texthead.setBounds(452, 30, 400, 50);

         // JButton back
        JButton back = new JButton("ย้อนกลับ");
        back.setFont(new java.awt.Font("Tahoma", 0, 24));
        back.setBounds(25, 25, 120, 40);
        
         // JButton rekumnuan
        JButton newround = new JButton("เริ่มคำนวณใหม่");
        newround.setFont(new java.awt.Font("Tahoma", 0, 24));
        newround.setBounds(950, 25, 200, 40);

        //Jpanel สีฟ้า
        JPanel headbar = new JPanel();
        headbar.setLayout(null);
        headbar.setBounds(0, 0, 1200, 90);
        headbar.setBackground(new Color(204,229,255));
        
        // JLabel
        JLabel year = new JLabel("ปีที่คำนวณ : ");
        year.setFont(new java.awt.Font("Tahoma", 0, 24));
        year.setBounds(670, 150, 200, 40);
        JLabel totalincome = new JLabel("รายได้สุทธิ : ");
        totalincome.setFont(thaiFontBold);
        totalincome.setBounds(670, 200,200 , 40);
        JLabel cutTax = new JLabel("รายได้สุทธิ : ");
        cutTax.setFont(thaiFontBold);
        cutTax.setBounds(670,250 ,200 ,40 );
        JLabel payTax = new JLabel("ภาษีที่ต้องจ่าย : ");
        payTax.setFont(thaiFontBold);
        payTax.setBounds(670, 300, 200, 40);

        //สร้างกราฟแแท่ง
        

        // สร้างกราฟวงกลม
        PieChartPanel pieChart = new PieChartPanel("ผลการคำนวณภาษี");
        pieChart.setBounds(0, 150, 650, 300);
        pieChart.setBackground(new Color(246, 249, 255));
        
        
        // เพิ่มข้อมูล      
        pieChart.addSlice("ภาษีเงินได้บุคคลธรรมดา", 45.5, new Color(255, 99, 132));
        pieChart.addSlice("ภาษีมูลค่าเพิ่ม", 30.2, new Color(54, 162, 235));
        pieChart.addSlice("ภาษีธุรกิจเฉพาะ", 15.8, new Color(255, 205, 86));
        pieChart.addSlice("ภาษีอื่นๆ", 8.5, new Color(75, 192, 192));


        // เพิ่มลงใน bg
        headbar.add(back);
        headbar.add(Texthead);
        headbar.add(newround);
        bg.add(year);
        bg.add(pieChart);//Insert Circle Chart
        bg.add(headbar);
        bg.add(totalincome);
        bg.add(cutTax);
        bg.add(payTax);

        // เพิ่ม bg ลงใน JFrame
        add(bg);

        setVisible(true);
    }

   
    public static void main(String[] args) {
        new Result();
    }
}


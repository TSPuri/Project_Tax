
import javax.swing.*;
import javax.swing.border.Border;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;

public class Result extends JFrame {
    
    private double[] pieValues = new double[3];//ประกาศตัวแปรระดับคลาส ใช้ได้ทั้งคลาส
    private String currentUserID;
    public Result(String userID) {
        this.currentUserID = userID;
        setTitle("Result");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        // ฟอนต์
        Font thaiFontBold = new Font("Tahoma", 0, 24);

        // JPanel ใหญ่ ที่อยู่บน JFrame อีกที
        JPanel bg = new JPanel();
        bg.setBackground(new Color(240, 231, 252));
        bg.setLayout(null);
        bg.setBounds(0, 0, 1200, 800); // สำคัญมาก    

        // หัว
        JLabel Texthead = new JLabel("Calculation Results");
        Texthead.setFont(new Font("Tahoma", Font.PLAIN, 32));
        Texthead.setBounds(452, 30, 400, 50);

         // JButton back
        JButton back = new JButton("ย้อนกลับ");
        back.setFont(new java.awt.Font("Tahoma", 0, 24));
        back.setBounds(25, 25, 130, 40);
        back.setFocusPainted(false);
        back.setBorder(new kobmon(20));
        back.setContentAreaFilled(true);
        back.setOpaque(true);
        back.setBackground(new Color(238, 226, 251));
        //------------------------------------------------------//
        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new TaxDeduction(null,null);
               
            }
            
        });
    
        
        // JButton rekumnuan
        JButton newround = new JButton("เริ่มคำนวณใหม่");
        newround.setFont(new java.awt.Font("Tahoma", 0, 24));
        newround.setBounds(950, 25, 200, 40);
        newround.setFocusPainted(false);//ขอบมน ปิดเส้นกรอบตอนกด
        newround.setBorder(new kobmon(20));
        newround.setBorderPainted(true);//ปิดวาดขอบ
        newround.setContentAreaFilled(true);//มีแอ็กชั่น
        newround.setOpaque(true);
        newround.setBackground(new Color(238, 226, 251));
        newround.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            dispose();
            new Income().setVisible(true);
            }
            
        });

        //Jpanel สีฟ้า
        JPanel headbar = new JPanel();
        headbar.setLayout(null);
        headbar.setBounds(0, 0, 1200, 90);
        headbar.setBackground(new Color(182,227,245));
        
        // JLabel
        JLabel year = new JLabel("ปีที่คำนวณ : ");
        year.setFont(new java.awt.Font("Tahoma", 0, 24));
        year.setBounds(705, 150, 200, 40);
        JLabel totalincome = new JLabel("รายได้สุทธิ : ");
        totalincome.setFont(thaiFontBold);
        totalincome.setBounds(705, 200,200 , 40);
        JLabel cutTax = new JLabel("ส่วนลดหย่อน : ");
        cutTax.setFont(thaiFontBold);
        cutTax.setBounds(682,250 ,200 ,40 );
        JLabel payTax = new JLabel("ภาษีที่ต้องจ่าย : ");
        payTax.setFont(thaiFontBold);
        payTax.setBounds(670, 300, 200, 40);

        //JLabel Output
        JLabel outputyear = new JLabel("-");
        outputyear.setFont(new Font("Tahoma", Font.BOLD, 24));
        outputyear.setBounds(880, 150, 300, 40);
        //-----------------------------------------------//
        JLabel outputincome = new JLabel("-");
        outputincome.setFont(new Font("Tahoma", Font.BOLD, 24));
        outputincome.setBounds(880, 200, 300, 40);
        //-----------------------------------------------//
        JLabel outputlodyon = new JLabel("-");
        outputlodyon.setFont(new Font("Tahoma", Font.BOLD, 24));
        outputlodyon.setBounds(880, 250, 300, 40);
        //-----------------------------------------------//
        JLabel outputpay = new JLabel("-");
        outputpay.setFont(new Font("Tahoma", Font.BOLD, 24));
        outputpay.setBounds(880, 300, 300, 40);

        //ใส่ข้อมูลกราฟแท่ง
        DefaultCategoryDataset dataset = readCSV("Part_Data/taxhistory.csv",currentUserID, outputyear, outputincome, outputlodyon, outputpay);

        //สร้างกราฟแท่ง
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "ปี",
                "จำนวนเงิน (บาท)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // เซตขอบ
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(220, 220, 220)); // สีเส้นกริด
        plot.setRangeGridlinesVisible(true);


        //ปรับสเกลกราฟ
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 2000000);            // ตั้งค่าสูงสุด
        rangeAxis.setTickUnit(new NumberTickUnit(500000)); // แสดงทุก 50,000 บาท
        rangeAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 18));
        rangeAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 16));

        //สีแท่งกราฟ
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(93, 173, 226));  // ฟ้า
        renderer.setSeriesPaint(1, new Color(88, 214, 141));  // เขียว
        renderer.setSeriesPaint(2, new Color(245, 176, 65));  // เหลือง

        renderer.setBarPainter(new StandardBarPainter()); // ลบเงา
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelFont(new Font("Tahoma", Font.BOLD, 16));
        renderer.setBaseItemLabelPaint(Color.DARK_GRAY);
        renderer.setBarPainter(new StandardBarPainter());//
        renderer.setShadowVisible(false);

        barChart.getTitle().setFont(new Font("Tahoma", Font.BOLD, 22));
        barChart.setBackgroundPaint(new Color(240, 231, 252));
        

        //สร้างกราฟแท่ง
        ChartPanel chartPanel = new ChartPanel(barChart);//ย้อนหลังปี
        chartPanel.setBounds(35, 380, 1100, 370);
        chartPanel.setBackground(new Color(240, 231, 252));
        bg.add(chartPanel); //ใส่ลง bg


        // สร้างกราฟวงกลม
        PieChartPanel pieChart = new PieChartPanel("ผลการคำนวณภาษี");
        pieChart.setBounds(0, 105, 650, 300);
        pieChart.setBackground(new Color(240, 231, 252));
        
        
        // เพิ่มข้อมูล
        double[] pieValues = readPieData("Part_Data/taxhistory.csv", "9999999999999");//เรียกอ่านไฟล์

        pieChart.addSlice("คงเหลือส่วนต่าง", pieValues[0], new Color(255, 99, 132));
        pieChart.addSlice("ส่วนลดหย่อน", pieValues[1], new Color(255, 205, 86));
        pieChart.addSlice("ภาษีที่ต้องจ่าย", pieValues[2], new Color(54, 162, 235));

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
        bg.add(outputyear);
        bg.add(outputincome);
        bg.add(outputlodyon);
        bg.add(outputpay);

        // เพิ่ม bg ลงใน JFrame
        add(bg);


        setVisible(true);
    }

    private DefaultCategoryDataset readCSV(String filePath,String currentUserID,JLabel outputyear,JLabel outputincome,JLabel outputlodyon,JLabel outputpay) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String line;
    String lastYear = null;
    String[] lastReccord = null;

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length >= 6 && values[0].trim().equals(currentUserID)) {
                String category = values[1].trim();

                if(lastYear == null || Integer.parseInt(category) > Integer.parseInt(lastYear)){
                    lastYear = category;
                    lastReccord = values; 
                }
                

                double totalincome = Double.parseDouble(values[2].trim());
                double lodyon = Double.parseDouble(values[3].trim());
                double finalincome = Double.parseDouble(values[4].trim());

                dataset.addValue(totalincome, "รายได้", category);
                dataset.addValue(lodyon, "ส่วนลดหย่อน", category);
                dataset.addValue(finalincome, "รายได้สุทธิ", category);

             
            }
        }
        if (lastReccord != null) {
            outputyear.setText(lastYear);

            double totalIncome = Double.parseDouble(lastReccord[2].trim());
            double lodyon = Double.parseDouble(lastReccord[3].trim());
            double taxPay = Double.parseDouble(lastReccord[4].trim());

            outputincome.setText(String.format("%,.2f บาท", totalIncome));
            outputlodyon.setText(String.format("%,.2f บาท", lodyon));
            outputpay.setText(String.format("%,.2f บาท", taxPay));

            // คำนวณเปอร์เซ็นต์ 
            double sum = lodyon + taxPay + (totalIncome - lodyon - taxPay);
            if (sum == 0) sum = 1;

            double remaining = (totalIncome - lodyon - taxPay) / sum * 100;
            double deductionPercent = lodyon / sum * 100;
            double taxPercent = taxPay / sum * 100;

            //เอาใส่ตัวแปร
            pieValues = new double[]{remaining, deductionPercent, taxPercent};
        }
        

    } catch (Exception e) {
        e.printStackTrace();
    }

    return dataset;
}


    //ใสข้อมูลแผนภูมิวงกลม คล้ายๆกับแผนภูมแท่ง
    private double[] readPieData(String filePath, String targetID) {
    double totalIncome = 0;
    double lodyon = 0;
    double taxPay = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        br.readLine(); // ข้ามบรรทัดหัวตาราง

        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");

            if (values.length >= 6 && values[0].trim().equals(targetID)) {
                totalIncome = Double.parseDouble(values[2].trim());
                lodyon = Double.parseDouble(values[3].trim());
                taxPay = Double.parseDouble(values[4].trim());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // คำนวณเปอร์เซ็นต์
    double sum = lodyon + taxPay + (totalIncome - lodyon - taxPay);
    if (sum == 0) sum = 1; // ป้องกันหารศูนย์

    double remaining = (totalIncome - lodyon - taxPay) / sum * 100;
    double deductionPercent = lodyon / sum * 100;
    double taxPercent = taxPay / sum * 100;

    return new double[]{remaining, deductionPercent, taxPercent};
}

    public class kobmon implements Border {//เอาไว้ใส่ขอบมน

        private int radius;

        kobmon(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
           return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    
        
    }

   
    public static void main(String[] args) {
        new Result(null);
    }
}


package Part_Data;
import java.io.*;
import java.util.*;

public class TaxCalculation {

    // ===== โครงสร้างเก็บขั้นบันไดภาษี =====
    static class TaxBracket {
        double min, max, rate;
        TaxBracket(double min, double max, double rate) {
            this.min = min;
            this.max = max;
            this.rate = rate;
        }
    }

    //import file
    public static List<TaxBracket> loadTaxBrackets(int year) {
        List<TaxBracket> brackets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("tax_data.csv"))) {
            String line = br.readLine(); // ข้าม header
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 5) continue;

                int y = Integer.parseInt(p[0]);
                if (y == year) {
                    double min = Double.parseDouble(p[2]);
                    double max = (p[3].isEmpty()) ? Double.MAX_VALUE : Double.parseDouble(p[3]);
                    double rate = Double.parseDouble(p[4]) / 100.0; // จาก % → อัตรา
                    brackets.add(new TaxBracket(min, max, rate));
                }
            }
        } catch (IOException e) {
            System.err.println("ไม่สามารถอ่านไฟล์ภาษี tax_data.csv");
        }
        return brackets;
    }

    // ===== คำนวณภาษีจากรายได้สุทธิ =====
    public static double calculateTax(double netIncome, int year) {
        List<TaxBracket> brackets = loadTaxBrackets(year);
        if (brackets.isEmpty()) {
            System.err.println("ไม่พบขั้นบันไดภาษีสำหรับปี " + year);
            return 0;
        }

        double tax = 0.0;
        double remaining = netIncome;

        for (TaxBracket b : brackets) {
            if (netIncome > b.min) {
                double taxable = Math.min(b.max - b.min, remaining);
                tax += taxable * b.rate;
                remaining -= taxable;
                if (remaining <= 0) break;
            }
        }

        return Math.max(tax, 0);
    }

    // ===== ตัวอย่างทดสอบการคำนวณ =====
    public static void main(String[] args) {
        int year = 2567;
        double netIncome = 950000; // รายได้สุทธิหลังหักค่าลดหย่อน
        double tax = calculateTax(netIncome, year);

        System.out.println("ปีภาษี: " + year);
        System.out.println("รายได้สุทธิ: " + netIncome);
        System.out.println("ภาษีที่ต้องชำระ: " + tax);
    }
}

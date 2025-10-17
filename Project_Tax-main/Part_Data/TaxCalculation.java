package Part_Data;

import java.io.*;
import java.util.*;

public class TaxCalculation {

    static class TaxBracket {
        double min, max, rate;
        TaxBracket(double min, double max, double rate) {
            this.min = min;
            this.max = max;
            this.rate = rate;
        }
    }

    // โหลดขั้นบันไดภาษีตามปี
    public static List<TaxBracket> loadTaxBrackets(int year) {
        List<TaxBracket> brackets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Part_Data/tax_data.csv"))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 5) continue;
                int y = Integer.parseInt(p[0]);
                if (y != year) continue;
                double min = Double.parseDouble(p[2]);
                double max = (p[3].isEmpty()) ? Double.MAX_VALUE : Double.parseDouble(p[3]);
                double rate = Double.parseDouble(p[4]) / 100.0;
                brackets.add(new TaxBracket(min, max, rate));
            }
        } catch (IOException e) {
            System.err.println("ไม่สามารถอ่านไฟล์ tax_data.csv");
        }
        return brackets;
    }

    // คำนวณภาษีขั้นบันไดตามรายได้สุทธิ
    public static double calculateTax(double netIncome, int year) {
        List<TaxBracket> brackets = loadTaxBrackets(year);
        if (brackets.isEmpty()) return 0;

        double tax = 0;
        double remaining = netIncome;

        for (TaxBracket b : brackets) {
            if (remaining <= 0) break;
            double taxable = Math.min(b.max - b.min, remaining);
            tax += taxable * b.rate;
            remaining -= taxable;
        }
        return Math.max(tax, 0);
    }

}

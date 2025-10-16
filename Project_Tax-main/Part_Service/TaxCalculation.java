package Part_Service;

import java.io.*;
import java.util.*;

/**
 * TaxCalculation.java
 * เวอร์ชันสมบูรณ์ (2568)
 * รองรับทั้งคำนวณจากไฟล์ user_data.csv และจากฟอร์ม (GUI)
 * ปรับให้ตรงกับ tax_data.csv แบบ 5 คอลัมน์: year,index,min,max,rate(%)
 */
public class TaxCalculation {

    // =============================================================
    // ใช้ในระบบหลัก (อ่านข้อมูลจาก user_data.csv)
    public static void calculateTax(String userId, String year) {
        File inputFile = new File("user_data.csv");
        File outputFile = new File("taxhistory.csv");
        File taxDataFile = new File("tax_data.csv");

        // ===== ตรวจสอบไฟล์พื้นฐาน =====
        if (!inputFile.exists()) {
            System.err.println("⚠️ ไม่พบไฟล์ user_data.csv");
            return;
        }
        if (!taxDataFile.exists()) {
            System.err.println("⚠️ ไม่พบไฟล์ tax_data.csv");
            return;
        }

        // ===== โหลดประวัติภาษีเก่า =====
        List<String> history = new ArrayList<>();
        if (outputFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(outputFile))) {
                String line;
                while ((line = br.readLine()) != null) history.add(line);
            } catch (IOException e) {
                System.err.println("⚠️ อ่านไฟล์ taxhistory.csv ไม่สำเร็จ: " + e.getMessage());
            }
        }
        if (history.isEmpty()) {
            history.add("UserID,Year,รายได้รวม,ค่าลดหย่อน,รายได้สุทธิ,ภาษีที่ต้องจ่าย");
        }

        // ===== อ่านข้อมูลผู้ใช้จาก user_data.csv =====
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("UserID")) continue;

                String[] data = line.split(",", -1);
                if (data.length < 17) continue;
                if (!data[0].equals(userId) || !data[1].equals(year)) continue;

                System.out.println("พบข้อมูลผู้ใช้: " + userId + " | ปี " + year);

                // ===== รายได้รวม =====
                double salary = parseDouble(data[2]);
                double bonus = parseDouble(data[3]);
                double other = parseDouble(data[4]);
                double totalIncome = salary + bonus + other;

                // ===== ค่าลดหย่อนรวม =====
                double totalDeduction = 60000; // ✅ ค่าลดหย่อนส่วนตัวพื้นฐาน
                for (int i = 5; i <= 16 && i < data.length; i++) {
                    totalDeduction += parseDouble(data[i]);
                }

                // ===== รายได้สุทธิ =====
                double netIncome = Math.max(totalIncome - totalDeduction, 0);
                System.out.println("รายได้รวม: " + format(totalIncome));
                System.out.println("ค่าลดหย่อน: " + format(totalDeduction));
                System.out.println("รายได้สุทธิ: " + format(netIncome));

                // ===== คำนวณภาษีตามปี =====
                double tax = calculateProgressiveTaxByYear(netIncome, year, taxDataFile);
                System.out.println("ภาษีที่ต้องจ่าย: " + format(tax));

                // ===== บันทึกลงประวัติ =====
                history.removeIf(h -> h.startsWith(userId + "," + year + ","));
                history.add(userId + "," + year + "," +
                        format(totalIncome) + "," +
                        format(totalDeduction) + "," +
                        format(netIncome) + "," +
                        format(tax));
            }

            // ===== เขียนผลลัพธ์ลงไฟล์ taxhistory.csv =====
            try (FileWriter fw = new FileWriter(outputFile, false)) {
                for (String l : history) fw.write(l + "\n");
            }

            System.out.println("✅ คำนวณเสร็จสิ้น บันทึกลงใน taxhistory.csv แล้ว");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("เกิดข้อผิดพลาดระหว่างคำนวณภาษี: " + e.getMessage());
        }
    }

    // =============================================================
    // ✅ โหลดช่วงภาษีจาก tax_data.csv ตามปี
    private static List<TaxBracket> loadBracketsForYear(String year, File taxDataFile) {
        List<TaxBracket> brackets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(taxDataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("year")) continue;

                String[] p = line.split(",");
                if (p.length >= 5 && p[0].trim().equals(year)) {
                    double min = Double.parseDouble(p[2].trim());
                    double max = p[3].trim().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(p[3].trim());
                    double rate = Double.parseDouble(p[4].trim()) / 100.0;
                    brackets.add(new TaxBracket(min, max, rate));
                }
            }

            // ===== เรียงตามลำดับขั้นต่ำเพื่อความชัวร์ =====
            brackets.sort(Comparator.comparingDouble(b -> b.min));

            // ===== ตรวจช่วงต่อกันไหม =====
            for (int i = 1; i < brackets.size(); i++) {
                TaxBracket prev = brackets.get(i - 1);
                TaxBracket cur = brackets.get(i);
                if (cur.min - prev.max > 1) {
                    System.err.println("⚠️ ช่วงภาษีต่อกันไม่สนิท: " +
                            format(prev.max) + " → " + format(cur.min));
                }
            }

            System.out.println("โหลดช่วงภาษีปี " + year + " ทั้งหมด " + brackets.size() + " ช่วง");

        } catch (IOException e) {
            System.err.println("❌ โหลดข้อมูลภาษีจาก tax_data.csv ไม่สำเร็จ: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("⚠️ พบข้อมูลไม่เป็นตัวเลขใน tax_data.csv: " + e.getMessage());
        }

        return brackets;
    }

    // =============================================================
    // ✅ คำนวณภาษีแบบขั้นบันได (อ่านจาก tax_data.csv)
    private static double calculateProgressiveTaxByYear(double income, String year, File taxDataFile) {
        List<TaxBracket> brackets = loadBracketsForYear(year, taxDataFile);
        if (brackets.isEmpty()) {
            System.err.println("⚠️ ไม่พบช่วงภาษีสำหรับปี " + year + " → คืนค่า 0");
            return 0;
        }

        double tax = 0;
        for (TaxBracket b : brackets) {
            if (income > b.max) {
                tax += (b.max - b.min) * b.rate;
                System.out.println("▶️ ช่วง " + format(b.min) + "-" + format(b.max) +
                        " อัตรา " + (b.rate * 100) + "% = " + format((b.max - b.min) * b.rate));
            } else if (income > b.min) {
                tax += (income - b.min) * b.rate;
                System.out.println("▶️ ช่วง " + format(b.min) + "-" + format(income) +
                        " อัตรา " + (b.rate * 100) + "% = " + format((income - b.min) * b.rate));
                break;
            }
        }
        return tax;
    }

    // =============================================================
    // ✅ ใช้คำนวณจากฟอร์ม GUI (TaxDeduction)
    public static double calculateTaxFromForm(
            double income,
            double provident,
            double social,
            double homeLoan,
            double lifeIns,
            double healthIns,
            double otherFund,
            int childDeduction
    ) {
        File taxDataFile = new File("tax_data.csv");
        String year = "2568"; // ค่าเริ่มต้น

        // ✅ 60,000 = ค่าลดหย่อนส่วนตัวพื้นฐานตามกฎหมาย
        double totalDeduction = 60000 + provident + social + homeLoan + lifeIns + healthIns + otherFund + childDeduction;
        double taxableIncome = Math.max(income - totalDeduction, 0);

        double tax = 0;
        if (taxDataFile.exists()) {
            tax = calculateProgressiveTaxByYear(taxableIncome, year, taxDataFile);
        } else {
            // สำรองกรณีไม่มี tax_data.csv
            if (taxableIncome <= 150000) tax = 0;
            else if (taxableIncome <= 300000) tax = (taxableIncome - 150000) * 0.05;
            else if (taxableIncome <= 500000) tax = 7500 + (taxableIncome - 300000) * 0.10;
            else if (taxableIncome <= 750000) tax = 27500 + (taxableIncome - 500000) * 0.15;
            else if (taxableIncome <= 1000000) tax = 65000 + (taxableIncome - 750000) * 0.20;
            else if (taxableIncome <= 2000000) tax = 115000 + (taxableIncome - 1000000) * 0.25;
            else if (taxableIncome <= 5000000) tax = 365000 + (taxableIncome - 2000000) * 0.30;
            else tax = 1265000 + (taxableIncome - 5000000) * 0.35;
        }

        System.out.println("💰 รายได้รวม: " + format(income));
        System.out.println("📉 ค่าลดหย่อนรวม: " + format(totalDeduction));
        System.out.println("🧾 รายได้สุทธิ: " + format(taxableIncome));
        System.out.println("💸 ภาษีที่ต้องชำระ: " + format(tax));
        return tax;
    }

    // =============================================================
    // Helper Class สำหรับเก็บข้อมูลช่วงภาษี
    private static class TaxBracket {
        double min, max, rate;
        TaxBracket(double min, double max, double rate) {
            this.min = min;
            this.max = max;
            this.rate = rate;
        }
    }

    // =============================================================
    // Helper Method
    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim().replace(",", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private static String format(double n) {
        if (Math.abs(n - Math.round(n)) < 0.001)
            return String.valueOf((long) Math.round(n));
        else
            return String.format("%.2f", n);
    }
}

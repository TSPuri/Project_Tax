package Part_Service;

import java.io.*;
import java.util.*;

public class TaxCalculation {

    public static void calculateTax(String userId, String year) {
        File inputFile = new File("user_data.csv");
        File outputFile = new File("taxhistory.csv");
        File taxDataFile = new File("tax_data.csv"); // ไฟล์ภาษีแยกตามปี

        // ===== ตรวจสอบไฟล์พื้นฐาน =====
        if (!inputFile.exists()) {
            System.err.println("⚠️ ไม่พบไฟล์ user_data.csv");
            return;
        }
        if (!taxDataFile.exists()) {
            System.err.println("⚠️ ไม่พบไฟล์ tax_data.csv");
            return;
        }

        // ===== โหลดประวัติภาษีเก่า (ถ้ามี) =====
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

        // ===== อ่านข้อมูลผู้ใช้ =====
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("UserID")) continue;

                String[] data = line.split(",", -1);
                if (data.length < 17) continue;
                if (!data[0].equals(userId) || !data[1].equals(year)) continue;

                System.out.println(" พบข้อมูลผู้ใช้: " + userId + " | ปี " + year);

                // ===== รายได้รวม =====
                double monthlySalary = parseDouble(data[2]); // เงินเดือนหลัก
                double otherMonthly = parseDouble(data[3]);  // รายได้อื่นรายเดือน
                double bonus = parseDouble(data[4]);         // โบนัส
                double totalIncome = (monthlySalary + otherMonthly) * 12 + bonus;

                // ===== ค่าลดหย่อน =====
                double totalDeduction = 0;
                for (int i = 5; i <= 16 && i < data.length; i++) {
                    totalDeduction += parseDouble(data[i]);
                }

                double netIncome = Math.max(totalIncome - totalDeduction, 0);
                System.out.println(" รายได้รวม: " + format(totalIncome));
                System.out.println(" ค่าลดหย่อน: " + format(totalDeduction));
                System.out.println(" รายได้สุทธิ: " + format(netIncome));

                // ===== ใช้ภาษีตามปี (อ่านจาก tax_data.csv) =====
                double tax = calculateProgressiveTaxByYear(netIncome, year, taxDataFile);
                System.out.println(" ภาษีที่ต้องจ่าย: " + format(tax));

                // ===== ลบข้อมูลเก่าของ userId + year แล้วเพิ่มใหม่ =====
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

            System.out.println(" คำนวณเสร็จสิ้น บันทึกลงใน taxhistory.csv แล้ว");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("เกิดข้อผิดพลาดระหว่างคำนวณภาษี: " + e.getMessage());
        }
    }

    // =============================================================
    // อ่านช่วงภาษีจาก tax_data.csv ตามปีที่ระบุ
    private static List<TaxBracket> loadBracketsForYear(String year, File taxDataFile) {
        List<TaxBracket> brackets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(taxDataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("year")) continue;
                String[] parts = line.split(",");
                if (parts[0].trim().equals(year)) {
                    double min = Double.parseDouble(parts[2].trim());
                    double max = Double.parseDouble(parts[3].trim());
                    double rate = Double.parseDouble(parts[4].trim()) / 100.0; // จาก % เป็นทศนิยม
                    brackets.add(new TaxBracket(min, max, rate));
                }
            }
        } catch (IOException e) {
            System.err.println(" โหลดข้อมูลภาษีจาก tax_data.csv ไม่สำเร็จ: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println(" พบข้อมูลไม่เป็นตัวเลขใน tax_data.csv: " + e.getMessage());
        }

        System.out.println(" โหลดช่วงภาษีปี " + year + " ทั้งหมด " + brackets.size() + " ช่วง");
        return brackets;
    }

    // =============================================================
    // คำนวณภาษีแบบขั้นบันไดโดยอ้างอิงปี
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
    //  Helper: class สำหรับเก็บช่วงภาษี
    private static class TaxBracket {
        double min, max, rate;
        TaxBracket(double min, double max, double rate) {
            this.min = min;
            this.max = max;
            this.rate = rate;
        }
    }

    // =============================================================
    // Helper: parse และ format ตัวเลข
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

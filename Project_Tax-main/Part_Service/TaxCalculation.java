package Part_Service;

import java.io.*;
import java.util.*;

public class TaxCalculation {

    public static void calculateTax(String userId, String year) {
        File inputFile = new File("user_data.csv");
        File outputFile = new File("taxhistory.csv");

        if (!inputFile.exists()) {
            System.err.println("ไม่พบไฟล์ user_data.csv");
            return;
        }

        List<String> history = new ArrayList<>();
        if (outputFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(outputFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    history.add(line);
                }
            } catch (IOException e) {
                System.err.println("⚠️ อ่านไฟล์ taxhistory.csv ไม่สำเร็จ");
                e.printStackTrace();
            }
        }

        // ถ้ายังไม่มี header ให้เพิ่ม header ไทย
        if (history.isEmpty()) {
            history.add("UserID,Year,รายได้รวม,ค่าลดหย่อน,รายได้สุทธิ,ภาษีที่ต้องจ่าย");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("UserID")) continue;

                String[] data = line.split(",", -1);
                if (data.length < 17) continue;
                if (!data[0].equals(userId) || !data[1].equals(year)) continue;

                System.out.println("พบข้อมูลผู้ใช้: " + userId + " | ปี " + year);

                // ===== (ช่อง 3 + ช่อง 4)*12 + ช่อง 5 =====
                double monthlySalary = parseDouble(data[2]); // เงินเดือนหลัก
                double otherMonthly = parseDouble(data[3]);  // รายได้อื่นรายเดือน
                double bonus = parseDouble(data[4]);         // โบนัส
                double totalIncome = (monthlySalary + otherMonthly) * 12 + bonus;

                // ===== ช่อง 6–16 คือค่าลดหย่อนทั้งหมด =====
                double totalDeduction = 0;
                for (int i = 5; i <= 16 && i < data.length; i++) {
                    totalDeduction += parseDouble(data[i]);
                }

                // ===== รายได้สุทธิ =====
                double netIncome = Math.max(totalIncome - totalDeduction, 0);
                System.out.println("รายได้รวม: " + totalIncome);
                System.out.println("ค่าลดหย่อน: " + totalDeduction);
                System.out.println("รายได้สุทธิ: " + netIncome);

                // ===== คำนวณภาษีขั้นบันได =====
                double tax = calculateProgressiveTax(netIncome);
                System.out.println("💸 ภาษีที่ต้องจ่าย: " + tax);

                // ===== ลบข้อมูลเก่าของ userId + year =====
                history.removeIf(h -> h.startsWith(userId + "," + year + ","));

                // ===== เพิ่มข้อมูลใหม่ =====
                history.add(userId + "," + year + "," +
                        format(totalIncome) + "," +
                        format(totalDeduction) + "," +
                        format(netIncome) + "," +
                        format(tax));
            }

            // ===== เขียนไฟล์ใหม่ =====
            try (FileWriter fw = new FileWriter(outputFile, false)) {
                for (String l : history) fw.write(l + "\n");
            }

            System.out.println("คำนวณเสร็จสิ้น บันทึกลงใน taxhistory.csv แล้ว");

        } catch (IOException e) {
            System.err.println("เกิดข้อผิดพลาดระหว่างคำนวณภาษี");
            e.printStackTrace();
        }
    }

    // ===== ภาษีขั้นบันได =====
    private static double calculateProgressiveTax(double income) {
        if (income <= 150000) return 0;

        double[][] brackets = {
            {150000, 0.00},
            {300000, 0.05},
            {500000, 0.10},
            {750000, 0.15},
            {1000000, 0.20},
            {2000000, 0.25},
            {5000000, 0.30},
            {Double.MAX_VALUE, 0.35}
        };

        double tax = 0;
        double lastLimit = 0;

        for (double[] b : brackets) {
            double limit = b[0];
            double rate = b[1];
            if (income > limit) {
                tax += (limit - lastLimit) * rate;
                lastLimit = limit;
            } else {
                tax += (income - lastLimit) * rate;
                break;
            }
        }

        return tax;
    }

    // ===== Helper: แปลง String → Double =====
    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim().replace(",", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    // ===== Helper: จัดรูปแบบตัวเลขสวย ๆ =====
    private static String format(double n) {
        if (Math.abs(n - Math.round(n)) < 0.001)
            return String.valueOf((long) Math.round(n));
        else
            return String.format("%.2f", n);
    }
}

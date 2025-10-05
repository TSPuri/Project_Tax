package test;

import java.math.BigDecimal;
import java.util.Map;

import Domain.TaxInput;

public class testTaxInput {
    public void testTaxInput1(){
        System.out.println("--- 1. การทดสอบกรณีข้อมูลถูกต้อง (Happy Path) ---");
        try {
            // สร้างข้อมูลตัวเลข BigDecimal ที่ถูกต้อง (ไม่เป็น null และไม่ติดลบ)
            BigDecimal income = new BigDecimal("500000.00");
            BigDecimal year = new BigDecimal("2025");
            BigDecimal bonus = new BigDecimal("50000.00");
            BigDecimal other = new BigDecimal("10000.00");
            BigDecimal annualFee = new BigDecimal("2000.00");
            BigDecimal securityDeposit = new BigDecimal("5000.00");
            BigDecimal homeLoanInterest = new BigDecimal("120000.00");

            // ลองสร้าง Object TaxInput
            TaxInput validInput = new TaxInput(
                income, year, bonus, other, annualFee, securityDeposit, homeLoanInterest
            );

            System.out.println("สร้าง Object TaxInput สำเร็จ");

            // ทดลองเรียกใช้เมธอด toParameterMap() แล้วพิมพ์ผลลัพธ์
            Map<String, Object> parameterMap = validInput.toParameterMap();
            System.out.println("ผลลัพธ์จาก toParameterMap(): " + parameterMap);

            // ทดลองเรียกใช้เมธอด validate()
            System.out.println("ผลลัพธ์จาก validate(): " + validInput.validate());

        } catch (IllegalArgumentException e) {
            System.out.println("เกิดข้อผิดพลาดที่ไม่คาดคิด: " + e.getMessage());
        }

        System.out.println("\n---------------------------------------------------\n");


        System.out.println("--- 2. การทดสอบกรณีข้อมูลผิดพลาด (โบนัสติดลบ) ---");
        try {
            // ลองสร้าง Object โดยจงใจใส่ค่า bonus ให้ติดลบ
            new TaxInput(
                new BigDecimal("500000.00"),
                new BigDecimal("2025"),
                new BigDecimal("-5000.00"), // <-- ข้อมูลที่ผิด
                new BigDecimal("10000.00"),
                new BigDecimal("2000.00"),
                new BigDecimal("5000.00"),
                new BigDecimal("120000.00")
            );
        } catch (IllegalArgumentException e) {
            // เรา "คาดหวัง" ว่าจะเกิด Exception นี้, ซึ่งถือว่าการทดสอบสำเร็จ
            System.out.println(" ทดสอบสำเร็จ โปรแกรมดักจับข้อมูลที่ผิดพลาดได้อย่างถูกต้อง");
            System.out.println("ข้อความ Exception ที่ได้รับ: \"" + e.getMessage() + "\"");
        }


        System.out.println("\n---------------------------------------------------\n");


        System.out.println("--- 3. การทดสอบกรณีข้อมูลผิดพลาด (รายได้อื่นๆ เป็น null) ---");
        try {
            // ลองสร้าง Object โดยจงใจใส่ค่า other ให้เป็น null
            new TaxInput(
                new BigDecimal("500000.00"),
                new BigDecimal("2025"),
                new BigDecimal("50000.00"),
                null, // <-- ข้อมูลที่เป็น null
                new BigDecimal("2000.00"),
                new BigDecimal("5000.00"),
                new BigDecimal("120000.00")
            );
        } catch (IllegalArgumentException e) {
            // การทดสอบนี้ก็ควรจะสำเร็จเช่นกัน
            System.out.println("ทดสอบสำเร็จ! โปรแกรมดักจับข้อมูลที่เป็น null ได้อย่างถูกต้อง");
            System.out.println("ข้อความ Exception ที่ได้รับ: \"" + e.getMessage() + "\"");
        }
    }
    }


package Part_Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PersistenceManager {

    
    private static final String[] HEADERS = {"id", "firstName", "lastName", "password"};

    
    public static void saveToCsv(String filePath, String[] headers, String[] data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("[");
            pw.println("  {");
            for (int i = 0; i < headers.length; i++) {
                pw.printf("    \"%s\": \"%s\"%s%n", headers[i], data[i], (i < headers.length - 1 ? "," : ""));
            }
            pw.println("  }");
            pw.println("]");
            System.out.println("เขียนไฟล์ใหม่เรียบร้อย: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void appendToCsv(String filePath, String[] data) {
        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));
            
            if (lines.size() > 0 && lines.get(lines.size() - 1).trim().equals("]")) {
                lines.remove(lines.size() - 1);
            }

            lines.add("  ,{");
            for (int i = 0; i < HEADERS.length; i++) {
                lines.add(String.format("    \"%s\": \"%s\"%s", HEADERS[i], data[i], (i < HEADERS.length - 1 ? "," : "")));
            }
            lines.add("  }");
            lines.add("]");

            Files.write(Paths.get(filePath), lines);
            System.out.println("เพิ่มผู้ใช้ใหม่ลงไฟล์แล้ว: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> loadFromCsv(String filePath) {
        List<String[]> rows = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
    
            String line;
            Map<String, String> current = new LinkedHashMap<>();
    
            while ((line = reader.readLine()) != null) {
                line = line.trim();
    
                // ถ้าเจอบรรทัดเปิด object ใหม่
                if (line.equals("{") || line.equals(",{")) {
                    current = new LinkedHashMap<>();
                }
                // ถ้าเจอบรรทัดข้อมูลจริง เช่น "id": "123"
                else if (line.startsWith("\"")) {
                    String[] parts = line.replace("\"", "").split(":");
                    if (parts.length >= 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim().replace(",", "");
                        current.put(key, value);
                    }
                }
                // ถ้าเจอปิด object
                else if (line.equals("}") || line.equals("},")) {
                    if (!current.isEmpty()) {
                        rows.add(new String[]{
                                current.getOrDefault("id", ""),
                                current.getOrDefault("firstName", ""),
                                current.getOrDefault("lastName", ""),
                                current.getOrDefault("password", "")
                        });
                    }
                }
            }
    
            System.out.println("โหลดข้อมูล JSON-like สำเร็จ: " + rows.size() + " แถว");
    
        } catch (FileNotFoundException e) {
            System.err.println("ไม่พบไฟล์: " + filePath);
        } catch (IOException e) {
            System.err.println("อ่านไฟล์ผิดพลาด: " + e.getMessage());
        }
    
        return rows;
    }
    
}

package Part_Service;

import java.io.*;
import java.util.*;

public class UserService {

    private static final String FILE_PATH = "src/main/data/users.csv";
    private static final String[] HEADERS = {"id", "firstName", "lastName", "password"};

    public static boolean registerUser(String id, String firstName, String lastName, String password) {
        File file = new File(FILE_PATH);
        boolean exists = file.exists();

        List<String[]> existing = PersistenceManager.loadFromCsv(FILE_PATH);
        for (String[] row : existing) {
            if (row[0].equals(id)) {
                System.out.println("⚠️ ID นี้มีอยู่แล้วในระบบ");
                return false;
            }
        }

        String[] data = {id, firstName, lastName, password};
        if (!exists) {
            
            PersistenceManager.saveToCsv(FILE_PATH, HEADERS, data);
        } else {
           
            PersistenceManager.appendToCsv(FILE_PATH, data);
        }
        return true;
    }

    public static boolean loginUser(String id, String password) {
        List<String[]> users = PersistenceManager.loadFromCsv(FILE_PATH);

        for (String[] row : users) {
            if (row.length < 4 || row[0].equals("id")) continue;

            String savedId = row[0];
            String savedPassword = row[3];

            if (savedId.equals(id) && savedPassword.equals(password)) {
                System.out.println("เข้าสู่ระบบสำเร็จ: " + row[1] + " " + row[2]);
                return true;
            }
        }
        System.out.println("รหัสผ่านหรือไอดีไม่ถูกต้อง");
        return false;
    }

    public static void showAllUsers() {
        List<String[]> users = PersistenceManager.loadFromCsv(FILE_PATH);
        System.out.println("รายชื่อผู้ใช้ทั้งหมด:");
        System.out.printf("%-15s %-15s %-15s %-15s%n", "ID", "First Name", "Last Name", "Password");
        System.out.println("--------------------------------------------------------");
        for (String[] row : users) {
            if (row[0].equals("id")) continue;
            System.out.printf("%-15s %-15s %-15s %-15s%n", row[0], row[1], row[2], row[3]);
        }
    }
}

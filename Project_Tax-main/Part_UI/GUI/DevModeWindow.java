import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;;


public class DevModeWindow extends JFrame {

    private final JTextArea console;
    private final JTextField inputField;
    private final File userFile;
    private final JLabel statusLabel; // เพิ่มช่องแสดงสถานะ

    public DevModeWindow() {
        super("Developer Console");
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        userFile = new File("users.csv");

        // ====== Console area ======
        console = new JTextArea();
        console.setEditable(false);
        console.setBackground(Color.BLACK);
        console.setForeground(new Color(0, 255, 70)); // green matrix
        console.setFont(new Font("Consolas", Font.PLAIN, 13));
        console.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(console);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // ====== Input area ======
        inputField = new JTextField();
        inputField.setBackground(new Color(18, 18, 18));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.GREEN);
        inputField.setFont(new Font("Consolas", Font.PLAIN, 13));
        inputField.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        // กด Enter เพื่อส่งคำสั่ง
        inputField.addActionListener(e -> {
            String cmd = inputField.getText().trim(); // อ่านข้อความที่ผู้ใช้งานพิม .trim() เอาไว้ตัดช่องว่างหน้าหลังออก เชน " show users "
            if (!cmd.isEmpty()) { // ตรวจสอบว่าไม่ว่างดปล่า ถ้าว่างไม่ทำไร
                log("> " + cmd); // แสดงข้อความที่พิมลง console
                handleCommand(cmd);// ประมสลผลตามทีพิม
                inputField.setText("");//เคียรืกล่องจ้อความ
            }
        });

        // ====== Status bar ======
        statusLabel = new JLabel("Ready");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(30, 30, 30));
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // layout
        setLayout(new BorderLayout(6, 6));
        add(scroll, BorderLayout.CENTER);

        // รวม input + status ไว้ด้านล่าง
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        printHeader();
        showUsers(); // แสดงผู้ใช้ตอนเปิดครั้งแรก (ถ้ามี)

        setVisible(true);
    }

    // ===== helper: พิมพ์ header =====
    private void printHeader() {
        log("=== Developer Mode Enabled ===");
        log("System: Tax Management System (dev console)");
        log("Type 'help' for available commands.");
        log("---------------------------------------");
    }

    // ===== helper: append log และ auto-scroll =====
    //ง่ายๆคือเอาไว้เลื่อนบรรทัดตอน พิมคำสั่งใหม่
    private void log(String s) {
        console.append(s + "\n");        //ตำแหน่งท้ายสุดของข้อความทั้งหมดในกล่อง”
        console.setCaretPosition(console.getDocument().getLength());
    }

    // helper สำหรับแสดงข้อความสถานะ
    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    // ====== คำสั่ง: วิเคราะห์และเรียกใช้งาน ======
    private void handleCommand(String cmd) {
        try {
            if (cmd.equalsIgnoreCase("help")) {
                showHelp();
                setStatus("แสดงคำสั่งทั้งหมด", Color.CYAN);
                return;
            }

            if (cmd.equalsIgnoreCase("show users")) {
                showUsers();
                setStatus("แสดงข้อมูลผู้ใช้ทั้งหมด", Color.CYAN);
                return;
            }

            if (cmd.toLowerCase().startsWith("show id=")) {
                String id = cmd.substring("show id=".length()).trim();
                showUserById(id);
                setStatus("แสดงข้อมูล ID: " + id, Color.CYAN);
                return;
            }

            if (cmd.toLowerCase().startsWith("add ")) {
                // รูปแบบที่รองรับ: add <id> <fname> <lname> <pass>
                String[] parts = cmd.split("\\s+"); // แบ่ง array ทุกๆช่องว่าง
                // ตรวจว่าครบ 5 ส่วนมั้ย
                if (parts.length == 5) {
                    // แบ่งจ้อความที่กรอกม่ชาเป็น array
                    String id = parts[1];
                    String fname = parts[2];
                    String lname = parts[3];
                    String pass = parts[4];
                    addUser(id, fname, lname, pass);
                    log("[+] เพิ่มผู้ใช้ใหม่เรียบร้อย: " + id);
                    showUsers(); // อัปเดตให้อัตโนมัติ
                    setStatus("เพิ่มผู้ใช้ใหม่เรียบร้อย: " + id, new Color(0, 255, 100));
                } else {
                    log("รูปแบบผิด: add <id> <fname> <lname> <pass>");
                    setStatus("คำสั่ง add ผิดรูปแบบ", Color.RED);
                }
                return;
            }

            if (cmd.toLowerCase().startsWith("delete ")) {
                String[] parts = cmd.split("\\s+");
                if (parts.length == 2) {
                    deleteUser(parts[1]);
                    showUsers();
                    setStatus("ลบผู้ใช้เรียบร้อย: " + parts[1], new Color(255, 150, 0));
                } else {
                    log("รูปแบบผิด: delete <id>");
                    setStatus("คำสั่ง delete ผิดรูปแบบ", Color.RED);
                }
                return;
            }

            if (cmd.toLowerCase().startsWith("edit ")) {
                // edit <id> <field>=<value>
                String[] parts = cmd.split("\\s+", 3);
                if (parts.length == 3 && parts[2].contains("=")) { //ส่วนที่สองต้องมี = ด้วย
                    String id = parts[1]; // เก็บไอดี
                    String[] kv = parts[2].split("=", 2);//ตัดเป็นสองส่วนคือส่วนของ pass = value
                    String field = kv[0].toLowerCase();
                    String value = kv[1];
                    editUser(id, field, value);
                    showUsers();
                    setStatus("แก้ไขข้อมูลสำเร็จ: " + id, new Color(0, 200, 255));
                } else {
                    log("[!] รูปแบบผิด: edit <id> <field>=<value>  (field=name|lname|pass)");
                    setStatus("คำสั่ง edit ผิดรูปแบบ", Color.RED);
                }
                return;
            }

            if (cmd.equalsIgnoreCase("clear")) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Confirm: ลบข้อมูลทั้งหมดใน users.csv ?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    clearFile();
                    log("เคลียร์ไฟล์เรียบร้อย");
                    setStatus("เคลียร์ไฟล์เรียบร้อย", new Color(255, 200, 0));
                } else {
                    log("ยกเลิกการเคลียร์ไฟล์");
                    setStatus("ยกเลิกการเคลียร์ไฟล์", Color.GRAY);
                }
                showUsers();
                return;
            }

            if (cmd.equalsIgnoreCase("exit")) {
                dispose();
                setStatus("ปิดหน้าต่าง Developer Console", Color.GRAY);
                return;
            }

            log("Unknown command. Type 'help' for list.");
            setStatus("คำสั่งไม่ถูกต้อง", Color.RED);
        } catch (Exception ex) {
            log("[ERROR] " + ex.getMessage());
            setStatus("เกิดข้อผิดพลาด: " + ex.getMessage(), Color.RED);
            ex.printStackTrace();
        }
    }

    // ====== แสดง help ======
    private void showHelp() {
        log("Available commands:");
        log("  help");
        log("  show users                - แสดงข้อมูลทั้งหมด");
        log("  show id=<id>              - แสดงข้อมูลผู้ใช้ตาม ID");
        log("  add <id> <fname> <lname> <pass>");
        log("  delete <id>               - ลบผู้ใช้ตาม ID");
        log("  edit <id> <field>=<value> - แก้ field (name|lname|pass)");
        log("  clear                     - ล้างไฟล์ users.csv (มี confirm)");
        log("  exit                      - ปิดหน้าต่าง");
    }

    // ====== แสดงผู้ใช้ทั้งหมด (อ่านจากไฟล์) ======
    private void showUsers() {
        console.setText(""); // เคลียร์ก่อนแสดงใหม่
        printHeader();

        if (!userFile.exists()) {
            log("ไม่พบไฟล์ users.csv");
            console.append("---------------------------------------\n");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line; // เอาไว้เก็บช่อม฿ลแจ่ละบรรทัด
            while ((line = br.readLine()) != null) {
                // แสดงตามบรรทัด ถ้าเจอบรรทัดคั่นเพิ่มบรรทัดว่างให้อ่านง่าย
                console.append(line + "\n"); // เขียนจ้อมูลลง cosole
                
                    console.append("\n");
                }
            
            console.append("---------------------------------------\n");
            console.setCaretPosition(console.getDocument().getLength());
        } catch (IOException e) {
            log("อ่านไฟล์ไม่สำเร็จ: " + e.getMessage());
            setStatus("อ่านไฟล์ไม่สำเร็จ", Color.RED);
        }
    }
        
    
    

    // ====== แสดงผู้ใช้เฉพาะ ID ======
    private void showUserById(String id) {
        if (!userFile.exists()) {
            log("ไม่พบไฟล์ users.csv");
            return;
        }
        try {
            List<String> lines = Files.readAllLines(userFile.toPath()); // อ่ายทุกบรรทักลง list
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) { //วนลูปดช็คทุกบรรทัด
                String line = lines.get(i).trim();
                if (line.equals("ID: " + id)) {
                    found = true; // เจอให่้เป็น จริง
                    // assume block format: ID / ชื่อ / นามสกุล / รหัสผ่าน / separator
                    String name = safeGet(lines, i + 1); // safeGet อ่า่นข้อมูล
                    String lname = safeGet(lines, i + 2); // มีบวกเพื่แอ่านให้ถ฿ก ตน
                    String pass = safeGet(lines, i + 3);

                    log("---------------------------------------");
                    log(line);
                    log(name);
                    log(lname);
                    log(pass);
                    log("---------------------------------------");
                    break;
                }
            }
            if (!found) log("ไม่พบ ID: " + id);
        } catch (IOException e) {
            log("อ่านไฟล์ไม่สำเร็จ: " + e.getMessage());
            setStatus("อ่านไฟล์ไม่สำเร็จ", Color.RED);
        }
    }

    // helper ปลอดภัยอ่าน index ใน list
    private String safeGet(List<String> list, int idx) {
        if (idx >= 0 && idx < list.size()) return list.get(idx);
        return "";
    }

    // ====== เพิ่มผู้ใช้ (append แบบบล็อก) ======
    private void addUser(String id, String fname, String lname, String pass) {
        // validation ง่าย ๆ
        if (id == null || id.isEmpty()) { log("[!] id ว่าง"); return; }
         try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile, true))) {
            bw.write("ID: " + id + "\n");
            bw.write("ชื่อ: " + fname + "\n");
            bw.write("นามสกุล: " + lname + "\n");
            bw.write("รหัสผ่าน: " + pass + "\n");
            bw.write("-------------------------\n");
            bw.flush();
        } catch (IOException e) {
            log("เพิ่มผู้ใช้ไม่สำเร็จ: " + e.getMessage());
            setStatus("เพิ่มผู้ใช้ไม่สำเร็จ", Color.RED);
        }
    }

    // ====== ลบผู้ใช้ตาม ID ======
    private void deleteUser(String id) {
        if (!userFile.exists()) {
            log("ไม่พบไฟล์ users.csv");
            return;
        }
        try {
            List<String> lines = Files.readAllLines(userFile.toPath());
            List<String> out = new ArrayList<>(); // สร้างมาเก็บ บรรทัดที่่ไม่ถูกลบ
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.equals("ID: " + id)) {
                    found = true;
                    // ข้ามบล็อก 4 บรรทัดถัดไป + separator (รวมเป็น 5 บรรทัด)
                    i += 4; // จะข้าม ID, ชื่อ, นามสกุล, รหัสผ่าน แล้ว loop เพิ่ม i++ จะข้าม separator ด้วย
                } else {
                    out.add(lines.get(i)); // ถ้าไม่ใช้ id ที่ต้องการ ลบ add in arraylist
                }
            }
            if (found) {
                Files.write(userFile.toPath(), out, StandardOpenOption.TRUNCATE_EXISTING);
                log("[-] ลบ ID: " + id + " เรียบร้อย");
            } else {
                log("ไม่พบ ID: " + id);
            }
        } catch (IOException e) {
            log(" ลบไม่สำเร็จ: " + e.getMessage());
            setStatus("ลบไม่สำเร็จ", Color.RED);
        }
    }

    // ====== แก้ข้อมูลผู้ใช้ (field = name|lname|pass) ======
    private void editUser(String id, String field, String value) {
        if (!userFile.exists()) {
            log("ไม่พบไฟล์ users.csv");
            return;
        }
        try {
            List<String> lines = Files.readAllLines(userFile.toPath());
            List<String> out = new ArrayList<>();
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.equals("ID: " + id)) {
                    found = true;
                    out.add(lines.get(i)); // ID line (ใส่เหมือนเดิม)
                    // อ่าน 3 บรรทัดถัดไป (อาจเกิด IndexOutOfBounds ได้ถ้าไฟล์เสียรูปแบบ)
                    String nameLine = safeGet(lines, i + 1);
                    String lnameLine = safeGet(lines, i + 2);
                    String passLine = safeGet(lines, i + 3);
                    // ดัดแปลงตาม field
                    if ("name".equalsIgnoreCase(field)) {
                        nameLine = "ชื่อ: " + value;
                    } else if ("lname".equalsIgnoreCase(field)) {
                        lnameLine = "นามสกุล: " + value;
                    } else if ("pass".equalsIgnoreCase(field)) {
                        passLine = "รหัสผ่าน: " + value;
                    } else {
                        log("[!] field ต้องเป็น name | lname | pass");
                        return;
                    }
                    out.add(nameLine);
                    out.add(lnameLine);
                    out.add(passLine);
                    // เพิ่ม separator ถ้ามี
                    String sep = safeGet(lines, i + 4); // กัน erorr ถ้ารูปแบบไฟล์ผิดปกติ
                    out.add(sep);
                    i += 4; // ข้ามบล็อกเดิม
                    log("[~] แก้ไข " + id + " สำเร็จ");
                } else {
                    out.add(lines.get(i));
                }
            }
            if (found) {
                Files.write(userFile.toPath(), out, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                log("ไม่พบ ID: " + id);
            }
        } catch (IOException e) {
            log("แก้ไขไม่สำเร็จ: " + e.getMessage());
            setStatus("แก้ไขไม่สำเร็จ", Color.RED);
        }
    }

    // ====== ล้างไฟล์ users.csv ======
    private void clearFile() {
        try {
            Files.write(userFile.toPath(), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log("เคลียร์ไฟล์ไม่สำเร็จ: " + e.getMessage());
            setStatus("เคลียร์ไฟล์ไม่สำเร็จ", Color.RED);
        }
    }
}

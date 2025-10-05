
import Part_Service.PersistenceManager;

public class Main {
    public static void main(String[] args) {

        // ===== เพิ่มข้อมูลใหม่ลงไฟล์ =====
        DummyData data = new DummyData("ภูริ", 20, "นักศึกษาคอม");
        PersistenceManager.appendToJsonArray("data/test_data.json", data, DummyData.class);

        DummyData data2 = new DummyData("puri", 12, "null");
        PersistenceManager.appendToJsonArray("data/test_data.json", data2, DummyData.class);

        // ===== โหลดข้อมูลทั้งหมด =====
        DummyData[] loaded = PersistenceManager.loadFromJson("data/test_data.json", DummyData[].class);
        if (loaded != null) {
            System.out.println("✅ โหลดสำเร็จ (" + loaded.length + " รายการ)");
            for (DummyData d : loaded) {
                System.out.println("ชื่อ: " + d.name + " อายุ: " + d.age + " job: " + d.job);
            }
        } else {
            System.out.println("โหลดไม่สำเร็จ");
        }
    }

    // ===== คลาสจำลองสำหรับทดสอบ =====
    static class DummyData {
        String name;
        int age;
        String job;

        DummyData(String name, int age, String job) {
            this.name = name;
            this.age = age;
            this.job = job;
        }
    }
}


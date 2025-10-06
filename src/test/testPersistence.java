package test;
import Part_Service.PersistenceManager;

public class TestPersistence {
    public static void main(String[] args) {
        
        DummyData data = new DummyData("ภูริ", 20, "นักศึกษาคอม");
        PersistenceManager.saveToJson("data/test_data.json", data);

       // DummyData data2 = new DummyData("puri", 12, "null");
        
        

        // โหลดค่าทั้งหมด
        DummtData[] loaded = PersistenceManager.loadFromJson("data/teat_data.json", DummyData[].class);
        if(loaded != null){
            System.out.println("โหลดสำเร็จ");
            for (DummtData dummtData : loaded) {
                System.out.println("ชื่อ:" + dummtData.name + "อายุ:" + dummtData.age + "job:" + dummtData.job);
            }
        }else{
            System.out.println("โหลดไม่สำเร็จ");
        }

    }

    // คลาสจำลองสำหรับทดสอบ
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

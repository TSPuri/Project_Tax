package Part_Service;
import Domain.*;
public class UserService {
    
    private static final String USER_FILE = "data/users.json";

    //โหลดผู้ใช้ทั้งหมดจากไฟล์
    public static UserAccount[] loadUsers(){
        UserAccount[] users = PersistenceManager.loadFromJson(USER_FILE, UserAccount[].class);
        return (users != null) ? users : new UserAccount[0];
    }

    //ตรวจว่ามีผู้ใช้ในระบบมั้ย
    public static boolean hasAnyUser(){
        return loadUsers().length > 0;
    }

    //ตรวจสอบล็อกอิน
    public static boolean validateLogin(String inputId, String inputPass){
        UserAccount[] users = loadUsers();
        for (UserAccount user : users) {
            if(user.getId().equals(inputId) && user.getPassword().equals(inputPass)){

                return true;
            }
        }
        return false;
    }
}

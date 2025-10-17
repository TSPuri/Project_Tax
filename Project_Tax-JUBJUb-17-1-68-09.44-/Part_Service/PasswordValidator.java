package Part_Service;

public class PasswordValidator {
    public static String checkStrength(String password){
        
        if(password == null || password.isEmpty()){
            return "กรุณากรอกรหัสผ่าน";
        }
        
        int score = 0;
        if(password.length() >= 8) score++;
        if(password.matches(".*[A-Z].*"))score++;
        if(password.matches(".*[0-9].*"))score++;
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) score++;
        switch (score) {
            case 0, 1:
                return "รหัสผ่านของคุณอ่อนมาก ";
            case 2:
                return "รหัสผ่านระดับกลาง";
            case 3:
                return "รหัสผ่านค่อนข้างปลอดภัย";
            case 4:
                return "รหัสผ่านปลอดภัยสูงสุด";
            default:
                return "ไม่สามารถประเมินได้";
        }

    }
    public static boolean isStrongEnough(String password){
        return password != null && password.length() >= 8 && password.matches(".*[A-Z].*")
        && password.matches(".*[0-9].*");
    }
}

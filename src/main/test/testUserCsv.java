package test;
import Part_Data.*;
import Part_Service.*;
import Part_UI.*;
import Domain.*;
import Part_UI.GUI.AdminPanelUI;
import Part_UI.GUI.IncomeForm;
import Part_UI.GUI.Login;
import Part_UI.GUI.RegisterUI;
import Part_UI.GUI.TaxDeduction;
public class TestUserCSV {
    public static void main(String[] args) {
        // สมัครผู้ใช้ใหม่
        boolean registered = Part_Service.UserServiceCSV.registerUser("001", "ภูริ", "สว่างเนตร", "1234");
        System.out.println("Register success? " + registered);

        // ล็อกอิน
        boolean login = Part_Service.UserServiceCSV.loginUser("001", "1234");
        System.out.println("Login success? " + login);

        // แสดงทั้งหมด
        Part_Service.UserServiceCSV.showAllUsers();
    }
}

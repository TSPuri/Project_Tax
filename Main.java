
import javax.swing.*;
import java.awt.*;
import Part_UI.*;

public class Main{
    public static void main(String[] args) {
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageForeground", Color.BLACK);
       new Login();
    }
}
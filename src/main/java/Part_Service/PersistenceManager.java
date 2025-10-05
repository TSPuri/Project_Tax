package Part_Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

public class PersistenceManager {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // อ่านไฟล์ Json แปลงไปเป้น Object
    public static <T> T loadFromJson(String filePath, Class<T> clazz){
        try {
            
            String Json = new String(Files.readAllBytes(Paths.get(filePath)));
            return gson.fromJson(Json, clazz);
        } catch (Exception e) {
            System.err.println("Error reading file : " + filePath);
            e.printStackTrace();
            return null;
        }
    }

    //เขียน Object ลง Json
    public static <T> void saveToJson(String filePath, T Object){
        try (FileWriter writer = new FileWriter(filePath)){
            
            gson.toJson(Object, writer);
            System.out.println("File saved successfully : " + filePath);
        } catch (Exception e) {
            
            System.err.println("Error saving file : " + filePath);
            e.printStackTrace();
        }
    }
    
    // เขียนเพิ่มลง JSON โดยไม่ลบของเก่า
    public static <T> void appendToJsonArray(String filePath,T newObject,Class<T> clazz){
        try {
            
            List<T> dataList = new ArrayList<>();
            File file = new File(filePath);

            if(file.exists()){
                String json = new String(Files.readAllBytes(Paths.get(filePath)));
                if(!json.isEmpty()){
                    //แปลง Json เป็น List<T>
                    dataList = gson.fromJson(json, TypeToken.getParameterized(List.class,  clazz).getType());
                }

            }
            // เพิ่มข่้อม฿ลใหม่เข้า list
            dataList.add(newObject);

            try(FileWriter writer = new FileWriter(filePath)){
                gson.toJson(dataList, writer);
                System.out.println("ข้อม฿ลใหม่ถูกเพิ่มลงไฟล์ : " + filePath);
            }
        } catch (Exception e) {
           System.err.println(" Error appending to json : " + filePath);
           e.printStackTrace();
        }
    }
}

package Domain;


// classนี้เกี่ยวกับ JSON (Data base)

public class TaxRule {
    private String ruleId; // เราเอาไว้เก็บเลขรหัสของแต่ละปี
    private int year;
    private String type;
    private String description; // คำอธิบายสั่นๆ ตามที่เราต้องการ
    private String bracketsJson; // โครงสรา้งช่วงภาษี เราจะเก็บเป็น JSON
    private String deductionsJson; // โครงสรา้งเก็บค่าลดหย่อน เก็บเป็น JSON

    public TaxRule(String ruleId, int year, String type, String description, String bracketsJson, String deductionsJson){

        this.ruleId = ruleId;
        this.year = year;
        this.type = type;
        this.description = description;
        this.deductionsJson = deductionsJson;
        this.bracketsJson = bracketsJson;
    }
   // Part get 
    public String getRuleId(){
        return ruleId;
    }
    public int getYear(){
        return year;
    }
    public String getType(){
        return type;
    }
    public String getDescription(){
        return description;
    }
    public String getBracketsJson(){
        return bracketsJson;
    }
    public String getDeductionJson(){
        return deductionsJson;
    }

    //part set
    public void setRuleId(String ruleId){
        this.ruleId = ruleId;
    }
    public void setYear(int year){
        this.year = year;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setBracketsJson(String bracketsJson){
        this.bracketsJson = bracketsJson;
    }
    public void setDeductionJson(String deductionsJson){
        this.deductionsJson = deductionsJson;
    }






}


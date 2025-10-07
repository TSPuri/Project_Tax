package Domain;

import java.math.BigDecimal;
import java.util.*;

public class TaxInput {
    private BigDecimal income;
    private BigDecimal year;
    private BigDecimal bonus;
    private BigDecimal other;
    private BigDecimal annual_fee;
    private BigDecimal security_deposit;
    private BigDecimal home_loan_interest;

 
    public TaxInput(BigDecimal income, BigDecimal year, BigDecimal bonus, BigDecimal other, BigDecimal annual_fee, BigDecimal security_deposit, BigDecimal home_loan_interest){
        // เช็คข้อมูลคความถูกต้องทั้งแต่ สร้าง construnter เลย เพื่อมั่นใจว่าถูกต้องแน่นอน 100%
        validateNotNullAndNotNegative("income", income);
        validateNotNullAndNotNegative("year", year);
        validateNotNullAndNotNegative("bonus", bonus);
        validateNotNullAndNotNegative("other", other);
        validateNotNullAndNotNegative("annual_fee", annual_fee);
        validateNotNullAndNotNegative("security_deposit", security_deposit);
        validateNotNullAndNotNegative("home_loan_interset", home_loan_interest);
        
        this.income = income;
        this.year = year;
        this.bonus = bonus;
        this.other = other;
        this.annual_fee = annual_fee;
        this.security_deposit = security_deposit;
        this.home_loan_interest = home_loan_interest;

    }
    public BigDecimal getIncome(){
        return this.income;
    }
    public BigDecimal getYear(){
        return this.year;
    }
    public BigDecimal getBonus(){
        return this.bonus;
    }
    public BigDecimal getOther(){
        return this.other;
    }
    public BigDecimal getAnnual_fee(){
        return this.annual_fee;
    }
    public BigDecimal getsecurity_deposit(){
        return this.security_deposit;
    }
    public BigDecimal getHome_loan_interest(){
        return this.home_loan_interest;
    }
    private void validateNotNullAndNotNegative(String fieldName, BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            
            throw new IllegalArgumentException(fieldName + " cannot be null or negative."); 
        }
    }

    //เช็คค่าว่าไม่เป็น null && >= 0
    public boolean validate(){
        return income != null && year != null && bonus != null && other != null && annual_fee != null && security_deposit != null && home_loan_interest  != null && income.compareTo(BigDecimal.ZERO) >= 0 && year.compareTo(BigDecimal.ZERO) >= 0 && bonus.compareTo(BigDecimal.ZERO) >= 0 && other.compareTo(BigDecimal.ZERO) >= 0 && annual_fee.compareTo(BigDecimal.ZERO) >= 0 && security_deposit.compareTo(BigDecimal.ZERO) >= 0 && home_loan_interest.compareTo(BigDecimal.ZERO) >= 0 ;
    }
    // อัพข้อมูลลง map โดย key คือชื่อตัวแปรนั่นๆเลยตรงตัว
    
    public Map<String, Object> toParameterMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("income", income);
        map.put("year", year);
        map.put("bonus", bonus);
        map.put("other", other);
        map.put("annual_fee", annual_fee);
        map.put("security_deposit", security_deposit);
        map.put("home_loan_interest", home_loan_interest);
        return map;

    }
}

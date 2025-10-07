package Domain;
import java.util.*;

import Domain.table_Tax.Breacket;
import Domain.table_Tax.Deductions;
import Domain.table_Tax.ExpenseDeduction;



// classนี้เกี่ยวกับ JSON (Data base)

public class TaxRule {

    private int year;
    private String description;

    private ExpenseDeduction expenseDeduction;
    private Deductions deductions;
    private List<Breacket> breackets;

    public TaxRule(){}

    public int getYear(){
        return year;
    }
    public void setYear(int year){
        this.year = year;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public ExpenseDeduction getExpenseDeduction(){
        return expenseDeduction;
    }
    public void setExpenseDeduction(ExpenseDeduction expenseDeduction){
        this.expenseDeduction = expenseDeduction;

    }

    public Deductions getDeductions(){
        return deductions;
    }
    public void setDeduction(Deductions deductions){
        this.deductions = deductions;

    }


    public List<Breacket> getBreackets(){
        return breackets;
    }
    public void setBreacket(List<Breacket> breackets){
        this.breackets = breackets;
    }






}


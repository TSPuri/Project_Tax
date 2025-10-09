package Domain.table_Tax;

public class ExpenseDeduction {
    private double percentage;
    private double max;

    public ExpenseDeduction(){}

    public double getPercentage(){
        return percentage;
    }
    
    public void setPercentage(double percentage){
        this.percentage = percentage;
    }
    
    public double getMax(){
        return max;
    }
    
    public void setMax(double max){
        this.max = max;
    }
}

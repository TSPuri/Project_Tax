package Domain.table_Tax;

public class Deductions {
    private double personal;
    private double spouse;

    public Deductions(){}

    public double getPersonal(){
        return personal;
    }

    public void setPersonal(double personal){
        this.personal = personal;
    }

    public double getSpouse(){
        return spouse;
    }

    public void setSpouse(double spouse){
        this.spouse = spouse;

    }
}

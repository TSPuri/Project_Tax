package Domain;


public interface TaxRuleCalculator {

    TaxInput calculateTax(TaxInput input);
    int getYear();
    String getDescription();
    
    
} 
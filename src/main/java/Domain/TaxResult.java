package Domain;

import java.math.BigDecimal;

public class TaxResult {
    private final BigDecimal netIncome;
    private final BigDecimal taxAmount;

    public TaxResult(BigDecimal netIncome, BigDecimal taxAmount) {
        this.netIncome = netIncome;
        this.taxAmount = taxAmount;
    }

    public BigDecimal getNetIncome() {
        return this.netIncome;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    // รอออกแบบว่าจะให้พิมไง
    //@Override
    //public String toString() {
      //  return "Net Income: " + netIncome + ", Tax Amount: " + taxAmount;
   // }
}

package Part_Service;

public class TaxCalculation {

    public static double calculateTaxFromForm(double totalIncome,
                                              double provident, double social, double homeLoan,
                                              double life, double health, double otherFund,
                                              int childDeduction) {
        double totalDeduction = provident + social + homeLoan + life + health + otherFund + childDeduction;
        double netIncome = Math.max(totalIncome - totalDeduction, 0);
        return calculateProgressiveTax(netIncome);
    }

    private static double calculateProgressiveTax(double income) {
        if (income <= 150000) return 0;
        double[][] brackets = {
            {150000, 300000, 0.05},
            {300000, 500000, 0.10},
            {500000, 750000, 0.15},
            {750000, 1000000, 0.20},
            {1000000, 2000000, 0.25},
            {2000000, 5000000, 0.30},
            {5000000, Double.MAX_VALUE, 0.35}
        };
        double tax = 0;
        for (double[] b : brackets) {
            double lower = b[0], upper = b[1], rate = b[2];
            if (income > lower) tax += (Math.min(income, upper) - lower) * rate;
            else break;
        }
        return tax;
    }

    public static double parseDoubleSafe(String s) {
        try { return s == null || s.isEmpty() ? 0 : Double.parseDouble(s.trim().replace(",", "")); }
        catch (Exception e) { return 0; }
    }

    public static int parseIntSafe(String s) {
        try { return s == null || s.isEmpty() ? 0 : Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }
}

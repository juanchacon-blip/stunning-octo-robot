package shop;

public class SmartShippingCalculator implements ShippingCalculator {
    @Override
    public double calculate(Order o) {
        return o.weightKg() * 2.5;
    }
}
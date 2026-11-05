package shop;

public class SmartShippingCalculator implements ShippingCalculator {
    @Override
public double calculate(Order o) {
    double base = o.weightKg() * 2.5;
    return o.city().equals("Bogotá") ? base : base + 5;
}
}
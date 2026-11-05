package shop;

public class LegacyShippingCalculator implements ShippingCalculator {
    @Override
    public double calculate(Order order) { return 10.0; }
}
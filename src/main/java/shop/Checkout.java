package shop;

public class Checkout {
    private final LegacyShippingCalculator calc = new LegacyShippingCalculator();
    public double total(Order o) { return o.subtotal() + calc.calculate(o); }
}

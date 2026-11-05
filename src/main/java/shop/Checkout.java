package shop;

public class Checkout {
    private final ShippingCalculator calc;

    public Checkout(ShippingCalculator calc) { this.calc = calc; }

    public double total(Order o) { return o.subtotal() + calc.calculate(o); }
}
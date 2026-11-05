package shop;

public class ShippingCalculatorProxy implements ShippingCalculator {
    private final ShippingCalculator legacy;
    private final ShippingCalculator smart;
    private final FeatureFlags flags;

    public ShippingCalculatorProxy(ShippingCalculator legacy,
                                   ShippingCalculator smart,
                                   FeatureFlags flags) {
        this.legacy = legacy;
        this.smart = smart;
        this.flags = flags;
    }

 @Override
public double calculate(Order order) {
    return flags.isEnabled("smart-shipping")
            ? smart.calculate(order)
            : legacy.calculate(order);
}
}
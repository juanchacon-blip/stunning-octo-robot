package shop;

public class ShopConfig {
    public static Checkout checkout() {
        return new Checkout(new SmartShippingCalculator());
    }
}
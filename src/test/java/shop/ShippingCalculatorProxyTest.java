package shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShippingCalculatorProxyTest {
    Order order = new Order("o1", "c1", "Medellín", 100, 4);
    FeatureFlags flags = new FeatureFlags();
    ShippingCalculatorProxy proxy = new ShippingCalculatorProxy(
            new LegacyShippingCalculator(), new SmartShippingCalculator(), flags);

    @Test
    void usaLegacyConFlagApagado() {
        assertEquals(10.0, proxy.calculate(order));
    }
    
    @Test
void usaSmartConFlagEncendido() {
    flags.enable("smart-shipping");
    assertEquals(15.0, proxy.calculate(order)); // 4 kg × 2,5 + 5
}

@Test
void clienteFueraDelCanaryUsaLegacyAunConFlagEncendido() {
    flags.enable("smart-shipping");
    Order fuera = new Order("o2", "c3", "Medellín", 100, 4);
    assertEquals(10.0, proxy.calculate(fuera));
}
}
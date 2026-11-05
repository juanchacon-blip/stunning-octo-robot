package shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SmartShippingCalculatorTest {
    SmartShippingCalculator calc = new SmartShippingCalculator();

    @Test
    void cobraDosCincoPorKilo() {
        Order order = new Order("o1", "c1", "Bogotá", 100, 4);
        assertEquals(10.0, calc.calculate(order));
    }
}
package shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutTest {
    @Test
    void totalIncluyeElEnvioLegacy() {
        Order order = new Order("o1", "c1", "Medellín", 100, 4);
        assertEquals(110.0, new Checkout().total(order));
    }
}
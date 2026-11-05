package shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutTest {
    @Test
    void totalIncluyeElEnvioLegacy() {
        Order order = new Order("o1", "c1", "Medellín", 100, 4);
        assertEquals(110.0, new Checkout(new LegacyShippingCalculator()).total(order));
    }
    
    @Test
void checkoutFuncionaConCualquierCalculador() {
    Checkout checkout = new Checkout(o -> 7.0);
    assertEquals(107.0, checkout.total(new Order("o1", "c1", "Medellín", 100, 4)));
}
}
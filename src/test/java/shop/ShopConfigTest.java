package shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShopConfigTest {
    @Test
    void laTiendaUsaElCalculoNuevo() {
        Order order = new Order("o1", "c1", "Medellín", 100, 4);
        assertEquals(115.0, ShopConfig.checkout().total(order)); // 100 + (4 × 2,5 + 5)
    }
}

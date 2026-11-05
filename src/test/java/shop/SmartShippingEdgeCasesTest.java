package shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SmartShippingEdgeCasesTest {
    SmartShippingCalculator calc = new SmartShippingCalculator();

    @Test
    void pesoCeroEnBogotaNoCuesta() {
        assertEquals(0.0, calc.calculate(new Order("o1", "c1", "Bogotá", 100, 0)), 0.0001);
    }

    @Test
    void pesoCeroFueraDeBogotaSoloPagaElRecargo() {
        assertEquals(5.0, calc.calculate(new Order("o1", "c1", "Medellín", 100, 0)), 0.0001);
    }

    @Test
    void aceptaPesosConDecimalesEnBogota() {
        assertEquals(6.25, calc.calculate(new Order("o1", "c1", "Bogotá", 100, 2.5)), 0.0001);
    }

    @Test
    void aceptaPesosConDecimalesFueraDeBogota() {
        assertEquals(11.25, calc.calculate(new Order("o1", "c1", "Medellín", 100, 2.5)), 0.0001);
    }

    @Test
    void elTotalSumaSubtotalMasEnvio() {
        Checkout checkout = new Checkout(calc);
        assertEquals(110.0, checkout.total(new Order("o1", "c1", "Bogotá", 100, 4)), 0.0001);
    }
}

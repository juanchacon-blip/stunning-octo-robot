package shop;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Locale;

/**
 * Interfaz gráfica de la tienda: recalcula el envío y el total mientras se escribe.
 * No tiene reglas de negocio propias: delega todo en Checkout (ver ShopConfig).
 */
public class ShippingApp {

    private static final Color FONDO = new Color(0x0F1513);
    private static final Color TARJETA = new Color(0x161E1B);
    private static final Color TEXTO = new Color(0xE4ECE9);
    private static final Color SUAVE = new Color(0x97A8A2);
    private static final Color VERDE = new Color(0x3EC495);
    private static final Color NARANJA = new Color(0xF0A24D);

    private final JComboBox<String> ciudad =
            new JComboBox<>(new String[]{"Bogotá", "Medellín", "Cali", "Ibagué", "Barranquilla"});
    private final JTextField peso = new JTextField("4", 8);
    private final JTextField subtotal = new JTextField("100", 8);

    private final JLabel lblPeso = valor();
    private final JLabel lblRecargo = valor();
    private final JLabel lblEnvio = valor();
    private final JLabel lblTotal = new JLabel("—");
    private final JLabel lblZona = new JLabel(" ");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShippingApp().mostrar());
    }

    private void mostrar() {
        JFrame frame = new JFrame("Tienda · Envío inteligente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel raiz = new JPanel(new BorderLayout(0, 16));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel titulo = new JLabel("Calculadora de envío");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        titulo.setForeground(TEXTO);
        raiz.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 16, 0));
        centro.setOpaque(false);
        centro.add(panelEntrada());
        centro.add(panelResultado());
        raiz.add(centro, BorderLayout.CENTER);

        DocumentListener recalcular = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizar(); }
            public void removeUpdate(DocumentEvent e) { actualizar(); }
            public void changedUpdate(DocumentEvent e) { actualizar(); }
        };
        peso.getDocument().addDocumentListener(recalcular);
        subtotal.getDocument().addDocumentListener(recalcular);
        ciudad.addActionListener(e -> actualizar());
        actualizar();

        frame.setContentPane(raiz);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel panelEntrada() {
        JPanel p = tarjeta();
        p.add(etiqueta("Ciudad de entrega"));
        p.add(ciudad);
        p.add(Box.createVerticalStrut(12));
        p.add(etiqueta("Peso (kg)"));
        p.add(peso);
        p.add(Box.createVerticalStrut(12));
        p.add(etiqueta("Subtotal del pedido"));
        p.add(subtotal);
        for (Component c : p.getComponents()) {
            if (c instanceof JComponent) ((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        return p;
    }

    private JPanel panelResultado() {
        JPanel p = tarjeta();
        p.add(fila("Peso × 2,5", lblPeso));
        p.add(fila("Recargo fuera de Bogotá", lblRecargo));
        p.add(fila("Envío", lblEnvio));
        p.add(Box.createVerticalStrut(10));
        p.add(etiqueta("TOTAL"));
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 34f));
        lblTotal.setForeground(VERDE);
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblTotal);
        lblZona.setForeground(SUAVE);
        lblZona.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(6));
        p.add(lblZona);
        return p;
    }

    private void actualizar() {
        try {
            double kg = leer(peso);
            double sub = leer(subtotal);
            String city = (String) ciudad.getSelectedItem();

            Order order = new Order("gui", "gui", city, sub, kg);
            double total = ShopConfig.checkout().total(order);
            double envio = total - sub;
            double base = kg * 2.5;

            lblPeso.setText(fmt(base));
            lblRecargo.setText(fmt(envio - base));
            lblEnvio.setText(fmt(envio));
            lblTotal.setText(fmt(total));
            boolean bogota = "Bogotá".equals(city);
            lblZona.setForeground(bogota ? VERDE : NARANJA);
            lblZona.setText(bogota ? "Zona Bogotá: sin recargo" : "Fuera de Bogotá: recargo aplicado");
        } catch (NumberFormatException ex) {
            lblPeso.setText("—");
            lblRecargo.setText("—");
            lblEnvio.setText("—");
            lblTotal.setText("—");
            lblZona.setForeground(SUAVE);
            lblZona.setText("Escribe números válidos (ej. 4 o 2.5)");
        }
    }

    private static double leer(JTextField f) {
        double v = Double.parseDouble(f.getText().trim().replace(',', '.'));
        if (v < 0) throw new NumberFormatException("negativo");
        return v;
    }

    private static String fmt(double v) {
        return String.format(Locale.forLanguageTag("es-CO"), "%.2f", v);
    }

    private static JPanel tarjeta() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(TARJETA);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        return p;
    }

    private static JLabel etiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(SUAVE);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static JLabel valor() {
        JLabel l = new JLabel("—");
        l.setForeground(TEXTO);
        return l;
    }

    private static JPanel fila(String texto, JLabel valor) {
        JPanel f = new JPanel(new BorderLayout(24, 0));
        f.setOpaque(false);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        JLabel l = new JLabel(texto);
        l.setForeground(SUAVE);
        f.add(l, BorderLayout.WEST);
        f.add(valor, BorderLayout.EAST);
        return f;
    }
}

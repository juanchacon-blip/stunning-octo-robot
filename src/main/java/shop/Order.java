package shop;

public record Order(String id, String customerId, String city,
                    double subtotal, double weightKg) {}

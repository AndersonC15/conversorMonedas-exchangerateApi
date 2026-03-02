import java.util.Map;

/**
 * Record que representa la respuesta de la API de ExchangeRate.
 *
 * Contiene el estado de la consulta, la moneda base utilizada
 * y las tasas de conversion hacia todas las demas monedas.
 *
 * @param result estado de la consulta ("success" o "error")
 * @param base_code codigo de la moneda base (ej: USD)
 * @param conversion_rates map con monedas y sus tasas de conversion
 *
 * @author Anderson Coello
 * @version 1.0
 * @since 2026-03-02
 */
public record Moneda(
        String result,
        String base_code,
        Map<String, Double> conversion_rates
) {
}
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clase encargada de consultar la API de ExchangeRate.
 *
 * Proporciona metodos para obtener monedas disponibles, tasas de cambio
 * y realizar conversiones entre monedas.
 *
 * @author Anderson Coello
 * @version 1.0
 * @since 2026-03-02
 */
public class ConsultaAPI {

    private static final String API_KEY = "1faa560f170e7b7d7226a9e9";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    /**
     * Obtiene todas las monedas disponibles desde la API.
     *
     * Utiliza USD como moneda base para obtener el listado
     * completo de monedas soportadas.
     *
     * @return Map ordenado con codigos de moneda y sus tasas respecto a USD
     * @throws RuntimeException si falla la conexion con la API
     */
    public Map<String, Double> obtenerTodasLasMonedas() {
        try {
            Moneda moneda = consultarAPI("USD");
            return new LinkedHashMap<>(moneda.conversion_rates());

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las monedas: " + e.getMessage());
        }
    }

    /**
     * Obtiene la tasa de conversion entre dos monedas.
     *
     * @param monedaOrigen codigo de la moneda origen (ej: USD)
     * @param monedaDestino codigo de la moneda destino (ej: EUR)
     * @return tasa de conversion de origen a destino
     * @throws RuntimeException si la moneda destino no existe o falla la API
     */
    public double obtenerTasaConversion(String monedaOrigen, String monedaDestino) {
        try {
            Moneda moneda = consultarAPI(monedaOrigen);

            if (!moneda.conversion_rates().containsKey(monedaDestino)) {
                throw new RuntimeException("Moneda destino no encontrada");
            }

            return moneda.conversion_rates().get(monedaDestino);

        } catch (Exception e) {
            throw new RuntimeException("Error en la conversion: " + e.getMessage());
        }
    }

    /**
     * Realiza la conversion de una cantidad de una moneda a otra.
     *
     * @param cantidad valor a convertir
     * @param monedaOrigen codigo de la moneda origen
     * @param monedaDestino codigo de la moneda destino
     * @return cantidad convertida a la moneda destino
     * @throws RuntimeException si ocurre un error en la API
     */
    public double convertir(double cantidad, String monedaOrigen, String monedaDestino) {
        double tasa = obtenerTasaConversion(monedaOrigen, monedaDestino);
        return cantidad * tasa;
    }

    /**
     * Realiza una consulta a la API de ExchangeRate.
     *
     * @param codigoMoneda codigo de la moneda base para la consulta
     * @return objeto Moneda con la informacion obtenida de la API
     * @throws Exception si falla la conexion o la respuesta es invalida
     */
    private Moneda consultarAPI(String codigoMoneda) throws Exception {
        URI direccion = URI.create(BASE_URL + API_KEY + "/latest/" + codigoMoneda);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en la API: " + response.statusCode());
        }

        Moneda moneda = new Gson().fromJson(response.body(), Moneda.class);

        if (!"success".equals(moneda.result())) {
            throw new RuntimeException("No encontre esa moneda.");
        }

        return moneda;
    }
}
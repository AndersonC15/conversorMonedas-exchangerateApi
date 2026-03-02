import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultaAPI {

    private static final String API_KEY = "1faa560f170e7b7d7226a9e9";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    /**
     * Obtiene todas las monedas disponibles desde la API
     * @return Map ordenado con todas las monedas disponibles
     */
    public Map<String, Double> obtenerTodasLasMonedas() {
        try {
            Moneda moneda = consultarAPI("USD");

            // Usamos LinkedHashMap para mantener el orden
            return new LinkedHashMap<>(moneda.conversion_rates());

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las monedas: " + e.getMessage());
        }
    }

    /**
     * Obtiene la tasa de conversión entre dos monedas
     * @param monedaOrigen Código de la moneda origen (ej: USD)
     * @param monedaDestino Código de la moneda destino (ej: EUR)
     * @return Tasa de conversión
     */
    public double obtenerTasaConversion(String monedaOrigen, String monedaDestino) {
        try {
            Moneda moneda = consultarAPI(monedaOrigen);

            if (!moneda.conversion_rates().containsKey(monedaDestino)) {
                throw new RuntimeException("Moneda destino no encontrada");
            }

            return moneda.conversion_rates().get(monedaDestino);

        } catch (Exception e) {
            throw new RuntimeException("Error en la conversión: " + e.getMessage());
        }
    }

    /**
     * Realiza la conversión de una cantidad de moneda origen a destino
     * @param cantidad Cantidad a convertir
     * @param monedaOrigen Código de la moneda origen
     * @param monedaDestino Código de la moneda destino
     * @return Cantidad convertida
     */
    public double convertir(double cantidad, String monedaOrigen, String monedaDestino) {
        double tasa = obtenerTasaConversion(monedaOrigen, monedaDestino);
        return cantidad * tasa;
    }

    /**
     * Método privado que realiza la consulta a la API
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
            throw new RuntimeException("No encontré esa moneda.");
        }

        return moneda;
    }
}
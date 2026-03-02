import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaAPI {

    public Moneda buscarPelicula(int TipoMoneda){

        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/1faa560f170e7b7d7226a9e9/latest/"+TipoMoneda);


        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.n ewBuilder()
                .uri(direccion)
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(),Moneda.class);

        } catch (Exception e) {
            throw new RuntimeException("No encontre esa Moneda.");
        }

    }
}



}

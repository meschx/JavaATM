import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import org.json.JSONArray;
import org.json.JSONObject;

public class CurrencyConverter {

    public static double getExchangeRate(String currencyCode) throws Exception {
        if (currencyCode.equals("PLN")) {
            return 1.0;
        }

        URI uri = new URI("https://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "/");
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        JSONObject json = new JSONObject(content.toString());
        JSONArray rates = json.getJSONArray("rates");
        double exchangeRate = rates.getJSONObject(0).getDouble("mid");

        return Math.round(exchangeRate * 100.0) / 100.0;
    }

}
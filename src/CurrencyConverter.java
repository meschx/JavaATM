import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class CurrencyConverter {

    public double getExchangeRate(String currencyCode) throws Exception {
        URL url = new URL("https://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "/");

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

    public static void main(String[] args) {
        CurrencyConverter currencyConverter = new CurrencyConverter();
        try {
            double exchangeRate = currencyConverter.getExchangeRate("USD");
            System.out.println("Kurs waluty USD: " + exchangeRate);
            double exchangeRate2 = currencyConverter.getExchangeRate("EUR");
            System.out.println("Kurs waluty EUR: " + exchangeRate2);
            double exchangeRate3 = currencyConverter.getExchangeRate("CZK");
            System.out.println("Kurs waluty CZK: " + exchangeRate3);
        } catch (Exception e) {
            System.out.println("Wystąpił błąd: " + e.getMessage());
        }
    }
}
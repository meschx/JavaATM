import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class CurrencyConverter {

    public static double getExchangeRate(String currencyCode) throws Exception {
        if (currencyCode.equals("PLN")) {
            return 1.0;
        }

        URI uri = new URI("https://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "/?format=xml");
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputStream inputStream = connection.getInputStream();
        Document doc = db.parse(inputStream);

        NodeList nodeList = doc.getElementsByTagName("Mid");
        double exchangeRate = Double.parseDouble(nodeList.item(0).getTextContent());

        inputStream.close();
        connection.disconnect();

        return Math.round(exchangeRate*100.00)/100.0;
    }
}
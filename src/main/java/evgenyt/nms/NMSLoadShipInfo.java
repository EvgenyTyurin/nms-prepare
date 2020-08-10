package evgenyt.nms;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class NMSLoadShipInfo {

    public static void main(String[] args) throws JDOMException, IOException {
        // Read XML file with ships
        SAXBuilder saxBuilder = new SAXBuilder();
        File xmlFile = new File("D:\\temp\\nms\\ships.xml");
        Document document = saxBuilder.build(xmlFile);
        List<Element> shipElements = document.getRootElement().getChildren("ship");
        for (Element shipElement : shipElements) {
            NMSShip ship = new NMSShip(shipElement.getAttribute("name").getValue(),
                    shipElement.getAttribute("url").getValue());
            String url = "https://nomanssky.gamepedia.com" + ship.getUrl();
            System.out.println("Loading " + url + "...");
            String html = readUrl(url);
            try (PrintWriter out = new PrintWriter("D:\\temp\\nms\\wiki\\" +
                    ship.getName().replace("/", " ") + ".html")) {
                out.println(html);
            }
        }
    }

    // Get url content
    public static String readUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        Authenticator authenticator = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication("user",
                        "password".toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress("proxy.host", 3128));
        HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }

}

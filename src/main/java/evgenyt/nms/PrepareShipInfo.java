package evgenyt.nms;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.*;

public class PrepareShipInfo {
    public static void main(String[] args) throws IOException, JDOMException {

        // Read XML file with ships
        SAXBuilder saxBuilder = new SAXBuilder();
        File xmlFile = new File("D:\\temp\\nms\\ships.xml");
        Document document = saxBuilder.build(xmlFile);
        List<Element> shipElements = document.getRootElement().getChildren("ship");
        List<NMSShip> shipList = new ArrayList<>();
        for (Element shipElement : shipElements) {
            NMSShip ship = new NMSShip(shipElement.getAttribute("name").getValue(),
                    shipElement.getAttribute("url").getValue());
            System.out.println("*** " + ship.getName());
            String fileName = "D:\\temp\\nms\\wiki\\" +
                    ship.getName().replace("/"," ") + ".html";
            Scanner scanner = new Scanner(new File(fileName));
            String htmlText = scanner.useDelimiter("\\A").next();
            scanner.close();
            String[] words = htmlText.split(" ");
            Optional<String> imageUrl =
                    Arrays.stream(words).filter(x -> x.contains("525px")).findFirst();
            if (imageUrl.isPresent())
                ship.setImageUrl(imageUrl.get().substring(8));
            ship.setGalaxy(getVal(htmlText, "Galaxy"));
            ship.setRegion(getVal(htmlText, "Region"));
            ship.setStar(getVal(htmlText, "Star system"));
            ship.setCoordinates(getTag(htmlText, "coords"));
            ship.setInventory(getTag(htmlText, "Inventory"));
            ship.setCost(getTag(htmlText, "Cost", " "));
            ship.setPlatform(getTag(htmlText, "Platform"));

            int pos1 = htmlText.indexOf("td>+");
            if (pos1 > 0) {
                int pos2 = htmlText.indexOf("%", pos1);
                ship.setDamage(htmlText.substring(pos1 + 3, pos2 + 1));
                pos1 = htmlText.indexOf("td>+", pos2);
                pos2 = htmlText.indexOf("%", pos1);
                ship.setShield(htmlText.substring(pos1 + 3, pos2 + 1));
                pos1 = htmlText.indexOf("td>+", pos2);
                pos2 = htmlText.indexOf("%", pos1);
                ship.setWarp(htmlText.substring(pos1 + 3, pos2 + 1));
            }
            shipList.add(ship);
        }

        // Save ships info to xml file
        Element root = new Element("ships");
        for (NMSShip nmsShip : shipList) {
            Element shipElement = new Element("ship");
            shipElement.setAttribute("name", nmsShip.getName());
            shipElement.setAttribute("url", nmsShip.getUrl());
            shipElement.setAttribute("image", nmsShip.getImageUrl());
            shipElement.setAttribute("galaxy", nmsShip.getGalaxy());
            shipElement.setAttribute("region", nmsShip.getRegion());
            shipElement.setAttribute("star", nmsShip.getStar());
            shipElement.setAttribute("coordinates", nmsShip.getCoordinates());
            shipElement.setAttribute("inventory", nmsShip.getInventory());
            shipElement.setAttribute("cost", nmsShip.getCost());
            shipElement.setAttribute("damage", nmsShip.getDamage());
            shipElement.setAttribute("shield", nmsShip.getShield());
            shipElement.setAttribute("warp", nmsShip.getWarp());
            shipElement.setAttribute("platform", nmsShip.getPlatform());
            root.addContent(shipElement);
        }
        org.jdom2.Document documentXML = new org.jdom2.Document();
        documentXML.setRootElement(root);
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(documentXML,
                new FileWriter(new File("D:\\temp\\nms\\ships-info.xml")));
    }

    static String getVal(String htmlText, String tag) {
        int pos = htmlText.indexOf(tag);
        pos = htmlText.indexOf("\"", pos);
        int pos2 = htmlText.indexOf("\"", pos + 1);
        return htmlText.substring(pos + 1, pos2);
    }

    static String getTag(String htmlText, String tag) {
        int pos = htmlText.indexOf(tag);
        pos = htmlText.indexOf("<td>", pos);
        int pos2 = htmlText.indexOf("/td", pos);
        return htmlText.substring(pos + 4, pos2 - 1);
    }

    static String getTag(String htmlText, String tag, String fin) {
        int pos = htmlText.indexOf(tag);
        pos = htmlText.indexOf("<td>", pos);
        int pos2 = htmlText.indexOf(fin, pos);
        return htmlText.substring(pos + 4, pos2);
    }

}

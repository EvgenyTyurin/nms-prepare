package evgenyt.nms;

import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 * NoMansSky Ships Prepare: Convert html file to xml with base ship info
 */

public class NMSPrepareShipList {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        File shipsFile = new File("D:\\temp\\nms\\ships.html");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(shipsFile);
        document.getDocumentElement().normalize();
        NodeList aList = document.getElementsByTagName("a");
        List<NMSShip> nmsShips = new ArrayList<>();
        for (int aIdx = 0; aIdx < aList.getLength(); aIdx++) {
            Node aNode = aList.item(aIdx);
            nmsShips.add(new NMSShip(aNode.getAttributes().item(1).getNodeValue(),
                    aNode.getAttributes().item(0).getNodeValue()));
        }
        Element root = new Element("ships");
        for (NMSShip nmsShip : nmsShips) {
            Element shipElement = new Element("ship");
            shipElement.setAttribute("name", nmsShip.getName());
            shipElement.setAttribute("url", nmsShip.getUrl());
            root.addContent(shipElement);
        }
        org.jdom2.Document documentXML = new org.jdom2.Document();
        documentXML.setRootElement(root);
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(documentXML, new FileWriter(new File("D:\\temp\\nms\\ships.xml")));
    }
}

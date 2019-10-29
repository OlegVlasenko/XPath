package com.ogvlasenko.examples.xml;

import com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Application {
    private final static String SIGNING_CERTIFICATE_SELECTOR = "/EntityDescriptor/RoleDescriptor[@type=\"fed:SecurityTokenServiceType\"]/KeyDescriptor[@use=\"signing\"]/KeyInfo/X509Data/X509Certificate";


    public static void main(String[] args){

        System.setProperty("jaxp.debug", "true");

        //XPathFactory xPathFactory = XmlFactory.createXPathFactory(true);
        XPathFactory xPathFactory = new XPathFactoryImpl();

        XPath xPath = xPathFactory.newXPath();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        String wsfmContent = getResourceFile();

        try {
            builder = documentBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(wsfmContent)));
            NodeList nodes = (NodeList) xPath.evaluate(SIGNING_CERTIFICATE_SELECTOR, doc, XPathConstants.NODESET);

            IntStream.range(0, nodes.getLength()).forEach(i -> {System.out.println(nodes.item(i).getNodeName());
                System.out.println(nodes.item(i).getTextContent());});

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    private static String getResourceFile() {
        try {
            Path path = Paths.get(Objects.requireNonNull(Application.class.getClassLoader()
                    .getResource("FederationMetadata.xml"), "resource not found " + "FederationMetadata.xml").toURI());

            Stream<String> lines = null;

            assert path != null : "path may not be bull";
            lines = Files.lines(path);

            String data = Objects.requireNonNull(lines).collect(Collectors.joining("\n"));
            lines.close();

            return data;

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}

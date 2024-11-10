package com.labab;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XSLTTransformer {
    public static void main(String[] args) {
        try {
            File xmlFile = new File("src/main/resources/beer.xml");
            File xsltFile = new File("src/main/resources/beer_transform.xslt"); 
            File outputFile = new File("src/main/resources/transformed_beer.xml"); 

            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(xsltFile);
            Transformer transformer = factory.newTransformer(xslt);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4"); 
            Source xmlSource = new StreamSource(xmlFile);
            Result output = new StreamResult(outputFile);
            transformer.transform(xmlSource, output);

            System.out.println("Transformation completed successfully!");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}

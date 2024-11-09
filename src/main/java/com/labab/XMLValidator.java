package com.labab;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class XMLValidator {
    private static final Logger logger = Logger.getLogger(XMLValidator.class.getName());

    public boolean validateXMLSchema(String xsdPath, String xmlPath) {
        try (InputStream xsdStream = XMLValidator.class.getClassLoader().getResourceAsStream(xsdPath);
             InputStream xmlStream = XMLValidator.class.getClassLoader().getResourceAsStream(xmlPath)) {
            if (xsdStream != null && xmlStream != null) {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(new StreamSource(xsdStream));

                Validator validator = schema.newValidator();
                validator.validate(new StreamSource(xmlStream));

                logger.info("XSD Validation successful for XML file: " + xmlPath);
            } else {
                logger.severe("One or both of the files were not found in resources.");
                return false;
            }
        } catch (IOException | SAXException e) {
            logger.severe("Validation error: " + e.getMessage());
            return false;
        }
        return true;
    }
}
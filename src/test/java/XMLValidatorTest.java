import com.labab.XMLValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XMLValidatorTest {
    XMLValidator validator = new XMLValidator();

    @Test
    void testValidXML() {
        String xsdPath = "xsd/beer.xsd";
        String xmlPath = "beer.xml";

        assertTrue(validator.validateXMLSchema(xsdPath, xmlPath));
    }

    @Test
    void testInvalidXML() {
        String xsdPath = "xsd/orangery.xsd";
        String xmlPath = "invalid_orangery.xml";

        assertFalse(validator.validateXMLSchema(xsdPath, xmlPath));
    }
}
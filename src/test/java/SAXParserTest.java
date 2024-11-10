

import com.labab.SAXParser;
import generated.Beer;
import generated.BeerType;
import generated.AlcoholType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SAXParserTest {
    private SAXParser saxParser;
    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        saxParser = new SAXParser();
        mockLogger = Mockito.mock(Logger.class);
    }

    @Test
    void testParseXML_ValidXML_ReturnsBeerList() {
        String xmlFilePath = "src/test/resources/test_beer.xml";

        List<Beer.BeerItem> beerItems = saxParser.parseXML(xmlFilePath);

        assertNotNull(beerItems);
        assertEquals(3, beerItems.size());

        Beer.BeerItem firstBeer = beerItems.get(0);
        assertEquals(BigInteger.valueOf(1), firstBeer.getId());
        assertEquals("Stout", firstBeer.getName());
        assertEquals(BeerType.ТЕМНЕ, firstBeer.getType());
        assertEquals(AlcoholType.АЛКОГОЛЬНЕ, firstBeer.getAl());
        assertEquals("Brewery A", firstBeer.getManufacturer());

        List<String> ingredients = firstBeer.getIngredients().getIngredient();
        assertTrue(ingredients.contains("Water"));
        assertTrue(ingredients.contains("Hops"));

        Beer.BeerItem.Chars chars = firstBeer.getChars();
        assertEquals(new BigDecimal("6.5"), chars.getAbv());
        assertEquals(new BigDecimal("20"), chars.getTransparency());
        assertEquals("Yes", chars.getFiltered());
        assertEquals(150, chars.getNutritionalValue());

        Beer.BeerItem.Chars.Packaging packaging = chars.getPackaging();
        assertEquals(new BigDecimal("0.5"), packaging.getVolume());
        assertEquals("Glass", packaging.getMaterial());
    }

    @Test
    void testParseXML_InvalidXML_ReturnsEmptyList() {
        String xmlFilePath = "src/test/resources/invalid_beer.xml";

        List<Beer.BeerItem> beerItems = saxParser.parseXML(xmlFilePath);

        assertNotNull(beerItems);
        assertTrue(beerItems.isEmpty());
    }
}

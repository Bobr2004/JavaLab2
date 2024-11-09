

import com.labab.StAXParser;
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

class StAXParserTest {
    private StAXParser stAXParser;
    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        stAXParser = new StAXParser();
        mockLogger = Mockito.mock(Logger.class);
        // Inject mockLogger into stAXParser if possible, or verify log calls directly.
    }

    @Test
    void testParseXML_ValidXML_ReturnsBeerList() {
        // Arrange
        String xmlFilePath = "src/test/resources/test_beer.xml"; // Ensure this file exists

        // Act
        List<Beer.BeerItem> beerItems = stAXParser.parseXML(xmlFilePath);

        // Assert
        assertNotNull(beerItems);
        assertEquals(3, beerItems.size()); // Adjust based on expected item count in XML

        // Validate first beer item
        Beer.BeerItem firstBeer = beerItems.get(0);
        assertEquals(BigInteger.valueOf(1), firstBeer.getId());
        assertEquals("Stout", firstBeer.getName());
        assertEquals(BeerType.ТЕМНЕ, firstBeer.getType());
        assertEquals(AlcoholType.АЛКОГОЛЬНЕ, firstBeer.getAl());
        assertEquals("Brewery A", firstBeer.getManufacturer());

        // Validate ingredients
        List<String> ingredients = firstBeer.getIngredients().getIngredient();
        assertTrue(ingredients.contains("Water"));
        assertTrue(ingredients.contains("Hops"));

        // Validate characteristics
        Beer.BeerItem.Chars chars = firstBeer.getChars();
        assertEquals(new BigDecimal("6.5"), chars.getAbv());
        assertEquals(new BigDecimal("20"), chars.getTransparency());
        assertEquals("Yes", chars.getFiltered());
        assertEquals(150, chars.getNutritionalValue());

        // Validate packaging
        Beer.BeerItem.Chars.Packaging packaging = chars.getPackaging();
        assertEquals(new BigDecimal("0.5"), packaging.getVolume());
        assertEquals("Glass", packaging.getMaterial());
    }

    @Test
    void testParseXML_InvalidXML_ReturnsEmptyList() {
        // Arrange
        String xmlFilePath = "src/test/resources/invalid_beer.xml"; // Ensure this file exists and is invalid

        // Act
        List<Beer.BeerItem> beerItems = stAXParser.parseXML(xmlFilePath);

        // Assert
        assertNotNull(beerItems);
        assertTrue(beerItems.isEmpty());
    }
}

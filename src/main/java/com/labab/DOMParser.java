package com.labab;

import generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DOMParser {
    private static final Logger logger = LoggerFactory.getLogger(DOMParser.class);
    private List<Beer.BeerItem> beerItems = new ArrayList<>();

    public List<Beer.BeerItem> parseXML(String xmlFilePath) {
        try {
            logger.info("DOM parsing started");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlFilePath));
            document.getDocumentElement().normalize();

            NodeList beerItemList = document.getElementsByTagName("beerItem");
            for (int i = 0; i < beerItemList.getLength(); i++) {
                Element beerItemElement = (Element) beerItemList.item(i);
                Beer.BeerItem beerItem = new Beer.BeerItem();

                // Set beerItem attributes
                beerItem.setId(BigInteger.valueOf(Long.valueOf(beerItemElement.getAttribute("id"))));
                beerItem.setName(beerItemElement.getAttribute("name"));
                beerItem.setType(BeerType.valueOf(beerItemElement.getAttribute("type").toUpperCase()));
                beerItem.setAl(AlcoholType.valueOf(beerItemElement.getAttribute("al").toUpperCase()));
                beerItem.setManufacturer(beerItemElement.getAttribute("manufacturer"));

                // Parse ingredients
                List<String> ingredients = new ArrayList<>();
                NodeList ingredientList = beerItemElement.getElementsByTagName("ingredient");
                for (int j = 0; j < ingredientList.getLength(); j++) {
                    ingredients.add(ingredientList.item(j).getTextContent().trim());
                }
                Beer.BeerItem.Ingredients ingredients1 = new Beer.BeerItem.Ingredients();
                ingredients1.setIngredient(ingredients);
                beerItem.setIngredients(ingredients1);

                // Parse characteristics (chars)
                Element charsElement = (Element) beerItemElement.getElementsByTagName("chars").item(0);
                Beer.BeerItem.Chars chars = new Beer.BeerItem.Chars();

                if (charsElement.getElementsByTagName("abv").getLength() > 0) {
                    chars.setAbv(new BigDecimal(charsElement.getElementsByTagName("abv").item(0).getTextContent().trim()));
                }

                chars.setTransparency(new BigDecimal(charsElement.getElementsByTagName("transparency").item(0).getTextContent().trim()));
                chars.setFiltered(charsElement.getElementsByTagName("filtered").item(0).getTextContent().trim());
                chars.setNutritionalValue(Integer.parseInt(charsElement.getElementsByTagName("nutritionalValue").item(0).getTextContent().trim()));

                // Parse packaging
                Element packagingElement = (Element) charsElement.getElementsByTagName("packaging").item(0);
                Beer.BeerItem.Chars.Packaging packaging = new Beer.BeerItem.Chars.Packaging();
                packaging.setVolume(new BigDecimal(packagingElement.getElementsByTagName("volume").item(0).getTextContent().trim()));
                packaging.setMaterial(packagingElement.getElementsByTagName("material").item(0).getTextContent().trim());

                chars.setPackaging(packaging);
                beerItem.setChars(chars);

                beerItems.add(beerItem);
            }
            logger.info("DOM parsing completed successfully");
        } catch (Exception e) {
            logger.error("DOM parsing failed: {}", e.getMessage());
            e.printStackTrace();
        }

        Collections.sort(beerItems, new BeerComparator()); // Sort beerItems if a comparator is available
        return beerItems;
    }
}

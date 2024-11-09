package com.labab;

import generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StAXParser {
    private static final Logger logger = LoggerFactory.getLogger(StAXParser.class);
    private List<Beer.BeerItem> beerItems = new ArrayList<>();
    private List<String> ingList = new ArrayList<>();

    public List<Beer.BeerItem> parseXML(String xmlFilePath) {
        try {
            logger.info("StAX parsing started");
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(xmlFilePath));
            Beer.BeerItem beerItem = null;
            Beer.BeerItem.Chars beerChars = null;
            Beer.BeerItem.Chars.Packaging packaging = null;
            StringBuilder data = new StringBuilder();

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase("beerItem")) {
                        beerItem = new Beer.BeerItem();
                        beerItem.setId(BigInteger.valueOf(Long.parseLong(startElement.getAttributeByName(new QName("id")).getValue())));
                        beerItem.setName(startElement.getAttributeByName(new QName("name")).getValue());
                        beerItem.setType(BeerType.valueOf(startElement.getAttributeByName(new QName("type")).getValue().toUpperCase()));
                        beerItem.setAl(AlcoholType.valueOf(startElement.getAttributeByName(new QName("al")).getValue().toUpperCase()));
                        beerItem.setManufacturer(startElement.getAttributeByName(new QName("manufacturer")).getValue());
                    } else if (qName.equalsIgnoreCase("chars")) {
                        beerChars = new Beer.BeerItem.Chars();
                    } else if (qName.equalsIgnoreCase("packaging")) {
                        packaging = new Beer.BeerItem.Chars.Packaging();
                    }
                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    String qName = endElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase("ingredient")) {
                        ingList.add(data.toString().trim());
                        Beer.BeerItem.Ingredients ingredients = new Beer.BeerItem.Ingredients();
                        ingredients.setIngredient(ingList);
                        beerItem.setIngredients(ingredients);
                    } else if (qName.equalsIgnoreCase("abv")) {
                        beerChars.setAbv(new BigDecimal(data.toString().trim()));
                    } else if (qName.equalsIgnoreCase("transparency")) {
                        beerChars.setTransparency(new BigDecimal(data.toString().trim()));
                    } else if (qName.equalsIgnoreCase("filtered")) {
                        beerChars.setFiltered(data.toString().trim());
                    } else if (qName.equalsIgnoreCase("nutritionalValue")) {
                        beerChars.setNutritionalValue(Integer.parseInt(data.toString().trim()));
                    } else if (qName.equalsIgnoreCase("volume")) {
                        packaging.setVolume(new BigDecimal(data.toString().trim()));
                    } else if (qName.equalsIgnoreCase("material")) {
                        packaging.setMaterial(data.toString().trim());
                    } else if (qName.equalsIgnoreCase("packaging")) {
                        beerChars.setPackaging(packaging);
                    } else if (qName.equalsIgnoreCase("chars")) {
                        beerItem.setChars(beerChars);
                    } else if (qName.equalsIgnoreCase("beerItem")) {
                        beerItems.add(beerItem);
                    }
                    data.setLength(0); // Clear StringBuilder after each element
                } else if (event.isCharacters()) {
                    data.append(event.asCharacters().getData());
                }
            }
            logger.info("StAX parsing completed successfully");
        } catch (Exception e) {
            logger.error("StAX parsing failed: {}", e.getMessage());
            e.printStackTrace();
        }
        Collections.sort(beerItems, new BeerComparator()); // Sort items if comparator available
        return beerItems;
    }
}

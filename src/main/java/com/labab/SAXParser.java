package com.labab;

import generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SAXParser extends DefaultHandler {

    private static final Logger logger = LoggerFactory.getLogger(SAXParser.class);
    private List<Beer.BeerItem> beerItems = new ArrayList<>();
    private Beer.BeerItem beerItem;
    private Beer.BeerItem.Chars beerChars;
    private Beer.BeerItem.Chars.Packaging packaging;
    private StringBuilder data;
    private List<String> ingList = new ArrayList<>();

    public List<Beer.BeerItem> parseXML(String xmlFilePath) {
        try {
            logger.info("SAX parsing started");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(xmlFilePath), this);
            logger.info("SAX parsing completed successfully");
        } catch (Exception e) {
            logger.error("SAX parsing failed: {}", e.getMessage());
            e.printStackTrace();
        }
        Collections.sort(beerItems, new BeerComparator()); // Sort if a comparator is available
        return beerItems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        data = new StringBuilder();
        if (qName.equalsIgnoreCase("beerItem")) {
            beerItem = new Beer.BeerItem();
            beerItem.setId(BigInteger.valueOf(Long.parseLong(attributes.getValue("id"))));
            beerItem.setName(attributes.getValue("name"));
            beerItem.setType(BeerType.valueOf(attributes.getValue("type").toUpperCase()));
            beerItem.setAl(AlcoholType.valueOf(attributes.getValue("al").toUpperCase()));
            beerItem.setManufacturer(attributes.getValue("manufacturer"));
        } else if (qName.equalsIgnoreCase("chars")) {
            beerChars = new Beer.BeerItem.Chars();
        } else if (qName.equalsIgnoreCase("packaging")) {
            packaging = new Beer.BeerItem.Chars.Packaging();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
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
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}

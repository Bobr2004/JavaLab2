<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!-- Main template -->
    <xsl:template match="/">
        <GroupedBeers>
            <!-- Get unique values of Manufacturer -->
            <xsl:for-each select="//beerItem[not(@manufacturer=preceding-sibling::beerItem/@manufacturer)]">
                <Group>
                    <Manufacturer>
                        <xsl:value-of select="@manufacturer"/>
                    </Manufacturer>
                    <BeersByManufacturer>
                        <!-- Group beers by their Manufacturer -->
                        <xsl:for-each select="//beerItem[@manufacturer=current()/@manufacturer]">
                            <Beer id="{@id}">
                                <Name><xsl:value-of select="@name"/></Name>
                                <Type><xsl:value-of select="@type"/></Type>
                                <AlcoholType><xsl:value-of select="@al"/></AlcoholType>
                                <Ingredients>
                                    <xsl:for-each select="ingredients/ingredient">
                                        <Ingredient><xsl:value-of select="."/></Ingredient>
                                    </xsl:for-each>
                                </Ingredients>
                                <Characteristics>
                                    <ABV><xsl:value-of select="chars/abv"/></ABV>
                                    <Transparency><xsl:value-of select="chars/transparency"/></Transparency>
                                    <Filtered><xsl:value-of select="chars/filtered"/></Filtered>
                                    <NutritionalValue><xsl:value-of select="chars/nutritionalValue"/></NutritionalValue>
                                    <Packaging>
                                        <Volume><xsl:value-of select="chars/packaging/volume"/></Volume>
                                        <Material><xsl:value-of select="chars/packaging/material"/></Material>
                                    </Packaging>
                                </Characteristics>
                            </Beer>
                        </xsl:for-each>
                    </BeersByManufacturer>
                </Group>
            </xsl:for-each>
        </GroupedBeers>
    </xsl:template>
</xsl:stylesheet>

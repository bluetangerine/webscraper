package uk.co.bluetangerine.pagescraper.ParserUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by tony on 15/11/2016.
 * Test class to test parsing utilities
 */
public class ParsingUtilsTest {
    private ParsingUtils underTest = new ParsingUtils();

    @Test
    public void givenStringContainsValidValueThenextractPriceFromString() {
        String stringContainingCurrencyValue = "£10.99GBP";
        String result = underTest.extractPriceFromString(stringContainingCurrencyValue);
        Assert.assertEquals(result, "10.99");
    }

    @Test
    public void givenStringContainsNoMantissaThenReturnCharacteristicSpotZero() {
        String stringWithNoValue = "NoFactionalValue23Here";
        String result = underTest.extractPriceFromString(stringWithNoValue);
        Assert.assertEquals(result, "23.00");
    }

    @Test
    public void givenStringContainsNoValueThenReturnZero() {
        String stringWithNoValue = "NoValueHere";
        String result = underTest.extractPriceFromString(stringWithNoValue);
        Assert.assertEquals(result, "0.00");
    }

}

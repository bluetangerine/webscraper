package uk.co.bluetangerine.pagescraper.ParserUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tony on 16/11/2016.
 * Class created to contain reusable utils
 * that can assist in the parsing process
 * across and number of implementations
 */
public class ParsingUtils {
    /**
     * Extract just the decimal price from a string
     * removing and currency symbols or suffixes etc
     * @param priceString Extracted price String
     * @return String value of value alone
     */
    public String extractPriceFromString(String priceString) {
        Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher matcher = regex.matcher(priceString);
        if (matcher.find()) {
            BigDecimal bd = new BigDecimal(matcher.group(1));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bd.toString();
        }

        //Should there be no parsable value, assume 0.00
        return "0.00";
    }
}

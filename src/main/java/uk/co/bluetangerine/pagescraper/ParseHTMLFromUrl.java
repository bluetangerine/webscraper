package uk.co.bluetangerine.pagescraper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uk.co.bluetangerine.pagescraper.ParserUtils.DocumentHelper;
import uk.co.bluetangerine.pagescraper.ParserUtils.ParsingUtils;
import uk.co.bluetangerine.pagescraper.dto.ProductDto;
import uk.co.bluetangerine.pagescraper.dto.ResultsDto;
import uk.co.bluetangerine.pagescraper.dto.SubProductDto;

/**
 * Created by tony on 16/11/2016.
 * Parser implementation for HTMLfromURL.
 * Deals with parsing parent page and child pages
 */

class ParseHTMLFromUrl {
    private DocumentHelper docHelper = new DocumentHelper();

    ParseHTMLFromUrl() {
    }

    /**
     * Constructor to assist TDD
     *
     * @param docHelper
     */
    ParseHTMLFromUrl(DocumentHelper docHelper) {
        this.docHelper = docHelper;
    }

    /**
     * private package access as we are using from
     * same package
     *
     * @param url Resource location
     * @return String json results
     * @throws IOException
     */
    String parse(String url) throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        ResultsDto results = parseToDto(url);
        return gson.toJson(results);
    }

    /**
     * Parse the main parent page. Parses main parent page to
     * extract Title and unit price also keep a running total.
     * Calls the child page method to populate rest of dto fields.
     *
     * @param url Url for the parent page
     * @return populated ResultsDto object
     * @throws IOException
     */
    private ResultsDto parseToDto(String url) throws IOException {
        //Create one resultsDto to contain all products found
        //and provide a running total
        ResultsDto resultsDto = new ResultsDto();
        List<ProductDto> productDtos = new ArrayList<ProductDto>();
        Document doc = docHelper.getDocumentHelper(url);
        BigDecimal runningTotal = new BigDecimal("0");
        ParsingUtils utils = new ParsingUtils();

        //Collate all the productInner elements that contains the product list
        Elements products = doc.getElementsByClass("productInner");

        //Iterate the product items to populate some of the Dto fields
        for (Element item : products) {
            ProductDto itemDto = new ProductDto();
            //ProductInner has only one productInfo so safe to assume 1 will exist and use it.
            Element productInfo = item.getElementsByClass("productInfo").first();
            itemDto.setTitle(StringEscapeUtils.unescapeHtml4(productInfo.select("a[href]").first().ownText()));
            itemDto.setUnitPrice(utils.extractPriceFromString(StringEscapeUtils.unescapeHtml4(item.getElementsByClass("pricePerUnit").text())));
            runningTotal = runningTotal.add(new BigDecimal(itemDto.getUnitPrice()));
            //Call the sub page function with the extracted URL and allow it to populate rest of Dto fields
            SubProductDto subProductDto = parseSubPageUrl(productInfo.select("a").first().attr("href"));
            //As spec requires flat json product structure, set values from subProductDto object
            itemDto.setDescription(subProductDto.getDescription());
            itemDto.setSize(subProductDto.getSize());
            productDtos.add(itemDto);
        }

        resultsDto.setProducts(productDtos);
        resultsDto.setTotal(runningTotal);
        return resultsDto;
    }

    /**
     * Parse the child page. Parses child page to
     * extract description and size and populate
     * remaining fields on the Dto.
     * Access modifier set to protected to allow unit testing
     *
     * @param childUrl Url for the child page
     * @return ProductDto new copy of product DTO with additional fields populated
     */
    private SubProductDto parseSubPageUrl(String childUrl) throws IOException {
        SubProductDto result = new SubProductDto();
        Document doc = docHelper.getDocumentHelper(childUrl);

        //For each productDataItemHeader ...
        Elements productDataItemHeaders = doc.getElementsByClass("productDataItemHeader");
        //... there is a corresponding product text
        Elements productTexts = doc.getElementsByClass("productText");

        //Seek out Description and Size to populate Dto using the Text of productDataItemHeader.
        //The corresponding element in productText holds the value
        for (int i = 0; i < productDataItemHeaders.size(); i++) {
            if (productDataItemHeaders.get(i).text().equals("Description")) {
                result.setDescription(StringEscapeUtils.unescapeHtml4(productTexts.get(i).text()));
            } else if (productDataItemHeaders.get(i).text().equals("Size")) {
                result.setSize(StringEscapeUtils.unescapeHtml4(productTexts.get(i).text()));
            }
        }
        return result;
    }
}

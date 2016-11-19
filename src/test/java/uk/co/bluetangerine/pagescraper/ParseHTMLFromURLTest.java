package uk.co.bluetangerine.pagescraper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.*;
import uk.co.bluetangerine.pagescraper.ParserUtils.DocumentHelper;
import uk.co.bluetangerine.pagescraper.dto.ProductDto;
import uk.co.bluetangerine.pagescraper.dto.ResultsDto;
import uk.co.bluetangerine.pagescraper.dto.SubProductDto;

/**
 * Test class to mock up JSoup to replicate
 * a scrapped page content. At this stage the
 * assumption is a valid endpoint matching the requirements.
 * Ultimately, with further checks in place to handle exceptions
 * more tests would added to ensure desired handling of any exceptions
 * Created by tony on 15/11/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class ParseHTMLFromURLTest {
    @Mock
    Jsoup jsoupMock;

    @Mock
    DocumentHelper docHelperMock;

    private ParseHTMLFromUrl underTest;

    @Before
    public void setUp() throws Exception {
        jsoupMock = Mockito.mock(Jsoup.class);
        docHelperMock = Mockito.mock(DocumentHelper.class);
        underTest = new ParseHTMLFromUrl(docHelperMock);
    }

    @Test
    public void givenResultsDTOObjectThenReturnJson() throws IOException {
        ResultsDto results = new ResultsDto();
        ProductDto Product1Dto = new ProductDto();
        ProductDto Product2Dto = new ProductDto();

        Product1Dto.setSize("5pack");
        Product1Dto.setDescription("Product description");
        Product1Dto.setTitle("Product Title");
        Product1Dto.setUnitPrice("£1.56GBP");

        Product2Dto.setSize("4Items");
        Product2Dto.setDescription("Product2 description");
        Product2Dto.setTitle("Product2 Title");
        Product2Dto.setUnitPrice("£3.96GBP");
        List<ProductDto> products = new ArrayList<ProductDto>();
        products.add(0, Product1Dto);
        products.add(1, Product2Dto);
        results.setProducts(products);
        results.setTotal(new BigDecimal("5.52"));

        ParseHTMLFromUrl underTestSpy = Mockito.spy(underTest);
        Mockito.doReturn(results).when(underTestSpy).parseToDto(Mockito.anyString());

        String jsonResponse = underTestSpy.parse("URL");

        assertNotNull(jsonResponse);

        JSONObject jsonObj = new JSONObject(jsonResponse);

        JSONArray array = jsonObj.getJSONArray("results");
        assertEquals(array.getJSONObject(0).get("title"), "Product Title");
        assertEquals(array.getJSONObject(0).get("size"), "5pack");
        assertEquals(array.getJSONObject(0).get("unit_price"), "£1.56GBP");
        assertEquals(array.getJSONObject(0).get("description"), "Product description");

        assertEquals(array.getJSONObject(1).get("title"), "Product2 Title");
        assertEquals(array.getJSONObject(1).get("size"), "4Items");
        assertEquals(array.getJSONObject(1).get("unit_price"), "£3.96GBP");
        assertEquals(array.getJSONObject(1).get("description"), "Product2 description");
    }

    @Test
    public void givenUrlThenScrapeParentPageToDto() throws IOException {
        ParseHTMLFromUrl underTestSpy = Mockito.spy(underTest);
        setUpMockDocumentStructure();
        SubProductDto subProduct = new SubProductDto();
        //Just satisfy call out to sub method so not nulls
        subProduct.setDescription("description");
        subProduct.setSize("size");
        Mockito.doReturn(subProduct).when(underTestSpy).parseSubPageUrl(anyString());

        ResultsDto results = underTestSpy.parseToDto("someUrl");

        Assert.assertNotNull(results);
        Assert.assertTrue(results.getProducts().size() == 2);
        Assert.assertEquals(results.getProducts().get(0).getTitle(), "Title1");
        Assert.assertEquals(results.getProducts().get(0).getUnitPrice(), "123.45");
    }

    @Test
    public void givenUrlThenScrapeChildPageToDto() throws IOException {
        setUpMockDocumentStructure();
        SubProductDto result = underTest.parseSubPageUrl("anyUrl");
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getSize(), "53Pieces");
        Assert.assertEquals(result.getDescription(), "the description");
    }

    /**
     * This is more of an integration test across json parsing,
     * Parent page and child page scraping
     */
    @Test
    public void givenHtmlDocumentThenReturnJson() throws Exception {
        setUpMockDocumentStructure();

        String response = underTest.parse("someUrl");
        assertNotNull(response);

        JSONObject jsonObj = new JSONObject(response);

        JSONArray array = jsonObj.getJSONArray("results");
        assertEquals(array.getJSONObject(0).get("title"), "Title1");
        assertEquals(array.getJSONObject(0).get("size"), "53Pieces");
        assertEquals(array.getJSONObject(0).get("unit_price"), "123.45");
        assertEquals(array.getJSONObject(0).get("description"), "the description");

        assertEquals(array.getJSONObject(1).get("title"), "Title2");
        assertEquals(array.getJSONObject(1).get("size"), "16Parts");
        assertEquals(array.getJSONObject(1).get("unit_price"), "321.54");
        assertEquals(array.getJSONObject(1).get("description"), "the 2nd description");
    }

    private void setUpMockDocumentStructure() throws IOException {
        Document doc = mock(Document.class);
        Iterator iterator = Mockito.mock(Iterator.class);
        when(docHelperMock.getDocumentHelper(anyString())).thenReturn(doc);
        Elements elementsMock = mock(Elements.class);
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        Elements productInfo = mock(Elements.class);
        Elements childProductInfo = mock(Elements.class);
        Elements pricePerUnits = mock(Elements.class);
        Element productInfo1 = mock(Element.class);
        Element productInfo2 = mock(Element.class);
        Element pricePerUnit1 = mock(Element.class);
        Element pricePerUnit2 = mock(Element.class);

        when(doc.getElementsByClass("productInner")).thenReturn(elementsMock);
        when(elementsMock.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(iterator.next()).thenReturn(element1).thenReturn(element2);
        when(element1.getElementsByClass("productInfo")).thenReturn(productInfo);
        when(element1.getElementsByClass("pricePerUnit")).thenReturn(pricePerUnits);
        when(element2.getElementsByClass("productInfo")).thenReturn(productInfo);
        when(element2.getElementsByClass("pricePerUnit")).thenReturn(pricePerUnits);
        when(productInfo.first()).thenReturn(productInfo1).thenReturn(productInfo2);
        when(productInfo1.select("a")).thenReturn(childProductInfo).thenReturn(childProductInfo);
        when(productInfo2.select("a")).thenReturn(childProductInfo).thenReturn(childProductInfo);
        when(productInfo1.select("a[href]")).thenReturn(childProductInfo).thenReturn(childProductInfo);
        when(productInfo2.select("a[href]")).thenReturn(childProductInfo).thenReturn(childProductInfo);
        when(childProductInfo.first()).thenReturn(productInfo1).thenReturn(productInfo2);
        when(childProductInfo.text()).thenReturn("123.45").thenReturn("321.54");
        when(productInfo1.ownText()).thenReturn("Title1");
        when(productInfo2.ownText()).thenReturn("Title2");
        when(pricePerUnits.text()).thenReturn("123.45").thenReturn("321.54");
        when(productInfo1.attr("href")).thenReturn("SubPageURL");
        when(productInfo2.attr("href")).thenReturn("SubPageURL2");
        when(pricePerUnits.get(0)).thenReturn(pricePerUnit1).thenReturn(pricePerUnit2);

        //SubPage
        Elements productDataItemHeadersMock = mock(Elements.class);
        Elements productTextsMock = mock(Elements.class);
        Element productDataItemHeaderMock1 = mock(Element.class);
        Element productDataItemHeaderMock2 = mock(Element.class);
        Element productDataItemHeaderMock3 = mock(Element.class);
        Element productTextMock1 = mock(Element.class);
        Element productTextMock2 = mock(Element.class);
        Element productTextMock3 = mock(Element.class);
        when(doc.getElementsByClass("productDataItemHeader")).thenReturn(productDataItemHeadersMock);
        when(doc.getElementsByClass("productText")).thenReturn(productTextsMock);
        when(productDataItemHeadersMock.size()).thenReturn(3);
        when(productTextsMock.get(0)).thenReturn(productTextMock1);
        when(productTextsMock.get(1)).thenReturn(productTextMock2);
        when(productTextsMock.get(2)).thenReturn(productTextMock3);
        when(productDataItemHeadersMock.get(0)).thenReturn(productDataItemHeaderMock1);
        when(productDataItemHeadersMock.get(1)).thenReturn(productDataItemHeaderMock2);
        when(productDataItemHeadersMock.get(2)).thenReturn(productDataItemHeaderMock3);
        when(productDataItemHeaderMock1.text()).thenReturn("Description");
        when(productDataItemHeaderMock2.text()).thenReturn("Nutrition");
        when(productDataItemHeaderMock3.text()).thenReturn("Size");

        when(productTextMock1.text()).thenReturn("the description").thenReturn("the 2nd description");
        when(productTextMock2.text()).thenReturn("Nutrition1").thenReturn("Nutrition2");
        when(productTextMock3.text()).thenReturn("53Pieces").thenReturn("16Parts");
    }
}

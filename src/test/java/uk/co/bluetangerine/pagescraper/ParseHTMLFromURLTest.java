package uk.co.bluetangerine.pagescraper;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.*;
import uk.co.bluetangerine.pagescraper.ParserUtils.DocumentHelper;

/**
 * Test class to mock up JSoup to replicate
 * a scrapped page content. At this stage the
 * assumption is a valid endpoint matching the requirements.
 * Ulitmately, with further checks in place to handle exceptions
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

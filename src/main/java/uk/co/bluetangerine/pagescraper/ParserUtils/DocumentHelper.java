package uk.co.bluetangerine.pagescraper.ParserUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by tony on 16/11/2016.
 * Class to manage JSoup document creation.
 * By having as a seperate class, allows us
 * to mock for testing
 */
public class DocumentHelper {
    public Document getDocumentHelper(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}

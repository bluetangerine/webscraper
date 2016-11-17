package uk.co.bluetangerine.pagescraper;

import java.io.IOException;

/**
 * Created by tony on 16/11/2016.
 * A command implementation for ParseHTMLFromURL
 */
class ParseHTMLFromUrlCommand implements Command {
    private ParseHTMLFromUrl parseHTMLFromUrl;

    ParseHTMLFromUrlCommand(ParseHTMLFromUrl parseHTMLFromUrl) {
        this.parseHTMLFromUrl = parseHTMLFromUrl;
    }

    public String parse(String url) throws IOException {
        return parseHTMLFromUrl.parse(url);
    }
}

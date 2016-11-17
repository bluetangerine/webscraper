package uk.co.bluetangerine.pagescraper;

import java.io.IOException;

/**
 * Created by tony on 16/11/2016.
 * Main entry point for command line.
 * Keep the entry point as lightweight as possible
 *
 */
public class WebPageScraper {
    public static void main (String args[]) throws IOException {
        ParserControl parserControl = new ParserControl();
        ParseHTMLFromUrl parserImpl = new ParseHTMLFromUrl();
        Command parseHtmlControl = new ParseHTMLFromUrlCommand(parserImpl);

        parserControl.setCommand(parseHtmlControl);
        System.out.println(parserControl.ExecuteParser(args[0]));
    }
}


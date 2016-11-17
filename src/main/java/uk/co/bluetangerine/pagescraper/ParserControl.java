package uk.co.bluetangerine.pagescraper;

import java.io.IOException;

/**
 * Created by tony on 16/11/2016.
 * ParserControl to allow common interface should
 * any other parsers be introduced
 */
class ParserControl {
    private Command command;

    void setCommand(Command command) {
        this.command = command;
    }

    String ExecuteParser(String resourceLocation) throws IOException {
        return command.parse(resourceLocation);
    }
}

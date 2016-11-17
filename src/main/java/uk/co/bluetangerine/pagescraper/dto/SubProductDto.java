package uk.co.bluetangerine.pagescraper.dto;

/**
 * Created by tony on 16/11/2016.
 * Ultimately could be used as an
 * object  in ProductDto in place of
 * size and description, however, alters the
 * json response from the spec. Currently
 * used to collect date from subpage and
 * assign to main DTO
 */
public class SubProductDto {
    private String size;
    private String description;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

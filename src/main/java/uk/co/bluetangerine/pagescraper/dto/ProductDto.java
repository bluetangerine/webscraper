package uk.co.bluetangerine.pagescraper.dto;

/**
 * Created by tony on 16/11/2016.
 * Dto class for product details
 */
public class ProductDto {
    private String title;
    private String size;
    private String unit_price;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUnitPrice() {
        return unit_price;
    }

    public void setUnitPrice(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

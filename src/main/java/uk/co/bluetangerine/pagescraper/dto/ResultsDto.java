package uk.co.bluetangerine.pagescraper.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tony on 16/11/2016.
 * Wrapper Dto for list of products and total
 */
public class ResultsDto {
    private List<ProductDto> results;
    private BigDecimal total;

    public List<ProductDto> getProducts() {
        return results;
    }

    public void setProducts(List<ProductDto> products) {
        this.results = products;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}

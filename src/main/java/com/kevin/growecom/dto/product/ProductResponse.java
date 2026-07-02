package com.kevin.growecom.dto.product;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer totalStock;
}

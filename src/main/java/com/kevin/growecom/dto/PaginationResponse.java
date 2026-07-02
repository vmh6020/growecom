package com.kevin.growecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class PaginationResponse<T> {
    private List<T> result;
    private MetaDTO meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MetaDTO {
        private int page;
        private int pageSize;
        private int pages;
        private Long total;
    }
}

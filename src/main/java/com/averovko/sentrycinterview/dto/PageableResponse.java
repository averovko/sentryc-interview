package com.averovko.sentrycinterview.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PageableResponse<T> {
    List<T> data;
    PageMeta meta;

    public static <T> PageableResponse<T> of(Page<T> page) {
        var meta = new PageMeta(page.getTotalPages(), page.getNumber(), page.getSize(), page.getTotalElements());
        return new PageableResponse<>(page.getContent(), meta);
    }

    @Value
    public static class PageMeta {
        int totalPages;
        int page;
        int size;
        long totalElements;
    }
}

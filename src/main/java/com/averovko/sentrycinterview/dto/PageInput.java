package com.averovko.sentrycinterview.dto;

import lombok.Value;

@Value
public class PageInput {
    private final int page;
    private final int size;
}

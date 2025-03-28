package com.paulvalue.servicebot.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private String name;
    private Long parentId;

}

package com.epam.esm.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FilterParams {

    private List<String> tags;
    private String part;
    private String sortBy;
    private SortType type;
}

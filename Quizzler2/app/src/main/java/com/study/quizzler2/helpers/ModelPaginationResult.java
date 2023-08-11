package com.study.quizzler2.helpers;

import java.util.List;

public class ModelPaginationResult<T> {
    private List<T> data;
    private String nextToken;

    public ModelPaginationResult(List<T> data, String nextToken) {
        this.data = data;
        this.nextToken = nextToken;
    }

    public List<T> getData() {
        return data;
    }

    public String getNextToken() {
        return nextToken;
    }
}

package com.epam.esm.model.service;

import com.epam.esm.model.service.exception.IncorrectArgumentException;

public class Page {

    private int page;
    private int size;
    private long countEntity;

    public Page(int page, int size, long countEntity) throws IncorrectArgumentException {
        if (page < 0) {
            throw new IncorrectArgumentException("Page index must not be less than zero!");
        }
        this.page = page;
        if (size < 1) {
            throw new IncorrectArgumentException("Page size must not be less than one!");
        }
        if ((page - 1) * size >= countEntity) {
            throw new IncorrectArgumentException("Page with index=" + page + " not exist!");
        }
        this.size = size;
        this.countEntity = countEntity;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}

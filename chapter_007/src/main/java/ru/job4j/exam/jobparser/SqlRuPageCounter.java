package ru.job4j.exam.jobparser;

import java.util.concurrent.atomic.AtomicInteger;

public class SqlRuPageCounter implements PageCounter {

    private final AtomicInteger currentPageNumber;
    private final String urlPagingFormat;

    public SqlRuPageCounter(String urlPagingFormat, int initValue) {
        this.urlPagingFormat = urlPagingFormat;
        this.currentPageNumber = new AtomicInteger(initValue);
    }

    public SqlRuPageCounter() {
        this("%s/%s", 1);
    }

    public String getNextPage(String url) {
        return String.format(urlPagingFormat, url, currentPageNumber.getAndIncrement());
    }

    @Override
    public String getPreviousPage(String url) {
        return String.format(urlPagingFormat, url, currentPageNumber.getAndDecrement());
    }
}

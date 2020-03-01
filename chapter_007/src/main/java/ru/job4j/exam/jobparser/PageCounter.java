package ru.job4j.exam.jobparser;

public interface PageCounter {

    String getNextPage(String url);

    String getPreviousPage(String url);

}

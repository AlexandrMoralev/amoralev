package ru.job4j.inputoutput.basics;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import static java.util.function.Predicate.not;

public class EvenNumberFile {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("even.txt")) {
            int read;
            StringBuilder text = new StringBuilder();
            while ((read = fis.read()) != -1) {
                text.append((char) read);
            }
            Arrays.stream(text.toString().split(System.lineSeparator()))
                    .map(String::strip)
                    .filter(not(String::isBlank))
                    .map(Integer::valueOf)
                    .forEach(checkEvenness);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Consumer<Integer> checkEvenness = number ->
            System.out.println(String.format("%s is %s", number, number % 2 == 0 ? "even" : "odd"));

}

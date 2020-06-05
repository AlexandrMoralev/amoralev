package ru.job4j.inputoutput.basics;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadFile {
    public static void main(String[] args) {
        try(FileInputStream fis = new FileInputStream("input.txt")) {
            StringBuilder text = new StringBuilder();
            int read;
            while ((read = fis.read()) != -1) {
                text.append((char) read);
            }
//            System.out.println(text);
            String[] lines = text.toString().split(System.lineSeparator());
            for (String line: lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

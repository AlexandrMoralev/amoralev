package ru.job4j.inputoutput.basics;

import ru.job4j.array.Matrix;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ResultFile {

    public static final int DIMENSION = 10;
    public static final String LINE_SEPARATOR = System.lineSeparator();

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("result.txt")) {

            Matrix matrix = new Matrix();

            int[][] result = matrix.multiple(DIMENSION);

            // java 7
            for (int[] rows : result) {
                for (int element : rows) {
                    fos.write((" " + element).getBytes(StandardCharsets.UTF_8));
                }
                fos.write(LINE_SEPARATOR.getBytes(StandardCharsets.UTF_8));
            }

            // java 8+
            String table = Arrays.stream(result)
                    .map(arr -> Arrays.stream(arr)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(" ")) + LINE_SEPARATOR)
                    .collect(Collectors.joining());
            fos.write(table.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

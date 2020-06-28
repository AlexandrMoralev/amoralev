package ru.job4j.util;

import ru.job4j.common.NamedArgs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import static ru.job4j.common.Constants.OUTPUT_FILEPATH;

public final class ResultWriter {

    public static void writeToFile(Collection<String> data, NamedArgs params) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(params.get(OUTPUT_FILEPATH)))) {
            for (String item : data) {
                writer.write(item);
                writer.newLine();
            }
            writer.flush();
        }
    }
}

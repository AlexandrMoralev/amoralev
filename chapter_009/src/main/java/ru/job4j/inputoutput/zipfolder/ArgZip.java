package ru.job4j.inputoutput.zipfolder;

import ru.job4j.inputoutput.namedargs.ArgsName;

import java.util.List;
import java.util.Objects;

public class ArgZip {

    private static final String DIRECTORY_LITERAL = "d";
    private static final String EXCLUSION_LITERAL = "e";
    private static final String OUTPUT_LITERAL = "o";

    private static final List<String> KEYS = List.of(
            DIRECTORY_LITERAL,
            EXCLUSION_LITERAL,
            OUTPUT_LITERAL
    );

    private final ArgsName args;

    public ArgZip(String[] args) {
        this.args = ArgsName.of(args);
    }

    public boolean isValid() {
        return KEYS.stream()
                .map(this.args::get)
                .noneMatch(Objects::isNull);
    }

    public String directory() {
        return this.args.get(DIRECTORY_LITERAL);
    }

    public String exclude() {
        return this.args.get(EXCLUSION_LITERAL);
    }

    public String output() {
        return this.args.get(OUTPUT_LITERAL);
    }
}

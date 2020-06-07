package ru.job4j.inputoutput.serveravailabilityanalysis;

import java.util.Arrays;
import java.util.Optional;

public enum StatusCode {
    OK("200"),
    REDIRECT("300"),
    BAD_REQUEST("400"),
    SERVER_ERROR("500");

    public final String code;

    StatusCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static Optional<StatusCode> valueOfCode(String code) {
        return Arrays.stream(values()).filter(v -> code.equals(v.code)).findFirst();
    }
}

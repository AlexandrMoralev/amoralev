package ru.job4j.inputoutput.consolechat;

import java.io.*;

public class ConsoleImpl implements Console {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ConsoleImpl(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    public String readLine() {
        try {
            return reader.readLine().strip();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void write(String answer) {
        try {
            writer.newLine();
            writer.write(answer);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

package ru.job4j.inputoutput.files;

import java.io.File;

public class Dir {

    public static void main(String[] args) {
        File file = new File("c:\\projects");
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("Not exists %s", file.getAbsoluteFile()));
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(String.format("Not directory %s", file.getAbsoluteFile()));
        }
        for (File subfile : file.listFiles()) {
            File f = subfile.getAbsoluteFile();
            if (f.isFile()) {
                System.out.println(String.format("filename: %s, size: %s bytes", f.getName(), f.length()));
            }
        }
    }
}

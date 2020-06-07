package ru.job4j.inputoutput.files;

import java.io.File;

public class Dir {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Root folder is null. Usage java -jar dir.jar ROOT_FOLDER");
        }
        File file = new File(args[0]);
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

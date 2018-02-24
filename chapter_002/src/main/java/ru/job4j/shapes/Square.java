package ru.job4j.shapes;

public class Square implements Shape {
    @Override
    public String draw() {
        StringBuilder pic = new StringBuilder();
        String separator = System.lineSeparator();

        pic.append("4444");
        pic.append(separator);
        pic.append("1  1");
        pic.append(separator);
        pic.append("1  1");
        pic.append(separator);
        pic.append("4444");
        pic.append(separator);

        return pic.toString();
    }
}

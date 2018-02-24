package ru.job4j.shapes;

public class Triangle implements Shape {
    @Override
    public String draw() {
        StringBuilder pic = new StringBuilder();
        String separator = System.lineSeparator();

        pic.append("  *  ");
        pic.append(separator);
        pic.append(" *** ");
        pic.append(separator);
        pic.append("*****");
        pic.append(separator);

        return pic.toString();
    }
}

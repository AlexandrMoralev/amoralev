package ru.job4j.util;

public final class RegexUtil {

    public static String wildcardToRegex(String wildcard) {
        StringBuffer buffer = new StringBuffer(wildcard.length());
        buffer.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch (c) {
                case '*':
                    buffer.append(".*");
                    break;
                case '?':
                    buffer.append(".");
                    break;
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    buffer.append("\\");
                    buffer.append(c);
                    break;
                default:
                    buffer.append(c);
                    break;
            }
        }
        buffer.append('$');
        return (buffer.toString());
    }

}

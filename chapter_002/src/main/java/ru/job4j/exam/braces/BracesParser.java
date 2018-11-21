package ru.job4j.exam.braces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * BracesParser
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BracesParser {

    private final List<BracketsPair> bracketsList;
    private final Stack<Character> bracesStack;
    private final Stack<Integer> indexStack;

    /**
     * BracesParser instance constructor
     */
    public BracesParser() {
        this.bracketsList = new ArrayList<>();
        this.bracesStack = new Stack<>();
        this.indexStack = new Stack<>();
    }

    /**
     * Method parse - parsing round, square and curly brackets from a string
     *
     * First, validates string for brackets:
     * valid strings "{{}} [] " or "[{}{}]", invalid example - "{[}]".
     * For valid strings, returns a List of all pairs of brackets with opening and closing positions.
     * For invalid strings, returns an EmptyList.
     *
     * @param str String to be parsed, for null references throws IllegalArgumentException
     * @return List<BracketsPair> of brackets pairs, when str is valid,
     * otherwise returns EmptyList.
     */
    public List<BracketsPair> parse(final String str) {
        boolean isWorkable = checkArg(str);
        char[] chars = str.toCharArray();
        for (int index = 0; index < chars.length; index++) {
            char ch = chars[index];
            if (isNotBracket(ch)) {
                continue;
            }
            if (isOpenedBrace(ch)) {
                bracesStack.push(ch);
                indexStack.push(index);
                continue;
            }
            if (isClosedBrace(ch)) {
                if (bracesStack.isEmpty()) {
                    break;
                }
                if (matchedPair(ch)) {
                    bracesStack.pop();
                    this.bracketsList.add(new BracketsPair(indexStack.pop(), index));
                    continue;
                }
                break;
            }
        }
        return (isWorkable & bracesStack.empty()) ? this.bracketsList : Collections.EMPTY_LIST;
    }

    private boolean checkArg(final String string) {
        if (string == null) {
            throw new IllegalArgumentException();
        }
        return !string.isEmpty();
    }

    private boolean matchedPair(char character) {
        return (character == Brackets.CLOSED_ROUND
                & bracesStack.peek() == Brackets.OPENED_ROUND)
                || (character == Brackets.CLOSED_SQUARE
                & bracesStack.peek() == Brackets.OPENED_SQUARE)
                || (character == Brackets.CLOSED_CURLY
                & bracesStack.peek() == Brackets.OPENED_CURLY);
    }

    private boolean isOpenedBrace(Character ch) {
        return ch == Brackets.OPENED_ROUND
                || ch == Brackets.OPENED_SQUARE
                || ch == Brackets.OPENED_CURLY;
    }

    private boolean isClosedBrace(Character ch) {
        return ch == Brackets.CLOSED_ROUND
                || ch == Brackets.CLOSED_SQUARE
                || ch == Brackets.CLOSED_CURLY;
    }

    private boolean isNotBracket(Character ch) {
        return Character.isLetter(ch)
                || Character.isDigit(ch)
                || Character.isWhitespace(ch);
    }
}

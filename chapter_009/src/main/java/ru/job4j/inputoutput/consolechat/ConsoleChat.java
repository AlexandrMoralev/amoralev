package ru.job4j.inputoutput.consolechat;

import ru.job4j.inputoutput.consolechat.commands.DefaultCommand;
import ru.job4j.inputoutput.consolechat.commands.EndChatCommand;
import ru.job4j.inputoutput.consolechat.commands.ProceedCommand;
import ru.job4j.inputoutput.consolechat.commands.StopCommand;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class ConsoleChat {

    private static final String STOP = "стоп";
    private static final String PROCEED = "продолжить";
    private static final String END = "закончить";

    private static final String LOGFILE_PATH = "console_chat_log.txt";
    private static final String CHATBOT_ANSWERS_FILEPATH = "chatbot_answers.txt";


    public static void main(String[] args) throws IOException {

        Console console = new ConsoleImpl(System.in, System.out);
        ChatLogger logger = new ChatLogger(LOGFILE_PATH);

        logger.info("Init log");

        Set<String> cachedAnswers = cacheAnswers(CHATBOT_ANSWERS_FILEPATH);
        Collection<String> buffer = new ArrayDeque<>();
        boolean isRun = true;
        boolean isStopped = false;

        final Map<String, Command> commands = Map.of(
                STOP, new StopCommand(buffer),
                END, new EndChatCommand(console),
                PROCEED, new ProceedCommand(buffer, logger)
        );

        console.write("Добро пожаловать в чат!");

        while (isRun) {
            String sentence = console.readLine();
            logger.logUserInput(sentence);

            if (sentence.equalsIgnoreCase(END)) {
                isRun = commands.get(END).execute(isStopped);
            } else {
                isStopped = commands.getOrDefault(
                        sentence.toLowerCase(),
                        new DefaultCommand(sentence, buffer, cachedAnswers, logger, console)
                ).execute(isStopped);
            }
        }
    }

    private static Set<String> cacheAnswers(String filePath) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            return fileReader.lines()
                    .map(String::strip)
                    .filter(not(String::isBlank))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

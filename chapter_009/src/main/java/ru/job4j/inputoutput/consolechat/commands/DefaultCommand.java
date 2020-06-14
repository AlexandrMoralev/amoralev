package ru.job4j.inputoutput.consolechat.commands;

import ru.job4j.inputoutput.consolechat.ChatLogger;
import ru.job4j.inputoutput.consolechat.Command;
import ru.job4j.inputoutput.consolechat.Console;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

public class DefaultCommand implements Command {

    private final String sentence;
    private final Collection<String> buffer;
    private final Collection<String> cachedAnswers;
    private final ChatLogger logger;
    private final Console console;

    public DefaultCommand(String sentence,
                          Collection<String> buffer,
                          Collection<String> cachedAnswers,
                          ChatLogger logger,
                          Console console
    ) {
        this.sentence = sentence;
        this.buffer = buffer;
        this.cachedAnswers = cachedAnswers;
        this.logger = logger;
        this.console = console;
    }

    @Override
    public boolean execute(boolean isStopped) {
        if (isStopped) {
            buffer.add(sentence);
        } else {
            String answer = getAnswer.apply(cachedAnswers);
            logger.logBotAnswer(answer);
            console.write(answer);
        }
        return isStopped;
    }

    private Function<Collection<String>, String> getAnswer = cachedAnswers -> {
        Random rand = new Random();
        return rand.ints(cachedAnswers.size(), 0, cachedAnswers.size() - 1)
                .mapToObj(i -> cachedAnswers.toArray(new String[]{})[i])
                .findFirst()
                .orElse("It's nothing to say.");
    };
}

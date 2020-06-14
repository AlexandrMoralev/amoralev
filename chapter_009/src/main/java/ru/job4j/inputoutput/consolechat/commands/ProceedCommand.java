package ru.job4j.inputoutput.consolechat.commands;

import ru.job4j.inputoutput.consolechat.ChatLogger;
import ru.job4j.inputoutput.consolechat.Command;

import java.util.Collection;

public class ProceedCommand implements Command {

    private final Collection<String> buffer;
    private final ChatLogger logger;

    public ProceedCommand(Collection<String> buffer,
                          ChatLogger logger
    ) {
        this.buffer = buffer;
        this.logger = logger;
    }

    @Override
    public boolean execute(boolean isStopped) {
        if (isStopped) {
            buffer.forEach(logger::logUserInput);
        }
        return false;
    }
}

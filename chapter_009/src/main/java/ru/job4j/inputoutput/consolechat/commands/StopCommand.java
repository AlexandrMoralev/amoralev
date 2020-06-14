package ru.job4j.inputoutput.consolechat.commands;

import ru.job4j.inputoutput.consolechat.Command;

import java.util.Collection;

public class StopCommand implements Command {

    private final Collection<String> buffer;

    public StopCommand(Collection<String> buffer) {
        this.buffer = buffer;
    }

    @Override
    public boolean execute(boolean isStopped) {
        if (!isStopped) {
            buffer.clear();
        }
        return true;
    }
}

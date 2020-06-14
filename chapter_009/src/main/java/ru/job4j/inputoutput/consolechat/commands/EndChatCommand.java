package ru.job4j.inputoutput.consolechat.commands;

import ru.job4j.inputoutput.consolechat.Command;
import ru.job4j.inputoutput.consolechat.Console;

public class EndChatCommand implements Command {

    private final Console console;

    public EndChatCommand(Console console) {
        this.console = console;
    }

    @Override
    public boolean execute(boolean isStopped) {
        console.write("До новых встреч!");
        return false;
    }
}

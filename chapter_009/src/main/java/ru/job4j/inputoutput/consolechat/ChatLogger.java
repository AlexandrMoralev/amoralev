package ru.job4j.inputoutput.consolechat;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ChatLogger {

    private static final String INFO_FORMAT = "%s";
    private static final String USER_FORMAT = "User: %s";
    private static final String CHATBOT_FORMAT = "ChatBot: %s";
    private final Logger logger;

    public ChatLogger(String logfilePath) throws IOException {
        this.logger = Logger.getLogger("chat_log");
        logger.setUseParentHandlers(false);
        FileHandler fileHandler = new FileHandler(logfilePath);
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    public void info(String msg) {
        log(INFO_FORMAT, msg);
    }

    public void logUserInput(String sentence) {
        log(USER_FORMAT, sentence);
    }

    public void logBotAnswer(String sentence) {
        log(CHATBOT_FORMAT, sentence);
    }

    private void log(String format, String msg) {
        this.logger.info(String.format(format, msg));
    }
}

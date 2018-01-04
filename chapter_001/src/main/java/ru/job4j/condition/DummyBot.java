package ru.job4j.condition;

/**
 * DummuBot
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class DummyBot {
    /**
     * Отвечает на вопросы.
     * @param question - вопрос клиента.
     * @return Ответ.
     */

    public String answer(String question) {

        String rsl = "Этот вопрос ставит меня в тупик. Задайте другой вопрос.";

        if ("Привет, Бот.".equals(question)) {
            rsl = "Привет, умник.";
        } else if ("Пока.".equals(question)) {
            rsl = "До скорой встречи.";
        }
        return rsl;
    }
}

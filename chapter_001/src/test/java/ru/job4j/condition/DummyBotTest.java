package ru.job4j.condition;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * DummyBotTest.
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class DummyBotTest {
    /**
     * Test greeting.
     */
    @Test
    public void whenGreetBot() {
        DummyBot bot = new DummyBot();
        assertThat(
                bot.answer("Привет, Бот."),
                is("Привет, умник.")
        );
    }

    /**
     * Test farewell.
     */
    @Test
    public void whenByeBot() {
        DummyBot bot = new DummyBot();
        assertThat(
                bot.answer("Пока."),
                is("До скорой встречи.")
        );
    }

    /**
     * Test anyOtherQuestion.
     */
    @Test
    public void whenUnknownBot() {
        DummyBot bot = new DummyBot();
        assertThat(
                bot.answer("Сколько будет 2 + 2 ?"),
                is("Этот вопрос ставит меня в тупик. Задайте другой вопрос.")
        );
    }
}

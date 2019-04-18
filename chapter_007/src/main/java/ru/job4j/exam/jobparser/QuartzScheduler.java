package ru.job4j.exam.jobparser;

import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

/**
 * Scheduler
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class QuartzScheduler {
    // отвечает за расписание запуска парсера и работу с датами(?)
/*

    private final Config config;
    private final static String CRONTIME = "cron.time";
    private final TriggerBuilder trigger;

    public QuartzScheduler(Config config) {
        this.config = config;
        this.trigger = newTrigger()
                .withSchedule(cronSchedule(config.get(CRONTIME)))
                .forJob(ParserSQLru.class)
                .build();
    }
*/

}

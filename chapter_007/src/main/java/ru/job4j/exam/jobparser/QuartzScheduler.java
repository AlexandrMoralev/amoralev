package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * Scheduler
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class QuartzScheduler {
    // отвечает за расписание запуска парсера

    private final static Logger LOG = LogManager.getLogger(QuartzScheduler.class);
    private final static String CRONTIME = "cron.time";

    public static void main(String[] args) {
        String cronExp = new Config().getString(CRONTIME);
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class)
                    .withIdentity("ParserSqlRu")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .startNow()
                    .withSchedule(cronSchedule(cronExp))
                    .withIdentity("ParserSqlRuTrigger")
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error(e);
        }
    }
}

package ru.job4j.exam.jobparser;

/**
 * Scheduler
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class QuartzScheduler {
    // отвечает за расписание запуска парсера
/*
    private final Config config;
    private final static String CRONTIME = "cron.time";
    private final TriggerBuilder trigger;

    public QuartzScheduler(Config config) {
        this.config = config;
        *//*this.trigger = newTrigger()
                .withSchedule(cronSchedule(config.get(CRONTIME)))
                .forJob(ParserSQLru.class)
                .build();*//*
    }

    public static void main(String[] args) {
        String cronExp = new Config().get("cron.time");
        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class)
                .withIdentity("ParserSqlRu")
                .build();
        final Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(
                        cronSchedule(cronExp)
                ).startNow()
                .build();
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }*/
}

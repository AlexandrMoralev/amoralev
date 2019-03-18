package ru.job4j.exam.jobparser;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * ParserSQLru
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ParserSQLru implements Job { // implements Parser
    // парсит Java вакансии с форума SQL.ru
    // постранично, за последние сутки (первый раз за последний год)

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Config config = new Config();
        config.init();



    }
}

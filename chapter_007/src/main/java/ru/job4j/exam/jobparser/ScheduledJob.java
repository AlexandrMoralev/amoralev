package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;

public class ScheduledJob implements Job {

    private final static Logger LOG = LogManager.getLogger(ScheduledJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Config config = new Config("jobparser_app.properties");
        Store<Vacancy> store = new InMemoryStore(config);
        PageCounter pageCounter = new SqlRuPageCounter();
        ParserSQLru parser = new ParserSQLru(config, store, pageCounter);
        try {
            parser.searchVacancies();
        } catch (IOException e) {
            LOG.error(e);
            throw new JobExecutionException(e);
        }
    }
}

package ru.job4j.exam.jobparser;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.sql.Timestamp;
import java.util.Collection;

public class ScheduledJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
/*        ParserSQLru parser = new ParserSQLru();
        StoreDB store = new StoreDB(new Config());
        Vacancy lastParsed = store.findRecent(1).get(0); //TODO check indexOfBounds
        Timestamp lastParsingDate = lastParsed.getCreatedAsTimeStamp();
        Collection<Vacancy> vacancies = parser.search(lastParsingDate);
        store.addAll(vacancies);*/
    }
}

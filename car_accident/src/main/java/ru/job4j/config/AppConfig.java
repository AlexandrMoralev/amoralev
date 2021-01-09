package ru.job4j.config;

//import liquibase.integration.spring.SpringLiquibase;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.job4j.repository.AccidentHibernate;
import ru.job4j.repository.AccidentJdbcTemplate;
import ru.job4j.repository.AccidentMem;
import ru.job4j.repository.AccidentStore;
import ru.job4j.service.AccidentService;
import ru.job4j.service.AccidentServiceImpl;

@Configuration
@PropertySource("classpath:car_accident_app.properties")
@Import({JdbcConfig.class, HbmConfig.class})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean("accidentMem")
    public AccidentMem accidentMem() {
        return new AccidentMem();
    }

    @Bean("accidentJdbcTemplate")
    public AccidentJdbcTemplate accidentJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new AccidentJdbcTemplate(jdbcTemplate);
    }

    @Bean("accidentHibernateStore")
    public AccidentHibernate accidentHibernateStore(SessionFactory sessionFactory) {
        return new AccidentHibernate(sessionFactory);
    }

    @Bean
    public AccidentService accidentService(AccidentStore store) {
        return new AccidentServiceImpl(store);
    }

//    @Bean
//    public SpringLiquibase liquibase(DataSource ds) {
//        SpringLiquibase liquibase = new SpringLiquibase();
////        liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
//        liquibase.setChangeLog("classpath:db/master.xml");
//        liquibase.setDataSource(ds);
//        return liquibase;
//    }
}

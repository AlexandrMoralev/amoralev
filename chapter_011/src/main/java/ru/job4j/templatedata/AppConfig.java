package ru.job4j.templatedata;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.job4j.mvc.repository.AccidentJdbcTemplate;
import ru.job4j.mvc.repository.AccidentMem;
import ru.job4j.mvc.repository.AccidentStore;
import ru.job4j.mvc.service.AccidentService;

@Configuration
@PropertySource("classpath:cars_app.properties")
@Import({JdbcConfig.class})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public AccidentMem accidentMem() {
        return new AccidentMem();
    }

    @Bean
    public AccidentJdbcTemplate accidentJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new AccidentJdbcTemplate(jdbcTemplate);
    }

    @Bean
    public AccidentService accidentService(@Qualifier("accidentJdbcTemplate") AccidentStore store) {
        return new AccidentService(store);
    }

}

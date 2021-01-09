package ru.job4j.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.job4j.repository.AccidentMem;
import ru.job4j.repository.AccidentStore;
import ru.job4j.service.AccidentService;
import ru.job4j.service.AccidentServiceImpl;

@Configuration
@PropertySource("classpath:car_accident_app.properties")
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

//    @Bean
//    public AccidentJdbcTemplate accidentJdbcTemplate(JdbcTemplate jdbcTemplate) {
//        return new AccidentJdbcTemplate(jdbcTemplate);
//    }

    @Bean
    public AccidentService accidentService(AccidentStore store) {
        return new AccidentServiceImpl(store);
    }

}

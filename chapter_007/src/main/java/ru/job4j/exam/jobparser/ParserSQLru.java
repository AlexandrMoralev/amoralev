package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

/**
 * ParserSQLru
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ParserSQLru {
    // парсит Java вакансии с форума SQL.ru(постранично)
    // при первом запуске - за последний год, далее запускается по cron и парсит за последние сутки
    // периоды парсинга конфигурируются в properties-файле

    private final static Logger LOG = LogManager.getLogger(ParserSQLru.class);

    private final Config config;
    private final Store<Vacancy> store;
    private final PageCounter paging;

    private static final Set<String> IGNORE_WORDS = Set.of("javascript", "js", "java script", "java-script", "[закрыт]");
    private static final Set<String> KEYWORDS = Set.of("java", "javaee");

    private final String url;
    private final int topicsOnPage;
    private final LocalDateTime parsingDateLimit;


    public ParserSQLru(final Config config,
                       final Store<Vacancy> store,
                       final PageCounter pageCounter
    ) {
        this.config = config;
        this.store = store;
        this.paging = pageCounter;
        this.parsingDateLimit = DateTimeUtil.getParsingDateLimit(this.config, this.store);
        this.url = this.config.getString("parser.init_page_url");
        this.topicsOnPage = this.config.getInt("parser.topics_on_page");

    }

    public Collection<Vacancy> searchVacancies() throws IOException {
        // TODO check if condition needs optimization
        LOG.info("searchVacancies start: {}", LocalDateTime.now().toString());
        while (isDateLimitNotBeenReached(this.parsingDateLimit)) {
            LOG.info("isDateLimitNotBeenReached finished: {}", System.nanoTime());
            String nextPage = paging.getNextPage(url);
            LinkedList<Vacancy> parsedVacancies = new LinkedList<>(parsePage(nextPage));
            LOG.info("searchVacancies batch: {}", parsedVacancies.getLast().toString());
            this.store.addAll(parsedVacancies);
        }
        LOG.info("searchVacancies finish: {}", LocalDateTime.now().toString());
        return this.store.findAll();
    }

    private boolean isDateLimitNotBeenReached(LocalDateTime dateLimit) {
        LOG.info("isDateLimitNotBeenReached start: {}", System.nanoTime());
        Collection<Vacancy> lastVacancy = store.findRecent(1);
        if (lastVacancy.isEmpty()) {
            return true;
        } else {
            return lastVacancy.stream().anyMatch(vacancy -> vacancy.getCreated().isAfter(dateLimit));
        }
    }

    public Collection<Vacancy> parsePage(String pageUrl) throws IOException {
        ArrayDeque<Vacancy> result = new ArrayDeque<>(topicsOnPage);

        getTableElements(pageUrl).stream()
                .filter(vacancy -> isTargetTopic(vacancy.text()))
                .peek(v -> LOG.info(v.text()))
                .filter(vacancy -> parsingDateLimit.isBefore(DateTimeUtil.parseToLocalDateTime(vacancy.child(5).text())))
                .map(vacancy -> {
                    String link = getLink(vacancy);
                    return Vacancy.newBuilder()
                                    .setName(vacancy.child(1).text())
                                    .setCreated(DateTimeUtil.parseToLocalDateTime(vacancy.child(5).text()))
                                    .setLink(link)
                                    .setDescription(getDescription(link))
                                    .build();
                        }
                )
                .forEach(result::add);
        return result;
    }

    private Elements getTableElements(String pageUrl) throws IOException {
        Document document = Jsoup.connect(pageUrl).get();
        return document.getElementsByAttributeValue("class", "forumTable")
                .get(0)
                .getElementsByTag("tr");
    }

    private String getLink(Element vacancy) {
        return vacancy.select("a").first().attr("href");
    }

    private String getDescription(String link) {
        Document inTopic;
        try {
            inTopic = Jsoup.connect(link).get();
            return inTopic.getElementsByAttributeValue("class", "msgTable")
                    .get(0)
                    .getElementsByTag("tr")
                    .get(1)
                    .getElementsByTag("td")
                    .get(1)
                    .text();
        } catch (IOException e) {
            LOG.error(String.format("Error getting description from link: %s.", link), e);
        }
        return "";
    }

    private boolean isTargetTopic(String topicText) {
        return Arrays.stream(topicText.toLowerCase().split("\\s+"))
                .anyMatch(word -> KEYWORDS.stream().anyMatch(keysPredicate(word))
                        && IGNORE_WORDS.stream().noneMatch(keysPredicate(word)));
    }

    private Predicate<String> keysPredicate(String word) {
        return key -> word.startsWith(key) || word.contains(key);
    }

}
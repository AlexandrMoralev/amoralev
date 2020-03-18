package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.job4j.exam.jobparser.DateTimeUtil.now;

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

    public void searchVacancies() throws IOException {
        this.searchVacanciesToDate(this.parsingDateLimit);
    }

    public void searchVacanciesToDate(LocalDateTime dateLimit) throws IOException {
        LOG.debug("Start search: {}", now());
        while (true) {
            String nextPage = paging.getNextPage(url);
            Collection<Vacancy> parsedVacancies = parsePage(nextPage);
            if (isParsingLimitReached(parsedVacancies, dateLimit)) {
                LOG.debug("Parsing limit reached: {}", now());
                this.store.addAll(
                        parsedVacancies.stream()
                                .filter(isAfterParsingDateLimit(dateLimit))
                                .collect(Collectors.toList())
                );
                break;
            }
            this.store.addAll(parsedVacancies);
        }
        LOG.debug("Finish search at: {}", now());
    }

    private boolean isParsingLimitReached(Collection<Vacancy> parsedVacancies, LocalDateTime parsingDateLimit) {
        return parsedVacancies.stream()
                .anyMatch(v -> parsingDateLimit.isAfter(v.getCreated()));
    }

    private Predicate<Vacancy> isAfterParsingDateLimit(LocalDateTime parsingDateLimit) {
        return v -> parsingDateLimit.isBefore(v.getCreated());
    }

    public Collection<Vacancy> parsePage(String pageUrl) throws IOException {
        LOG.debug("Start parsing page: {} at {}", pageUrl, now());
        List<Vacancy> pageVacancies = getTableElements(pageUrl).stream()
                .filter(vacancy -> isTargetTopic(vacancy.text()))
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
                .collect(Collectors.toList());
        LOG.debug("Finish parsing page {} at {}. Found {} vacancies", pageUrl, now(), pageVacancies.size());
        return pageVacancies;
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
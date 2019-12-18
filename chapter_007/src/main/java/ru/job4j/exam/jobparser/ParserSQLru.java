package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ParserSQLru
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ParserSQLru {
    // парсит Java вакансии с форума SQL.ru
    // постранично, за последние сутки (первый раз за последний год)

    private final static Logger LOG = LogManager.getLogger(ParserSQLru.class);

    private final Config config;
    private final StoreDB storeDB;
    private final Set<String> ignoreWords = Set.of("javascript", "js", "java script");
    private final Set<String> keywords = Set.of("java", "javaee", "spring");

    public ParserSQLru(final Config config, final StoreDB storeDB) {
        this.config = config;
        this.storeDB = storeDB;
    }

    public Collection<Vacancy> search(String url) throws IOException {
        validateString(url);
        Timestamp lastParsingDate = getLastParsingDate();
        Timestamp parsingDateLimit;
        Set<Vacancy> result;
        String initPageURL = this.config.get("parser.init_page_url");

        // Deque<Vacancy> result = new ArrayDeque<>();

        parsingDateLimit = getParsingDateLimit(lastParsingDate); //TODO check if it works
        result = new HashSet<>(parsePagesByPeriod(initPageURL, parsingDateLimit));
        return result;
    }

    private void validateString(String aString) {
        if (aString == null) {
            throw new NullPointerException();
        }
        if (aString.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    private Timestamp getLastParsingDate() {
        List<Vacancy> vacancies = this.storeDB.findRecent(1);
        return vacancies.isEmpty()
                ? null
                : vacancies.get(0).getCreatedAsTimeStamp();
    }

    private Timestamp getParsingDateLimit(Timestamp lastParsingDate) {
        long parsePeriodYears;
        long parsePeriodDays;
        if (lastParsingDate == null) {
            parsePeriodYears = Long.valueOf(this.config.get("parser.parse_period_years"));
            parsePeriodDays = 0;
        } else {
            parsePeriodYears = 0;
            parsePeriodDays = Long.valueOf(this.config.get("parser.parse_period_days"));
        }
        // TODO check the count of limit
        return new Timestamp(LocalDateTime.now()
                .minusYears(parsePeriodYears)
                .minusDays(parsePeriodDays)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        );
    }

    private Collection<Vacancy> parsePagesByPeriod(String initPageURL, Timestamp parsingDateLimit) throws IOException {
        Collection<Vacancy> result = new HashSet<>();
        String currentPageURL = initPageURL;
        Collection<Vacancy> parsedPage = parsePage(currentPageURL, parsingDateLimit);
        int currentPageNumber = 0;
        while (!parsedPage.isEmpty()) {
            result.addAll(parsedPage);
            currentPageURL = nextPage(initPageURL, currentPageNumber++);
            parsedPage = parsePage(currentPageURL, parsingDateLimit);
        }
        return result;
    }

    private String nextPage(String initPageURL, int currentPageNumber) {
        String suffix = currentPageNumber == 0 ? "" : String.format("/%s", currentPageNumber);
        return String.format("%s%s", initPageURL, suffix);
    }

    private Collection<Vacancy> parsePage(String pageUrl, Timestamp parsingDateLimit) throws IOException {
        Set<Vacancy> result = new HashSet<>();
        Document document = Jsoup.connect(pageUrl).get();
        Elements table = document.getElementsByAttributeValue("class", "forumTable")
                .get(0)
                .getElementsByTag("tr");
        String topicText = "";
        String name;
        String description;
        String link;
        Timestamp created;
        for (Element vacancy : table) {
            topicText = vacancy.text().toLowerCase();
            if (this.keywords.stream().anyMatch(topicText::contains)
                    && this.ignoreWords.stream().noneMatch(topicText::contains)) {
                /*Timestamp vacancyDate = DateConverter
                        .convertStringDateToTimestamp(
                                vacancy.child(5).text());*/
                System.out.println("vacancy = " + vacancy.child(5).text());
                String dateText = vacancy.child(5).text();
                System.out.println("dateText = " + dateText);
                Timestamp vacancyDate = DateConverter.convertLocalDateTimeToTimestamp(
                        //  this.parseDateTime(DateConverter.replaceMonth(dateText))
                        this.parseDateTime(dateText)
                );
                if (vacancyDate.after(parsingDateLimit)) {
                    name = vacancy.child(1).text();
                    link = vacancy.select("a").first().attr("href");
                    // vacancy.child(1)
                    // .attr("href");
                    //.getElementsByAttributeStarting("href:").text(); //TODO link parsing doesn't work
                    // .getElementsByAttribute("href").text();

                    created = vacancyDate;
                    Document inTopic = Jsoup.connect(link).get();
                    description = inTopic.getElementsByClass("msgBody")
                            .get(0)
                            .text();
                    Vacancy newVac = new Vacancy(name, description, link, created);
                    System.out.println(newVac.toString());
                    result.add(newVac);
                }
            }
        }
        return result;
    }

    private LocalDateTime parseDateTime(String dateTime) {
    /*    LocalDateTime result;
       // dateTime = dateTime .replace("май", "мая");
        //String pattern = dateTime.trim().length() == 15 ? "d MMM yy, HH:mm" : "dd MMM yy, HH:mm";
        String pattern = dateTime.trim().length() == 15 ? "d MMM yy, HH:mm" : "dd MMM yy, HH:mm";
        //String pattern =  "dd MMM yy, hh:mm";
        if (dateTime.contains("сегодня") || dateTime.contains("вчера")) {
            String[] splitDate = dateTime.split("[, :]");
            int hh = Integer.parseInt(splitDate[2]);
            int mm = Integer.parseInt(splitDate[3]);
            result = LocalDate.now().atTime(hh, mm);
            if (dateTime.contains("вчера")) {
                result = result.minusDays(1L);
            }
        } else {
            //result = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern).withLocale( new Locale("ru", "RU")));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withLocale( new Locale("ru"));
            result = LocalDateTime.parse(dateTime, formatter);
            *//*
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLL yy, HH:mm").withLocale(new Locale("ru", "RU"));
            result = LocalDateTime.parse(dateTime, formatter);*//*
        }
        return result;*/

        return DateConverter.convertStringToLocalDateTime(dateTime);

    }


    public Collection<Vacancy> parsePage(String url) throws IOException {
        validateString(url);
        LocalDateTime aDay = LocalDateTime.now().minusDays(1);
        return this.parsePage(url, DateConverter.convertLocalDateTimeToTimestamp(aDay));
    }

    public static void main(String[] args) throws IOException {
        Config config = new Config("jobparser_app.properties");
        System.out.println(config.toString());
        StoreDB storeDB = new StoreDB(config);
        ParserSQLru parserSQLru = new ParserSQLru(config, storeDB);
        Collection<Vacancy> vacancies = parserSQLru.parsePage("https://www.sql.ru/forum/job-offers/");
        for (Vacancy vac : vacancies) {
            System.out.println(vac.toString());
        }
    }
}
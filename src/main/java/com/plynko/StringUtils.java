package com.plynko;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Stores util methods to work with URLs and Strings.
 */
public final class StringUtils {

    public static final List<String> ACCEPTABLE_PROTOCOLS = Arrays.asList("http", "test");

    // Sign by which we define the HTML content.
    private static final List<String> ACCEPTABLE_PAGE_PREFIXES = Arrays.asList("<!doctype html", "<html");

    // The regular expression that defines the entire set of possible delimiters.
    private static final String DELIMITERS = "([\\s,-.;]+)|(<[^<]*?>)";

    // The regular expression that defines what kind of words we want to exclude.
    private static final String IGNORED_WORDS = "^$|.*?[\\d\\p{Punct}]+.*";

    private static final List<String> IGNORED_TAGS = Arrays.asList("style", "script", "image", "object");

    // Suppresses default constructor, ensuring non-instantiability.
    private StringUtils() {}

    /**
     * Check if {@code URL} has one of the {@code ACCEPTABLE_PROTOCOLS}.
     *
     * @param  url the URL to check.
     * @throws IllegalArgumentException if none of the protocols match.
     */
    public static void checkUrl(URL url) {
        String urlProtocol = url.getProtocol();
        for (String protocol : ACCEPTABLE_PROTOCOLS) {
            if (urlProtocol.equals(protocol)) {
                return;
            }
        }
        throw new IllegalArgumentException("Acceptable protocols: " + ACCEPTABLE_PROTOCOLS);
    }

    /**
     * Downloads page from the Internet by given {@code URL}.
     * Checks if downloaded page has HTML content.
     *
     * @param  url the URL to download.
     * @return downloaded page as a {@code String}, if it starts with one of the {@code ACCEPTABLE_PAGE_PREFIXES};
     *         otherwise, {@code null}.
     * @throws IOException if an I/O exception occurs.
     */
    public static String getPage(URL url) throws IOException {
        String page = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();

        // remove UTF8 BOM
        page = page.startsWith("\uFEFF") ? page.substring(1) : page;

        for (String prefix : ACCEPTABLE_PAGE_PREFIXES) {
            if (page.regionMatches(true, 0, prefix, 0, prefix.length())) {
                return page;
            }
        }
        return null;
    }

    /**
     * Looks for all words on the page, excluding tags and {@code IGNORED_WORDS}.
     *
     * @param  page the string which represent page content.
     * @return the {@code List} of all found words.
     */
    public static List<String> getWordsList(String page) {
        String s = IGNORED_TAGS.size() > 0 ? removeTags(page) : page;

        String[] wordsArray = s.split(DELIMITERS);

        return Arrays.stream(wordsArray)
                .filter(w -> !w.matches(IGNORED_WORDS))
                .collect(Collectors.toList());
    }

    /**
     * Replaces {@code IGNORED_TAGS} and their content with spaces.
     *
     * @param  page the string which represent page content.
     * @return the string constructed by replacing each matching subsequence by the space.
     */
    private static String removeTags(String page) {
        String tags = IGNORED_TAGS.stream().collect(Collectors.joining("|"));
        String regex = String.format("<(%s)[^<]*?>.*?</\\1>", tags);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return pattern.matcher(page).replaceAll(" ");
    }

    /**
     * Counts the number of repetitions of each word and sorts words.
     *
     * @param  wordsList the list of all words.
     * @return the sorted map where the key is the number of repetitions and the value is the word.
     */
    public static Map<String, Long> getWordsSortedMap(List<String> wordsList) {
        return wordsList.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        TreeMap::new,
                        Collectors.counting()));
    }
}
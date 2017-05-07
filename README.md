## Words counter
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/73fcce952df54946bd79a5a9947b5a06)](https://www.codacy.com/app/pavlo-plynko/word-counter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shcho-isle/word-counter&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/shcho-isle/word-counter.svg?branch=master)](https://travis-ci.org/shcho-isle/word-counter)

The program calculates the total number of words on the given page and the number of repetitions for each word.

## Code Example
    private static String removeTags(String page) {
        String tags = IGNORED_TAGS.stream().collect(Collectors.joining("|"));
        String regex = String.format("<(%s)[^<]*?>.*?</\\1>", tags);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return pattern.matcher(page).replaceAll(" ");
    }
    
## Launching
You can run the program from your favorite environment. Doing this, keep in mind that:
- the entry point to the program is the Counter class;
- url string should be passed as the first argument;
- all other arguments will be ignored.

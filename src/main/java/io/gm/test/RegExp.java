package io.gm.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {
    /**
     * The method provides a recursive case insensitive search
     * The following options are enabled:
     * - CASE_INSENSITIVE
     * - DOTALL
     * - MULTILINE
     *
     * @param text
     * @param regExp
     * @return a list of arrays
     */
    public static List<String[]> getMatches(String text, String regExp) {
        List<String[]> res = new ArrayList<>();
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = pattern.matcher(text);

        while (m.find()) {
            String[] s = new String[m.groupCount()];
            for (int i = 1; i <= m.groupCount(); i++) {
                s[i - 1] = m.group(i);
            }
            res.add(s);
        }
        return res;
    }

    public static String replaceAll(String text, String regExp, String replacement) {
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        return pattern.matcher(text).replaceAll(replacement);
    }

    public static String replaceAll(String text, String regExp, Function<String, String> replacement) {
        String t = text;
        for (String s : getMatches(text, regExp).get(0)) {
            t = t.replaceAll("\\Q" + s + "\\E", replacement.apply(s));
        }
        return t;
    }
}

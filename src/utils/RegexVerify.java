package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexVerify {
    public static boolean matchesRegex(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}

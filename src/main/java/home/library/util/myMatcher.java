package home.library.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class myMatcher
{
    public boolean matchPersonalInfo(String lastName, String firstName, String patronymic)
    {
        Pattern pattern = Pattern.compile("[^a-zA-Zа-яА-Я-'\\s]+");
        Matcher matcher = pattern.matcher(lastName);
        if (matcher.find())
            return false;
        matcher = pattern.matcher(firstName);
        if (matcher.find())
            return false;
        matcher = pattern.matcher(patronymic);
        if (matcher.find())
            return false;
        return true;
    }

    public boolean matchPhone(String phone)
    {
        Pattern pattern = Pattern.compile("[^0-9\\+\\-\\(\\)]+");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.find())
            return false;
        return true;
    }


}

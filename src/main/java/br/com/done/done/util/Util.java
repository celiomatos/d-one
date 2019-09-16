package br.com.done.done.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Slf4j
public class Util {

    public static String arrayToCommaDelimited(String strArray) {
        strArray = StringUtils.trimAllWhitespace(strArray);
        return strArray.replace(']', ')').replace('[', '(');
    }

    public static String arrayToStringCommaDelimited(String strArray) {
        strArray = StringUtils.trimAllWhitespace(strArray);
        return strArray.replaceAll("]", "')")
                .replaceAll(",", "','")
                .replaceAll(Pattern.quote("["), "('");
    }

    public static BigDecimal strToBigDecimal(String vl) {
        BigDecimal value = null;
        if (vl != null && (!vl.isEmpty())) {
            try {
                vl = vl.replaceAll("[R$, .]", "");
                if (vl.length() > 2) {
                    vl = vl.substring(0, vl.length() - 2)
                            + "."
                            + vl.substring(vl.length() - 2);

                    value = new BigDecimal(vl);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return value;
    }
}

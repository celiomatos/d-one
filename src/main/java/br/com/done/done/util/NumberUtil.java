package br.com.done.done.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class NumberUtil {

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

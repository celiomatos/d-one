package br.com.done.done.util;

import java.text.Normalizer;

public class StringUtil {

    /**
     * remove acentos cedilhas e apcrifos de string
     *
     * @param str
     * @return
     */
    public static String removerAcento(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        str = str.replaceAll("[']", "");
        return inTrim(str);
    }

    /**
     * remove os spaces do inicio, fim e duplos de string
     *
     * @param str
     * @return
     */
    public static String inTrim(String str) {
        str = str.trim();
        char vt[] = str.toCharArray();
        boolean l = true;
        StringBuilder r = new StringBuilder();
        for (char c : vt) {

            if (c != ' ') {
                l = true;
            }

            if (l) {
                r.append(c);
                if (c == ' ') {
                    l = false;
                }
            }
        }
        return r.toString();
    }

}

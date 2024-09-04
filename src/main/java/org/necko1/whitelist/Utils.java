package org.necko1.whitelist;

import java.util.UUID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static UUID getUUID(String nickname) {
        return UUID.nameUUIDFromBytes(nickname.getBytes());
    }

    public static long parseTime(String s, boolean add_current) {
        Pattern p = Pattern.compile("(\\d+)([smhd])");
        Matcher m = p.matcher(s);
        long num = 0;
        while (m.find()) {
            int i = Integer.parseInt(m.group(1));
            String unit = m.group(2);

            if (unit.equals("m"))
                i = i * 60;
            if (unit.equals("h"))
                i = i * 60 * 60;
            if (unit.equals("d"))
                i = i * 60 * 60 * 24;
            num += i;
        }
        long res = num * 1000;
        if (add_current) res += System.currentTimeMillis();
        return res;
    }

}

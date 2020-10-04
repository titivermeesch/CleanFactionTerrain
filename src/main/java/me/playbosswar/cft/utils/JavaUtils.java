package me.playbosswar.cft.utils;

import java.util.Set;

public class JavaUtils {
    public static int getIndex(Set<? extends Object> set, Object value) {
        int result = 0;
        for (Object entry:set) {
            if (entry.equals(value)) return result + 1;
            result++;
        }
        return -1;
    }
}

package vn.edu.fpt.traffic_license.utils;

import java.util.Collection;

public class Utils {

    private Utils() {
    }

    public static <D, E extends Collection<D>> boolean isEmpty(E collection) {
        return collection == null || collection.isEmpty();
    }

    public static <D, E extends Collection<D>> boolean isNotEmpty(E collection) {
        return collection != null && !collection.isEmpty();
    }

}

package io.gmartov;

import java.util.List;

public class Utils {

    /**
     * Return requested element of specified array or null if array does not contain such element
     *
     * @param num  - requested number of array element. -1 means the last element.
     * @param list - array
     * @param <E>  - type of element
     * @return element od array or null
     */
    public static <E> E getOrNull(int num, List<E> list) {
        final int size = list.size();
        if (size > 0 && size > num) {
            return list.get(num == -1 ? size - 1 : num);
        } else {
            return null;
        }
    }

    public static <T> boolean isEmpty(T... arrayOfAny) {
        return arrayOfAny == null || arrayOfAny.length == 0;
    }

    public static <T> T defaultOrFirst(T _default, T... arrayOfAny) {
        return isEmpty(arrayOfAny) ? _default : arrayOfAny[0];
    }
}

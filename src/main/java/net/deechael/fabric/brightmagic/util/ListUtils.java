package net.deechael.fabric.brightmagic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public final class ListUtils {

    public static <E> List<E> findSame(List<E> aList, List<E> bList) {
        return intersectionList(aList, bList, (a, b) -> a == b);
    }

    public static <E> List<E> intersectionList(List<E> srcList , List<E> subList , BiPredicate<E, E> comparator) {
        List<E> newList = new ArrayList<>();
        for (final E next : srcList) {
            for (E e : subList) {
                if (comparator.test(next, e)) {
                    newList.add(next);
                    break;
                }
            }
        }
        return newList;
    }

    public static byte[] classToPrimitive(Byte[] classBytes) {
        byte[] bytes = new byte[classBytes.length];
        for (int i = 0; i < classBytes.length; i++) {
            bytes[i] = classBytes[i];
        }
        return bytes;
    }

    private ListUtils() {}

}

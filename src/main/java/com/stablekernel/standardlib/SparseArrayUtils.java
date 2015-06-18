package com.stablekernel.standardlib;

import android.support.v4.util.SparseArrayCompat;

import java.util.ArrayList;
import java.util.List;

public class SparseArrayUtils {
    public static <T> List<T> asList(SparseArrayCompat<T> sparseArray) {
        if (sparseArray == null) {
            return null;
        }

        List<T> list = new ArrayList<>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            list.add(sparseArray.valueAt(i));
        }
        return list;
    }
}

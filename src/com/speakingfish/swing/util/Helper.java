package com.speakingfish.swing.util;

import java.util.Iterator;

import javax.swing.DefaultListModel;

public class Helper {

    public static <E, M extends DefaultListModel<E>> M collect(M dest, Iterator<E> src) {
        while(src.hasNext()) {
            dest.add(dest.size(), src.next());
        }
        return dest;
    }
    
}

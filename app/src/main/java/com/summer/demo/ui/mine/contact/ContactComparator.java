package com.summer.demo.ui.mine.contact;

import com.summer.demo.ui.mine.contact.bean.LibrarySelectInfo;

import java.util.Comparator;

/**
 * 按照字母进行排序
 */
public class ContactComparator implements Comparator<LibrarySelectInfo> {

    @Override
    public int compare(LibrarySelectInfo info1, LibrarySelectInfo info2) {
        String o1 = info1.getCharacter();
        String o2 = info2.getCharacter();
        int c1 = (o1.charAt(0) + "").toUpperCase().hashCode();
        int c2 = (o2.charAt(0) + "").toUpperCase().hashCode();

        boolean c1Flag = (c1 < "A".hashCode() || c1 > "Z".hashCode()); // 不是字母
        boolean c2Flag = (c2 < "A".hashCode() || c2 > "Z".hashCode()); // 不是字母
        if (c1Flag && !c2Flag) {
            return 1;
        } else if (!c1Flag && c2Flag) {
            return -1;
        }

        return c1 - c2;
    }

}

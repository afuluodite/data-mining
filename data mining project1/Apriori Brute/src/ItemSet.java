import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class ItemSet {
    TreeSet<Integer> item;
    int support;
    List<ItemCon> ics = new ArrayList<ItemCon>();

    ItemSet(ItemSet is) {
        this.item = new TreeSet<Integer>(is.item);
    }

    ItemSet() {
        item = new TreeSet<Integer>();
    }

    ItemSet(int i, int v) {
        this();
        merge(i);
        setValue(v);
    }

    void setValue(int i) {
        this.support = i;
    }

    void merge(int i) {
        item.add(i);
    }

    void calcon(ItemCon ic) {
        ics.add(ic);
    }

    boolean isMerge(ItemSet item2) {
        if (item2 == null || item2.item.size() != item.size())
            return false;
        Iterator<Integer> i1 = item.iterator();
        Iterator<Integer> i2 = item2.item.iterator();
        int n = item.size();
        while (i1.hasNext() && i2.hasNext() && --n > 0)
            if (i1.next() != i2.next())
                return false;

        return !(item.last() == item2.item.last());
    }
}
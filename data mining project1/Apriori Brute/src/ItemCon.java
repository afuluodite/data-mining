import java.util.List;

public class ItemCon {
    Integer i;
    List<Integer> li;
    double confidence1;
    double confidence2;

    ItemCon(Integer i, List<Integer> li) {
        this.i = i;
        this.li = li;
    }

    void setC1(double confidence1) {
        this.confidence1 = confidence1;
    }

    void setC2(double confidence2) {
        this.confidence2 = confidence2;
    }
}

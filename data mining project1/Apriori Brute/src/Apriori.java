import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


public class Apriori {
    private int minSup;      //Support count
    private double minConf;    // Confidence
    private List<Set<Integer>> input;
    private String ouputPath;
    private List<List<ItemSet>> result = new ArrayList<List<ItemSet>>();
    private List<ItemSet> firstRound;

    public Apriori(int minSup, double minConf, String inputpath, String ouputPath) {
        this.ouputPath = ouputPath;
        this.minConf = minConf;
        this.minSup = minSup;
        input = new ArrayList<Set<Integer>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(inputpath)));
            String line = null;
            Set<Integer> record;
            while ((line = br.readLine()) != null) {
                if (!"".equals(line.trim())) {
                    record = new TreeSet<Integer>();
                    String[] items = line.split(" |,");
                    for (String item : items) {
                        record.add(Integer.valueOf(item));
                    }
                    input.add(record);
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("The input file has error");
        }

        if (input.size() == 0) {
            System.err.println("The input is null！");
            System.exit(1);
        }
    }


    private void first() {
        firstRound = new ArrayList<ItemSet>();
        Map<Integer, Integer> first = new HashMap<Integer, Integer>();
        for (Set<Integer> seti : input)
            for (Integer i : seti) {
                if (first.get(i) == null)
                    first.put(i, 1);
                else
                    first.put(i, first.get(i) + 1);
            }

        for (Integer i : first.keySet())
            if (first.get(i) >= minSup)
                firstRound.add(new ItemSet(i, first.get(i)));
    }

    private void loop(List<ItemSet> items) {
        List<ItemSet> copy = new ArrayList<ItemSet>(items);
        List<ItemSet> res = new ArrayList<ItemSet>();
        int size = items.size();

        // connect
        for (int i = 0; i < size; i++)
            for (int j = i + 1; j < size; j++)
                if (copy.get(i).isMerge(copy.get(j))) {
                    ItemSet is = new ItemSet(copy.get(i));
                    is.merge(copy.get(j).item.last());
                    res.add(is);
                }

        // prune
        pruning(copy, res);

        if (res.size() != 0) {
            result.add(res);
            loop(res);
        }
        System.out.println("finish pruning");
    }

    private void pruning(List<ItemSet> before, List<ItemSet> after) {

        // the subset of k is belong to k-1
        Iterator<ItemSet> itis = after.iterator();
//        System.out.println("enter pruning");
        while (itis.hasNext()) {
            // all the subset of k-1
            ItemSet current = itis.next();
            Map<Integer, List<Integer>> ss = subSet(current);
            // whether appeared in the before
            boolean flag = false;
            for (List<Integer> li : ss.values()) {
                if (flag)
                    break;
                for (ItemSet pis : before) {
                    if (pis.item.containsAll(li)) {
                        flag = false;
                        break;
                    }
                    flag = true;
                }
            }
            if (flag) {
                itis.remove();
                continue;
            }

            // support
            //           System.out.println("support");
            int i = 0;
            for (Set<Integer> sr : input) {
                if (sr.containsAll(current.item))
                    i++;

                current.support = i;
            }
            if (current.support < minSup) {
                itis.remove();
                continue;
            }

            // association rule
//            System.out.println("rule");
            double csupport = current.support;
            for (Map.Entry<Integer, List<Integer>> me : ss.entrySet()) {
                ItemCon ic = new ItemCon(me.getKey(), me.getValue());
                int asupport = 0;

                for (ItemSet f : firstRound)
                    if (f.item.contains(me.getKey())) {
                        asupport = f.support;
                        break;
                    }
                if (csupport / asupport > minConf) {
                    current.calcon(ic);
                    ic.setC1(csupport / asupport);
                }
                for (ItemSet pis : before)
                    if (pis.item.size() == me.getValue().size() && pis.item.containsAll(me.getValue())) {
                        asupport = pis.support;
                        break;
                    }
                if (csupport / asupport > minConf)
                    ic.setC2(csupport / asupport);
            }
        }
        System.out.println("prunning end");
    }

    private Map<Integer, List<Integer>> subSet(ItemSet is) {
        List<Integer> list1 = new ArrayList<Integer>(is.item);
        Map<Integer, List<Integer>> res = new HashMap<Integer, List<Integer>>();
        for (int i = 0, j = list1.size(); i < j; i++) {
            List<Integer> list2 = new ArrayList<Integer>(list1);
            list2.remove(i);
            res.put(list1.get(i), list2);
        }
        return res;
    }

    private void output() throws FileNotFoundException {
        if (result.size() == 0) {
            System.err.println("no output！");
            return;
        }
        FileOutputStream out = new FileOutputStream(ouputPath);
        PrintStream ps = new PrintStream(out);
        for (List<ItemSet> li : result) {
            ps.println("=============frequent" + li.get(0).item.size() + "set=============");
            //ps.println("=============frequent" + li.get(0).item.size() + "set=============");
            for (ItemSet is : li) {
                ps.println(is.item + " \t " + is.support);
                ps.println();
                if (is.ics.size() != 0) {
                    ps.println("******association rule******");
                    for (ItemCon ic : is.ics) {
                        ps.println("Rules:" + ic.i + " ---> " + ic.li + "\t Confidence:" + ic.confidence1);
                        if (ic.confidence2 > minConf)
                            ps.println("Rules:" + ic.li + " ---> " + ic.i + "\t Confidence:" + ic.confidence2);
                    }
                    ps.println("******************");
                    ps.println();
                }
            }
            ps.println("=====================================");
        }

        ps.close();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        long time1 = c1.getTimeInMillis();
        //System.out.println("time1"+time1);

//        Apriori apriori = new Apriori(10, 0.25, "IBM.data", "Apriori-output.txt");
        Apriori apriori = new Apriori(4, 0.25, "IBM.data", "Apriori-output.txt");
        apriori.first();
        apriori.loop(apriori.firstRound);
        apriori.output();

        long time2 = c2.getTimeInMillis();
        //System.out.println("time2 "+ time2);
        System.out.println("Time cosuming is "+ (time2-time1) + "ms");
        System.out.println("finish out");
    }
}




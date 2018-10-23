import java.io.*;
import java.util.*;


public class MyFptree2 {
    private int minSup = 4; //Support count
    private double minConf; // Confidence
    String inputpath;
    private String itemSplit = ",";
    public Map<String, Integer> frequentCollectionMap = new HashMap<String, Integer>();
    public Map<Map<String, String>, Double> rules = new HashMap<Map<String, String>, Double>();
    public Map<String, Integer> oneCount = new HashMap<String, Integer>();
    private StringBuilder builder = new StringBuilder();
    public Map<String, Integer> ordermap = new HashMap<String, Integer>();   //store the first order


    public LinkedList<LinkedList<String>> readFile(String filePath ) throws IOException {
        this.inputpath = filePath;
        LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath)));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (line.length() == 0 || "".equals(line))
                continue;
            String[] str = line.split(" |,");
            LinkedList<String> litm = new LinkedList<String>();
            for (int i = 0; i < str.length; i++) {
                litm.add(str[i].trim());
            }
            records.add(litm);
        }
        br.close();
        return records;
    }


    public void count() {
        LinkedList<LinkedList<String>> records;
        try {
            records = readFile(inputpath);
            for (LinkedList<String> l : records) {
                for (String s : l) {
                    if (oneCount.keySet().contains(s)) {
                        oneCount.put(s, oneCount.get(s) + 1);
                    } else {
                        oneCount.put(s, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Create head list to store all the node and sort its support in decrease
    public LinkedList<TreeNode2> buildHeaderLink(LinkedList<LinkedList<String>> records) {
        LinkedList<TreeNode2> header = null;
        if (records.size() > 0) {
            header = new LinkedList<TreeNode2>();
        } else {
            return null;
        }
        Map<String, TreeNode2> map = new HashMap<String, TreeNode2>();
        for (LinkedList<String> items : records) {
            for (String item : items) {
                //if the item node exit count++, else create new node
                if (map.containsKey(item)) {
                    map.get(item).Sum(1);
                } else {
                    TreeNode2 node = new TreeNode2();
                    node.setName(item);
                    node.setCount(1);
                    map.put(item, node);
                }
            }
        }
        // The support of the item above minSup would add to the headerlist
        Set<String> names = map.keySet();
        for (String name : names) {
            TreeNode2 tnode = map.get(name);
            if (tnode.getCount() >= minSup) {
                header.add(tnode);
            }
        }
        sort(header);
        return header;
    }


    public List<TreeNode2> sort(List<TreeNode2> list) {
        int len = list.size();
        for (int i = 0; i < len; i++) {

            for (int j = i + 1; j < len; j++) {
                TreeNode2 node1 = list.get(i);
                TreeNode2 node2 = list.get(j);
                if (node1.getCount() < node2.getCount()) {
                    TreeNode2 tmp = node2;
                    list.remove(j);
                    list.add(j, node1);
                    list.remove(i);
                    list.add(i, tmp);
                }
                //if the count is equal, sort by name according to the dictionary sequence
                if (node1.getCount() == node2.getCount()) {
                    String name1 = node1.getName();
                    String name2 = node2.getName();
                    int flag = name1.compareTo(name2);
                    if (flag > 0) {
                        TreeNode2 tmp = node2;
                        list.remove(j);
                        list.add(j, node1);
                        list.remove(i);
                        list.add(i, tmp);
                    }
                }
            }
        }

        return list;
    }

    //sort the linked list in decent
    public List<String> itemsort(LinkedList<String> linklist, List<TreeNode2> header) {
        //sort linked list
        int len = linklist.size();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                String key1 = linklist.get(i);
                String key2 = linklist.get(j);
                Integer value1 = findcountByname(key1, header);
                //System.out.println("key1 is "+ key1 + " value1 is "+value1);
                if (value1 == -1) continue;
                Integer value2 = findcountByname(key2, header);
                //System.out.println("key2 is "+ key2 + " value2 is "+value2);
                if (value2 == -1) continue;
                if (value1 < value2) {
                    String tmp = key2;
                    linklist.remove(j);
                    linklist.add(j, key1);
                    linklist.remove(i);
                    linklist.add(i, tmp);
                }

                if (value1 == value2) {
                    int v1 = ordermap.get(key1);
                    int v2 = ordermap.get(key2);
                    if (v1 > v2) {
                        String tmp = key2;
                        linklist.remove(j);
                        linklist.add(j, key1);
                        linklist.remove(i);
                        linklist.add(i, tmp);
                    }
                }
            }
        }
        return linklist;
    }

    public Integer findcountByname(String itemname, List<TreeNode2> header) {
        Integer count = -1;
        for (TreeNode2 node : header) {
            if (node.getName().equals(itemname)) {
                count = node.getCount();
            }
        }
        return count;
    }


    public TreeNode2 builderFpTree(LinkedList<LinkedList<String>> records, List<TreeNode2> header) {
        TreeNode2 root;
        if (records.size() <= 0) {
            return null;
        }
        root = new TreeNode2();
        for (LinkedList<String> items : records) {
            itemsort(items, header);
            addNode(root, items, header);
        }

        return root;
    }

    //if the branch exit, decide whether the new node belong to this branch
    public TreeNode2 addNode(TreeNode2 root, LinkedList<String> items, List<TreeNode2> header) {
        if (items.size() <= 0)
            return null;
        String item = items.poll();
        //if current node doesn't have the new node as child, create a new node
        TreeNode2 node = root.findChild(item);
        if (node == null) {
            node = new TreeNode2();
            node.setName(item);
            node.setCount(1);
            node.setParent(root);
            root.addChild(node);

            //Add nodes to the linkedlist
            for (TreeNode2 head : header) {
                if (head.getName().equals(item)) {
                    while (head.getNextHomonym() != null) {
                        head = head.getNextHomonym();
                    }
                    head.setNextHomonym(node);
                    break;
                }
            }
        } else {
            node.setCount(node.getCount() + 1);
        }

        addNode(node, items, header);
        return root;
    }

    //find the way from the leave to the root
    public void toroot(TreeNode2 node, LinkedList<String> newrecord) {
        if (node.getParent() == null)
            return;
        String name = node.getName();
        newrecord.add(name);
       // System.out.println("treenode is "+name+" father "+node.getParent().getName());
        toroot(node.getParent(), newrecord);
    }


    //fp-growth
    public void fpgrowth(LinkedList<LinkedList<String>> records, String item) {
        //saved the records，to reconstruct FP-tree
        LinkedList<LinkedList<String>> newrecords = new LinkedList<LinkedList<String>>();
        LinkedList<TreeNode2> header = buildHeaderLink(records);
        TreeNode2 fptree = builderFpTree(records, header);
        if (header.size() <= 0 || fptree == null) {
            return;
        }

        //print the frequent set
        if (item != null) {
            //seek the model set from the tail of the linked list
            for (int i = header.size() - 1; i >= 0; i--) {
                TreeNode2 head = header.get(i);
                Integer count = 0;
                while (head.getNextHomonym() != null) {
                    head = head.getNextHomonym();
                    //The count of leaves is the number of the records.
                    count = count + head.getCount();

                }
                //print the set
                String items = "";
                items = item + "," + head.getName();
                //items = item + "," + head.getName() + ",";
                if (items.split(",").length != 0 ) {
                   //System.out.println("------items "+items+","+head.getName()+"\t count is"+count);
                    frequentCollectionMap.put(items, count);
                }

                System.out.println(items+"\t"+count);
//                System.out.println(item+","+head.getName()+"="+count);
//                builder.append(item+","+head.getName()+"="+count+"\n");
            }
        }

        //seek the model set from the tail of the linked list
        for (int i = header.size() - 1; i >= 0; i--) {
            TreeNode2 head = header.get(i);
            String itemname;
            //再组合
            if (item == null) {
                itemname = head.getName();
            } else {
                itemname = head.getName() + "," + item;
            }

            while (head.getNextHomonym() != null) {
                head = head.getNextHomonym();
                //The count of leaves is the number of the records.
                Integer count = head.getCount();
                for (int n = 0; n < count; n++) {
                    LinkedList<String> record = new LinkedList<String>();
                    toroot(head.getParent(), record);
                    newrecords.add(record);
                }
            }
            fpgrowth(newrecords, itemname);
        }
    }


    public void orderF1(LinkedList<TreeNode2> orderheader) {
        for (int i = 0; i < orderheader.size(); i++) {
            TreeNode2 node = orderheader.get(i);
            ordermap.put(node.getName(), i);
        }

    }

    public void getRelationRules(Map<String, Integer> frequentCollectionMap, double minConf) {
        this.minConf = minConf;
        count();
        //Map<String, Double> relationRules = new HashMap<String, Double>();
        Set<String> keySet = frequentCollectionMap.keySet();
      // System.out.println(";;;;;;;;;;;;; "+frequentCollectionMap);

        for (String key : keySet) {
            double countAll = frequentCollectionMap.get(key);
           // System.out.println("===key is "+ key +" countall "+countAll);
            String[] keyItems = key.split(itemSplit);
            if (keyItems.length > 1) {
                List<String> source = new ArrayList<String>();
                Collections.addAll(source, keyItems);
                List<List<String>> result = new ArrayList<List<String>>();

                buildSubSet(source, result);

             // System.out.println("----------\n"+source.toString()+"\n"+result.toString()+"\n-----------------\n");
                for (List<String> resultItemList : result) {
                    if (resultItemList.size() < source.size()) {// only deal with the proper subset
                        List<String> otherList = new ArrayList<String>();
                        for (String sourceItem : source) {
                            if (!resultItemList.contains(sourceItem)) {
                                otherList.add(sourceItem);
                               // System.out.println("sourceItem is "+sourceItem +" result list "+resultItemList+" other lsi "+otherList);
                            }
                        }
                        String reasonStr = "";   //reason set
                        String resultStr = "";   //result set
                        for (String item : resultItemList) {
                            //System.out.println("#######item is"+item);
                            if (reasonStr == ""){
                                reasonStr = item;
                            }else{
                                reasonStr = item + itemSplit+ reasonStr ;
                            }

                        }
                       // System.out.println("&&&&&&&&&&&&resultitemlist"+resultItemList);
                       // System.out.println("&&&&&&&&&&resonstr "+reasonStr);
                        for (String item : otherList) {
                            if (resultStr == ""){
                                resultStr = item;
                            }else{
                                resultStr = item + itemSplit+ resultStr ;
                            }

                        }
                       // System.out.println("&&&&&&&&&&&other "+ resultStr);

                       // System.out.println("reasonStr is "+reasonStr);
                        double countReason = 0.0;
                        if (!reasonStr.contains(",")){
                            countReason = oneCount.get(reasonStr);
                        }else {
                            if (frequentCollectionMap.containsKey(reasonStr))
                                countReason = frequentCollectionMap.get(reasonStr);
                            else{
                                String[] str = reasonStr.split(",");
                               // String position = "";
                                for (String sk :keySet) {
                                    int flag = 0;
                                    String[] sk1 = sk.split(",");
                                    for (String s : str) {
                                        if (sk.contains(s))
                                            flag = flag + 1;
                                        if (sk1.length == flag) {
                                           /* System.out.println("wulalalalllal" + " s is " + s + " sk is " + sk);
                                            for (int i = 0; i < str.length; i++) {
                                                System.out.println("str[" + i + "] is " + str[i]);
                                            }  */
                                            //position = sk;
                                        }
                                    }
                                    /*if (flag!=0)
                                        countReason = frequentCollectionMap.get(position);*/
                                    if (sk1.length == flag)
                                        countReason = frequentCollectionMap.get(sk);
                                }
                            }
                        }
                       // System.out.println("oncount is "+oneCount+" countreason is "+countReason);

                        double itemConfidence = countAll / countReason;
                        if (itemConfidence >= minConf) {
                          //String rule = reasonStr + "->" + resultStr;
                            Map rule = new HashMap<String, String>();
                            rule.put(reasonStr, resultStr);
                            //System.out.println("******rule is"+rule+" reason "+reasonStr+" result "+resultStr);
                            rules.put(rule, itemConfidence);
                        }
                    }
                }
            }
        }
    }

    private void buildSubSet(List<String> sourceSet, List<List<String>> result) {
        // only one object
        if (sourceSet.size() == 1) {
            List<String> set = new ArrayList<String>();
            set.add(sourceSet.get(0));
            result.add(set);
        } else if (sourceSet.size() > 1) {
            // find the n-1 subset and store it to the result
            buildSubSet(sourceSet.subList(0, sourceSet.size() - 1), result);
            int size = result.size();
            // add the nth object into the set
            List<String> single = new ArrayList<String>();
            single.add(sourceSet.get(sourceSet.size() - 1));
            result.add(single);

            // to retain the n-1 subset
            List<String> clone;
            for (int i = 0; i < size; i++) {
                clone = new ArrayList<String>();
                for (String str : result.get(i)) {
                    clone.add(str);
                }
                clone.add(sourceSet.get(sourceSet.size() - 1));

                result.add(clone);
            }
        }
    }

    private void output(String ouputPath) throws FileNotFoundException {
        if (frequentCollectionMap.size() == 0) {
            System.err.println("no output！");
            return;
        }

        FileOutputStream out = new FileOutputStream(ouputPath);
        PrintStream ps = new PrintStream(out);

        ps.println("============print frequent set===============");
       // ps.println(frequentCollectionMap);
        Set s1 = frequentCollectionMap.entrySet();
        Map.Entry[] entries1 = (Map.Entry[]) s1.toArray(new Map.Entry[s1.size()]);
        for (int i = 0; i < entries1.length; i++) {
            ps.println("[" + entries1[i].getKey().toString() + "]" + "\t" + entries1[i].getValue().toString());
        }
        ps.println("=====================================");
        ps.println();
        ps.println("******association rule******");
        Set s2 = rules.entrySet();
        Map.Entry[] entries2 = (Map.Entry[]) s2.toArray(new Map.Entry[s2.size()]);
        for (int i = 0; i < entries2.length; i++) {
            ps.println("Rules:" + entries2[i].getKey().toString().split("=")[0] + " ---> " + entries2[i].getKey().toString().split("=")[1] + "\t Confidence:" + entries2[i].getValue().toString());
        }

        ps.println("******************");
        ps.close();
    }


    public static void main(String[] args) throws IOException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        long time1 = c1.getTimeInMillis();
        //System.out.println("time1"+time1);

        MyFptree2 fpg = new MyFptree2();
        String inputpath = "IBM.data";             //supermarket.csv   //IBM.data  support=3 minConf=0.15
        LinkedList<LinkedList<String>> input = fpg.readFile(inputpath);
        LinkedList<TreeNode2> orderheader = fpg.buildHeaderLink(input);
        fpg.orderF1(orderheader);
        fpg.fpgrowth(input, null);
        fpg.getRelationRules(fpg.frequentCollectionMap, 0.25);
        fpg.output("fptree-output.txt");   //fptree-game-output1.txt  input game1.csv support count 100, minconf 0.54

        long time2 = c2.getTimeInMillis();
        //System.out.println("time2 "+ time2);
        System.out.println("Time cosuming is "+ (time2-time1) + "ms");
        System.out.println("finish out");

    }
}

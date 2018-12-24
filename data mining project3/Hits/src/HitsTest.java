public class HitsTest {
    private int nodesNumber;
    private String graphName;
    private String dataPath;
    private double THRESHOLD=0.0001;
  //  private double Damping_factor=0.15;


    public HitsTest(int totalNodeNumber ,String testGraphName){
        nodesNumber = totalNodeNumber;
        graphName = testGraphName;
        dataPath = "./dataset/"+graphName+".txt";
    }

    public void rank(){
        FileParser fp = new FileParser(dataPath,nodesNumber);
        double time1,time2;

        try {
            fp.readData();
            int[][] adjMatrix =fp.getAdjacentMatrix();
            double[] hub = fp.getHub();
            double[] authority = fp.getAuthority();

            for (int i=0;i<nodesNumber;i++)
                System.out.print(hub[i]+"\t");
            System.out.println();

            for (int i=0;i<nodesNumber;i++)
                System.out.print(authority[i]+"\t");
            System.out.println();
         //   Hits pagerank = new Hits(nodesNumber,adjMatrix,THRESHOLD, Damping_factor,hub);

            for (int i=0;i<nodesNumber;i++) {
                for (int j = 0; j < nodesNumber; j++) {
                   // System.out.println("i=" + i + " j=" + j + " is " + adjMatrix[i][j]);
                    System.out.print(adjMatrix[i][j]+"\t");
                }
                System.out.println();
            }

            Hits hit = new Hits(nodesNumber,adjMatrix,THRESHOLD,hub,authority);
          //  System.out.println("lalalla");

            time1= System.nanoTime();
            hit.calculate();
            time2= System.nanoTime();
           // pagerank.sortHits();

            System.out.println("Time consumption of "+ graphName + " is " + (time2-time1)/1000000);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        HitsTest test1 = new HitsTest(6,"graph_1");
        HitsTest test2 = new HitsTest(5,"graph_2");
        HitsTest test3 = new HitsTest(4,"graph_3");
        HitsTest test4 = new HitsTest(7,"graph_4");
        HitsTest test5 = new HitsTest(469,"graph_5");
        HitsTest test6 = new HitsTest(1228,"graph_6");
        HitsTest test7 = new HitsTest(1791,"graph_7");
        HitsTest test8 = new HitsTest(1791,"graph_8");

        test8.rank();
		/*test1.rank();
		System.out.println("~~~~~~~~~~");
		test2.rank();
		System.out.println("~~~~~~~~~~");
		test3.rank();
		System.out.println("~~~~~~~~~~");
		test4.rank();
		System.out.println("~~~~~~~~~~");
        test5.rank();
       // System.out.println("~~~~~~~~~~");
        test6.rank();
        //System.out.println("~~~~~~~~~~");
        test7.rank();
        //System.out.println("~~~~~~~~~~");
        test8.rank();*/

    }

}

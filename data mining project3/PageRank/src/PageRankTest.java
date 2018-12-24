public class PageRankTest {
	private int nodesNumber;
	private String graphName;
	private String dataPath;
	private double THRESHOLD=0.0001;
	private double Damping_factor=0.15;


	public PageRankTest(int totalNodeNumber ,String testGraphName){
		nodesNumber = totalNodeNumber;
		graphName = testGraphName;
		dataPath = "./dataset/"+graphName+".txt";
	}
	
	public void rank(){
		 FileParser fp = new FileParser(dataPath,nodesNumber);
		 double time1 ,time2;
		 
	        try {
	        	fp.readData();
	        	int[][] adjMatrix =fp.getAdjacentMatrix();
	        	int[] outDegree = fp.getOutdegreeVector();
	            PageRank pagerank = new PageRank(nodesNumber,adjMatrix,THRESHOLD, Damping_factor,outDegree);
	            
	            time1= System.nanoTime();
	            pagerank.calculate();
	            time2= System.nanoTime();
	            pagerank.sortPageRank();

	            System.out.println("Time consumption of "+ graphName + " is " + (time2-time1)/1000000);
	        }catch (Exception e) {
	        	System.out.println(e.getMessage());
	        }
	}

    public static void main(String[] args) {
        PageRankTest test0 = new PageRankTest(6,"graph_0");
       // test0.rank();

		PageRankTest test1 = new PageRankTest(6,"graph_1");
		PageRankTest test2 = new PageRankTest(5,"graph_2");
		PageRankTest test3 = new PageRankTest(4,"graph_3");
		PageRankTest test4 = new PageRankTest(7,"graph_4");
		PageRankTest test5 = new PageRankTest(469,"graph_5");
		PageRankTest test6 = new PageRankTest(1228,"graph_6");
		PageRankTest test7 = new PageRankTest(1791,"graph_7");
		PageRankTest test8 = new PageRankTest(1791,"graph_8");


		/*test1.rank();
		System.out.println("~~~~~~~~~~");
		test2.rank();
		System.out.println("~~~~~~~~~~");
		test3.rank();
		System.out.println("~~~~~~~~~~");
		test4.rank();
		System.out.println("~~~~~~~~~~");*/
		//test5.rank();
		System.out.println("~~~~~~~~~~");
		//test6.rank();
		System.out.println("~~~~~~~~~~");
		//est7.rank();
		System.out.println("~~~~~~~~~~");
		test8.rank();

    }

}

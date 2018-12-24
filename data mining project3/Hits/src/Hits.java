import java.util.Arrays;

public class Hits {
    int[][] adjacentMatrix;
//    public int[][] path ;
    //public double[] pagerank;
    private int totalNodes=0;
    private double threshold=0;
  //  private double dampingFactor=0;
    private double[] hub;
    private double[] authority;
    private double[] aveHub;
    private double[] aveAuthority;


    public Hits(int totalNodes,int[][] adjacentMatrix,double threshold,double[] hub, double[] authority) {
        this.totalNodes = totalNodes;
        this.adjacentMatrix = adjacentMatrix;
        this.threshold = threshold;
        this.hub= hub;
        this.authority=authority;

        this.aveAuthority=new double[totalNodes];
        this.aveHub=new double[totalNodes];

        for(int k=0; k<totalNodes; k++){
            //initial normalized authority and hub
            aveAuthority[k] = authority[k] ;
           // System.out.println("k is "+k+"\t"+aveAuthority[k]);
            aveHub[k] = hub[k];
           // System.out.println("initial k authority is"+aveAuthority[k]+"\t"+ aveHub[k]);
        }
    }

    public void calculate() throws Exception{
        //sum of the hub and authority used to normalize
        double newMaxHub = 0.0;
        double newMaxAuthority = 0.0;

        //store the temp authority and hub
        double[] newHub = new double[totalNodes];
        double[] newAuthority = new double[totalNodes];
        double[] newAveAuthority = new double[totalNodes];
        double[] newAveHub = new double[totalNodes];

        int iteration=0;


        while(!isConvergent(aveAuthority,aveHub, newAveAuthority,newAveHub)) {// Iterations
            iteration++;
            //new iteration some variable clear 0
            newMaxHub=0.0;
            newMaxAuthority=0.0;
            for(int k=0; k<totalNodes; k++){
                newHub[k] = 0;
                newAuthority[k] = 0;
            }

            //authority update
            for(int i=0; i<totalNodes; i++){
                for(int j=0; j<totalNodes; j++){
                    if(adjacentMatrix[i][j] == 1){
                        newAuthority[j] += hub[i];
                    }
                }
            }

            //hub update
            for(int i=0; i<totalNodes; i++){
                for(int j=0; j<totalNodes; j++){
                    if(adjacentMatrix[i][j] == 1){
                        newHub[i] += authority[j];
                    }
                }
            }


            for(int k=0; k<totalNodes; k++){
                //compute the max hub and authority
               if(newMaxHub < newHub[k])
                    newMaxHub = newHub[k];
                if(newMaxAuthority < newAuthority[k])
                    newMaxAuthority = newAuthority[k];
            }

            System.out.println("Normalization");

            //Normalization
            for(int k=0; k<totalNodes; k++){
                newAveHub[k]=newHub[k] / newMaxHub;
                newAveAuthority[k]=newAuthority[k] / newMaxAuthority;

                System.out.println("node"+k+":authority："+newAveAuthority[k] + ", hub:" +newAveHub[k]);

                hub[k] = newHub[k];
                authority[k] = newAuthority[k];
                aveHub[k] = newAveHub[k];
                aveAuthority[k] = newAveAuthority[k];
            }
            System.out.println("---------");

        }
    }

    private boolean isConvergent(double[] aveAuthority,double[] aveHub,double[] newAveAuthority,double[] newAveHub){
        double diff=0.0;
        for(int i=0; i < totalNodes ;i++){
            diff += Math.abs(newAveHub[i] - aveHub[i])+Math.abs(newAveAuthority[i] - aveAuthority[i]);
            if(diff > threshold)
                return false;
        }
        return true;
    }


/*
    public void sortHits(){
        int[] nodeNumbers= new int[pagerank.length];
        for(int index=0; index < nodeNumbers.length ;index++)
            nodeNumbers[index]=index;
        double[] tmpPageRank = Arrays.copyOf(pagerank, this.pagerank.length);

        for(int i=1; i < pagerank.length ;i++)
            for(int j=i;j<pagerank.length ; j++ )
            {
                if(tmpPageRank[i] <tmpPageRank[j]){
                    int tmpIndex=nodeNumbers[j];
                    nodeNumbers[j]=nodeNumbers[i];
                    nodeNumbers[i]=tmpIndex;

                    double tmpRank = tmpPageRank[i];
                    tmpPageRank[i] = tmpPageRank[j];
                    tmpPageRank[j]=tmpRank;
                }
            }

        // Display final PageRank
        System.out.printf("\n Sort Hits : \n");
        for(int k=1;k<=totalNodes;k++)
        {
            System.out.printf(" Page Rank of "+nodeNumbers[k]+" is :\t"+tmpPageRank[k]+"\n");
        }
    }

*/
}

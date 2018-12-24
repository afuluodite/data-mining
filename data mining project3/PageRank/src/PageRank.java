import java.util.*;
import java.io.*;

public class PageRank {
  
public int[][] path ;
public double[] pagerank;
private int totalNodes=0;
private double threshold=0;
private double dampingFactor=0;
private int[] outdegreeVector;


public PageRank(int totalNodes,int[][] adjacentMatrix,double threshold,double dampingFactor, int[] outdegreeVector) {
	this.totalNodes = totalNodes;
	path = adjacentMatrix;
	pagerank = new double[totalNodes+1];
	this.threshold = threshold;
	this.dampingFactor = dampingFactor;
	this.outdegreeVector=outdegreeVector;
}
  
public void calculate() throws Exception{
    // Initialization
	double initialPageRank =1 / (double)totalNodes;
	double TempPageRank[] = new double[totalNodes+1];
	int ExternalNodeNumber;
	int InternalNodeNumber;
	Arrays.fill(this.pagerank, initialPageRank); 

	while(!isConvergent(TempPageRank, pagerank)) // Iterations
	 {
		 // Store the PageRank in Temporary Array
		 TempPageRank=Arrays.copyOf(this.pagerank, this.pagerank.length);
		 Arrays.fill(this.pagerank, 0);	 
	     
		 //Count PageRank of each node
		 for(InternalNodeNumber=1;InternalNodeNumber<=totalNodes;InternalNodeNumber++){
			 for(ExternalNodeNumber=1;ExternalNodeNumber<=totalNodes;ExternalNodeNumber++){
				 if(this.path[ExternalNodeNumber][InternalNodeNumber] == 1){
                 // Calculate PageRank
					 this.pagerank[InternalNodeNumber]+=TempPageRank[ExternalNodeNumber]*(1/(double)outdegreeVector[ExternalNodeNumber]);
				 }
			 }
			 this.pagerank[InternalNodeNumber] = dampingFactor+ (1-dampingFactor)*this.pagerank[InternalNodeNumber];
		 }
	}
 }

	private boolean isConvergent(double[] oldPageRank,double[] newPageRank){
	int length = oldPageRank.length;
	for(int pageNumber=0; pageNumber < length ;pageNumber++){
		double diff = Math.abs(newPageRank[pageNumber] - oldPageRank[pageNumber]);
		if(diff > threshold)
			return false;
	}
	return true;
}
 
	public void sortPageRank(){
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
		System.out.printf("\n Sort Page Rank : \n"); 
		for(int k=1;k<=totalNodes;k++)
		{
		 System.out.printf(" Page Rank of "+nodeNumbers[k]+" is :\t"+tmpPageRank[k]+"\n"); 
		 }
	}		

}
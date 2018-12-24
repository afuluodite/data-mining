import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class FileParser {
	private int nodeNumber;
	private String dataPath;
	private int[][] adjacentMatrix;
	private double[] hub;
	private double[] authority;
	
	
	public FileParser (String dataPath,int nodeNumber) {
		this.dataPath = dataPath;
		this.nodeNumber = nodeNumber;
	}
	
	public void readData() throws Exception{
		
		if( DataPathIsValid() && nodeNumberIsValid() ) {	
			FileReader fr = new FileReader(dataPath);
			BufferedReader br = new BufferedReader(fr);
			adjacentMatrix = new int[nodeNumber+1][nodeNumber+1];
			hub = new double[nodeNumber+1];
			authority = new double[nodeNumber+1];
			//adjacentMatrix = new int[nodeNumber][nodeNumber];
			//hub = new double[nodeNumber];
			//authority = new double[nodeNumber];
			
			for(int row=0;row < nodeNumber ; row++) {
				Arrays.fill(adjacentMatrix[row],0 );
			}		
			Arrays.fill(hub, 1);
			Arrays.fill(authority, 1);
			
	        while (br.ready()) {
	        	String line = br.readLine();
	        	String edge[] =line.split(",");
	        	int origin = Integer.parseInt(edge[0]);
	        	int terminus = Integer.parseInt(edge[1]);
	        	adjacentMatrix[origin-1][terminus-1] =1;
				//System.out.println("origin is "+origin+" terminus is "+terminus);
				hub[origin-1]=hub[origin-1]+1;
				//System.out.println("hub "+(origin-1)+" is "+hub[origin-1]);
				authority[terminus-1]=authority[terminus-1]+1;
	        }             
	        fr.close();
		}
	}
	
	private boolean DataPathIsValid() {
		return !dataPath.isEmpty();
	}
	
	private boolean nodeNumberIsValid() {
		return nodeNumber > 0;
	}
	
		
	public int[][] getAdjacentMatrix() {
		return adjacentMatrix;
	}

	public double[] getHub() {
		return hub;
	}

	public double[] getAuthority() {
		return authority;
	}

}

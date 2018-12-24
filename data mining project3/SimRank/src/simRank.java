import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class simRank {
	// static int k=100;
	public static double threshold = 0.0001;
	public static double C = 0.25;

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new FileReader("./dataset/graph_5.txt"));
		String line;
		int count = 0;
		String[] arrays;
		int max=0;

		while ((line = reader.readLine()) != null) {
			arrays = line.split(",");
			count+=arrays.length;
		}
		//System.out.println("count is "+count);
		int[] num = new int[count];

		BufferedReader reader2 = new BufferedReader(new FileReader("./dataset/graph_5.txt"));
		count = 0;
		while ((line = reader2.readLine()) != null) {
			arrays = line.split(",");
			for (int i = 0; i < arrays.length; i++) {
				num[count] = Integer.valueOf(arrays[i].trim());
				if (num[count]>max)
					max=num[count];
				count++;
			}
		}

		int[][] node = new int[max][max];
		double[][] answer = new double[max][max];
		for (int i = 0; i < max; i++)
			for (int j = 0; j < max; j++) {
				if (i == j)
					answer[i][j] = 1;
				else
					answer[i][j] = 0;
			}
        //add the edge to the node
		for (int k = 0; k < count; k += 2) {
			 //System.out.println("[" + num[k] + "," + num[k + 1] + "]");
			node[num[k + 1] - 1][num[k] - 1] = 1;
		}

/*
		for (int i = 0; i < node.length; i++) {
			 System.out.print("[");
			for (int j = 0; j < node[i].length; j++) {
				 System.out.print(" " + node[i][j]);
			}
			 System.out.println("]");
		}
*/

		// double ans = sim(3, 4, node,20);
		// System.out.println(ans);
		double time = System.nanoTime()/1000;
		double[][] answer_temp = new double[max][max];
		for (int i = 0; i < max; i++) {
			for (int j = 0; j < max; j++) {
				if (i == j)
					answer_temp[i][j] = 1;
				else
					answer_temp[i][j] = 0;
			}
		}


		boolean done = false;
		int count_iteration = 1;
		while (!done) {
			for (int i = 0; i < node.length; i++) {
				for (int j = 0; j < node.length; j++) {
					answer[i][j] = sim(i, j, node, answer);
				}
			}

			// after simrank done , compare with old adj matrix
			done = stable(node.length, answer_temp, answer);

			// move this result to old adjmatrix
			if (!done) {
				for (int i = 0; i < node.length; i++) {
					for (int j = 0; j < node.length; j++) {
						answer_temp[i][j] = answer[i][j];
					}
				}
			}
			count_iteration++;

		}
		double time2 = System.nanoTime()/1000;
		double timeconsume=time2-time;
		System.out.println("Time consuming of graph_5 is "+ timeconsume + "ms");
		System.out.println("iterations are "+ count_iteration);
/*
		for (int i = 0; i < node.length; i++) {
			for (int j = 0; j < node.length; j++) {
				System.out.print(answer[i][j]+"\t");
			}
			System.out.println();
		}

*/
	}

	public static boolean stable(int len, double[][] old_matrix, double[][] new_matrix) {

		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				if (Math.abs(old_matrix[i][j] - new_matrix[i][j]) > threshold) {
					return false;
				}
			}
		}

		return true;
	}

	public static double sim(int a, int b, int[][] node, double[][] answer) {
		
		if (a == b) {
			// System.out.println("sim:"+1);
			return 1;
		} else {
			int aNum = 0;
			int bNum = 0;
			for (int i = 0; i < node[a].length; i++) {
				if (node[a][i] == 1) {
					aNum++;
				}
				if (node[b][i] == 1) {
					bNum++;
				}
			}

            if (aNum == 0 || bNum == 0) {
				return 0;
			} else {
				int[] aArr = new int[aNum];
				int[] bArr = new int[bNum];
				int countA = 0;
				int countB = 0;
				for (int i = 0; i < node[a].length; i++) {
					if (node[a][i] == 1) {
						aArr[countA] = i;
						countA++;
					}
					if (node[b][i] == 1) {
						bArr[countB] = i;
						countB++;
					}
				}

				double pre = C / (double) (countA * countB);
				//System.out.println("\npre:" + pre);
				double SUM = 0;

				for (int i = 0; i < aArr.length; i++) {
					for (int j = 0; j < bArr.length; j++) {
						SUM += answer[aArr[i]][bArr[j]];
					}
				}
				SUM = SUM * pre;
				return SUM;

			}
			
		}
	}

}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class DataParser {

	public static int[][] parse(){
		 File dir1 = new File(".");
		 try {
			String curPath = dir1.getCanonicalPath();
			int noUsers = 943;
			int noItems = 1682;
			BufferedReader br = new BufferedReader(new FileReader(curPath + "/u.data"));
			String line;
			int[][] data = new int[noUsers][noItems];
			while ((line = br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line , "\t");
				int userId = Integer.parseInt(st.nextToken()) - 1;
				int itemId = Integer.parseInt(st.nextToken()) - 1;
				int rating = Integer.parseInt(st.nextToken());
				data[userId][itemId] = rating;
			}
			br.close();
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		 
	}
	public static void main(String args[]){
		parse();
	}
}

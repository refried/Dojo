package DojoCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;


public class DataParser {

	public static double[][] parse(){
		 File dir1 = new File(".");
		 try {
                    String curPath = dir1.getCanonicalPath();
                    int noUsers = 943;
                    int noItems = 1682;
                     double[][] data;
                     try (BufferedReader br = new BufferedReader(new FileReader(curPath + "/CollaborativeFiltering/u.data"))) {
                         String line;
                         data = new double[noUsers][noItems];
  
                         while ((line = br.readLine()) != null){
                                 StringTokenizer st = new StringTokenizer(line , "\t");
                                 int userId = Integer.parseInt(st.nextToken()) - 1;
                                 int itemId = Integer.parseInt(st.nextToken()) - 1;
                                 double rating = Double.parseDouble(st.nextToken());
                                 data[userId][itemId] = rating;
                         }
                     }
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

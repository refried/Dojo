//package DojoCode;
//import Jama.Matrix;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NeighborhoodBased {
//    final double UNRATED = 0;
//    final double LAMBDA = 1;
//    final double GAMMA = 1;
//    final int numUsers;
//    final int numItems;
//
//    Matrix r;
//    Matrix w; //similarity matrix
//    Matrix ruv; // mean of common ratings
//
//    public NeighborhoodBased(){
//        r = new Matrix(DataParser.parse());
//
//        numUsers = r.getRowDimension();
//        numItems = r.getColumnDimension();
//
//        // Initialize weights
//        w = new Matrix(numUsers, numUsers, 0);
//
//        // Mean of common ratings
//        ruv = new Matrix(numUsers, numUsers, 0);
//
//        // Calculate means
//        calculateMeans();
//
//    }
//
//    public void calculateMeans(){
//        // Looping over all users
//        for(int i = 0; i < numUsers; i++){
//            for(int j = 0; j < numUsers; j++){
//                // Loop over all items
//                double sum_i = 0;
//                double sum_j = 0;
//                int counter = 0;
//                List<Integer> kList = new ArrayList<>();
//                for(int k = 0; k < numItems; k++){
//                    if(r.get(i,k) != 0 && r.get(j,k) != 0){
//                        sum_i = sum_i + r.get(i,k);
//                        sum_j = sum_j + r.get(j,k);
//                        counter = counter + 1;
//                        kList.add(k);
//                    }
//                }
//                // must have 2 or more items in common
//                if(counter >= 2){
//                    double ru = sum_i / counter;
//                    double rv = sum_j / counter;
//                    ruv.set(i, j, ru);
//                    ruv.set(j, i, rv);
//
//                    double sum = 0;
//                    double denomA = 0;
//                    double denomB = 0;
//                    for (int k : kList) {
//                        double numA = (r.get(i, k) - ru);
//                        double numB = (r.get(j, k) - rv);
//                        denomA += Math.pow(numA, 2);
//                        denomB += Math.pow(numB, 2);
//                        sum += numA * numB;
//                    }
//                    double ws = sum / (Math.sqrt(denomA) * Math.sqrt(denomB));
//                    w.set(i, j, ws);
//                }
//            }
//        }
//    }
//
//    public void calculateWeights(){
//        for (int i = 0; i < numUsers; i++) {
//            for (int j = 0; j < numUsers; j++) {
//
//            }
//        }
//    }
//
//    public void run(){
//        calculateWeights();
//    }
//
//}

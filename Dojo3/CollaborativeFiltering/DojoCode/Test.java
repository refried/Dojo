package DojoCode;

public class Test {
    public static double RMSE(){
        return 0;
    }
    
    public static double MAE(NeighborhoodBased nb){
        double mae = 0;
        int n = 0;
        for (int i = 0 ; i < nb.numUsers ; i++){
            for (int j = 0 ; j < nb.numItems ; j++){
                if (nb.r.get(i, j) != 0){
                    mae += Math.abs(nb.predictions.get(i,j) - nb.r.get(i, j));
                    n++;
                }
            }
            System.out.println(mae/n);
        }
        
        return (mae/n);
    }
    
    public static void main(String args[]){
//        MatrixFactorization mf = new MatrixFactorization();
//        mf.run();
        
        NeighborhoodBased nb = new NeighborhoodBased();
        nb.run();
        //MAE test
        System.out.println("MAE for neighborhood based = " + MAE(nb));
        
        //print errors for rmse and mae for both algorithms
        
        
    }
}

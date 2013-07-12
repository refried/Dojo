package DojoCode;

public class Test {
    public static double RMSE(){
        return 0;
    }
    
    public static double MAE(){
        return 0;
    }
    
    public static void main(String args[]){
        MatrixFactorization mf = new MatrixFactorization();
        mf.run();
        
        NeighborhoodBased nb = new NeighborhoodBased();
        nb.run();
        
        //print errors for rmse and mae for both algorithms
        
        
    }
}

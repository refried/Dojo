package DojoCode;
import DojoCode.DataParser;
import Jama.Matrix;
import java.util.ArrayList;

public class MatrixFactorization {
    final double UNRATED = 0;
    final double LAMBDA = 1;
    final double GAMMA = 1;
    final int FACTORS = 30;
    final int numUsers;
    final int numItems;
    final int NUMITERATIONS = 100;
    
    Matrix r;
    Matrix q[]; //item factor vectors
    Matrix p[]; //user factor vectors
    
    public MatrixFactorization(){
        r = new Matrix(DataParser.parse());
        numUsers = r.getRowDimension(); 
        numItems = r.getColumnDimension(); 
    }
    
    public void update(){

    }
    
    public void run(){
        for(int i = 0; i < NUMITERATIONS; i++){
            update();
        }
    }
}

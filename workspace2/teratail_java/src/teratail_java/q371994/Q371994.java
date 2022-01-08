package teratail_java.q371994;

import java.io.PrintStream;

public class Q371994 {

  public static void main(String[] args) {
/*
    GaussianEliminationMatrix matrix = new GaussianEliminationMatrix(
        new double[] {
            2, 4,-2, 8, //2[x1]+4[x2]-2[x3]=8
            1, 2, 1, 6, // [x1]+2[x2]+ [x3]=6
            1, 3, 2, 9, // [x1]+3[x2]+2[x3]=9
        });
*/
    GaussianEliminationMatrix matrix = new GaussianEliminationMatrix(
        new double[] {
             2.0,  4.0,-2.0,-4.0, -10.0,
            -6.0,-12.0,12.0,24.0,  60.0,
             4.0,  2.0, 2.0,-4.0,   8.0,
             2.0, -4.0,-2.0, 4.0,   6.0,
        });
    matrix.printMatrix("start ---");

    matrix.forwardElimination();
    matrix.printMatrix("forward ---");

    double[] x = matrix.backwardSubstitution();

    System.out.println("解は");
    for(int i=0; i<x.length; i++) {
      System.out.println(x[i]);
    }

    //Vector<Double> ans = new Vector<Double>(x.length);
    //for(Double b : x) ans.add(b);
    //return ans;
  }
}

final class GaussianEliminationMatrix {
  private int size;
  private double[][] a_b;

  GaussianEliminationMatrix(double[] matrix) {
    size = (int)(Math.sqrt(matrix.length));
    if(matrix.length != size*size+size) throw new IllegalArgumentException();

    int w = size + 1;
    a_b = new double[size][w];
    for(int i=0; i<matrix.length; i++) {
      a_b[i/w][i%w] = matrix[i];
    }
  }

  double getA(int i, int j) {
    if(j >= size) throw new ArrayIndexOutOfBoundsException("j");
    return a_b[i][j];
  }
  double getB(int i) { return a_b[i][size]; }

  //前進消去
  void forwardElimination() {
    for(int k=0; k<size-1; k++) {
      int p = pivoting(k);
      swap(k, p);
      division(k);
      for(int i=k+1; i<size; i++) {
        subtraction(k, i);
      }
    }
  }
  //k行k列から下の中で一番絶対値が大きい行
  private int pivoting(int k) {
    int pivot = 0;
    double m = 0;
    for(int l=k; l<size; l++) {
      if(m < Math.abs(getA(l,k))) {
        m = Math.abs(getA(l,k));
        pivot = l;
      }
    }
    return pivot;
  }
  //行の入れ替え
  private void swap(int k, int p) {
    double t;
    for(int i=0; i<size+1; i++) {
      t = a_b[k][i];
      a_b[k][i] = a_b[p][i];
      a_b[p][i] = t;
    }
    //System.out.println("swap: k="+k+", p="+p);
    //printMatrix("swap");
  }
  //行の徐算(k行k列を1にする)
  private void division(int k) {
    double q = getA(k,k);
    //System.out.println("division: k="+k+", q="+q);
    for(int l=k; l<size+1; l++) {
      a_b[k][l] /= q;
    }
    //printMatrix("division");
  }
  //行の減算(i行k列を0にする)
  private void subtraction(int k, int i) {
    double p = getA(i,k) / getA(k,k);
    //System.out.println("subtraction: k="+k+", i="+i+", p="+p);
    for(int l=k; l<size+1; l++) {
      a_b[i][l] -= p * a_b[k][l];
    }
    //printMatrix("subtraction");
  }

  //後退代入
  double[] backwardSubstitution() {
    double[] x = new double[size];

    x[size-1] = getB(size-1) / getA(size-1,size-1);
    for(int i=size-2; i>=0; i--) {
      double s=0;
      for(int j=i+1; j<size; j++) {
        s += getA(i,j) * x[j];
      }
      x[i] = (getB(i) - s) / getA(i,i);
    }

    return x;
  }

  //表示
  void printMatrix(String header) {
    System.out.println(header);
    printMatrix(System.out);
  }
  void printMatrix(PrintStream out) {
    for(int i=0; i<size; i++) {
      for(int j=0; j<size+1; j++) {
        System.out.print(a_b[i][j] + " ");
      }
      System.out.println();
    }
  }
}
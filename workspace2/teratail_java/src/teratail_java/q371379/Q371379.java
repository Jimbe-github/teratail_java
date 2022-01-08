package teratail_java.q371379;

public class Q371379 {

  static class graph {
    int vertex_num;
  }
  public static void main(String[] args) {
    int[][] mat = new int[][]{
    {0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] A=new int[10][10], B=new int[10][10], C=new int[10][10];
    graph g = new graph();

    for(int x = 0; x < g.vertex_num; x++){
      for(int src = 0; src < g.vertex_num; src++){
        for(int dest = 0; dest < g.vertex_num; dest++){
          for(int dest2 = 0; dest2 < g.vertex_num; dest2++){
            A[src][dest] += B[src][dest2] * C[dest2][dest];
            B[src][dest2] = A[src][dest];
          }
        }
      }
    }
  }

}

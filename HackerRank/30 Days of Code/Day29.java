import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner scanner = new Scanner(System.in);
        int tests = scanner.nextInt();
        int max = -1;
        int and;
        int [][] nk = new int[tests][2];

        for(int i = 0; i < tests; i++){
            nk[i][0] = scanner.nextInt();
            nk[i][1] = scanner.nextInt();
        }

        for(int i = 0; i < tests; i++){
            for(int j = 1; j <= nk[i][0]; j++){
                for(int z = j + 1; z <= nk[i][0]; z++) {
                    and = j & z;
                    if(and < nk[i][1] && and > max){
                        max = and;
                    }
                }
            }

            System.out.println(max);
            max = -1;
        }
    }
}

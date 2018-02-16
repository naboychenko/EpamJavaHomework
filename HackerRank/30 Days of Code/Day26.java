import java.io.*;
import java.util.*;

public class Solution {

    public static int getFine(String[][] dates){
        int[] coeff = {15, 500, 0, 0, 0, 10000};
        int diff;
        
        for(int i = 2; i >= 0; i--){
            diff = Integer.valueOf(dates[0][i]) - Integer.valueOf(dates[1][i]);
            if(diff > 0){
                return coeff[i] * diff + coeff[i + 3];
            }
            if(diff < 0)
                return 0;
        }
        return 0;
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);

        String[][] dates = new String[2][3];
        String str;
        for(int i = 0; i < 2; i++)
        {
            str = sc.nextLine();
            dates[i] = str.split(" ");

        }

        System.out.println(getFine(dates));
    }
}

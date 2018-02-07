import java.io.*;
import java.util.*;

public class Solution {
    
    public static int countOnes (int n){
        int result = 0;
        int count = 0;
        boolean flag = false;
        
        while(n > 0)
        {
            
            if(n%2 == 1)
            {
                count++;
                flag = true;
            }
            else
            {
                if(flag)
                {
                    if(result < count)
                        result = count;
                    count = 0;
                    flag = true;
                }
            }
            n /= 2;
        }
        return result < count ? count : result;
    }
    
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        System.out.println(countOnes(n));

    }
}

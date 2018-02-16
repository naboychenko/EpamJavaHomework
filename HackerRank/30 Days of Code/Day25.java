import java.io.*;
import java.util.*;

public class Solution {
    
    public static boolean isPrime(int n){
        if(n == 1)
            return false;
        
        for(int i = 2; i <= Math.sqrt(n); i++){
            if(n % i == 0)
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc=new Scanner(System.in);
        
        int T = sc.nextInt();
        LinkedList <Integer> list = new LinkedList<>();
        
        while(T-- > 0){
            int n = sc.nextInt();
            list.add(n);
        }
        
        for(Integer n : list){
            if(isPrime(n))
                System.out.println("Prime");
            else
                System.out.println("Not prime");
        }
    }
}

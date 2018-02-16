import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        int numberOfTestCases;
        Scanner scanner = new Scanner(System.in);
        numberOfTestCases = scanner.nextInt();
        scanner.nextLine();
        String[] stringArray = new String[numberOfTestCases];

        for(int i = 0; i < numberOfTestCases; i++)
        {
            stringArray[i] = scanner.nextLine();
        }
        char [] charArray;
        String first = "", second = "";

        for(int i = 0; i < numberOfTestCases; i++)
        {
            charArray = stringArray[i].toCharArray();
            for(int j = 0; j < charArray.length; j += 2)
            {
                first += charArray[j];
                if(j < charArray.length - 1)
                    second += charArray[j + 1];
            }
            System.out.println(first + " " + second);
            first = "";
            second = "";
        }
    }
}

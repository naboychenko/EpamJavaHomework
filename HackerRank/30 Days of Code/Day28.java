import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        String [][] emailsTable = new String[n][];
        Pattern p = Pattern.compile(".+@gmail\\.com");
        Matcher m;
        ArrayList<String> names = new ArrayList<>();
        
        scanner.nextLine();
        for(int i = 0; i < n; i++){

            emailsTable[i] = scanner.nextLine().split(" ");
            m = p.matcher(emailsTable[i][1]);
            if (m.find()) {
                names.add(emailsTable[i][0]);

            }
        }
        
        Collections.sort(names);

        for(String name : names){
            System.out.println(name);
        }
    }
}

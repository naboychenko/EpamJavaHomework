//Complete this code or write your own from scratch
import java.util.*;
import java.io.*;

class Solution{
    public static void main(String []argh){
        Scanner in = new Scanner(System.in);
        Map<String,Integer> phoneBook = new HashMap<String,Integer>();
        ArrayList<String> names = new ArrayList<>();
        int n = in.nextInt();
        for(int i = 0; i < n; i++){
            String name = in.next();
            int phone = in.nextInt();
            // Write code here
            phoneBook.put(name, phone);
        }
        while(in.hasNext()){
            String s = in.next();
            names.add(s);
            // Write code here
        }
        in.close();

        for(String name : names)
        {
            if(phoneBook.containsKey(name))
            {
                System.out.println(name + "=" + phoneBook.get(name));
            }
            else
            {
                System.out.println("Not found");
            }
        }
    }
}

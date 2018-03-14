import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Viktoria Naboychenko on 14.03.2018.
 */
public class BookInfoProvider {
    private static final String USER_AGENT = "Mozilla/5.0";

    private String httpRequest(String isbn) {
        StringBuffer response = null;
        try {

            String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&jscmd=data&format=json";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (ProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return response.toString();

    }

    private Book parseJSON(String response, String isbn) {
        ArrayList<String> authorsList = new ArrayList<String>();
        String title;

        JSONObject json = JSON.parseObject(response);
        if (json.size() == 0)
            return null;
        json = JSON.parseObject(json.get("ISBN:" + isbn).toString());
        title = json.get("title").toString();

        JSONArray authors = JSONArray.parseArray(json.get("authors").toString());
        for (int i = 0; i < authors.size(); i++) {
            JSONObject obj = (JSONObject) authors.get(i);
            String authorName = obj.getString("name");
            authorsList.add(authorName);
        }
        return new Book(isbn, title, authorsList);
    }


    public Book get(String isbn) {
        String response = httpRequest(isbn);
        Book book = parseJSON(response, isbn);
        return book;
    }
}

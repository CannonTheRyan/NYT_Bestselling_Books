import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Networking{

    public static String makeAPICall(String url){
        try {
            URI myUri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void parseJSON(String json, ArrayList<Book> list) {
        JSONObject jsonObj = new JSONObject(json);
        JSONObject results = jsonObj.getJSONObject("results");
        JSONArray books = results.getJSONArray("books");

        for (int i = 0; i < books.length(); i++){
            JSONObject bookObj = books.getJSONObject(i);
            String title = bookObj.getString("title");
            String author = bookObj.getString("author");
            String description = bookObj.getString("description");
            int rank = bookObj.getInt("rank");
            String bookImageUrl = bookObj.getString("book_image");
            String amazonUrl = bookObj.getString("amazon_product_url");
            Book book = new Book(title, author, description, rank, bookImageUrl, amazonUrl);
            list.add(book);
        }
    }
}
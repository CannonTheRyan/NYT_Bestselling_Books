import java.util.ArrayList;
public class BookManager {
    private String date; //default is current
    private String listType; //required
    private ArrayList<Book> bestSellerList;

    public BookManager() {
        bestSellerList = new ArrayList<Book>();
    }


    public String getDate() {
        return date;
    }

    public String getListType() {
        return listType;
    }

    public ArrayList<Book> getBestSellerList() {
        return bestSellerList;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public void setBestSellerList(ArrayList<Book> bestSellerList) {
        this.bestSellerList = bestSellerList;
    }
}
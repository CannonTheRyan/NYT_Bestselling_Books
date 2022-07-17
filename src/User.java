import java.util.ArrayList;

public class User {
  private String username;
  private String password;
  private ArrayList<Book> favBooksList;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    favBooksList = new ArrayList<Book>();
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }

  public ArrayList<Book> getFavBooksList() {
    return favBooksList;
  }

  public void addBook(Book book)
  {
    favBooksList.add(book);
  }

  public void deleteBook(Book book)
  {
    for (int i = 0; i < favBooksList.size(); i++){
      if (favBooksList.get(i).getTitle().equals(book.getTitle())){
        favBooksList.remove(i);
      }
    }
  }
}
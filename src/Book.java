public class Book {
    private String title;
    private String author;
    private String description;
    private int rank;
    private String bookImageUrl;
    private String amazonUrl;

    public Book(String title, String author, String description, int rank, String bookImageUrl, String amazonUrl) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.rank = rank;
        this.bookImageUrl = bookImageUrl;
        this.amazonUrl = amazonUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public int getRank() {
        return rank;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public String getAmazonUrl() {
        return amazonUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public void setAmazonUrl(String amazonUrl) {
        this.amazonUrl = amazonUrl;
    }
}
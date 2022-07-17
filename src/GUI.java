import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.event.ActionEvent;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class GUI{

	private JFrame frame;

	private JPanel loginRegisterPanel;
	private JLabel titleLabel;
	private JPanel bookImagePanel;
	private JButton loginButton;
	private JButton registerButton;

	private JPanel homePanel;
	private JButton getCurrentRankingBtn;
	private JButton getPastRankingBtn;
	private JButton viewFavBooksBtn;

	private JPanel rankingPanel;
	private JLabel rankingLabel;
	private DefaultListModel rankingData;
	private JList rankingList;
	private JButton showBookDetails;
	private JButton addToFav;

	private JPanel favPanel;
	private DefaultListModel favData;
	private JList favList;
	private JButton showFavDetails;
	private JButton deleteFavBook;

	private JButton back;

	private User user;
	private BookManager manager;

	//login/sign up panel
  /*
    JButton: login
    JButton: signup
  */
	//home panel
  /*
    JButton: "Get current ranking" (automatically uses the current bestSeller list, get the list type through inout dialouge) --> clicking it brings user to Ranking panel
    JButton: "Search past rankings" (get list type and date through input dialouge) --> clicking it brings user to Ranking panel
    JButton: "View Favorited Books" --> clicking it brings user to Fav panel
  */
	//Ranking panel (both buttons in the home panel can lead to this panel cause they both show same info anyways)
  /*
    JLabel: ranking list date and list type
    DefualtListModel and JList: get ranking list from API and list the ranking, title and author
    JButton: show book detail of the book selected--> shows the rank, title, author, description, bookImageUrl, and amazonUrl
    JButton: add to favorites --> add to User favorites list of book selected
  */
	//Fav panel
  /*
    DefualtListModel and JList: get Book list, list the title, author, bookImageUrl, and amazonUrl
    JButton: show book detail of the book selected--> shows the rank, title, author, description, bookImageUrl, and amazonUrl
    JButton: delete selected book from fav list
  */
	public GUI(){

	}

	public void initialize(){
		manager = new BookManager();
		frame = new JFrame("NYT Bestselling Books");
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		bookImagePanel = new JPanel();
		loginRegisterPanel = new JPanel();


		Image tempImage = new ImageIcon("src/book.jpg").getImage().getScaledInstance(250, 170, java.awt.Image.SCALE_SMOOTH);
		JLabel bookImageLabel = new JLabel(new ImageIcon(tempImage));
		bookImagePanel.add(bookImageLabel);


		loginRegisterPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		c.gridx = 0;
		c.gridy = 0;
		titleLabel = new JLabel("NYT Bestselling Books");
		titleLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
		loginRegisterPanel.add(titleLabel, c);

		c.gridy = 1;
		loginRegisterPanel.add(bookImagePanel, c);

		c.gridy = 2;
		registerButton = new JButton("Register");
		registerButton.addActionListener(this::actionPerformed);
		loginRegisterPanel.add(registerButton, c);

		c.gridy = 3;
		loginButton = new JButton("Login");
		loginButton.addActionListener(this::actionPerformed);
		loginRegisterPanel.add(loginButton, c);

		frame.add(loginRegisterPanel);
		frame.pack();
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source == registerButton) {
			String username = JOptionPane.showInputDialog(frame, "Enter a username");
			String password = JOptionPane.showInputDialog(frame, "Enter a password");
			if (username != null && password != null) {
				user = new User(username, password);

			}
		}
		if (source == loginButton && user != null) {
			while (true){
				String username = JOptionPane.showInputDialog(frame, "Enter your username");
				String password = JOptionPane.showInputDialog(frame, "Enter your password");
				if (username.equals(user.getUsername()) && password.equals(user.getPassword())){
					break;
				} else {
					JOptionPane.showMessageDialog(frame, "WRONG USERNAME/PASSWORD");
				}
			}
			loginRegisterPanel.setVisible(false);
			loadHomePanel();
		}
		if (source == getCurrentRankingBtn) {
			homePanel.setVisible(false);
			String listType = JOptionPane.showInputDialog(frame, "Enter the list type\n\nOptions:\nhardcover-fiction\nhardcover-nonfiction\npaperback-fiction\npaperback-nonfiction\ne-book-fiction\ne-book-nonfiction\ngraphic-books-and-manga\npicture-books");
			manager.setListType(listType);
			manager.setDate("current");
			String URL = "https://api.nytimes.com/svc/books/v3/lists/current/" + listType + "/.json?api-key=xj0oVh9V5UmFkZ4QhnrSDNR9PDbtTPOr";
			manager.clearList();
			String json = Networking.makeAPICall(URL);
			Networking.parseJSON(json, manager.getBestSellerList());
			loadRankingPanel();
		}
		if (source == getPastRankingBtn) {
			homePanel.setVisible(false);
			String listType = JOptionPane.showInputDialog(frame, "Enter the list type\n\nOptions:\nhardcover-fiction\nhardcover-nonfiction\npaperback-fiction\npaperback-nonfiction\ne-book-fiction\ne-book-nonfiction\ngraphic-books-and-manga\npicture-books");
			manager.setListType(listType);
			String date = JOptionPane.showInputDialog("Enter the date in the format yyyy-mm-dd");
			manager.setDate(date);
			String URL = "https://api.nytimes.com/svc/books/v3/lists/" + date + "/" + listType + "/.json?api-key=xj0oVh9V5UmFkZ4QhnrSDNR9PDbtTPOr";
			manager.clearList();
			String json = Networking.makeAPICall(URL);
			Networking.parseJSON(json, manager.getBestSellerList());
			loadRankingPanel();
		}
		if (source == viewFavBooksBtn) {
			homePanel.setVisible(false);
			loadFavPanel();
		}
		if (source == back) {
			if (rankingPanel != null) {
				rankingPanel.setVisible(false);
			}
			if (favPanel != null) {
				favPanel.setVisible(false);
			}
			loadHomePanel();
		}
		if (source == showBookDetails) {
			JFrame detailsFrame = new JFrame();
			JPanel panel = new JPanel();
			Book book = manager.getBestSellerList().get(rankingList.getSelectedIndex());

			try{

				panel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.insets = new Insets(10, 10, 10, 10);

				c.gridx = 0;
				c.gridy = 0;
				URL imageURL = new URL(book.getBookImageUrl());
				BufferedImage image = ImageIO.read(imageURL);
				ImageIcon icon = new ImageIcon(imageURL);
				Image tempImage = icon.getImage().getScaledInstance(250, image.getHeight() * 250 / image.getWidth(), java.awt.Image.SCALE_SMOOTH);
				panel.add(new JLabel(new ImageIcon(tempImage)), c);
				c.gridy = 1;
				JTextArea textArea = new JTextArea("Description: " + book.getDescription() + "\nAmazon URL: " + book.getAmazonUrl());
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setOpaque(false);
				textArea.setEditable(false);
				textArea.setSize(400, 100);

				panel.add(textArea, c);

			} catch (Exception ee) {

			}

			detailsFrame.add(panel);
			detailsFrame.setSize(600, 500);
			detailsFrame.setVisible(true);

		}
		if (source == addToFav) {
			user.addBook(manager.getBestSellerList().get(rankingList.getSelectedIndex()));
			JOptionPane.showMessageDialog(null, "Added");
		}
		if (source == showFavDetails) {
			JFrame detailsFrame = new JFrame();
			JPanel panel = new JPanel();
			Book book = user.getFavBooksList().get(favList.getSelectedIndex());

			try{
				panel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.insets = new Insets(10, 10, 10, 10);

				c.gridx = 0;
				c.gridy = 0;
				URL imageURL = new URL(book.getBookImageUrl());
				BufferedImage image = ImageIO.read(imageURL);
				ImageIcon icon = new ImageIcon(imageURL);
				Image tempImage = icon.getImage().getScaledInstance(250, image.getHeight() * 250 / image.getWidth(), java.awt.Image.SCALE_SMOOTH);
				panel.add(new JLabel(new ImageIcon(tempImage)), c);
				c.gridy = 1;
				JTextArea textArea = new JTextArea("Description: " + book.getDescription() + "\nAmazon URL: " + book.getAmazonUrl());
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setOpaque(false);
				textArea.setEditable(false);
				textArea.setSize(400, 100);

				panel.add(textArea, c);
			} catch (Exception ee) {

			}

			detailsFrame.add(panel);
			detailsFrame.setSize(600, 500);
			detailsFrame.setVisible(true);
		}
		if (source == deleteFavBook) {
			int index = favList.getSelectedIndex();
			user.deleteBook(user.getFavBooksList().get(index));
			favData.removeElementAt(index);
			JOptionPane.showMessageDialog(null, "Deleted");
		}
	}
	public void loadHomePanel() {
		homePanel = new JPanel();
		homePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		c.gridx = 0;
		c.gridy = 0;
		getCurrentRankingBtn = new JButton("Get Current Ranking");
		getCurrentRankingBtn.addActionListener(this::actionPerformed);
		homePanel.add(getCurrentRankingBtn, c);

		c.gridy = 1;
		getPastRankingBtn = new JButton("Get Past Ranking");
		getPastRankingBtn.addActionListener(this::actionPerformed);
		homePanel.add(getPastRankingBtn, c);

		c.gridy = 2;
		viewFavBooksBtn = new JButton("View Favorited Books");
		viewFavBooksBtn.addActionListener(this::actionPerformed);
		homePanel.add(viewFavBooksBtn, c);

		frame.add(homePanel);
	}

	public void loadRankingPanel() {
		rankingPanel = new JPanel();
		rankingPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		c.gridx = 0;
		c.gridy = 0;
		rankingLabel = new JLabel("Date: " + manager.getDate() + "     List Type: " + manager.getListType());
		rankingPanel.add(rankingLabel, c);

		c.gridy = 1;
		rankingData = new DefaultListModel();
		for (int i = 0; i < manager.getBestSellerList().size(); i++) {
			rankingData.addElement((i + 1) + ". " + manager.getBestSellerList().get(i).getTitle() + " by " + manager.getBestSellerList().get(i).getAuthor());
		}
		rankingList = new JList(rankingData);
		rankingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		rankingList.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(rankingList);
		listScroller.setPreferredSize(new Dimension(250, 200));
		rankingPanel.add(listScroller, c);

		c.gridy = 2;
		showBookDetails = new JButton("Show Book Details");
		showBookDetails.addActionListener(this::actionPerformed);
		rankingPanel.add(showBookDetails, c);

		c.gridy = 3;
		addToFav = new JButton("Add to Favorites");
		addToFav.addActionListener(this::actionPerformed);
		rankingPanel.add(addToFav, c);
		c.gridy = 4;
		back = new JButton("Back");
		back.addActionListener(this::actionPerformed);
		rankingPanel.add(back, c);

		frame.add(rankingPanel);
	}

	public void loadFavPanel() {
		favPanel = new JPanel();
		favPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		c.gridx = 0;
		c.gridy = 0;
		favData = new DefaultListModel();
		for (int i = 0; i < user.getFavBooksList().size(); i++) {
			favData.addElement((i + 1) + ". " + user.getFavBooksList().get(i).getTitle() + " by " + user.getFavBooksList().get(i).getAuthor());
		}
		favList = new JList(favData);
		favList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		favList.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(favList);
		listScroller.setPreferredSize(new Dimension(250, 200));
		favPanel.add(listScroller, c);

		c.gridy = 1;
		showFavDetails = new JButton("Show Book Details");
		showFavDetails.addActionListener(this::actionPerformed);
		favPanel.add(showFavDetails, c);

		c.gridy = 2;
		deleteFavBook = new JButton("Remove From Fav");
		deleteFavBook.addActionListener(this::actionPerformed);
		favPanel.add(deleteFavBook, c);

		c.gridy = 3;
		back = new JButton("Back");
		back.addActionListener(this::actionPerformed);
		favPanel.add(back, c);

		frame.add(favPanel);
	}
}
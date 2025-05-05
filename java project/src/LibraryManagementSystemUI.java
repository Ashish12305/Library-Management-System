import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

//javax.swing.* is a package that provides a set of lightweight components for Java GUI programming.
//java.awt is a package that provides classes for components that are dependent on the native 
//java.awt.event.ActionListener is an interface that receives action events, such as button clicks.
//java.util.ArrayList is a resizable array implementation of the List interface.
//java.util is a package that contains the collections framework, date and time facilities, internationalization, and other utility classes.
//java.awt.event.ActionEvent is a class that represents an action event, which is generated when a component is activated by the user.

public class LibraryManagementSystemUI extends JFrame {
    private DefaultListModel<String> bookListModel;
    private JList<String> bookList;
    private JTextField titleField;
    private JTextField authorField;
    private ArrayList<Book> books;

    public LibraryManagementSystemUI() {
        setTitle("Library Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        books = new ArrayList<>();

        // Add a colored title label for the project name
        JLabel projectTitle = new JLabel("Library Management System", SwingConstants.CENTER);
        projectTitle.setFont(new Font("Arial", Font.BOLD, 24));
        projectTitle.setForeground(Color.MAGENTA); // Set the color of the project name
        add(projectTitle, BorderLayout.NORTH);

        //Adding Books
        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(titleLabel);
        titleField = new JTextField();
        topPanel.add(titleField);
        JButton addButton = new JButton("Add Book");
        addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.WHITE);
        topPanel.add(addButton);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.BLUE);
        authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(authorLabel);
        authorField = new JTextField();
        topPanel.add(authorField);

        add(topPanel, BorderLayout.NORTH);

        //Book List
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        bookList.setFont(new Font("Courier New", Font.PLAIN, 12));
        bookList.setBackground(Color.WHITE);
        bookList.setForeground(Color.BLACK);
        add(new JScrollPane(bookList), BorderLayout.CENTER);

        //for Actions
        JPanel bottomPanel = new JPanel(new GridLayout(1, 7));
        bottomPanel.setBackground(Color.DARK_GRAY);
        JButton issueButton = new JButton("Issue Book");
        issueButton.setBackground(Color.ORANGE);
        issueButton.setForeground(Color.WHITE);
        JButton returnButton = new JButton("Return Book");
        returnButton.setBackground(Color.CYAN);
        returnButton.setForeground(Color.BLACK);
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        JButton deleteButton = new JButton("Delete Book");
        deleteButton.setBackground(Color.MAGENTA);
        deleteButton.setForeground(Color.WHITE);
        JButton detailsButton = new JButton("Show Details");
        detailsButton.setBackground(Color.YELLOW);
        detailsButton.setForeground(Color.BLACK);
        JButton editButton = new JButton("Edit Book");
        editButton.setBackground(Color.PINK);
        editButton.setForeground(Color.BLACK);
        JButton searchButton = new JButton("Search Book");
        searchButton.setBackground(Color.BLUE);
        searchButton.setForeground(Color.WHITE);
        bottomPanel.add(issueButton);
        bottomPanel.add(returnButton);
        bottomPanel.add(exitButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(detailsButton);
        bottomPanel.add(editButton);
        bottomPanel.add(searchButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Update the book list display to include book code details
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String bookCode = JOptionPane.showInputDialog(this, "Enter Book Code (e.g., A123):");

            if (title.isEmpty() || author.isEmpty() || bookCode == null || bookCode.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title, Author, and Book Code fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Book newBook = new Book(title, author, bookCode.trim());
                books.add(newBook);
                bookListModel.addElement(String.format("%-20s %-20s %-10s", title, author, bookCode)); // Align details
                titleField.setText("");
                authorField.setText("");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Issue Book Action
        issueButton.addActionListener(e -> {
            String selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                String title = selectedBook.split("\s{2,}")[0].trim(); // Extract title from formatted list
                for (Book book : books) {
                    if (book.getTitle().equalsIgnoreCase(title) && !book.isIssued()) {
                        String user = JOptionPane.showInputDialog(this, "Enter the name of the user:");
                        if (user != null && !user.trim().isEmpty()) {
                            book.issueBook(user, "2025-05-04"); // Use current date
                            JOptionPane.showMessageDialog(this, "Book issued successfully to " + user + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        } else {
                            JOptionPane.showMessageDialog(this, "User name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                JOptionPane.showMessageDialog(this, "Book not available or already issued.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to issue.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Return Book Action
        returnButton.addActionListener(e -> {
            String selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                String title = selectedBook.split("\s{2,}")[0].trim(); // Extract title from formatted list
                for (Book book : books) {
                    if (book.getTitle().equalsIgnoreCase(title) && book.isIssued()) {
                        book.returnBook();
                        JOptionPane.showMessageDialog(this, "Book returned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Book not found or not issued.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to return.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Exit Action
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        // Fixing the Delete Book Action
        deleteButton.addActionListener(e -> {
            String selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                String[] parts = selectedBook.split("\s{2,}"); // Split based on spacing
                String title = parts[0].trim(); // Extract title
                for (Book book : books) {
                    if (book.getTitle().equalsIgnoreCase(title)) {
                        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the book titled: " + book.getTitle() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            books.remove(book);
                            bookListModel.removeElement(selectedBook);
                            JOptionPane.showMessageDialog(this, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Book not found in the list.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Show Details Action
        detailsButton.addActionListener(e -> {
            String selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                String title = selectedBook.split("\s{2,}")[0].trim(); // Extract title from formatted list
                for (Book book : books) {
                    if (book.getTitle().equalsIgnoreCase(title)) {
                        StringBuilder details = new StringBuilder();
                        details.append("Title: ").append(book.getTitle()).append("\n");
                        details.append("Author: ").append(book.getAuthor()).append("\n");
                        details.append("Book Code: ").append(book.getBookNumber()).append("\n");
                        details.append("Issued: ").append(book.isIssued() ? "Yes" : "No").append("\n");
                        if (book.isIssued()) {
                            details.append("Issued To: ").append(book.getIssuedTo()).append("\n");
                            details.append("Issued Date: ").append(book.getIssuedDate()).append("\n");
                        }
                        JOptionPane.showMessageDialog(this, details.toString(), "Book Details", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to view details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Edit Book Action
        editButton.addActionListener(e -> {
            String selectedBook = bookList.getSelectedValue();
            if (selectedBook != null) {
                String title = selectedBook.split("\s{2,}")[0].trim(); // Extract title from formatted list
                for (Book book : books) {
                    if (book.getTitle().equalsIgnoreCase(title)) {
                        String newTitle = JOptionPane.showInputDialog(this, "Enter new title:", book.getTitle());
                        String newAuthor = JOptionPane.showInputDialog(this, "Enter new author:", book.getAuthor());
                        String newBookCode = JOptionPane.showInputDialog(this, "Enter new book code:", book.getBookNumber());

                        if (newTitle != null && !newTitle.trim().isEmpty() && 
                            newAuthor != null && !newAuthor.trim().isEmpty() && 
                            newBookCode != null && !newBookCode.trim().isEmpty()) {
                            book.setTitle(newTitle);
                            book.setAuthor(newAuthor);
                            book.setBookNumber(newBookCode.trim());
                            bookListModel.setElementAt(String.format("%-20s %-20s %-10s", newTitle, newAuthor, newBookCode), bookList.getSelectedIndex());
                            JOptionPane.showMessageDialog(this, "Book details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "All fields (title, author, and book code) must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Search Book Action
        searchButton.addActionListener(e -> {
            String searchCode = JOptionPane.showInputDialog(this, "Enter Book Code to Search:");
            if (searchCode != null && !searchCode.trim().isEmpty()) {
                for (Book book : books) {
                    if (book.getBookNumber().equalsIgnoreCase(searchCode.trim())) {
                        StringBuilder details = new StringBuilder();
                        details.append("Title: ").append(book.getTitle()).append("\n");
                        details.append("Author: ").append(book.getAuthor()).append("\n");
                        details.append("Book Code: ").append(book.getBookNumber()).append("\n");
                        details.append("Issued: ").append(book.isIssued() ? "Yes" : "No").append("\n");
                        if (book.isIssued()) {
                            details.append("Issued To: ").append(book.getIssuedTo()).append("\n");
                            details.append("Issued Date: ").append(book.getIssuedDate()).append("\n");
                        }
                        JOptionPane.showMessageDialog(this, details.toString(), "Book Found", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "No book found with the entered code.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Book code cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add dynamic color-changing functionality
        Timer colorTimer = new Timer(1000, e -> {
            float hue = (float) Math.random(); // Generate a random hue
            Color dynamicColor = Color.getHSBColor(hue, 0.5f, 0.9f); // Create a dynamic color

            // Apply the dynamic color to various components
            topPanel.setBackground(dynamicColor);
            bottomPanel.setBackground(dynamicColor.darker());
            bookList.setBackground(dynamicColor.brighter());
            bookList.setForeground(Color.BLACK);

            // Update button colors dynamically
            addButton.setBackground(dynamicColor);
            issueButton.setBackground(dynamicColor);
            returnButton.setBackground(dynamicColor);
            exitButton.setBackground(dynamicColor);
            deleteButton.setBackground(dynamicColor);
            detailsButton.setBackground(dynamicColor);
            editButton.setBackground(dynamicColor);
            searchButton.setBackground(dynamicColor);

            // Set button text colors to contrast with the background
            Color textColor = (dynamicColor.getRed() + dynamicColor.getGreen() + dynamicColor.getBlue() > 382) ? Color.BLACK : Color.WHITE;
            addButton.setForeground(textColor);
            issueButton.setForeground(textColor);
            returnButton.setForeground(textColor);
            exitButton.setForeground(textColor);
            deleteButton.setForeground(textColor);
            detailsButton.setForeground(textColor);
            editButton.setForeground(textColor);
            searchButton.setForeground(textColor);
        });

        colorTimer.start();
    }

    public void addBook(String title, String author) {
        Book newBook = new Book(title, author);
        books.add(newBook);
        System.out.println("Book added successfully.");
    }

    public void addBookByAdmin(String title, String author) {
        Book newBook = new Book(title, author);
        books.add(newBook);
        System.out.println("Admin added book successfully.");
    }

    public void issueBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && !book.isIssued()) {
                String user = JOptionPane.showInputDialog(this, "Enter the name of the user:", "User Input", JOptionPane.PLAIN_MESSAGE);
                if (user != null && !user.trim().isEmpty()) {
                    String location = JOptionPane.showInputDialog(this, "Enter the location of issuance:", "Location Input", JOptionPane.PLAIN_MESSAGE);
                    if (location != null && !location.trim().isEmpty()) {
                        String currentDate = "2025-05-03"; // Using the current date
                        String currentTime = java.time.LocalTime.now().toString(); // Fetching the current time
                        book.issueBook(user, currentDate);
                        JOptionPane.showMessageDialog(this, "Book issued successfully to " + user + " at " + location + " on " + currentDate + " at " + currentTime + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Location cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Book not available or already issued.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void returnBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isIssued()) {
                book.returnBook();
                System.out.println("Book returned successfully.");
                return;
            }
        }
        System.out.println("Book not found or not issued.");
    }

    public void deleteBook(String title) {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
        System.out.println("Book deleted successfully.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystemUI libraryUI = new LibraryManagementSystemUI();
            libraryUI.setVisible(true);
        });
    }
}

class Book {
    private String title;
    private String author;
    private String bookNumber;
    private boolean isIssued;
    private String issuedTo;
    private String issuedDate;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issuedTo = null;
        this.issuedDate = null;
    }

    public Book(String title, String author, String bookNumber) {
        this.title = title;
        this.author = author;
        this.bookNumber = bookNumber;
        this.isIssued = false;
        this.issuedTo = null;
        this.issuedDate = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void issueBook(String user, String date) {
        isIssued = true;
        issuedTo = user;
        issuedDate = date;
    }

    public void returnBook() {
        isIssued = false;
        issuedTo = null;
        issuedDate = null;
    }
}
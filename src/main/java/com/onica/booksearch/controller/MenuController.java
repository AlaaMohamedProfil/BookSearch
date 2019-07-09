package com.onica.booksearch.controller;

import com.onica.booksearch.model.Book;
import com.onica.booksearch.service.BookService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuController {

    private static final int VIEW = 1;
    private static final int ADD = 2;
    private static final int EDIT = 3;
    private static final int SEARCH = 4;
    private static final int SAVE = 5;

    BookService bookService = new BookService();
    private List<Book> booksInDatabase;

    public MenuController() {
        this.booksInDatabase = bookService.readBooksFromDatabase();
        System.out.println("Loaded " + booksInDatabase.size() + " books into the library");
    }

    public void startUserMenuChoices() {
        System.out.print("==== Book Manager ====\n"
                + "	1) View all books\n"
                + "	2) Add a book\n"
                + "	3) Edit a book\n"
                + "	4) Search for a book\n"
                + "	5) Save and exit\n"
                + "Choose [1-5]: ");
        Scanner userInput = new Scanner(System.in);
        validateInputFormat(userInput);
        validateInputNumberAndPerformAction(userInput);
    }

    private void validateInputFormat(Scanner userInput) {
        while (!userInput.hasNextInt()) {
            System.out.print("That's not a valid number! \nChoose [1-5]:");
            userInput.next();
        }
    }

    private void validateInputNumberAndPerformAction(Scanner userInput) {
        Integer userChoice = userInput.nextInt();
        if (userChoice > 5 || userChoice < 1) {
            System.out.println("That's not a valid number!");
            startUserMenuChoices();
        }
        checkUserInputAndPerformAction(userChoice);
    }

    private void checkUserInputAndPerformAction(Integer userChoice) {
        switch (userChoice) {
            case VIEW:
                viewBooks();
                break;
            case ADD:
                addToBookList();
                break;
            case EDIT:
                editBookInList();
                break;
            case SEARCH:
                searchInList();
                break;
            case SAVE:
                saveList();
                break;
        }
    }

    private void viewBooks() {
        System.out.println("==== View Books ====");
        displayBooks();
        getViewBookIdFromUser();
    }

    private void getViewBookIdFromUser() {
        System.out.print("To view details enter the book ID, to return press <Enter>.\n"
                + "Book ID: ");
        Scanner userInput = new Scanner(System.in);
        String choice = userInput.nextLine();
        if (choice.isEmpty()) {
            startUserMenuChoices();
        } else if (choice.matches("\\d+")) {
            searchBookById(choice);
        } else {
            viewBooks();
        }
    }
    
    private void addToBookList() {
        System.out.println("==== Add a Book ====");
        System.out.println("Please enter the following information:");
        Book newBook = new Book();
        newBook.setId(booksInDatabase.get(booksInDatabase.size() - 1).getId() + 1);
        Scanner userInput = new Scanner(System.in);
        System.out.print("\tTitle: ");
        String currentInput = userInput.nextLine();
        newBook.setTitle(currentInput);
        System.out.print("\tAuthor : ");
        currentInput = userInput.nextLine();
        newBook.setAuthor(currentInput);
        System.out.print("\tDescription : ");
        currentInput = userInput.nextLine();
        newBook.setDescription(currentInput);
        booksInDatabase.add(newBook);
        System.out.println("Book " + newBook.getId() + " saved.");
        startUserMenuChoices();
    }

    private void editBookInList() {
        System.out.println("==== Edit a Book ====");
        displayBooks();
        getCurrentBookId();
    }

    private void getCurrentBookId(){
        System.out.print("Enter the book ID of the book you want to edit; to return press <Enter>.\n" +
                           "Book ID: ");
        Scanner userInput = new Scanner(System.in);
        String choice = userInput.nextLine();
        if (choice.isEmpty()) {
            startUserMenuChoices();
        } else if (choice.matches("\\d+")) {
            editBookById(choice);
        } else {
            editBookInList();
        }
    }
    private void searchInList() {
        System.out.print("==== Search ====\n"
                + "Type in one or more keywords to search for\n"
                + "\tSearch: ");
        Scanner userInput = new Scanner(System.in);
        String searchKey = userInput.nextLine();
        if (searchKey.isEmpty()) {
            startUserMenuChoices();
        }
        searchBookByTitel(searchKey, userInput);
    }

    private void saveList() {
        bookService.writeBooksToDatabase(booksInDatabase);
        System.out.println("Library saved.");
        System.exit(0);
    }

    private void searchBookById(String choice) {
        Integer bookId = Integer.parseInt(choice);
        booksInDatabase.forEach((book) -> {
            if (book.getId() == bookId){
                displayBookDetails(book);
                return;
            }
        });
        getViewBookIdFromUser();
    }

    private void displayBookDetails(Book book) {
        System.out.println("\tID: " + book.getId() + "\n" +
"	Title: " + book.getTitle() + "\n" +
"	Author: " + book.getAuthor() + "\n" +
"	Description: " + book.getDescription());
    }

    private void searchBookByTitel(String searchKey, Scanner userInput) {
        Map<Integer, Book> searchBooks = new LinkedHashMap<>()  ;
        booksInDatabase.forEach((book) -> {
            if (book.getTitle().toLowerCase().contains(searchKey)) {
                System.out.println("\t" + book.toString());
                searchBooks.put(book.getId(), book);
            }
        });
        if(searchBooks.isEmpty()) {
            System.out.println("No results found");
            searchInList();
        }
        System.out.println("The following books matched your query. Enter the book ID to see more details, or <Enter> to return.");
        System.out.print("Book ID: ");
        String choice = userInput.nextLine();
        if (choice.matches("\\d+")) {
            Integer bookId = Integer.parseInt(choice);
            if(searchBooks.containsKey(bookId)){
                displayBookDetails(searchBooks.get(bookId));
            }
            
        }
        searchInList();
    }

    private void displayBooks() {
        booksInDatabase.forEach((book) -> {
            System.out.println("\t" + book.toString());
        });
    }

    private void editBookById(String choice) {
        System.out.println("Input the following information. To leave a field unchanged, hit <Enter>");
        Integer bookId = Integer.parseInt(choice);
        Scanner userInput = new Scanner(System.in);
        booksInDatabase.forEach((book) -> {
            if (book.getId() == bookId){
                System.out.print("\tTitle [" + book.getTitle() + "]: ");
                String currentInput = userInput.nextLine();
                book.setTitle(currentInput);
                System.out.print("\tAuthor [" + book.getAuthor()+ "]: ");
                currentInput = userInput.nextLine();
                book.setAuthor(currentInput);
                System.out.print("\tDescription [" + book.getDescription()+ "]: ");
                currentInput = userInput.nextLine();
                book.setDescription(currentInput);
                System.out.println("Book saved.");
                return;
            }
        });
        getCurrentBookId();
    }

}

package com.onica.booksearch.service;

import com.onica.booksearch.model.Book;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BookService {
    
    public static final String FILE_PATH = "/db/bookDB.txt";

    public List<Book> readBooksFromDatabase() {
        
        List<Book> booksInDb = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(getClass().getResource(FILE_PATH).getFile()))) {

            while ((line = br.readLine()) != null) {
                Book book = getBookFromLine(line);
                booksInDb.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return booksInDb;
    }

    private Book getBookFromLine(String line) {
        String[] lineSplit = line.split(";");
        Book book = new Book();
        book.setId(Integer.parseInt(lineSplit[0]));
        book.setTitle(lineSplit[1]);
        book.setAuthor(lineSplit[2]);
        book.setDescription(lineSplit[3]);
        return book;
    }
    
    public void writeBooksToDatabase(List<Book> books) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getClass().getResource(FILE_PATH).getFile()), "utf-8"))) {
            String fileContent = "";
            for (Book book : books) {
               fileContent += covertBookToLine(book) + "\n";
            }
            writer.write(fileContent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String covertBookToLine(Book book) {
        return book.getId() + ";" + book.getTitle() + ";" + book.getAuthor()+ ";" + book.getDescription();
    }
    
    
}

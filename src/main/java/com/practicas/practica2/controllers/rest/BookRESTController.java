package com.practicas.practica2.controllers.rest;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.practicas.practica2.entities.Book;
import com.practicas.practica2.entities.Library;
import com.practicas.practica2.entities.Student;
import com.practicas.practica2.service.BookService;
import com.practicas.practica2.service.LibraryService;
import com.practicas.practica2.service.StudentService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/books")
public class BookRESTController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<Collection<Book>> getAllBooksREST() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-book")
    public ResponseEntity<Book> createBookREST(@RequestBody JsonNode body) {
        Book newBook = new Book();

        JsonNode bookTitle = body.has("title") ? body.get("title") : null;
        JsonNode bookAuthor = body.has("author") ? body.get("author") : null;
        JsonNode bookIsbn = body.has("isbn") ? body.get("isbn") : null;
        JsonNode bookPD = body.has("publishDate") ? body.get("publishDate") : null;

        boolean validTitle = (bookTitle != null && !bookTitle.asText().equals(""));
        boolean validAuthor = (bookAuthor != null && !bookAuthor.asText().equals(""));
        boolean validIsbn = (bookIsbn != null && !bookIsbn.asText().equals(""));
        boolean validPD = (bookPD != null && !bookPD.asText().equals(""));

        boolean validDateFormat = validPD && isValidDate(bookPD.asText());

        if (validTitle && validAuthor && validIsbn && validPD && validDateFormat) {
            newBook.setTitle(bookTitle.asText());
            newBook.setAuthor(bookAuthor.asText());
            newBook.setISBN(bookIsbn.asText());
            newBook.setPublishDate(bookPD.asText());

            if (!body.get("library").isNumber() || !body.get("library").isNumber() || body.get("library").asLong() <= 0){
                return ResponseEntity.badRequest().build();
            }

            if (!body.get("student").isNumber() || !body.get("student").isNumber() || body.get("student").asLong() <= 0){
                return ResponseEntity.badRequest().build();
            }

            Long libraryId = body.has("library") ? (body.get("library").isNumber() ? body.get("library").asLong() : -1)
                    : -1;
            Long studentId = body.has("student") ? (body.get("student").isNumber() ? body.get("student").asLong() : -1)
                    : -1;

            Book createdBook = bookService.createBook(newBook, libraryId, studentId);
            if (createdBook != null) {
                return ResponseEntity.status(201).body(createdBook);
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody JsonNode body) {
        Book existingBook = bookService.getBookById(id);

        if (existingBook != null) {
            JsonNode bookTitle = body.has("title") ? body.get("title") : null;
            JsonNode bookAuthor = body.has("author") ? body.get("author") : null;
            JsonNode bookIsbn = body.has("isbn") ? body.get("isbn") : null;
            JsonNode bookPD = body.has("publishDate") ? body.get("publishDate") : null;

            boolean validTitle = (bookTitle != null && !bookTitle.asText().equals(""));
            boolean validAuthor = (bookAuthor != null && !bookAuthor.asText().equals(""));
            boolean validIsbn = (bookIsbn != null && !bookIsbn.asText().equals(""));
            boolean validPD = (bookPD != null && !bookPD.asText().equals(""));

            boolean validDateFormat = validPD && isValidDate(bookPD.asText());

            if (validTitle && validAuthor && validIsbn && validPD && validDateFormat) {
                existingBook.setTitle(bookTitle.asText());
                existingBook.setAuthor(bookAuthor.asText());
                existingBook.setISBN(bookIsbn.asText());
                existingBook.setPublishDate(bookPD.asText());

                if (!body.get("library").isNumber() || !body.get("library").isNumber() || body.get("library").asLong() <= 0){
                    return ResponseEntity.badRequest().build();
                }
    
                if (!body.get("student").isNumber() || !body.get("student").isNumber() || body.get("student").asLong() <= 0){
                    return ResponseEntity.badRequest().build();
                }

                Long libraryId = body.has("library")
                        ? (body.get("library").isNumber() ? body.get("library").asLong() : -1)
                        : -1;
                Long studentId = body.has("student")
                        ? (body.get("student").isNumber() ? body.get("student").asLong() : -1)
                        : -1;

                Book updatedBook = bookService.updateBook(existingBook, libraryId, studentId);
                if (updatedBook != null) {
                    return ResponseEntity.ok().body(updatedBook);
                } else {
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable long id) {
        Book existingBook = bookService.getBookById(id);

        if (existingBook != null) {
            return ResponseEntity.ok().body(bookService.deleteBook(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(@PathVariable long id, @RequestBody JsonNode body) {
        Book existingBook = bookService.getBookById(id);

        if (existingBook != null) {
            boolean hasTitle = body.has("title");
            boolean hasAuthor = body.has("author");
            boolean hasIsbn = body.has("isbn");
            boolean hasPublishDate = body.has("publishDate");
            boolean hasLibrary = body.has("library");
            boolean hasStudent = body.has("student");

            if (hasTitle || hasAuthor || hasIsbn || hasPublishDate || hasStudent || hasLibrary) {
                String title = hasTitle ? body.get("title").asText() : null;
                String author = hasAuthor ? body.get("author").asText() : null;
                String isbn = hasIsbn ? body.get("isbn").asText() : null;

                if (body.has("library")){
                    if (!body.get("library").isNumber() || body.get("library").asLong() <= 0){
                        return ResponseEntity.badRequest().build();
                    }
                }

                if (body.has("student")){
                    if (!body.get("student").isNumber() || body.get("student").asLong() <= 0){
                        return ResponseEntity.badRequest().build();
                    }
                }


                Long libraryId = body.has("library")
                        ? (body.get("library").isNumber() ? body.get("library").asLong() : -1)
                        : -1;
                Long studentId = body.has("student")
                        ? (body.get("student").isNumber() ? body.get("student").asLong() : -1)
                        : -1;

                if (hasTitle)
                    existingBook.setTitle(title);

                if (hasAuthor)
                    existingBook.setAuthor(author);

                if (hasIsbn)
                    existingBook.setISBN(isbn);

                if (hasPublishDate){
                    if (isValidDate(body.get("publishDate").asText())){
                        existingBook.setPublishDate(body.get("publishDate").asText());
                    }else{
                        return ResponseEntity.badRequest().build();
                    }
                }

                Book patchedBook = bookService.patchBook(existingBook, libraryId, studentId);
                if (patchedBook != null) {
                    return ResponseEntity.ok().body(patchedBook);
                }
                return ResponseEntity.badRequest().build();
            } else
                return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.notFound().build();
    }

    public static boolean isValidDate(String date) {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
}
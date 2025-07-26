package com.practicas.practica2.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practicas.practica2.entities.Book;
import com.practicas.practica2.service.BookService;
import com.practicas.practica2.service.LibraryService;
import com.practicas.practica2.service.StudentService;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private StudentService studentsService;

    @GetMapping("")
    public String showBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "getAll/books";
    }

    @GetMapping("/create-book")
    public String createBook(Model model) {
        model.addAttribute("newBook", new Book());
        model.addAttribute("existingLibraries", libraryService.getAllLibraries());
        model.addAttribute("existingStudents", studentsService.getAllStudents());
        return "create/createBook";
    }

    @PostMapping("/create-book")
    public String postCreateBook(Book newBook, Long libraryId, Long studentId) {
        libraryId = libraryId == null ? -1l : libraryId;
        studentId = studentId == null ? -1l : studentId;
        bookService.createBook(newBook, libraryId, studentId);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBookById(@PathVariable long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null)
            return "redirect:/books";

        model.addAttribute("book", book);
        return "getOne/book";
    }

    @GetMapping("/{id}/edit-book")
    public String editBook(@PathVariable long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null)
            return "error";

        model.addAttribute("book", book);
        model.addAttribute("actualLibraryId", book.getLibrary() != null ? book.getLibrary().getId() : 0);
        model.addAttribute("actualStudentId", book.getStudent() != null ? book.getStudent().getId() : 0);
        model.addAttribute("existingLibraries", libraryService.getAllLibraries());
        model.addAttribute("existingStudents", studentsService.getAllStudents());
        return "edit/editBook";
    }

    @PostMapping("/{id}/edit-book")
    public String updateBook(@PathVariable long id, Book book, Long libraryId, Long studentId) {
        libraryId = libraryId == null ? -1l : libraryId;
        studentId = studentId == null ? -1l : studentId;

        Book updatedBook = bookService.patchBook(book, libraryId, studentId);
        if (updatedBook == null) {
            return "error";
        }
        return "redirect:/books/" + id;
    }

    @GetMapping("/{id}/remove")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}

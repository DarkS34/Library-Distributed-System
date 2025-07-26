package com.practicas.practica2.controllers.web;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practicas.practica2.entities.Library;
import com.practicas.practica2.service.BookService;
import com.practicas.practica2.service.LibraryService;
import com.practicas.practica2.service.StudentService;

@Controller
@RequestMapping("/libraries")
public class LibraryController {

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @GetMapping("")
    public String showLibraries(Model model) {
        model.addAttribute("libraries", libraryService.getAllLibraries());
        return "getAll/libraries";
    }

    @GetMapping("/create-library")
    public String createLibrary(Model model) {
        model.addAttribute("newLibrary", new Library());
        model.addAttribute("existingBooks", bookService.getAllBooks());
        return "create/createLibrary";
    }

    @PostMapping("/create-library")
    public String postCreateLibrary(Library newLibrary,
            @RequestParam(value = "selectedBooks", required = false) Set<Long> selectedBooks) {
        Set<Long> books = (selectedBooks != null) ? new HashSet<>(selectedBooks) : null;
        Library result = libraryService.createLibrary(newLibrary, books);
        if (result == null)
            return "error";

        return "redirect:/libraries";
    }

    @GetMapping("/{id}")
    public String watchLibrary(@PathVariable int id, Model model) {
        Library library = libraryService.getLibraryById(id);
        if (library == null)
            return "redirect:/libraries";

        model.addAttribute("library", library);
        model.addAttribute("booksExist", !library.getBooks().isEmpty());
        model.addAttribute("studentsExist", !library.getStudentsBorrowed().isEmpty());
        return "getOne/library";
    }

    @GetMapping("/{id}/edit-library")
    public String editLibrary(@PathVariable int id, Model model) {
        Library library = libraryService.getLibraryById(id);
        if (library == null) {
            return "error-page";
        }
        model.addAttribute("library", library);
        model.addAttribute("existingBooks", bookService.getAllBooks());
        model.addAttribute("libraryBooks", library.getBooks() != null ? library.getBooks() : null);
        return "edit/editLibrary";
    }

    @PostMapping("/{id}/edit-library")
    public String updateLibrary(@PathVariable int id, Library updatedLibrary,
            @RequestParam(value = "selectedBooks", required = false) Set<Long> selectedBooks) {
        Library result = libraryService.patchLibrary(updatedLibrary, selectedBooks);
        if (result == null) {
            return "error";
        }
        return "redirect:/libraries/" + id;
    }

    @GetMapping("/{id}/remove")
    public String deleteLirbary(@PathVariable int id) {
        libraryService.deleteLibrary(id);
        return "redirect:/libraries";
    }

}

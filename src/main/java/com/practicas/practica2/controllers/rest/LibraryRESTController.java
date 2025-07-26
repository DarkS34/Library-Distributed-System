package com.practicas.practica2.controllers.rest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.practicas.practica2.entities.Library;
import com.practicas.practica2.service.LibraryService;

@RestController
@RequestMapping("api/libraries")
public class LibraryRESTController {
    @Autowired
    private LibraryService libraryService;

    @GetMapping
    public ResponseEntity<Collection<Library>> getAllLibrariesREST() {
        return ResponseEntity.ok(libraryService.getAllLibraries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getStudentByIdREST(@PathVariable int id) {
        Library library = libraryService.getLibraryById(id);
        if (library != null) {
            return ResponseEntity.ok(library);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-library")
    public ResponseEntity<Library> createLibraryREST(@RequestBody JsonNode body) {
        Library newLibrary = new Library();

        JsonNode libraryName = body.has("name") ? body.get("name") : null;
        JsonNode libraryEmail = body.has("email") ? body.get("email") : null;
        JsonNode libraryAddress = body.has("address") ? body.get("address") : null;

        boolean validTitle = (libraryName != null && !libraryName.asText().equals(""));
        boolean validEmail = (libraryEmail != null && !libraryEmail.asText().equals(""));
        boolean validAddress = (libraryAddress != null && !libraryAddress.asText().equals(""));

        boolean validEmailFormat = validEmail && isValidEmail(libraryEmail.asText());

        if (validTitle && validEmail && validAddress && validEmailFormat) {

            newLibrary.setName(libraryName.asText());
            newLibrary.setEmail(libraryEmail.asText());
            newLibrary.setAddress(libraryAddress.asText());
            Iterator<JsonNode> libraryBooksIt = body.has("books")
                    ? (body.get("books").isArray() ? body.get("books").elements() : null)
                    : null;
            Set<Long> books = libraryBooksIt != null ? iteratorToSet(libraryBooksIt) : null;

            Library createdLibrary = libraryService.createLibrary(newLibrary, books);
            if (createdLibrary != null)
                return ResponseEntity.status(201).body(createdLibrary);
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> updateLibraryREST(@PathVariable int id, @RequestBody JsonNode body) {
        Library existingLibrary = libraryService.getLibraryById(id);

        if (existingLibrary != null) {
            JsonNode libraryName = body.has("name") ? body.get("name") : null;
            JsonNode libraryEmail = body.has("email") ? body.get("email") : null;
            JsonNode libraryAddress = body.has("address") ? body.get("address") : null;

            boolean validTitle = (libraryName != null && !libraryName.asText().equals(""));
            boolean validEmail = (libraryEmail != null && !libraryEmail.asText().equals(""));
            boolean validAddress = (libraryAddress != null && !libraryAddress.asText().equals(""));

            boolean validEmailFormat = validEmail && isValidEmail(libraryEmail.asText());

            if (validTitle && validEmail && validAddress && validEmailFormat) {

                existingLibrary.setName(libraryName.asText());
                existingLibrary.setEmail(libraryEmail.asText());
                existingLibrary.setAddress(libraryAddress.asText());
                Iterator<JsonNode> libraryBooksIt = body.has("books")
                        ? (body.get("books").isArray() ? body.get("books").elements() : null)
                        : null;
                Set<Long> books = libraryBooksIt != null ? iteratorToSet(libraryBooksIt) : null;
                
                Library updatedLibrary = libraryService.updateLibrary(existingLibrary, books);
                if (updatedLibrary != null)
                    return ResponseEntity.ok().body(updatedLibrary);
                return ResponseEntity.internalServerError().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLibraryREST(@PathVariable int id) {
        Library existingLibrary = libraryService.getLibraryById(id);

        if (existingLibrary != null) {
            return ResponseEntity.ok().body(libraryService.deleteLibrary(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Library> patchLibraryREST(@PathVariable int id, @RequestBody JsonNode body) {
        Library existingLibrary = libraryService.getLibraryById(id);

        if (existingLibrary != null) {
            boolean hasName = body.has("name");
            boolean hasAddress = body.has("address");
            boolean hasEmail = body.has("email");
            boolean hasBooks = body.has("books");

            if (hasName || hasAddress || hasEmail || hasBooks) {
                String name = hasName ? body.get("name").asText() : null;
                String address = hasAddress ? body.get("address").asText() : null;
                Iterator<JsonNode> libraryBooksIt = body.has("books")
                        ? (body.get("books").isArray() ? body.get("books").elements() : null)
                        : null;
                Set<Long> books = libraryBooksIt != null ? iteratorToSet(libraryBooksIt) : null;

                if (hasName)
                    existingLibrary.setName(name);
                if (hasAddress)
                    existingLibrary.setAddress(address);
                if (hasEmail) {
                    String email = body.get("email").asText();
                    if (isValidEmail(email)) {
                        existingLibrary.setEmail(email);
                    } else
                        return ResponseEntity.badRequest().build();
                }

                Library patchedLibrary = libraryService.patchLibrary(existingLibrary, books);
                if (patchedLibrary != null)
                    return ResponseEntity.ok().body(patchedLibrary);
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.notFound().build();
    }

    // ADD BOOK TO LIBRARY
    // @PutMapping("/{library_id}/book/{book_id}")
    // public ResponseEntity<String> addBook(@PathVariable Long library_id,
    // @PathVariable Long book_id) {
    // Book book = bookService.getBookById(book_id);
    // Library library = libraryService.getLibraryById(library_id);
    // if (book == null || library == null) {
    // return ResponseEntity.notFound().build();
    // }
    // book.setLibrary(library);
    // library.addBook(book);
    // libraryService.saveLibrary(library);
    // return ResponseEntity.ok("Book: " + book.getISBN() + " has been added to
    // Library: " + library_id);
    // }

    // //ADD ROW TO RELATION TABLE LIBRARY - STUDENT
    // @PutMapping("/{library_id}/student/{student_id}")
    // public ResponseEntity<String> newLibraryStudentRelation(@PathVariable Long
    // library_id , @PathVariable Long student_id) {
    // Student student = studentService.getStudentById(student_id);
    // Library library = libraryService.getLibraryById(library_id);
    // if(student == null || library == null){
    // return ResponseEntity.notFound().build();
    // }
    // library.addBorrowedFromLibrary(student);
    // student.addBorrowedFromStudent(library);
    // libraryService.saveLibrary(library);
    // studentService.createStudent(student);
    // return ResponseEntity.ok("Student: " + student_id + " and Library: " +
    // library_id + " are now related");
    // }

    private Set<Long> iteratorToSet(Iterator<JsonNode> iterator) {
        Set<Long> resultSet = new HashSet<>();
        while (iterator.hasNext()) {
            JsonNode jsonNode = iterator.next();
            long value = jsonNode.asLong();
            resultSet.add(value);
        }
        return resultSet;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

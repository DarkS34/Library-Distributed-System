package com.practicas.practica2.controllers.rest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
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

import com.practicas.practica2.entities.Book;
import com.practicas.practica2.entities.Library;
import com.practicas.practica2.entities.Student;
import com.practicas.practica2.service.BookService;
import com.practicas.practica2.service.LibraryService;
import com.practicas.practica2.service.StudentService;

import static com.practicas.practica2.controllers.rest.LibraryRESTController.isValidEmail;

@RestController
@RequestMapping("api/students")
public class StudentRESTController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    private Set<Long> iteratorToSet(Iterator<JsonNode> iterator) {
        Set<Long> resultSet = new HashSet<>();
        while (iterator.hasNext()) {
            JsonNode jsonNode = iterator.next();
            long value = jsonNode.asLong();
            resultSet.add(value);
        }
        return resultSet;
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudentsREST() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentByIdREST(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-student")
    public ResponseEntity<Student> createStudentREST(@RequestBody JsonNode body) {
        Student newStudent = new Student();

        JsonNode studentName = body.has("name") ? body.get("name") : null;
        JsonNode studentEmail = body.has("email") ? body.get("email") : null;

        boolean validName = (studentName != null && !studentName.asText().equals(""));
        boolean validEmail = (studentEmail != null && !studentEmail.asText().equals(""));

        boolean validEmailFormat = validEmail && isValidEmail(studentEmail.asText());

        if (validName && validEmail && validEmailFormat) {

            newStudent.setName(studentName.asText());
            newStudent.setEmail(studentEmail.asText());
            Iterator<JsonNode> studentBooksIt = body.has("books")
                    ? (body.get("books").isArray() ? body.get("books").elements() : null)
                    : null;
            Set<Long> books = studentBooksIt != null ? iteratorToSet(studentBooksIt) : null;

            Student createdStudent = studentService.createStudent(newStudent, books);

            if (createdStudent != null)
                return ResponseEntity.status(201).body(createdStudent);
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentREST(@PathVariable Long id, @RequestBody JsonNode body) {
        Student existingStudent = studentService.getStudentById(id);

        if (existingStudent != null){

            JsonNode studentName = body.has("name") ? body.get("name") : null;
            JsonNode studentEmail = body.has("email") ? body.get("email") : null;
            
            boolean validName = (studentName != null && !studentName.asText().equals(""));
            boolean validEmail = (studentEmail != null && !studentEmail.asText().equals(""));
            
            boolean validEmailFormat = validEmail && isValidEmail(studentEmail.asText());
            
            if (validName && validEmail && validEmailFormat) {
                
                existingStudent.setName(studentName.asText());
                existingStudent.setEmail(studentEmail.asText());
                Iterator<JsonNode> studentBooksIt = body.has("books")
                ? (body.get("books").isArray() ? body.get("books").elements() : null)
                : null;
                Set<Long> books = studentBooksIt != null ? iteratorToSet(studentBooksIt) : null;
                
                Student updatedStudent = studentService.updateStudent(existingStudent, books);
                
                if (updatedStudent != null)
                    return ResponseEntity.ok().body(updatedStudent);
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentREST(@PathVariable Long id) {
        Student existingStudent = studentService.getStudentById(id);

        if (existingStudent != null) {
            return ResponseEntity.ok().body(studentService.deleteStudent(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudentREST(@PathVariable Long id, @RequestBody JsonNode body) {
        Student existingStudent = studentService.getStudentById(id);

        if (existingStudent != null) {
            boolean hasName = body.has("name");
            boolean hasEmail = body.has("email");
            boolean hasBooks = body.has("books");

            if (hasName || hasEmail || hasBooks) {
                String name = hasName ? body.get("name").asText() : null;
                Iterator<JsonNode> libraryBooksIt = body.has("books")
                        ? (body.get("books").isArray() ? body.get("books").elements() : null)
                        : null;
                Set<Long> books = libraryBooksIt != null ? iteratorToSet(libraryBooksIt) : null;

                if (hasName)
                    existingStudent.setName(name);
                if (hasEmail) {
                    String email = body.get("email").asText();
                    if (isValidEmail(email)) {
                        existingStudent.setEmail(email);
                    } else
                        return ResponseEntity.badRequest().build();
                }

                Student patchedStudent = studentService.patchStudent(existingStudent, books);
                if (patchedStudent != null)
                    return ResponseEntity.ok().body(patchedStudent);
                System.out.println("no patchedstudent");
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.notFound().build();
    }
}

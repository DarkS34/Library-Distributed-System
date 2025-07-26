package com.practicas.practica2.controllers.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practicas.practica2.entities.Student;
import com.practicas.practica2.service.BookService;
import com.practicas.practica2.service.LibraryService;
import com.practicas.practica2.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private BookService bookService;

    @Autowired
    private StudentService studentService;

    @GetMapping("")
    public String showStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "getAll/students";
    }

    @GetMapping("/create-student")
    public String createStudent(Model model) {
        model.addAttribute("newStudent", new Student());
        model.addAttribute("existingBooks", bookService.getAllBooks());
        return "create/createStudent";
    }

    @PostMapping("/create-student")
    public String postCreateStudent(Student newStudent,
            @RequestParam(value = "selectedBooks", required = false) Set<Long> selectedBooks) {
        Set<Long> books = (selectedBooks != null) ? new HashSet<>(selectedBooks) : null;
        Student result = studentService.createStudent(newStudent, books);
        if (result == null)
            return "error";

        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String showStudentById(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) 
            return "error";
        
        model.addAttribute("student", student);
        model.addAttribute("booksExist", !student.getBooks().isEmpty());
        model.addAttribute("librariesExist", !student.getLibraries().isEmpty());
        return "getOne/student";
    }

    @GetMapping("/{id}/edit-student")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) 
            return "error";
        
        model.addAttribute("student", student);
        model.addAttribute("existingBooks", bookService.getAllBooks());
        return "edit/editStudent";
    }

    @PostMapping("/{id}/edit-student")
    public String updateStudent(@PathVariable Long id, Student updatedStudent,
            @RequestParam(value = "selectedBooks", required = false) Set<Long> selectedBooks) {
        Set<Long> books = (selectedBooks != null) ? new HashSet<>(selectedBooks) : null;
        Student result = studentService.patchStudent(updatedStudent, books);
        if (result == null) 
            return "error";

        return "redirect:/students/" + id;
    }

    @GetMapping("/{id}/remove")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
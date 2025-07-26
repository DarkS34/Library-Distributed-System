package com.practicas.practica2.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;

    //Relations
    @JsonIgnore
    @ManyToMany(mappedBy = "studentsBorrowed")
    private Set<Library> libraries = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Book> books = new HashSet<>();

    //Methods
    public Student() {
    }

    public Student(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public Library addBorrowedFromStudent(Library library) {
        this.libraries.add(library);
        return library;
    }

    public void deleteBorrowedFromStudent(Library library) {
        this.libraries.remove(library);
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Book addBook(Book book) {
        this.books.add(book);
        return book;
    }

    public void deleteBook(Book book) {
        this.books.remove(book);
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", email=" + email + "]";
    }

}
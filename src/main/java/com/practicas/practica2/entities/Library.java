package com.practicas.practica2.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String address;

    //Relations
    @ManyToMany
    @JoinTable(
        name="student_library",
        joinColumns = @JoinColumn(name="library_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> studentsBorrowed  = new HashSet<>();

    @OneToMany(mappedBy = "library")
    private Set<Book> books = new HashSet<>();

    //Methods
    public Library() {
    }

    public Set<Student> getStudentsBorrowed() {
        return studentsBorrowed;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public Library(long id, String name, String email, String adr) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = adr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Student addBorrowedFromLibrary(Student student) {
        this.studentsBorrowed.add(student);
        return student;
    } 

    public void deleteBorrowedFromLibrary(Student student) {
        this.studentsBorrowed.remove(student);
    }

    public Book addBook(Book book) {
        this.books.add(book);
        return book;
    } 

    public Book deleteBook(Book book){
        this.books.remove(book);
        return book;
    }

    @Override
    public String toString() {
        return "Library [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", books="
                + "]";
    }

}
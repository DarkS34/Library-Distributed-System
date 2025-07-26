package com.practicas.practica2.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.practicas.practica2.entities.Student;
import com.practicas.practica2.entities.Book;
import com.practicas.practica2.entities.Library;
import com.practicas.practica2.repositories.BooksRepository;
import com.practicas.practica2.repositories.LibrariesRepository;
import com.practicas.practica2.repositories.StudentsRepository;

@Service
public class StudentService {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private LibrariesRepository librariesRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    public Collection<Student> getAllStudents() {
        return (Collection<Student>) studentsRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentsRepository.findById(id).orElse(null);
    }

    public Student createStudent(Student newStudent, Set<Long> books) {
        if (books != null) {
            for(Long bookId : books) {
                Book book = booksRepository.findById(bookId).orElse(null);
                if (book != null){
                    Library library = book.getLibrary();
                    if (library != null){
                        if (book.getStudent() == null) {
                            // add library and book to the student
                            newStudent.addBook(book);
                            newStudent.addBorrowedFromStudent(book.getLibrary());
                            book.getLibrary().addBorrowedFromLibrary(newStudent);
                            book.setStudent(newStudent);
                            booksRepository.save(book);
                        } else {
                            // delete book of the old student and the relation with the library if it's necessary
                            Set<Book> oldStudentBooks = book.getStudent().getBooks();
                            if (oldStudentBooks != null) {
                                int delete = 0;
                                for (Book b : oldStudentBooks) {
                                    if (b.getLibrary().getId() == book.getLibrary().getId()) {
                                        delete++;
                                    }
                                }
                                if (delete == 1) {
                                    book.getStudent().deleteBorrowedFromStudent(book.getLibrary());
                                    book.getLibrary().deleteBorrowedFromLibrary(book.getStudent());
                                }
                            }
                            book.getStudent().deleteBook(book);

                            // add book and library to student
                            newStudent.addBook(book);
                            newStudent.addBorrowedFromStudent(book.getLibrary());
                            book.getLibrary().addBorrowedFromLibrary(newStudent);
                            book.setStudent(newStudent);
                            booksRepository.save(book);
                        }
                    }
                } else return null;
            }
        }
        return studentsRepository.save(newStudent);
    }

    public Student updateStudent(Student existingStudent, Set<Long> books) {
        if (books != null) {
            for(Long bookId : books) {
                Book book = booksRepository.findById(bookId).orElse(null);
                if (book != null){
                    Library library = book.getLibrary();
                    if (library != null){
                        if (book.getStudent() == null) {
                            // add library and book to the student
                            existingStudent.addBook(book);
                            existingStudent.addBorrowedFromStudent(book.getLibrary());
                            book.getLibrary().addBorrowedFromLibrary(existingStudent);
                            book.setStudent(existingStudent);
                            booksRepository.save(book);
                        } else {
                            // delete book of the old student and the relation with the library if it's necessary
                            Set<Book> oldStudentBooks = book.getStudent().getBooks();
                            if (oldStudentBooks != null) {
                                int delete = 0;
                                for (Book b : oldStudentBooks) {
                                    if (b.getLibrary().getId() == book.getLibrary().getId()) {
                                        delete++;
                                    }
                                }
                                if (delete == 1) {
                                    book.getStudent().deleteBorrowedFromStudent(book.getLibrary());
                                    book.getLibrary().deleteBorrowedFromLibrary(book.getStudent());
                                }
                            }
                            book.getStudent().deleteBook(book);

                            // add book and library to student
                            existingStudent.addBook(book);
                            existingStudent.addBorrowedFromStudent(book.getLibrary());
                            book.getLibrary().addBorrowedFromLibrary(existingStudent);
                            book.setStudent(existingStudent);
                            booksRepository.save(book);
                        }
                    } else return null;
                } else return null;
            }
        }
        return studentsRepository.save(existingStudent);
    }

    public String deleteStudent(Long id) {
        Student studentToDelete = studentsRepository.findById(id).orElse(null);

        if (studentToDelete != null) {
            Set<Book> books = studentToDelete.getBooks();
            if (books != null) {
                for (Book book : books) {
                    book.setStudent(null);
                    if (book.getLibrary() != null) {
                        book.getLibrary().deleteBorrowedFromLibrary(studentToDelete);
                    }
                }
            }
            studentsRepository.deleteById(id);
            return "Student: " + id + " deleted.";
        }
        return null;
    }

    public Student patchStudent(Student existingStudent, Set<Long> books) {
        if (books != null) {
            for(Long bookId : books) {
                Book book = booksRepository.findById(bookId).orElse(null);
                if (book != null){
                    Library library = book.getLibrary();
                    if (library != null){
                        if (book.getStudent() == null) {
                            // add library and book to the student
                            existingStudent.addBook(book);
                            existingStudent.addBorrowedFromStudent(book.getLibrary());
                            book.getLibrary().addBorrowedFromLibrary(existingStudent);
                            book.setStudent(existingStudent);
                            booksRepository.save(book);
                        } else {
                            // delete book of the old student and the relation with the library if it's necessary
                            Set<Book> oldStudentBooks = book.getStudent().getBooks();
                            if (oldStudentBooks != null) {
                                int delete = 0;
                                for (Book b : oldStudentBooks) {
                                    if (b.getLibrary().getId() == book.getLibrary().getId()) {
                                        delete++;
                                    }
                                }
                                if (delete == 1) {
                                    book.getStudent().deleteBorrowedFromStudent(book.getLibrary());
                                    book.getLibrary().deleteBorrowedFromLibrary(book.getStudent());
                                }
                            }
                            book.getStudent().deleteBook(book);

                            // add book and library to student
                            existingStudent.addBook(book);
                            existingStudent.addBorrowedFromStudent(book.getLibrary());
                            book.getLibrary().addBorrowedFromLibrary(existingStudent);
                            book.setStudent(existingStudent);
                            booksRepository.save(book);
                        }
                    }else return null;
                } else return null;
            }
        }
        return studentsRepository.save(existingStudent);
    }
}

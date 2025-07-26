package com.practicas.practica2.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practicas.practica2.entities.Book;
import com.practicas.practica2.entities.Library;
import com.practicas.practica2.entities.Student;
import com.practicas.practica2.repositories.BooksRepository;
import com.practicas.practica2.repositories.LibrariesRepository;
import com.practicas.practica2.repositories.StudentsRepository;

@Service
public class BookService {
    @Autowired
    private BooksRepository bookRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private LibrariesRepository librariesRepository;

    public Collection<Book> getAllBooks() {
        return (Collection<Book>) bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book createBook(Book newBook, long libraryId, long studentId) {
        if (!validIsbn(newBook.getISBN(), 0))
            return null;
        
        if (libraryId != -1 && studentId != -1) {
            Library library = librariesRepository.findById(libraryId).orElse(null);
            Student student = studentsRepository.findById(studentId).orElse(null);

            if (library != null && student != null) {
                library.addBorrowedFromLibrary(student);
                student.addBorrowedFromStudent(library);
                newBook.setLibrary(library);
                newBook.setStudent(student);
                librariesRepository.save(library);
                studentsRepository.save(student);
            } else {
                return null;
            }

        } else if (libraryId != -1) {
            Library library = librariesRepository.findById(libraryId).orElse(null);
            if (library != null) {
                library.addBook(newBook);
                newBook.setLibrary(library);
                librariesRepository.save(library);
            }
        }

        return bookRepository.save(newBook);
    }

    public Book updateBook(Book updatedBook, long libraryId, long studentId) {
        if (!validIsbn(updatedBook.getISBN(), 1))
            return null;

        if (libraryId != -1 && studentId != -1) {
            Library library = librariesRepository.findById(libraryId).orElse(null);
            Student student = studentsRepository.findById(studentId).orElse(null);

            if (library != null && student != null) {
                if (updatedBook.getStudent() != null) {
                    Set<Book> oldStudentBooks = updatedBook.getStudent().getBooks();
                    int booksInOldLibrary = 0;

                    for (Book oldBook : oldStudentBooks) {
                        if (oldBook.getLibrary().getId() == updatedBook.getLibrary().getId()) {
                            booksInOldLibrary++;
                        }
                    }

                    if (booksInOldLibrary == 1) {
                        updatedBook.getStudent().deleteBorrowedFromStudent(updatedBook.getLibrary());
                        updatedBook.getLibrary().deleteBorrowedFromLibrary(updatedBook.getStudent());
                    }
                }

                

                student.addBorrowedFromStudent(library);
                library.addBorrowedFromLibrary(student);
                librariesRepository.save(library);
                studentsRepository.save(student);

                updatedBook.setStudent(student);
                updatedBook.setLibrary(library);

                return bookRepository.save(updatedBook);
            } else
                return null;
        } else {
            return null;
        }
    }

    public String deleteBook(Long id) {
        Book bookToDelete = bookRepository.findById(id).orElse(null);

        if (bookToDelete != null) {

            Library library = bookToDelete.getLibrary(); // Delete existing relation in library first
            Student student = bookToDelete.getStudent(); // Delete existing relation in student as well

            if (library != null && student != null) {
                int count = 0;
                for (Book studentBook : student.getBooks()) {
                    for (Book libraryBook : library.getBooks()) {
                        if (studentBook.getId().equals(libraryBook.getId())) {
                            count++;
                        }
                    }
                }
                if (count == 1) {
                    library.deleteBorrowedFromLibrary(student);
                }

                library.deleteBook(bookToDelete);
                bookToDelete.setLibrary(null);
                librariesRepository.save(library);

                student.deleteBook(bookToDelete);
                bookToDelete.setStudent(null);
                studentsRepository.save(student);
            }

            bookRepository.deleteById(id);

            return "Book: " + id + " deleted.";
        }
        return null;
    }

    public Book patchBook(Book bookToUpdate, Long libraryId, Long studentId) {
        boolean hasLibrary = libraryId != -1;
        boolean hasStudent = studentId != -1;
        System.out.println(hasLibrary);
        System.out.println(hasStudent);
        if (hasLibrary ^ hasStudent) { // XOR
            if (hasLibrary) {
                Library library = librariesRepository.findById(libraryId).orElse(null);
                if (library == null)
                    return null;
                    System.out.println(bookToUpdate.getStudent());
                if (bookToUpdate.getStudent() != null) {
                    Set<Book> oldStudentBooks = bookToUpdate.getStudent().getBooks();
                    int booksInOldLibrary = 0;
                    for (Book oldBook : oldStudentBooks) {
                        if (oldBook.getLibrary().getId() == bookToUpdate.getLibrary().getId()) {
                            booksInOldLibrary++;
                        }
                    }
                    System.out.println(booksInOldLibrary);
                    if (booksInOldLibrary == 1) {
                        System.out.println("-Entrea en BIOL if");
                        bookToUpdate.getStudent().deleteBorrowedFromStudent(bookToUpdate.getLibrary());
                        bookToUpdate.getLibrary().deleteBorrowedFromLibrary(bookToUpdate.getStudent());
                    }

                    bookToUpdate.getStudent().addBook(bookToUpdate);
                    library.addBorrowedFromLibrary(bookToUpdate.getStudent());
                }

                Library bookLibrary = bookToUpdate.getLibrary();
                if (bookLibrary != null && bookLibrary.getId() != libraryId)
                    bookLibrary.getBooks().remove(bookToUpdate);

                bookToUpdate.setLibrary(library);
                library.addBook(bookToUpdate);
                librariesRepository.save(library);
            } else { // hasStudent
                Library bookLibrary = bookToUpdate.getLibrary();
                if (bookLibrary == null)
                    return null; // You can't assign a book that doesn't belong to a Library

                Student student = studentsRepository.findById(studentId).orElse(null);
                if (student == null)
                    return null; // Student not found

                Student bookStudent = bookToUpdate.getStudent();
                if (bookStudent != null) {
                    if (!bookStudent.getId().equals(studentId)) {
                        int count = 0;
                        for (Book studentBook : bookStudent.getBooks()) {
                            if (studentBook.getLibrary().getId() == bookLibrary.getId())
                                count++;
                        }
                        if (count == 1) // Only 1 book with the library, delete relation N:M
                            bookLibrary.deleteBorrowedFromLibrary(bookStudent);
                    }

                    int count = 0;
                    for (Book studentBook : bookStudent.getBooks()) {
                        for (Book libraryBook : bookLibrary.getBooks()) {
                            if (studentBook.getId().equals(libraryBook.getId()))
                                count++;
                        }
                    }
                    if (count > 1) { // More that 1 relation in N:M
                        bookLibrary.addBorrowedFromLibrary(student);
                    } else if (count == 1) { // Just 1 relation in N:M
                        bookLibrary.deleteBorrowedFromLibrary(bookStudent);
                        bookLibrary.addBorrowedFromLibrary(student);
                    }
                    bookToUpdate.getStudent().getBooks().remove(bookToUpdate); // Remove book from previous student
                    bookToUpdate.setStudent(student); // Add student to the book
                    student.addBook(bookToUpdate); // Add book to student borrowed books
                    librariesRepository.save(bookLibrary); // Save library
                    studentsRepository.save(bookStudent); // Save old student
                } else { // Book it's not borrowed at the moment
                    bookToUpdate.setStudent(student); // Add student to the book
                    student.addBook(bookToUpdate); // Add book to student borrowed books
                    bookLibrary.addBorrowedFromLibrary(student); // Add relation between student and library
                                                                 // (book.library)
                    studentsRepository.save(student); // Save student
                }
            }
            return bookRepository.save(bookToUpdate);
        } else if (hasLibrary && hasStudent) {
            Student student = studentsRepository.findById(studentId).orElse(null);
            Library library = librariesRepository.findById(libraryId).orElse(null);

            if (bookToUpdate.getStudent() == null) { // Cambiar de biblioteca
                if (bookToUpdate.getLibrary() != null){
                    bookToUpdate.getLibrary().deleteBook(bookToUpdate);
                    bookToUpdate.setLibrary(library);
                    bookRepository.save(bookToUpdate);
                }else{
                    bookToUpdate.setLibrary(library);
                    bookRepository.save(bookToUpdate);
                }
            } else { // Cambiar de biblioteca y eliminar relación N:M si es el último libro que tiene
                     // de la biblioteca antigua
                Set<Book> oldStudentBooks = bookToUpdate.getStudent().getBooks();
                int booksInOldLibrary = 0;
                for (Book oldBook : oldStudentBooks) {
                    if (oldBook.getLibrary().getId() == bookToUpdate.getLibrary().getId()) {
                        booksInOldLibrary++;
                    }
                }

                if (booksInOldLibrary == 1) {
                    bookToUpdate.getStudent().deleteBorrowedFromStudent(bookToUpdate.getLibrary());
                    bookToUpdate.getLibrary().deleteBorrowedFromLibrary(bookToUpdate.getStudent());
                }

                bookToUpdate.getStudent().addBook(bookToUpdate);
                library.addBorrowedFromLibrary(bookToUpdate.getStudent());

                library.deleteBook(bookToUpdate);
                bookToUpdate.setLibrary(library);
                bookRepository.save(bookToUpdate);
            }

            library.addBorrowedFromLibrary(student);
            student.addBorrowedFromStudent(library);
            librariesRepository.save(library);

            bookToUpdate.setStudent(student);
            bookToUpdate.setLibrary(library);

            return bookRepository.save(bookToUpdate);
        }else if (libraryId == -1 && studentId != -1){
            return null;
        }
        else
            return bookRepository.save(bookToUpdate);

    }

    private boolean validIsbn(String isbn, int mode) {
        for (Book book : bookRepository.findAll()) {
            if (book.getISBN().equals(isbn)) {
                if (mode == 1)
                    mode--;
                else if (mode == 0)
                    return false;
            }
        }
        return true;
    }
}

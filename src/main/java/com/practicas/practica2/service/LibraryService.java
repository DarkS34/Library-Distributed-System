package com.practicas.practica2.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practicas.practica2.entities.Book;
import com.practicas.practica2.entities.Library;
import com.practicas.practica2.repositories.BooksRepository;
import com.practicas.practica2.repositories.LibrariesRepository;
import com.practicas.practica2.repositories.StudentsRepository;

@Service
public class LibraryService {

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private LibrariesRepository librariesRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    public Collection<Library> getAllLibraries() {
        return (Collection<Library>) librariesRepository.findAll();
    }

    public Library getLibraryById(long id) {
        return librariesRepository.findById(id).orElse(null);
    }

    public Library createLibrary(Library newLibrary, Set<Long> books) {
        if (books != null) {
            for (Long bookId : books) {
                Book book = booksRepository.findById(bookId).orElse(null);
                if (book != null) {
                    if (book.getLibrary() == null) {
                        book.setLibrary(newLibrary);
                        newLibrary.addBook(book);
                        booksRepository.save(book);
                    } else {
                        if (book.getStudent() == null) {
                            book.getLibrary().deleteBook(book);
                            book.setLibrary(newLibrary);
                            booksRepository.save(book);
                            newLibrary.addBook(book);
                            librariesRepository.save(book.getLibrary());
                        } else { // Has student and newLibrary
                            Set<Book> oldStudentBooks = book.getStudent().getBooks();
                            int booksInOldLibrary = 0;
                            for (Book oldBook : oldStudentBooks) {
                                if (oldBook.getLibrary().getId() == book.getLibrary().getId())
                                    booksInOldLibrary++;
                            }

                            if (booksInOldLibrary == 1) {
                                book.getStudent().deleteBorrowedFromStudent(book.getLibrary());
                                book.getLibrary().deleteBorrowedFromLibrary(book.getStudent());
                            }

                            book.getStudent().addBook(book);
                            newLibrary.addBorrowedFromLibrary(book.getStudent());

                            newLibrary.deleteBook(book);
                            book.setLibrary(newLibrary);
                            booksRepository.save(book);
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return librariesRepository.save(newLibrary);
    }

    public Library updateLibrary(Library updatedLibrary, Set<Long> books) {
        if (books != null) {
            for (Long bookId : books) {
                Book book = booksRepository.findById(bookId).orElse(null);
                if (book != null) {
                    if (book.getLibrary() == null) {
                        book.setLibrary(updatedLibrary);
                        updatedLibrary.addBook(book);
                        booksRepository.save(book);
                    } else {
                        if (book.getStudent() == null) {
                            book.getLibrary().deleteBook(book);
                            book.setLibrary(updatedLibrary);
                            booksRepository.save(book);
                            updatedLibrary.addBook(book);
                            librariesRepository.save(book.getLibrary());
                        } else { // Has student and updatedLibrary
                            Set<Book> oldStudentBooks = book.getStudent().getBooks();
                            int booksInOldLibrary = 0;
                            for (Book oldBook : oldStudentBooks) {
                                if (oldBook.getLibrary().getId() == book.getLibrary().getId())
                                    booksInOldLibrary++;
                            }

                            if (booksInOldLibrary == 1) {
                                book.getStudent().deleteBorrowedFromStudent(book.getLibrary());
                                book.getLibrary().deleteBorrowedFromLibrary(book.getStudent());
                            }

                            book.getStudent().addBook(book);
                            updatedLibrary.addBorrowedFromLibrary(book.getStudent());

                            updatedLibrary.deleteBook(book);
                            book.setLibrary(updatedLibrary);
                            booksRepository.save(book);
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return librariesRepository.save(updatedLibrary);
    }

    public String deleteLibrary(long id) {
        Library libraryToDelete = librariesRepository.findById(id).orElse(null);

        if (libraryToDelete != null) {
            Set<Book> books = libraryToDelete.getBooks();
            if (books != null) {
                for (Book book : books) {
                    book.setLibrary(null);
                    if (book.getStudent() != null) {
                        book.setStudent(null);
                    }
                }
            }
            librariesRepository.deleteById(id);
            return "Library: " + id + " deleted.";
        }
        return null;
    }

    public Library patchLibrary(Library updatedLibrary, Set<Long> books) {
        if (books != null) {
            for (Long bookId : books) {
                Book book = booksRepository.findById(bookId).orElse(null);
                if (book != null) {
                    if (book.getLibrary() == null) {
                        book.setLibrary(updatedLibrary);
                        updatedLibrary.addBook(book);
                        booksRepository.save(book);
                    } else {
                        if (book.getStudent() == null) {
                            book.getLibrary().deleteBook(book);
                            book.setLibrary(updatedLibrary);
                            booksRepository.save(book);
                            updatedLibrary.addBook(book);
                            librariesRepository.save(book.getLibrary());
                        } else { // Has student and updatedLibrary
                            Set<Book> oldStudentBooks = book.getStudent().getBooks();
                            int booksInOldLibrary = 0;
                            for (Book oldBook : oldStudentBooks) {
                                if (oldBook.getLibrary().getId() == book.getLibrary().getId())
                                    booksInOldLibrary++;
                            }

                            if (booksInOldLibrary == 1) {
                                book.getStudent().deleteBorrowedFromStudent(book.getLibrary());
                                book.getLibrary().deleteBorrowedFromLibrary(book.getStudent());
                            }

                            book.getStudent().addBook(book);
                            updatedLibrary.addBorrowedFromLibrary(book.getStudent());

                            updatedLibrary.deleteBook(book);
                            book.setLibrary(updatedLibrary);
                            booksRepository.save(book);
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return librariesRepository.save(updatedLibrary);
    }
}

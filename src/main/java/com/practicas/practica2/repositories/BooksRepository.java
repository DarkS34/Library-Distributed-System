package com.practicas.practica2.repositories;

import org.springframework.data.repository.CrudRepository;

import com.practicas.practica2.entities.Book;

public interface BooksRepository extends CrudRepository<Book, Long> {
}

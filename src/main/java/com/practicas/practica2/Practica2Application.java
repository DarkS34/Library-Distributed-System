package com.practicas.practica2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.practicas.practica2.entities.Book;
import com.practicas.practica2.entities.Library;
import com.practicas.practica2.entities.Student;
import com.practicas.practica2.repositories.BooksRepository;
import com.practicas.practica2.repositories.LibrariesRepository;
import com.practicas.practica2.repositories.StudentsRepository;

@SpringBootApplication
public class Practica2Application {
	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Practica2Application.class, args);
	}
}
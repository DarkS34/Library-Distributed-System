package com.practicas.practica2.repositories;

import org.springframework.data.repository.CrudRepository;

import com.practicas.practica2.entities.Student;

public interface StudentsRepository extends CrudRepository<Student, Long> {
}

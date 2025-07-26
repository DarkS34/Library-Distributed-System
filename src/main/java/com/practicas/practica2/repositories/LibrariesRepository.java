package com.practicas.practica2.repositories;

import org.springframework.data.repository.CrudRepository;

import com.practicas.practica2.entities.Library;

public interface LibrariesRepository extends CrudRepository<Library, Long> {
}

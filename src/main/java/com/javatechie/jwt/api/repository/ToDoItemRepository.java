package com.javatechie.jwt.api.repository;

import com.javatechie.jwt.api.entity.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoItemRepository extends JpaRepository<ToDoItem,Integer> {

}

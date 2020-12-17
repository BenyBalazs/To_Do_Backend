package com.javatechie.jwt.api.controller;

import com.javatechie.jwt.api.dto.ToDoItemDTO;
import com.javatechie.jwt.api.dto.ToDoItemDTOConvertUtil;
import com.javatechie.jwt.api.entity.ToDoItem;
import com.javatechie.jwt.api.entity.User;
import com.javatechie.jwt.api.repository.ToDoItemRepository;
import com.javatechie.jwt.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins="http://localhost:4200/")
@RequestMapping("/api/todo")
public class ToDoController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ToDoItemRepository toDoItemRepository;

    private final ToDoItemDTOConvertUtil toDoItemDTOConvertUtil = new ToDoItemDTOConvertUtil();

    @CrossOrigin(origins="*")
    @GetMapping("/getAllByUserName")
    ResponseEntity<List<ToDoItemDTO>> getAllByUserName(@RequestParam String username){

        User currentUser = userRepository.findByUserName(username);

        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());

        return ResponseEntity.
                ok(toDoItemDTOConvertUtil.convertToDoItemListToToDoItemDTOList(currentUser.getDoItems()));
    }

    @CrossOrigin(origins="*")
    @PostMapping("/addNew")
    ResponseEntity<String> addNewTodoItem(@RequestParam String username, @RequestBody ToDoItemDTO todoAddRequest){

        System.out.println(todoAddRequest.toString());
        User currentUser = userRepository.findByUserName(username);

        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A felhasználó nem található!");

        ToDoItem toDoItem = toDoItemDTOConvertUtil.createEmptyToDoFromDTO(todoAddRequest);
        toDoItem.setUserTodo(currentUser);

        try{
            toDoItemRepository.save(toDoItem);
            return ResponseEntity.ok("Feladat elmentve az adatbázisba");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Az adatbázis hibára futott");
        }
    }

    @CrossOrigin(origins="*")
    @PutMapping("/editItem")
    ResponseEntity<String> editTodoItem(@RequestParam String username, @RequestBody ToDoItemDTO todoEditRequest){

        User currentUser = userRepository.findByUserName(username);

        if(currentUser == null)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nem felhasználó a tevékenység");

        ToDoItem toDoItem;

        try{
            toDoItem = toDoItemRepository.findById(todoEditRequest.getId()).get();
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Nem található a tevékenység");
        }

        if(!currentUser.getDoItems().contains(toDoItem))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nem hozzád tartozik a tevékenység");

        toDoItem.setTitle(todoEditRequest.getTitle());
        toDoItem.setDueDate(todoEditRequest.getDueDate());

        try{
            toDoItemRepository.save(toDoItem);
            return ResponseEntity.ok("Feladat frissítve az adatbázisba");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Az adatbázis hibára futott");
        }

    }

    @CrossOrigin(origins="*")
    @DeleteMapping("/deleteItem")
    ResponseEntity<String> deleteToDoItem(@RequestParam String username, @RequestParam Integer id) {

        User currentUser = userRepository.findByUserName(username);
        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A felhasználónév nem található");

        if(!toDoItemRepository.findById(id).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A tennivaló nem található");

        ToDoItem toDoItem = toDoItemRepository.findById(id).get();

        try{
            toDoItem.setUserTodo(null);
            toDoItemRepository.save(toDoItem);
            toDoItemRepository.delete(toDoItem);
            return ResponseEntity.ok("Feladat törölve az adatbázisba");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Az adatbázis hibára futott");
        }

    }
}

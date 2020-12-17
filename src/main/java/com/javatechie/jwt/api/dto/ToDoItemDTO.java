package com.javatechie.jwt.api.dto;

import com.javatechie.jwt.api.entity.ToDoItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoItemDTO {

    private Integer id;
    private String title;
    private LocalDate dueDate;

}

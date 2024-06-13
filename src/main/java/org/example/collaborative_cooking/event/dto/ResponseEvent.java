package org.example.collaborative_cooking.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.collaborative_cooking.event.entity.Event;
import org.example.collaborative_cooking.recipe.dto.ResponseRecipe;
import org.example.collaborative_cooking.user.dto.ResponseUser;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEvent {
    private Long id;
    private String name;
    private Date date;
    private ResponseUser user;
    private ResponseRecipe recipe;


    public static ResponseEvent toModel(Event model) {
        return new ResponseEvent(
                model.getId(),
                model.getName(),
                model.getDate(),
                ResponseUser.toModel(model.getUser()),
                ResponseRecipe.toModel(model.getRecipe())
        );
    }
}

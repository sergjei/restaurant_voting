package com.github.sergjei.restaurant_voting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.github.sergjei.restaurant_voting.model.BaseEntity;

import java.time.LocalDate;

public class MenuItemTo extends BaseEntity {
    @NotNull
    private Integer restaurant;

    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private LocalDate menuDate;

    @NotNull
    private Integer price;

    public MenuItemTo(Integer id, Integer restaurant, LocalDate menuDate, String name, Integer price) {
        super(id);
        this.restaurant = restaurant;
        this.name = name;
        this.menuDate = menuDate;
        this.price = price;
    }

    public MenuItemTo() {
    }

    public Integer getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Integer restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}

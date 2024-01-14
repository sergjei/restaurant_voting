package com.github.sergjei.restaurant_voting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "menu_item", indexes =
        @Index(name = "restaurant_uniq_menuitem_date", columnList = "menu_date, id ,restaurant_id, name", unique = true)
)
public class MenuItem extends com.github.sergjei.restaurant_voting.model.BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private com.github.sergjei.restaurant_voting.model.Restaurant restaurant;

    @Column(name = "name")
    @NotBlank
    @NotNull
    private String name;

    @Column(name = "menu_date")
    @NotNull
    private LocalDate menuDate;

    @Column(name = "price")
    @NotNull
    private int price;

    public MenuItem(Integer id, com.github.sergjei.restaurant_voting.model.Restaurant restaurant, LocalDate menuDate, String name, int price) {
        super(id);
        this.restaurant = restaurant;
        this.name = name;
        this.menuDate = menuDate;
        this.price = price;
    }

    public MenuItem(Integer id, LocalDate menuDate, String name, int price) {
        super(id);
        this.name = name;
        this.menuDate = menuDate;
        this.price = price;
    }

    public MenuItem() {
    }

    public com.github.sergjei.restaurant_voting.model.Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(com.github.sergjei.restaurant_voting.model.Restaurant restaurant) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

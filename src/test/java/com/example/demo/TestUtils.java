package com.example.demo;


import org.mockito.internal.util.reflection.FieldSetter;
import java.math.BigDecimal;

import com.example.demo.model.persistence.*;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        try {
            FieldSetter.setField(target, target.getClass().getDeclaredField(fieldName), toInject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public static Item createItem(Long id, String name, String description, BigDecimal price) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        return item;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        Item item1 = createItem(1L, "Item 1", "Description 1", BigDecimal.TEN);
        cart.addItem(item1);
        Item item2 = createItem(2L, "Item 2", "Description 2", BigDecimal.valueOf(20));
        cart.addItem(item2);
        return cart;
    }
}


package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {



    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_items_to_cart() {
        User user = TestUtils.createUser("username", "password");
        Cart cart = TestUtils.createCart();
        user.setCart(cart);
        when(userRepository.findByUsername("username")).thenReturn(user);

        Item item1 = TestUtils.createItem(1L, "Item 1", "Item 1 Description", BigDecimal.TEN);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Cart returnedCart = response.getBody();

        assertNotNull(returnedCart);
        assertEquals(returnedCart, cart);
        Mockito.verify(userRepository, times(1)).findByUsername("username");
        Mockito.verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void add_items_to_cart_with_invalid_user() {
        when(userRepository.findByUsername("username")).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void add_invalid_item_to_cart() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void remove_item_from_cart() {
        User user = TestUtils.createUser("username", "password");
        Cart cart = TestUtils.createCart();
        user.setCart(cart);
        when(userRepository.findByUsername("username")).thenReturn(user);

        Item item1 = TestUtils.createItem(1L, "Item 1", "Item 1 Description", BigDecimal.TEN);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Cart returnedCart = response.getBody();

        assertNotNull(returnedCart);
        assertEquals(returnedCart, cart);
        Mockito.verify(userRepository, times(1)).findByUsername("username");
        Mockito.verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void remove_item_from_cart_with_invalid_user() {
        when(userRepository.findByUsername("username")).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_with_invalid_item() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("username");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }
}

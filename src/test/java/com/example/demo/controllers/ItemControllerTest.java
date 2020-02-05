package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.assertj.core.util.Lists;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items() {
        Item item1 = TestUtils.createItem(1L, "Item 1", "Item 1 Description", BigDecimal.TEN);
        Item item2 = TestUtils.createItem(2L, "Item 2", "Item 2 Description", BigDecimal.valueOf(20));

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertNotNull(items);
        assertArrayEquals(Arrays.asList(item1, item2).toArray(), items.toArray());
        Mockito.verify(itemRepository , times(1)).findAll();
    }

    @Test
    public void get_item_by_id_happy_path() {
        Item item1 = TestUtils.createItem(1L, "Item 1", "Item 1 Description", BigDecimal.TEN);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(Long.valueOf(1), item.getId());
        assertEquals("Item 1", item.getName());
        assertEquals("Item 1 Description", item.getDescription());
        assertEquals(BigDecimal.TEN, item.getPrice());
        Mockito.verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void get_item_by_id_with_invalid_id() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        Mockito.verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void get_item_by_name_happy_path() {
        Item item1 = TestUtils.createItem(1L, "Item 1", "Item 1 Description", BigDecimal.TEN);

        when(itemRepository.findByName("Item 1")).thenReturn(Arrays.asList(item1));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals("Item 1", items.get(0).getName());
        assertEquals("Item 1 Description", items.get(0).getDescription());
        assertEquals(BigDecimal.TEN, items.get(0).getPrice());
        Mockito.verify(itemRepository, times(1)).findByName("Item 1");
    }

    @Test
    public void get_item_by_id_with_invalid_itemname() {
        when(itemRepository.findByName(Mockito.anyString())).thenReturn(Lists.emptyList());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        Mockito.verify(itemRepository, times(1)).findByName(Mockito.anyString());
    }
}


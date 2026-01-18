package com.embabel.template.ecommerce;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Inventory {

    @LlmTool(description = "List of items with code, name, and stock")
    public List<Item> items() {
        return List.of(
                new Item("ITM001", "Laptop Dell XPS", 0),
                new Item("ITM002", "Mouse Wireless", 120),
                new Item("ITM003", "Keyboard Mechanical", 80),
                new Item("ITM004", "Monitor 24in", 30),
                new Item("ITM005", "Headphones Sony", 45),
                new Item("ITM006", "USB Cable Type-C", 200),
                new Item("ITM007", "Hard Drive 1TB", 25),
                new Item("ITM008", "Webcam HD", 60),
                new Item("ITM009", "Power Adapter", 90),
                new Item("ITM010", "Chocolate Bar Dairy Milk", 250),
                new Item("ITM011", "Croissant Fresh Bakery", 120),
                new Item("ITM012", "Marie Biscuits Pack", 180),
                new Item("ITM013", "Milk Full Cream 1L", 80),
                new Item("ITM014", "Butter Amul 500g", 60),
                new Item("ITM015", "Yogurt Plain 400g", 100),
                new Item("ITM016", "Cookies Oreo Pack", 140),
                new Item("ITM017", "Bread White Loaf", 90),
                new Item("ITM018", "Cheese Slice Pack", 70),
                new Item("ITM019", "Apple iPhone", 200),
                new Item("ITM020", "Ice Cream Vanilla Tub", 40)
        );
    }

}

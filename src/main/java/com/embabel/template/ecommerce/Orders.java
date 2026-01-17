package com.embabel.template.ecommerce;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Orders {

    @LlmTool(
            description = "History of orders delivered"
    )
    public List<Order> orderHistory() {

        LocalDate today = LocalDate.now();

        return List.of(
                new Order("ORD001", today.minusDays(0), "ITM010", 25),
                new Order("ORD002", today.minusDays(1), "ITM013", 40),
                new Order("ORD003", today.minusDays(2), "ITM001", 2),
                new Order("ORD004", today.minusDays(3), "ITM011", 60),
                new Order("ORD005", today.minusDays(4), "ITM012", 35),
                new Order("ORD006", today.minusDays(5), "ITM015", 20),
                new Order("ORD007", today.minusDays(7), "ITM002", 10),
                new Order("ORD008", today.minusDays(9), "ITM014", 15),
                new Order("ORD009", today.minusDays(12), "ITM016", 50),
                new Order("ORD010", today.minusDays(15), "ITM020", 8),
                new Order("ORD011", today.minusDays(20), "ITM006", 30),
                new Order("ORD012", today.minusDays(25), "ITM018", 25),
                new Order("ORD013", today.minusDays(30), "ITM004", 1),
                new Order("ORD014", today.minusDays(35), "ITM019", 75),
                new Order("ORD015", today.minusDays(40), "ITM017", 45)
        );
    }
}



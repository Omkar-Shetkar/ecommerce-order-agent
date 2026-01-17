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
    public List<Order> history() {

        LocalDate today = LocalDate.now();

        return List.of(
                new Order("order-1", today, "item-1", "LG TV", 1),
                new Order("order-2", today.minusDays(2), "item-2", "Chocolates", 50)
        );
    }
}


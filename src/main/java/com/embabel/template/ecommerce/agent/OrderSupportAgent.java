package com.embabel.template.ecommerce.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.prompt.persona.Persona;
import com.embabel.common.ai.model.LlmOptions;
import com.embabel.template.ecommerce.Inventory;
import com.embabel.template.ecommerce.Orders;
import com.embabel.template.ecommerce.RuleBook;

import java.util.List;

abstract class Personas {
    static final Persona CUSTOMER_REPRESENTATIVE = Persona.create(
            "Customer Representative",
            "Customer Representative",
            "Professional and helpful",
            "Help customer on his/her order replacement"
    );

    static final Persona STORE_REPRESENTATIVE = Persona.create(
            "Store Representative",
            "Store Representative",
            "Professional and precise",
            "Help Customer Representative with order replacement delivery"
    );
}

@Agent(description = "Support agent to check replacement of items and schedule delivery of replacement item")
public class OrderSupportAgent {

    private final RuleBook ruleBook;
    private final Orders orders;
    private final Inventory inventory;

    public OrderSupportAgent(RuleBook ruleBook, Orders orders, Inventory inventory) {
        this.ruleBook = ruleBook;
        this.orders = orders;
        this.inventory = inventory;
    }

    record ReplacementReport(String text) {
    }

    record StockReport(String text) {
    }

    record DeliveryReport(String text) {
    }


    @Action
    ReplacementReport replacementReport(UserInput userInput, Ai ai) {
        return ai
                // Medium temperature for accuracy
                .withLlm(LlmOptions
                        .withAutoLlm()
                        .withTemperature(0.5)
                )
                .withPromptContributor(Personas.CUSTOMER_REPRESENTATIVE)
                .withToolObjects(List.of(ruleBook, inventory, orders))
                .createObject(String.format("""
                        Process customer replacement request following standard procedure:
                        
                        1. Validate order exists in order history using 'orders' tool by matching OrderId
                        2. Fetch item name for the item code using 'inventory' tool
                        3. Confirm item qualifies per replacement policy using 'ruleBook' tool
                        4. Final report should contain whether items in the customer order eligible for replacement or not.
                        If an item is not eligible for replacement give the reason for the same.
                        
                        Customer request details:
                        %s
                        
                        Provide replacement decision with reasoning.
                        """, userInput.getContent()).trim(), ReplacementReport.class);
    }


    @Action
    StockReport stockReport(ReplacementReport replacementReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.STORE_REPRESENTATIVE)
                .withToolObjects(List.of(ruleBook, inventory, orders))
                .createObject(String.format("""
                                 Process Replacement Report using following standard procedure:
                                
                                 Replacement Report: %s
                                
                                - Check whether stock is available for the item using 'inventory' tool
                                - Create Stock report with availability details of the item
                                """, replacementReport.text())
                        .trim(), StockReport.class);
    }

    @AchievesGoal(
            description = "Order eligibility for replacement verified, stock availability checked, replacement delivery date confirmed")
    @Action
    DeliveryReport deliveryReport(StockReport stockReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.STORE_REPRESENTATIVE)
                .withToolObjects(List.of(ruleBook, inventory, orders))
                .createObject(String.format("""
                                Process Stock Report using following standard procedure:
                                
                                Stock Report: %s
                                
                                1. If stock is available, create Delivery report with replacement delivery date
                                2. If stock is not available, create Delivery report of non-availability of item
                                """, stockReport.text())
                        .trim(), DeliveryReport.class);
    }


}

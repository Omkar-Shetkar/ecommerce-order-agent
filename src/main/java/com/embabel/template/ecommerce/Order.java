package com.embabel.template.ecommerce;

import java.time.LocalDate;

public record Order(String OrderId, LocalDate orderDate, String itemCode, int quantity) {}

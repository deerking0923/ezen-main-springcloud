package com.example.recentreviewservice.dto;

import lombok.Data;

@Data
public class ReviewDto { // implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}

package com.coffeeshopmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequest {
    private Long id;
    private List<OrderDetailRequest> orderDetails;
}

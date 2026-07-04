package com.coffeeshopmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên nguyên liệu không được để trống")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Đơn vị tính không được để trống")
    @Column(nullable = false)
    private String unit;

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho không được nhỏ hơn 0")
    @Column(nullable = false)
    private Double stockQuantity;

    @NotNull(message = "Ngưỡng cảnh báo không được để trống")
    @Min(value = 0, message = "Ngưỡng cảnh báo không được nhỏ hơn 0")
    @Column(nullable = false)
    private Double minStockQuantity;
}

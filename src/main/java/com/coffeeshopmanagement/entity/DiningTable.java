package com.coffeeshopmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "dining_tables")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên bàn không được để trống")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Trạng thái không được để trống")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TableStatus status = TableStatus.AVAILABLE;
}

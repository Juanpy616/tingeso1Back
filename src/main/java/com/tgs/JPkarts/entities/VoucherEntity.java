package com.tgs.JPkarts.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class VoucherEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private int basePrice;
    private int sizeDiscount;
    private int specialDiscount;
    private int priceAfterDiscount;
    private int iva;
    private int finalPrice;
    private long reservationId;
    private String clientName;
    private String clientEmail;
}

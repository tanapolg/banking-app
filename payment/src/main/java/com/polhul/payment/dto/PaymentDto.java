package com.polhul.payment.dto;

import lombok.*;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class PaymentDto {
    @NonNull
    private double amount;

    @NonNull
    private String date;
}

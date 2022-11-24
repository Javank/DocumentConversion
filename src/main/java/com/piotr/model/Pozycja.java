package com.piotr.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"nazwa","towar", "ilosc", "netto", "vat"})
public class Pozycja {

    private String nazwa;
    private String towar;
    private int ilosc;
    private double netto;
    private int vat;

}

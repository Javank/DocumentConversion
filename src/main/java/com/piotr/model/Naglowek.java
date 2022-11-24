package com.piotr.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonPropertyOrder({"data_w","data_p", "dostawca", "odbiorca"})
public class Naglowek {
    private String data_w;
    private String data_p;
    private String dostawca;
    private String odbiorca;

}

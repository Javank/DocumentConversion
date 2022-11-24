package com.piotr.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JacksonXmlRootElement(localName = "dokument")
public class Faktura {

    Naglowek naglowek;
    @JacksonXmlElementWrapper(useWrapping = false)
    LinkedList<Pozycja> pozycja;

}

package com.piotr.format;

import com.piotr.model.Faktura;

import java.io.IOException;


public interface IDokument {
    void zapiszDokument(Faktura fv, String sPlik) throws IOException;
    Faktura odczytajDokument(String sPlik) throws IOException;
}

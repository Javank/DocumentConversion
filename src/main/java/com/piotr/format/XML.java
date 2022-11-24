package com.piotr.format;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.piotr.model.Faktura;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class XML implements IDokument {

static{
    log.info("implementacja obsługi dokumentów XML");
}

    @Override
    public void zapiszDokument(Faktura fv, String sPlik) {
        File file = new File(sPlik);
        log.info("parsowanie dokuemntu  {} do scięzki pliku {} ", fv, sPlik);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            xmlMapper.writeValue(file, fv);
        } catch (IOException e) {
            log.error("Problem z dostępem do pliki" + file);
            e.printStackTrace();
        }

    }

    @Override
    public Faktura odczytajDokument(String sPlik) {
        Faktura fv = null;
        try {
            File file = new File(sPlik);
            XmlMapper xmlMapper = new XmlMapper();
            log.info("czytanie pliku {} i mapowanie na pola zdefiniowane w klasie Faktura");
            fv = xmlMapper.readValue(file, Faktura.class);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return fv;
    }

}

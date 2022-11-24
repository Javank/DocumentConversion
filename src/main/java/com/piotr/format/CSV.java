package com.piotr.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.piotr.model.Faktura;
import com.piotr.model.Naglowek;
import com.piotr.model.Pozycja;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class CSV implements IDokument {

    private static  String getStringPosition(File f) {
        String ext = null;
        String s = f.getAbsolutePath();

        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = "_pozycja"+s.substring(i);
            s = s.substring(0,i) + ext;
        }
        return s;
    }

    @Override
    public void zapiszDokument(Faktura fv, String sPlik)  {

        CsvMapper csvMapper = new CsvMapper();

        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        CsvSchema schemaHeader = CsvSchema.builder()
                .addColumn("data_w")
                .addColumn("data_p")
                .addColumn("dostawca")
                .addColumn("odbiorca")
                .build().withHeader();
        CsvSchema schemaPositions = CsvSchema.builder()
                .addColumn("nazwa")
                .addColumn("towar")
                .addColumn("ilosc")
                .addColumn("netto")
                .addColumn("vat")
                .build().withHeader();

        ObjectWriter headerWriter = csvMapper.writerFor(Naglowek.class).with(schemaHeader.withColumnSeparator('|'));
        ObjectWriter positionsWriter = csvMapper.writerFor(Pozycja.class).with(schemaPositions.withColumnSeparator('|'));
        File plik1 = new File(sPlik);

        File plik2 = new File(getStringPosition(plik1));
        try {
            headerWriter.writeValues(plik1).write(fv.getNaglowek());
            positionsWriter.writeValues(plik2).writeAll(fv.getPozycja());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Faktura odczytajDokument(String sPlik)  {
        Naglowek naglowek = czytajNaglowek(sPlik);

        LinkedList<Pozycja> pozycje = czytajPozycje(sPlik);

        return new Faktura(naglowek, pozycje);
    }

    private LinkedList<Pozycja> czytajPozycje(String sPlik) {

        File plik = new File(getStringPosition(new File(sPlik)));;

        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();
        List<Object> poz = null;
        LinkedList<Pozycja> pozycje = new LinkedList<>();

        try {
            poz = (csvMapper).readerFor(Pozycja.class).with(csvSchema.withColumnSeparator('|')).readValues(plik).readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Object o : poz) {
            pozycje.add((Pozycja)o);
        }
        if(poz.size() < 1)
            return null;

        return pozycje;
    }

    private Naglowek czytajNaglowek(String sPlik) {
        File plik = new File(sPlik);
        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();
        List<Object> n = null;
        try {
            n = (csvMapper).readerFor(Naglowek.class).with(csvSchema.withColumnSeparator('|')).readValues(plik).readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(n.size() < 1)
            return null;

        return (Naglowek)n.get(0);
    }
}

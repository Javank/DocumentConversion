package com.piotr.format;

import com.piotr.model.Faktura;
import com.piotr.model.Naglowek;
import com.piotr.model.Pozycja;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Wini;

import java.io.*;
import java.util.LinkedList;

@Slf4j
public class INI implements IDokument {

    @Override
    public void zapiszDokument(Faktura fv, String sPlik) throws IOException {

        File plik = new File(sPlik);
        if(plik.exists()) {
            log.info("Usunięcie pliku " + sPlik);
            plik.delete();
        }

        log.info("Utowrzenie nowego pliku");
        plik.createNewFile();

        Wini ini = new Wini(plik);

        Naglowek n = fv.getNaglowek();
      LinkedList<Pozycja> poz = fv.getPozycja();
        ini.put("Naglowek", "data_w", n.getData_w());
        ini.put("Naglowek", "data_p", n.getData_p());
        ini.put("Naglowek", "dostawca", n.getDostawca());
        ini.put("Naglowek", "odbiorca", n.getOdbiorca());
        int pozLen = poz.size();

        ini.put("Naglowek", "pozycji", pozLen);

        int c = 1;
        //i pętla iterująca po pozycjach
        for(Pozycja p : poz) {
            ini.put("Pozycja" + c, "nazwa", p.getNazwa());
            ini.put("Pozycja" + c, "towar", p.getTowar());
            ini.put("Pozycja" + c, "ilosc", p.getIlosc());
            ini.put("Pozycja" + c, "netto", p.getNetto());
            ini.put("Pozycja" + c, "vat", p.getVat());
            c++;
        }
        ini.store();
    }

    @Override
    public Faktura odczytajDokument(String sPlik) throws IOException {
        File plik = new File(sPlik);
        if(!plik.exists()) {  log.error("Brak pliki {}", sPlik); return null;}

        Wini ini = new Wini(plik);

        Naglowek n = new Naglowek();

        LinkedList<Pozycja> poz = new LinkedList<>();
        n.setData_w(ini.get("Naglowek", "data_w", String.class));
        n.setData_p(ini.get("Naglowek", "data_p", String.class));
        n.setDostawca(ini.get("Naglowek", "dostawca", String.class));
        n.setOdbiorca(ini.get("Naglowek", "odbiorca", String.class));
        int pozLen = ini.get("Naglowek", "pozycji", Integer.class);

        for(int c = 1; c <= pozLen; c++) {
            Pozycja p = new Pozycja();
            p.setNazwa(ini.get("Pozycja" + c, "nazwa", String.class));
            p.setIlosc(ini.get("Pozycja" + c, "ilosc", Integer.class));
            p.setNetto(ini.get("Pozycja" + c, "netto", Double.class));
            p.setVat(ini.get("Pozycja" + c, "vat", Integer.class));
            poz.add(p);
        }
        return new Faktura(n, poz);
    }
}

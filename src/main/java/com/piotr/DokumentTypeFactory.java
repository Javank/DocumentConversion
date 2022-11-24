package com.piotr;

import com.piotr.format.CSV;
import com.piotr.format.IDokument;
import com.piotr.format.INI;
import com.piotr.format.XML;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class DokumentTypeFactory {

    public static  String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toUpperCase();
        }
        log.info("Wyciąganie typu roserzenia pliki : " + ext);
        return ext;
    }

    public static IDokument getDokumentParser(String sPath) {
        File plik = new File(sPath);
        log.info("Zwraca obiekt do parsowania dokumentu na podstawie  pliku " + sPath);
        String sTyp = getExtension(plik);
        switch(sTyp) {
            case "XML":
                return new XML();
            case "CSV":
                return new CSV();
            case "INI":
                return new INI();
            default:
                return null;
        }
    }
}

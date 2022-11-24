package com.piotr.gui;


import com.piotr.DokumentTypeFactory;
import com.piotr.format.IDokument;
import com.piotr.model.Faktura;
import com.piotr.model.Naglowek;
import com.piotr.model.Pozycja;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

public class MainFrame extends JFrame {

    private static final String[] columnNames = new String[]{"Nazwa", "Towar", "sztuk", "cena Netto", "Stawka VAT"};

    private JPanel panel = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextPane txtDataWystawienia = new JTextPane();
    private JTextPane txtDataZakupu = new JTextPane();
    private JTextPane txtDostawca = new JTextPane();
    private JTextPane txtKlient = new JTextPane();
    private JButton btOtworz = new JButton();
    private JButton btZapiszJako = new JButton();
    private Rectangle rcLabel = new Rectangle(10, 10, 100, 20);
    private Rectangle rcEdit = new Rectangle(120, 10, 100, 20);
    private JTable tabelaProdukty = null;
    private Faktura fv = null;

    public MainFrame() {

        setTitle("Faktura - Piotr Powszuk");
        setSize(600, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setMaximumSize(new Dimension(600, 440));


        panel.setLayout(null);
        getContentPane().add(panel);
        jLabel1.setText("Data zakupu:");
        jLabel1.setBounds(rcLabel);
        rcLabel.y += 30;
        panel.add(jLabel1);
        txtDataZakupu.setBounds(rcEdit);
        txtDataZakupu.setText("");
        txtDataZakupu.setBorder(BorderFactory.createEtchedBorder());
        rcEdit.y += 30;
        panel.add(txtDataZakupu);

        jLabel2.setText("Data wystawienia:");
        jLabel2.setBounds(rcLabel);
        panel.add(jLabel2);
        txtDataWystawienia.setBounds(rcEdit);
        txtDataWystawienia.setBorder(BorderFactory.createEtchedBorder());
        panel.add(txtDataWystawienia);

        jLabel3.setText("Dostawca:");
        rcLabel.x = rcLabel.width * 2 + 25;
        jLabel3.setBounds(rcLabel);
        panel.add(jLabel3);
        rcEdit.x = rcLabel.width * 3 - 15;
        txtDostawca.setBounds(rcEdit);
        txtDostawca.setBorder(BorderFactory.createEtchedBorder());
        panel.add(txtDostawca);

        jLabel4.setText("Odbiorca:");
        rcLabel.y -= 30;
        jLabel4.setBounds(rcLabel);
        panel.add(jLabel4);
        rcEdit.y -= 30;
        txtKlient.setBounds(rcEdit);
        txtKlient.setBorder(BorderFactory.createEtchedBorder());
        panel.add(txtKlient);


        btOtworz.setText("Otwórz");
        btOtworz.setBounds(rcLabel.x + rcLabel.width + 80, 10, 70, 25);
        panel.add(btOtworz);

        btZapiszJako.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                jfc.resetChoosableFileFilters();
                jfc.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
                jfc.addChoosableFileFilter(new FileNameExtensionFilter("ini", "ini"));
                jfc.addChoosableFileFilter(new FileNameExtensionFilter("csv", "csv"));

                int returnValue = jfc.showSaveDialog(null);


                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    if(DokumentTypeFactory.getExtension(selectedFile) == null ) {
                        JOptionPane.showMessageDialog(panel, "Podaj rozszerzenie pliku. (xml| ini | csv)");
                        return;
                    }
                    IDokument writer = DokumentTypeFactory.getDokumentParser(selectedFile.getAbsolutePath());
                    try {
                        Naglowek n = fv.getNaglowek();
                        n.setOdbiorca(txtKlient.getText());
                        n.setDostawca(txtDostawca.getText());
                        n.setData_p(txtDataZakupu.getText());
                        n.setData_w((txtDataWystawienia.getText()));
                        writer.zapiszDokument(fv, selectedFile.getAbsolutePath());

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        });
        btOtworz.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                jfc.resetChoosableFileFilters();
                jfc.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
                jfc.addChoosableFileFilter(new FileNameExtensionFilter("ini", "ini"));
                jfc.addChoosableFileFilter(new FileNameExtensionFilter("csv", "csv"));

                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();

                    IDokument reader = DokumentTypeFactory.getDokumentParser(selectedFile.getAbsolutePath());


                    try {
                        fv = reader.odczytajDokument(selectedFile.getAbsolutePath());
                        Naglowek n = fv.getNaglowek();
                        LinkedList<Pozycja> p = fv.getPozycja();

                        Vector<String []> data = new Vector<>();
                        for(Pozycja poz : p) {
                            String [] row = {poz.getNazwa(), poz.getTowar(), poz.getIlosc()+"", poz.getNetto()+"", poz.getVat()+""};
                            data.add(row);
                        }

                        tabelaProdukty.setModel(new DefaultTableModel(data.toArray(new String [0][]),  columnNames));
                        txtDataWystawienia.setText(n.getData_w());
                        txtDataZakupu.setText(n.getData_p());
                        txtDostawca.setText(n.getDostawca());
                        txtKlient.setText(n.getOdbiorca());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btZapiszJako.setText("Zapisz jako...");
        btZapiszJako.setBounds(rcLabel.x + rcLabel.width + 80 + 80, 10, 100, 25);
        panel.add(btZapiszJako);
        tabelaProdukty = new JTable(new DefaultTableModel(new String[][]{}, columnNames));
        tabelaProdukty.setShowGrid(true);
        tabelaProdukty.setDefaultEditor(Object.class, null);
        tabelaProdukty.setBorder(BorderFactory.createEtchedBorder());
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBounds(8, 70, 580, 300);
        tabPanel.add(tabelaProdukty.getTableHeader(), BorderLayout.PAGE_START);
        tabPanel.add(new JScrollPane(tabelaProdukty), BorderLayout.CENTER);

        panel.add(tabPanel);

    }
}
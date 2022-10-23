
package com.SIG.Controller;

import com.SIG.View.InvoiceDialog;
import com.SIG.View.LineDialog;
import com.SIG.Model.HeaderInvoice;
import com.SIG.Model.LineInvoice;
import com.SIG.Model.ViewTableLine;
import com.SIG.Model.TableView;
import com.SIG.View.InvoiceFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Handler implements ActionListener, ListSelectionListener {

    private InvoiceDialog invDialog;
    private LineDialog lineDialog;
    private InvoiceFrame frame;
    public Handler(InvoiceFrame frame){
        this.frame=frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action Handling called!");
        switch (e.getActionCommand()) {
            case "New Invoice":
                System.out.println("New invoice");
                newInvoice();
                break;

            case "Delete Invoice":
                System.out.println("Delete invoice");
                deleteInvoice();
                break;

            case "Add Item":
                System.out.println("Add Item");
                addItem();
                break;

            case "Delete Item":
                System.out.println("Delete Item");
                deleteItem();
                break;


            case "Load file":
                System.out.println("Load file");
                loadFile();
                break;

            case "Save file":
                System.out.println("Save file");
                saveFile();
                break;

            case "InvoiceCreated":
                System.out.println("InvoiceCreated");
                InvoiceCreated();
                break;

            case "NoInvoiceCreated":
                System.out.println("NoInvoiceCreated");
                NoInvoiceCreated();
                break;

            case "LineCreated":
                System.out.println("LineCreated");
                LineCreated();
                break;

            case "NoLineCreated":
                System.out.println("NoLineCreated");
                NoLineCreated();
                break;


            default:
                throw new AssertionError();
        }


    }

    private void newInvoice() {
        invDialog = new InvoiceDialog(frame);
        invDialog.setVisible(true);
        
    }


    private void deleteInvoice() {
        int rowChoosen= frame.getHeaderTable().getSelectedRow();
        if (rowChoosen != -1){
            frame.getInvoicess().remove(rowChoosen);
            frame.getTableView().fireTableDataChanged();
        }
    }



    private void addItem() {
        lineDialog = new LineDialog(frame);
        lineDialog.setVisible(true);

    }

    private void deleteItem() {
        int invoiceSelected = frame.getHeaderTable().getSelectedRow();
        int rowSelected = frame.getLineTable().getSelectedRow();

        if (invoiceSelected != -1 && rowSelected != -1){
            HeaderInvoice invoiceHeader = frame.getInvoicess().get(invoiceSelected);
            invoiceHeader.getLines().remove(rowSelected);
            ViewTableLine lineTableView = new ViewTableLine(invoiceHeader.getLines());
            frame.getLineTable().setModel(lineTableView);
            lineTableView.fireTableDataChanged();
        }

    }


    private void saveFile() {
    }




    @Override
    public void valueChanged(ListSelectionEvent e) {
        int Index =frame.getHeaderTable().getSelectedRow();
        if (Index!= -1){
            HeaderInvoice selectedInvoice = frame.getInvoicess().get(Index);
            System.out.println("selection happened");

            frame.getNumLabel().setText(""+selectedInvoice.getNum());
            frame.getDateLabel().setText(""+selectedInvoice.getDate());
            frame.getCustomerLabel().setText(""+selectedInvoice.getCustomer());
            frame.getTotalLabel().setText(""+selectedInvoice.getTotal_price());
            ViewTableLine lineTableView= new ViewTableLine(selectedInvoice.getLines());
            frame.getLineTable().setModel(lineTableView);
            lineTableView.fireTableDataChanged();
        }
    }


    private void loadFile() {
        JFileChooser fChooser = new JFileChooser();
        try {
            int selection=fChooser.showOpenDialog(null);
            if(selection==JFileChooser.APPROVE_OPTION){
                File headerf=fChooser.getSelectedFile();
                Path headerPath=Paths.get(headerf.getAbsolutePath());
                List<String>headerLines = Files.readAllLines(headerPath);
                System.out.println("file read");
                ArrayList<HeaderInvoice>inv = new ArrayList<>();
                for(String headerline : headerLines){
                    String [] splits = headerline.split(",");
                    int invNum = Integer.parseInt(splits[0]);
                    String invDate = splits[1];
                    String name = splits[2];
                    HeaderInvoice invoice = new HeaderInvoice(invNum,name , invDate);
                    inv.add(invoice);
                }

                selection=fChooser.showOpenDialog(null);
                if(selection==JFileChooser.APPROVE_OPTION){
                    File line = fChooser.getSelectedFile();
                    Path linePath = Paths.get(line.getAbsolutePath());
                    List<String>listLines=Files.readAllLines(linePath);
                    for (String listLine : listLines) {
                        String [] lineSplit = listLine.split(",");
                        int num=Integer.parseInt(lineSplit[0]);
                        String product = lineSplit[1];
                        int price = Integer.parseInt(lineSplit[2]);
                        int count=Integer.parseInt(lineSplit[3]);
                        HeaderInvoice Inv = null;
                        for(HeaderInvoice invoice :inv){
                            if(invoice.getNum()==num){
                                Inv =invoice;
                                break;
                            }
                        }
                        LineInvoice linesss = new LineInvoice(product, price, count, Inv);
                        Inv.getLines().add(linesss);
                    }


                }

                frame.setInvoicess(inv);
                TableView tableView = new TableView(inv);
                frame.setTableView(tableView);
                frame.getHeaderTable().setModel(tableView);
                frame.getTableView().fireTableDataChanged();
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void InvoiceCreated() {
        String date = invDialog.getDateField().getText();
        String name = invDialog.getCustomerField().getText();
        int invNumber = frame.getMaxNumber();

        HeaderInvoice invoiceHeader = new HeaderInvoice(invNumber, name, date);
        frame.getInvoicess().add(invoiceHeader);
        frame.getTableView().fireTableDataChanged();
        invDialog.setVisible(false);
        invDialog.dispose();
        invDialog = null;
    }

    private void NoInvoiceCreated() {
        invDialog.setVisible(false);
        invDialog.dispose();
        invDialog=null;
    }

    private void LineCreated() {
        String item = lineDialog.getItemField().getText();
        String count = lineDialog.getCountField().getText();
        int countUpd= Integer.parseInt(count);
        String price = lineDialog.getPriceField().getText();
        int priceUpd = Integer.parseInt(price);
        int invoiceSelected = frame.getHeaderTable().getSelectedRow();

        if(invoiceSelected != -1){
            HeaderInvoice invoiceHeader = frame.getInvoicess().get(invoiceSelected);
            LineInvoice invoiceLine= new LineInvoice(item, priceUpd, countUpd, invoiceHeader);
        }

    }

    private void NoLineCreated() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog=null;

    }











}

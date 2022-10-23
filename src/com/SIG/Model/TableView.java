
package com.SIG.Model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class TableView extends AbstractTableModel {
    private ArrayList<HeaderInvoice> invoices;

    private String []table_Columns = {"Inv. no","Date","Name","Total"};

    public TableView(ArrayList<HeaderInvoice> invoicess) {
        this.invoices = invoicess;
    }

    @Override
    public String getColumnName(int column) {
        return table_Columns[column] ;
    }



    @Override
    public int getRowCount() {
        return invoices.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HeaderInvoice invoice = invoices.get(rowIndex);
        switch (columnIndex){
            case 0: return invoice.getNum();
            case 1: return invoice.getDate();
            case 2: return invoice.getCustomer();
            case 3: return invoice.getTotal_price();
            default:return "";
        }

    }
}

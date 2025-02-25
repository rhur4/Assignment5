package org.example.Assignment;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FilterInvoice {
    QueryInvoicesDAO dao;

    public FilterInvoice(QueryInvoicesDAO dao) {
        this.dao = dao;
    }
    public List<Invoice> lowValueInvoices() {
            List<Invoice> all = dao.all();

            return all.stream()
                    .filter(invoice -> invoice.getValue() < 100)
                    .collect(toList());
    }
}

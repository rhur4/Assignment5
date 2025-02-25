package org.example.Assignment;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterInvoiceTest {

    /**
     *  Retrieves the list of invoices with low values (value < 100) from FilterInvoice.lowValueInvoices
     *  and ensures that those returned invoices all have values less than 100.
     */
    @Test
    @Tag("Integration")
    void filterInvoiceTest() {
        // test for the lowValueInvoices() fxn
        // does not stub the dependency

        FilterInvoice fi = new FilterInvoice();
        List<Invoice> invoices = fi.lowValueInvoices();

        for(Invoice invoice : invoices) {
            assertThat(invoice.getValue()).isLessThan(100);
        }
    }
}

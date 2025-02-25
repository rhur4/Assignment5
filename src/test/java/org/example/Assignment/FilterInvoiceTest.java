package org.example.Assignment;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        // below two lines added after refactoring FilterInvoice for stubbing
        Database db = new Database();
        QueryInvoicesDAO dao = new QueryInvoicesDAO(db);

        FilterInvoice fi = new FilterInvoice(dao);
        List<Invoice> invoices = fi.lowValueInvoices();

        for(Invoice invoice : invoices) {
            assertThat(invoice.getValue()).isLessThan(100);
        }
    }

    /**
     *  Mocks the Database and QueryInvoicesDAO for greater control and isolation over the database. (Refactored
     *  FilterInvoice to take in the mocked DAO via dependency injection.) Stubs the DAO such that dao.all() returns our
     *  preselected invoices_input to test on when calling lowValueInvoices(). Asserts that lowValueInvoices() correctly
     *  returns a list with exactly three Invoices, all of which have values less than 100.
     */
    @Test
    @Tag("Mocking")
    void filterInvoicesStubbedTest() {
        Database db = mock(Database.class);
        QueryInvoicesDAO dao = mock(QueryInvoicesDAO.class);

        List<Invoice> invoices_input = Arrays.asList(
                new Invoice("customer1", 25),
                new Invoice("customer2", 50),
                new Invoice("customer3", 75),
                new Invoice("customer4", 100)
        );

        when(dao.all()).thenReturn(invoices_input);

        FilterInvoice fi = new FilterInvoice(dao);

        List<Invoice> invoices_output = fi.lowValueInvoices();

        assertThat(invoices_output.size()).isEqualTo(3);

        for(Invoice invoice : invoices_output) {
            assertThat(invoice.getValue()).isLessThan(100);
        }
    }
}

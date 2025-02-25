package org.example.Assignment;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SAP_BasedInvoiceSenderTest {

    /**
     *  Mocks FilterInvoice and SAP objects to create a controlled SAP_BasedInvoiceSender object. Stubs the return value
     *  of lowValueInvoices() to a preselected invoices list when being calling in sendLowValuedInvoices(). Verifies
     *  that the sender sent each invoice to the SAP and that the correct total number of invoices were sent. (Modified
     *  to propagate FailToSendSAPInvoiceException from refactored SAP_BasedInvoiceSender class.)
     *
     *  @throws FailToSendSAPInvoiceException
     */
    @Test
    @Tag("Mocking")
    void testWhenLowInvoicesSent() throws FailToSendSAPInvoiceException {
        FilterInvoice fi = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);

        List<Invoice> invoices = Arrays.asList(
                new Invoice("customer1", 25),
                new Invoice("customer2", 50),
                new Invoice("customer3", 75)
        );

        when(fi.lowValueInvoices()).thenReturn(invoices);

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(fi, sap);

        sender.sendLowValuedInvoices();

        for (Invoice invoice : invoices) {
            verify(sap, times(1)).send(invoice);
        }

        verify(sap, times(3)).send(any(Invoice.class));
    }

    /**
     *  Similar configurations to the above test, only now verifying that the sender sent no invoices to SAP if there
     *  are no low-valued invoices. (Modified to propagate FailToSendSAPInvoiceException from refactored
     *  SAP_BasedInvoiceSender class.)
     *
     *  @throws FailToSendSAPInvoiceException
     */
    @Test
    @Tag("Mocking")
    void testWhenNoInvoices() throws FailToSendSAPInvoiceException {
        FilterInvoice fi = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);

        List<Invoice> invoices = List.of();

        when(fi.lowValueInvoices()).thenReturn(invoices);

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(fi, sap);

        sender.sendLowValuedInvoices();

        verify(sap, never()).send(any(Invoice.class));
    }

    /**
     *  Similar configurations to the above tests, now handling a FailToSendSAPInvoiceException to be thrown when
     *  stubbing the first element of the invoices list. Verifies that, although send() was attempted on all three
     *  elements, the list of failed sends correctly only contains the first element.
     *
     *  @throws FailToSendSAPInvoiceException
     */
    @Test
    @Tag("Mocking")
    void testThrowExceptionWhenBadInvoice() throws FailToSendSAPInvoiceException {
        FilterInvoice fi = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);

        List<Invoice> invoices = Arrays.asList(
                new Invoice("customer1", 100),
                new Invoice("customer2", 50),
                new Invoice("customer3", 75)
        );

        when(fi.lowValueInvoices()).thenReturn(invoices);
        doThrow(new FailToSendSAPInvoiceException("fail")).when(sap).send(invoices.getFirst());

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(fi, sap);

        List<Invoice> failedSends = sender.sendLowValuedInvoices();

        verify(sap, times(3)).send(any(Invoice.class));
        assertThat(failedSends.size()).isEqualTo(1);
        assertThat(failedSends).contains(invoices.getFirst());
    }
}

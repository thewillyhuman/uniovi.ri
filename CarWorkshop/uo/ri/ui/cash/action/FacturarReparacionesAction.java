package uo.ri.ui.cash.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import uo.ri.common.BusinessException;
import uo.ri.conf.ServicesFactory;
import alb.util.console.Console;
import alb.util.menu.Action;

public class FacturarReparacionesAction implements Action {

	@Override
	public void execute() throws BusinessException {
		List<Long> idsAveria = new ArrayList<Long>();

		// pedir las averias a incluir en la factura
		do {
			Long id = Console.readLong("ID de averia");
			idsAveria.add(id);
		} while (masAverias());

		Map<String, Object> invoice = ServicesFactory.getCashService()
				.createInvoice(idsAveria);
		mostrarFactura((long) invoice.get("invoiceNumber"),
				(Date) invoice.get("invoiceDate"),
				(double) invoice.get("invoiceTotal"),
				(double) invoice.get("invoiceTAX"),
				(double) invoice.get("invoiceImport"));

	}

	private void mostrarFactura(long numeroFactura, Date fechaFactura,
			double totalFactura, double iva, double totalConIva) {

		Console.printf("Factura nº: %d\n", numeroFactura);
		Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", fechaFactura);
		Console.printf("\tTotal: %.2f €\n", totalFactura);
		Console.printf("\tIva: %.1f %% \n", iva);
		Console.printf("\tTotal con IVA: %.2f €\n", totalConIva);
	}

	private boolean masAverias() {
		return Console.readString("¿Añadir más averias? (s/n) ")
				.equalsIgnoreCase("s");
	}

}

package uo.ri.persistence.jpa;

import uo.ri.business.repository.FacturaRepository;
import uo.ri.model.Averia;
import uo.ri.model.Factura;
import uo.ri.persistence.jpa.util.BaseRepository;
import uo.ri.persistence.jpa.util.Jpa;

public class FacturaJpaRepository
		extends BaseRepository<Factura>
		implements FacturaRepository {

	@Override public Factura findByNumber( Long numero ) {
		return null;
	}

	@Override public Long getNextInvoiceNumber() {
		return null;
	}

}

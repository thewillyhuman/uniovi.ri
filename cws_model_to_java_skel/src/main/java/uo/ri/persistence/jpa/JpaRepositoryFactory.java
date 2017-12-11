package uo.ri.persistence.jpa;

import uo.ri.business.repository.AveriaRepository;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.FacturaRepository;
import uo.ri.business.repository.MecanicoRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.business.repository.RepositoryFactory;
import uo.ri.business.repository.RepuestoRepository;

public class JpaRepositoryFactory implements RepositoryFactory {

	@Override public MecanicoRepository forMechanic() {
		return new MechanicJpaRepository();
	}

	@Override public AveriaRepository forAveria() {
		return new AveriaJpaRepository();
	}

	@Override public MedioPagoRepository forMedioPago() {
		return new MedioPagoJpaRepository();
	}

	@Override public FacturaRepository forFactura() {
		return new FacturaJpaRepository();
	}

	@Override public ClienteRepository forCliente() {
		return new ClienteJpaRepository();
	}

	@Override public RepuestoRepository forRepuesto() {
		return new RepuestoJpaRepository();
	}

}

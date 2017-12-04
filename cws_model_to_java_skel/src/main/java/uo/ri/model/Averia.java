package uo.ri.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import alb.util.assertion.Assert;
import uo.ri.model.types.AveriaStatus;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = "VEHICULO_ID, FECHA")
})
public class Averia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	private Date fecha;
	private double importe = 0.0;
	private AveriaStatus status = AveriaStatus.ABIERTA;

	@ManyToOne
	private Vehiculo vehiculo;

	@ManyToOne
	private Mecanico mecanico;

	@ManyToOne
	private Factura factura;

	@OneToMany(mappedBy = "averia")
	private Set<Intervencion> intervenciones = new HashSet<>();

	/**
	 * Default empty constructor. JPA.
	 */
	Averia() {}

	/**
	 * Averia represents a fault in the real world.
	 * 
	 * @param vehiculo that suffers the fault.
	 */
	public Averia( Vehiculo vehiculo ) {
		super();
		this.fecha = new Date();
		Association.Averiar.link( vehiculo, this );
	}

	/**
	 * 
	 * @param vehiculo
	 * @param descripcion
	 */
	public Averia( Vehiculo vehiculo, String descripcion ) {
		this( vehiculo );
		this.descripcion = descripcion;
	}

	/**
	 * @return the unique id of the object.
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @return the description of the fault.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Sets the description of the fault.
	 * 
	 * @param descripcion to set.
	 */
	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the invoice assigned to the fault.
	 */
	public Factura getFactura() {
		return factura;
	}

	/**
	 * @return the date of the fault.
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @return the amount generated by the fault.
	 */
	public double getImporte() {
		return importe;
	}

	/**
	 * @return a copy of the actions performed as a set of interventions.
	 */
	public Set<Intervencion> getIntervenciones() {
		return new HashSet<>( intervenciones );
	}

	/**
	 * @return the mechanic assigned to the fault.
	 */
	public Mecanico getMecanico() {
		return mecanico;
	}

	/**
	 * @return the status of the fault.
	 */
	public AveriaStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status of the fault.
	 * 
	 * @param status to set.
	 */
	public void setStatus( AveriaStatus status ) {
		this.status = status;
	}

	/**
	 * @return the vehicle of the fault.
	 */
	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	/**
	 * Assigns the current fault (Averia) to the given mechanic (Mecanico).
	 * 
	 * @param mecanico to assign the fault.
	 */
	public void assignTo( Mecanico mecanico ) {
		if (this.status.equals( AveriaStatus.ABIERTA )) {
			Association.Asignar.link( mecanico, this );
			this.setStatus( AveriaStatus.ASIGNADA );
		}
	}

	/**
	 * Detach a mechanic from the current fault.
	 */
	public void desassign() {
		Assert.isTrue( isAssigned() );
		Association.Asignar.unlink( mecanico, this );
		status = AveriaStatus.ABIERTA;
	}

	/**
	 * Reopens the fault.
	 */
	public void reopen() {
		// Solo se puede reabrir una averia que está TERMINADA
		if (this.status.equals( AveriaStatus.TERMINADA )) {
			// la averia pasa a ABIERTA
			this.setStatus( AveriaStatus.ABIERTA );
		}
	
	}

	/**
	 * @return true if the fault is assigned to any mechanic.
	 */
	public boolean isAssigned() {
		return AveriaStatus.ASIGNADA.equals( status );
	}

	/**
	 * Marks this fault (Averia) as finished.
	 */
	public void markAsFinished() {
		if (this.status.equals( AveriaStatus.ASIGNADA )) {
			calcularImporteAveria();
			Association.Asignar.unlink( mecanico, this );
			this.setStatus( AveriaStatus.TERMINADA );
		}
	}

	/**
	 * Changes the status of the current fault as invoiced.
	 */
	public void markAsInvoiced() {
		if (this.status.equals( AveriaStatus.TERMINADA )) {
			this.setStatus( AveriaStatus.FACTURADA );
		}
	}

	/**
	 * Marks the current fault as finished.
	 */
	public void markBackToFinished() {
		if (this.status.equals( AveriaStatus.FACTURADA )) {
			this.setStatus( AveriaStatus.TERMINADA );
		}
	
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Averia other = (Averia) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals( other.fecha ))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( fecha == null ) ? 0 : fecha.hashCode() );
		return result;
	}

	@Override
	public String toString() {
		return "Averia [descripcion=" + descripcion + ", fecha=" + fecha + ", importe=" + importe
				+ ", status=" + status
				+ ", vehiculo=" + vehiculo + "]";
	}

	/**
	 * @return the actions performed as a set of interventions.
	 */
	Set<Intervencion> _getIntervenciones() {
		return intervenciones;
	}

	/**
	 * Changes the invoice assigned to this fault.
	 * 
	 * @param factura to set.
	 */
	void _setFactura( Factura factura ) {
		this.factura = factura;
	}

	/**
	 * Changes the mechanic assigned to the fault.
	 * 
	 * @param mecanico to set.
	 */
	void _setMecanico( Mecanico mecanico ) {
		this.mecanico = mecanico;
	}

	/**
	 * Changes the vehicle of the fault.
	 * 
	 * @param vehiculo to set.
	 */
	void _setVehiculo( Vehiculo vehiculo ) {
		this.vehiculo = vehiculo;
	}

	/**
	 * Calculates the amount produced by the fault.
	 */
	private void calcularImporteAveria() {
		double acum = 0;
		for (Intervencion intervencion : intervenciones) {
			acum += intervencion.getImporte();
		}
		this.importe = acum;
	
	}

}

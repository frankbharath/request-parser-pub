package com.bharath.rm.model.domain;

/**
 * The Class Lease.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 25, 2020 3:19:10 PM
 */
public class Lease {
	
	/** The leaseid. */
	private Long leaseid;
	
	/** The leasetenantid. */
	private Long leasetenantid;
	
	/** The tenantspropertydetailid. */
	private Long tenantspropertydetailid;
	
	/** The movein. */
	private Long movein;
	
	/** The moveout. */
	private Long moveout;
	
	/** The occupants. */
	private Integer occupants;
	
	/** The rent. */
	private float rent;
	
	/** The contractid. */
	private String contractid;
	
	/** The status. */
	private ContractStatus contractstatus;
	
	/**
	 * Gets the leaseid.
	 *
	 * @return the leaseid
	 */
	public Long getLeaseid() {
		return leaseid;
	}
	
	/**
	 * Sets the leaseid.
	 *
	 * @param leaseid the new leaseid
	 */
	public void setLeaseid(Long leaseid) {
		this.leaseid = leaseid;
	}
	
	/**
	 * Gets the leasetenantid.
	 *
	 * @return the leasetenantid
	 */
	public Long getLeasetenantid() {
		return leasetenantid;
	}
	
	/**
	 * Sets the leasetenantid.
	 *
	 * @param leasetenantid the new leasetenantid
	 */
	public void setLeasetenantid(Long leasetenantid) {
		this.leasetenantid = leasetenantid;
	}
	
	/**
	 * Gets the tenantspropertydetailid.
	 *
	 * @return the tenantspropertydetailid
	 */
	public Long getTenantspropertydetailid() {
		return tenantspropertydetailid;
	}
	
	/**
	 * Sets the tenantspropertydetailid.
	 *
	 * @param tenantspropertydetailid the new tenantspropertydetailid
	 */
	public void setTenantspropertydetailid(Long tenantspropertydetailid) {
		this.tenantspropertydetailid = tenantspropertydetailid;
	}
	
	/**
	 * Gets the movein.
	 *
	 * @return the movein
	 */
	public Long getMovein() {
		return movein;
	}
	
	/**
	 * Sets the movein.
	 *
	 * @param movein the new movein
	 */
	public void setMovein(Long movein) {
		this.movein = movein;
	}
	
	/**
	 * Gets the moveout.
	 *
	 * @return the moveout
	 */
	public Long getMoveout() {
		return moveout;
	}
	
	/**
	 * Sets the moveout.
	 *
	 * @param moveout the new moveout
	 */
	public void setMoveout(Long moveout) {
		this.moveout = moveout;
	}
	
	/**
	 * Gets the occupants.
	 *
	 * @return the occupants
	 */
	public int getOccupants() {
		return occupants;
	}
	
	/**
	 * Sets the occupants.
	 *
	 * @param occupants the new occupants
	 */
	public void setOccupants(int occupants) {
		this.occupants = occupants;
	}

	/**
	 * @return the rent
	 */
	public float getRent() {
		return rent;
	}

	/**
	 * @param rent the rent to set
	 */
	public void setRent(float rent) {
		this.rent = rent;
	}

	/**
	 * Gets the contractid.
	 *
	 * @return the contractid
	 */
	public String getContractid() {
		return contractid;
	}

	/**
	 * Sets the contractid.
	 *
	 * @param contractid the contractid to set
	 */
	public void setContractid(String contractid) {
		this.contractid = contractid;
	}

	/**
	 * Gets the contractstatus.
	 *
	 * @return the contractstatus
	 */
	public ContractStatus getContractstatus() {
		return contractstatus;
	}

	/**
	 * Sets the contractstatus.
	 *
	 * @param contractstatus the contractstatus to set
	 */
	public void setContractstatus(ContractStatus contractstatus) {
		this.contractstatus = contractstatus;
	}

	
}

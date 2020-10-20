package com.bharath.rm.dto;

import com.bharath.rm.model.domain.ContractStatus;

/**
 * The Class LeaseDTO.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 29, 2020 4:21:11 PM
 * Class Description
 */
public class LeaseDTO {
	
	/** The propertyname. */
	private String propertyname;
	
	/** The propertyid. */
	private Long propertyid;
	
	/** The leaseid. */
	private Long leaseid;
	
	/** The leasetenantid. */
	private Long leasetenantid;
	
	/** The tenantspropertydetailid. */
	private Long tenantspropertydetailid;
	
	/** The movein. */
	private String movein;
	
	/** The moveout. */
	private String moveout;
	
	/** The occupants. */
	private Integer occupants;
	
	/** The rent. */
	private float rent;
	
	/** The contractid. */
	private String contractid;
	
	/** The status. */
	private ContractStatus contractstatus;
	
	/**
	 * Gets the propertyname.
	 *
	 * @return the propertyname
	 */
	public String getPropertyname() {
		return propertyname;
	}

	/**
	 * Sets the propertyname.
	 *
	 * @param propertyname the new propertyname
	 */
	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	/**
	 * Gets the propertyid.
	 *
	 * @return the propertyid
	 */
	public Long getPropertyid() {
		return propertyid;
	}

	/**
	 * Sets the propertyid.
	 *
	 * @param propertyid the new propertyid
	 */
	public void setPropertyid(Long propertyid) {
		this.propertyid = propertyid;
	}

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
	public String getMovein() {
		return movein;
	}
	
	/**
	 * Sets the movein.
	 *
	 * @param movein the new movein
	 */
	public void setMovein(String movein) {
		this.movein = movein;
	}
	
	/**
	 * Gets the moveout.
	 *
	 * @return the moveout
	 */
	public String getMoveout() {
		return moveout;
	}
	
	/**
	 * Sets the moveout.
	 *
	 * @param moveout the new moveout
	 */
	public void setMoveout(String moveout) {
		this.moveout = moveout;
	}
	
	/**
	 * Gets the occupants.
	 *
	 * @return the occupants
	 */
	public Integer getOccupants() {
		return occupants;
	}
	
	/**
	 * Sets the occupants.
	 *
	 * @param occupants the new occupants
	 */
	public void setOccupants(Integer occupants) {
		this.occupants = occupants;
	}
	
	/**
	 * Gets the rent.
	 *
	 * @return the rent
	 */
	public float getRent() {
		return rent;
	}

	/**
	 * Sets the rent.
	 *
	 * @param rent the new rent
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
	 * @param contractid the new contractid
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

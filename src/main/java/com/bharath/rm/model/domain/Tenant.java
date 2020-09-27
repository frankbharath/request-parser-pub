package com.bharath.rm.model.domain;

/**
 * The Class Tenant.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 25, 2020 3:05:25 PM
 * Class Description
 */
public class Tenant {
	
	/** The tenantsid. */
	private Long tenantid;
	
	/** The tenantuserid. */
	private Long tenantuserid;
	
	/** The firstname. */
	private String firstname;
	
	/** The lastname. */
	private String lastname;
	
	/** The email. */
	private String email;
	
	/** The dob. */
	private String dob;
	
	/** The nationality. */
	private String nationality;
	
	/**
	 * Gets the tenantsid.
	 *
	 * @return the tenantsid
	 */
	public Long getTenantid() {
		return tenantid;
	}
	
	/**
	 * Sets the tenantsid.
	 *
	 * @param tenantsid the new tenantsid
	 */
	public void setTenantid(Long tenantid) {
		this.tenantid = tenantid;
	}
	
	/**
	 * @return the tenantuserid
	 */
	public Long getTenantuserid() {
		return tenantuserid;
	}

	/**
	 * @param tenantuserid the tenantuserid to set
	 */
	public void setTenantuserid(Long tenantuserid) {
		this.tenantuserid = tenantuserid;
	}

	/**
	 * Gets the firstname.
	 *
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Sets the firstname.
	 *
	 * @param firstname the new firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Gets the lastname.
	 *
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * Sets the lastname.
	 *
	 * @param lastname the new lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the dob.
	 *
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	
	/**
	 * Sets the dob.
	 *
	 * @param dob the new dob
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	/**
	 * Gets the nationality.
	 *
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}
	
	/**
	 * Sets the nationality.
	 *
	 * @param nationality the new nationality
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}	
}

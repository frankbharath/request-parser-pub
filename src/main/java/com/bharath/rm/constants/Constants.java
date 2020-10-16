package com.bharath.rm.constants;

/**
 * The Class Constants.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 22, 2020 4:59:59 PM
 * This class holds the constant fields
 */
public class Constants {
	
	/** The Constant STATUS. */
	public static final String STATUS="status";
	
	/** The Constant SUCCESS. */
	public static final String SUCCESS="success";
	
	/** The Constant FAILED. */
	public static final String FAILED="failed ";
	
	/** The Constant MESSAGE. */
	public static final String MESSAGE="MESSAGE";
	
	/** The Constant ERROR_CODE. */
	public static final String ERROR_CODE="ERROR_CODE";
	
	/** The Constant ERROR_CODE. */
	public static final String SUCCESS_CODE="SUCCESS_CODE";
	
	/** The Constant DATA. */
	public static final String DATA="DATA";
	
	/** The Constant ALPHANUMLEN. */
	public static final int ALPHANUMLEN=16;
	
	/** The Constant EXPIRATIONINTERVAL. */
	public static final long EXPIRATIONINTERVAL=86400000;
	
	/** The Constant VERIFY. */
	public static final String VERIFY="VERIFY";
	
	/** The Constant MINDAYS. */
	public static final int MINDAYS=28;
	
	/**
	 * The Enum Tokentype.
	 */
	public enum Tokentype {
		
		/** The verify. */
		VERIFY(1),
		
		/** The reset. */
		RESET(2);
		
		/** The value. */
		private final int value;
		
		/**
		 * Instantiates a new tokentype.
		 *
		 * @param val the val
		 */
		private Tokentype(int val) {
			this.value=val;
		}
		
		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
	        return value;
	    }
	}
	
	/**
	 * The Enum PropertyType.
	 */
	public enum PropertyType {
		
		/** The house. */
		HOUSE,
		
		/** The appartment. */
		APPARTMENT
	}
	
	/**
	 * The Enum UserType.
	 */
	public enum UserType{
		
		/** The owner. */
		OWNER,
		
		/** The tenant. */
		TENANT;
	}
	
	/**
	 * The Enum ContractStatus.
	 */
	public enum ContractStatus{
		
		/** The nocontract. */
		NOCONTRACT,
		
		/** The initiated. */
		INITIATED,
		
		/** The ownersigned. */
		OWNERSIGNED,
		
		/** The tenantsigned. */
		TENANTSIGNED,
		
		/** The terminated. */
		TERMINATED
	}
}

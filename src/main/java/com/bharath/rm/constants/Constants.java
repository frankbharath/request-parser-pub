package com.bharath.rm.constants;

/**
 * The Class Constants.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 22, 2020 4:59:59 PM
 * Class Description
 */
public class Constants {
	
	/** The Constant STATUS. */
	public static final String STATUS="STATUS";
	
	/** The Constant SUCCESS. */
	public static final String SUCCESS="SUCCESS";
	
	/** The Constant FAILED. */
	public static final String FAILED="FAILED ";
	
	/** The Constant MESSAGE. */
	public static final String MESSAGE="MESSAGE";
	
	/** The Constant ERROR_CODE. */
	public static final String ERROR_CODE="ERROR_CODE";
	
	/** The Constant ERROR_CODE. */
	public static final String SUCCESS_CODE="SUCCESS_CODE";
	
	/** The Constant DATA. */
	public static final String DATA="DATA";
	
	public static final int ALPHANUMLEN=16;
	
	public static final long EXPIRATIONINTERVAL=86400000;
	
	public static final String VERIFY="VERIFY";
	
	public enum Tokentype {
		VERIFY(1),
		RESET(2);
		private final int value;
		private Tokentype(int val) {
			this.value=val;
		}
		public int getValue() {
	        return value;
	    }
	}
	
	public enum UserTye{
		OWNER,
		TENANT;
	}
}

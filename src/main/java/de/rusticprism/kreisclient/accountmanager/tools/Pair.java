package de.rusticprism.kreisclient.accountmanager.tools;

import de.rusticprism.kreisclient.accountmanager.tools.alt.AltDatabase;

import java.io.Serializable;

/**
 * Simple Pair system with 2 variables.
 * @author MRebhan
 * @author The_Fireplace
 * 
 * @deprecated Inconvenient. Insure (saved user passwords). Used only for conversion from old accounts to new accounts.
 */
@Deprecated
public class Pair implements Serializable {
	private static final long serialVersionUID = 2586850598481149380L;
	public String obj1;
	public AltDatabase obj2;
}

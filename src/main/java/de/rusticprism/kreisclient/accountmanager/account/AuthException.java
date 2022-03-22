package de.rusticprism.kreisclient.accountmanager.account;

import net.minecraft.text.Text;

public class AuthException extends Exception {
	private static final long serialVersionUID = 1L;
	private Text text;
	
	public AuthException(Text text) {
		super(text.getString());
		this.text = text;
	}

	public Text getText() {
		return text;
	}
}

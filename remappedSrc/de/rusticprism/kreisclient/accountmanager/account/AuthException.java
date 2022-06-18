package de.rusticprism.kreisclient.accountmanager.account;

import net.minecraft.text.Text;

import java.io.Serial;

public class AuthException extends Exception {
	@Serial
	private static final long serialVersionUID = 1L;
	private final Text text;
	
	public AuthException(Text text) {
		super(text.getString());
		this.text = text;
	}

	public Text getText() {
		return text;
	}
}

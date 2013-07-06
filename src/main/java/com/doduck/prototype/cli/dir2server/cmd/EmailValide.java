package com.doduck.prototype.cli.dir2server.cmd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class EmailValide implements IParameterValidator {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public boolean validate(String emailStr) {
	        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
	        return matcher.find();
	}
	
	@Override
	public void validate(String name, String value) throws ParameterException {
		if(!this.validate(value)){
			throw new ParameterException("email is invalide");
		}
	}

}

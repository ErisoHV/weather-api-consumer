package com.weather.services.core.common.language;

import java.beans.PropertyEditorSupport;

import com.weather.exception.WeatherIlegalParameterException;

public class LanguageEnumConverter extends PropertyEditorSupport{

	@Override
    public void setAsText(String text) throws IllegalArgumentException {
		try {
	        String capitalized = text.toLowerCase();
	        Language language = Language.valueOf(capitalized);
	        setValue(language);
		} catch (IllegalArgumentException e) {
			throw new WeatherIlegalParameterException(text, Language.getValuesArray());
		}
    }
	
}

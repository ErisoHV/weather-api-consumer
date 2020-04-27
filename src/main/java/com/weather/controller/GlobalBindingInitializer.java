package com.weather.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.weather.services.core.common.language.Language;
import com.weather.services.core.common.language.LanguageEnumConverter;

@ControllerAdvice
public class GlobalBindingInitializer {

	@InitBinder
	 public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(Language.class, new LanguageEnumConverter());
	 }

}

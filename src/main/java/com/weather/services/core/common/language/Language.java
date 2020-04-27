package com.weather.services.core.common.language;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Language {
	ar, 				// Arabic	
	az,					// Azerbaijani
	bn,					// Bengali	
	bg,					// Bulgarian
	bs, 				// Bosnian
	ca, 				// Catalan
	zh, 				// Chinese Simplified
	zh_tw, 				// Chinese Traditional	
	cs,					// Czech
	da,					// Danish
	nl,					// Dutch
	fi,					// Finnish
	fr,					// French
	de,					// German
	el,					// Greek
	en, 				// English
	hi, 				// Hindi
	hr,					// Croatian
	hu, 				// Hungarian
	it,					// Italian
	ja,					// Japanese
	jv,					// Javanese
	ko,					// Korean
	zh_cmn,				// Mandarin
	mr, 				// Marathi
	pl, 				// Polish
	pt,					// Portuguese
	pa, 				// Punjabi
	ro,		 			// Romanian
	ru,		 			// Russian
	sr,					// Serbian
	si,					// Sinhalese
	sk, 				// Slovak
	es,					// Spanish
	sv,					// Swedish
	ta,					// Tamil
	te, 				// Telugu
	tr,					// Turkish
	uk,					// Ukrainian
	ur,					// Urdu
	vi,					// Vietnamese
	zh_wuu,				// Wu (Shanghainese)	
	zh_hsn,				// Xiang
	zh_yue,				// Yue (Cantonese)
	zuZulu;
	
	public static List<String> getValuesArray() {
		return Arrays.asList(values()).stream()
				.map(Language::toString).collect(Collectors.toList());
	}
	
	public static Language parse(String lang) {
		return Arrays.asList(values()).stream()
				.filter(lan -> lan.toString().equals(lang)).findFirst().orElse(null);
	}
	
}

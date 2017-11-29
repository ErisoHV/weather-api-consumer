package com.weather.model;

public class WeatherStatusType {
	private long id;
	private String name;
	private String locName;
	private String icon;
	
	public long getIdeTipoClima() {
		return id;
	}
	
	public void setIdeTipoClima(long ideTipoClima) {
		this.id = ideTipoClima;
	}

	public String getNombreEN() {
		return name;
	}

	public void setNombreEN(String nombreEN) {
		this.name = nombreEN;
	}

	public String getNombreES() {
		return locName;
	}

	public void setNombreES(String nombreES) {
		this.locName = nombreES;
	}

	public String getIcono() {
		return icon;
	}

	public void setIcono(String icono) {
		this.icon = icono;
	}
	
	@Override
	public String toString() {
		return "{" + id + ", " + name + ", " + locName + "," + icon + "}";
	}
}

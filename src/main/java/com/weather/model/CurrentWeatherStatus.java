package com.weather.model;

import java.sql.Timestamp;

public class CurrentWeatherStatus {
	private long ideHistoricoClima;
	private long ideSitio;
	private long ideClima;
	private Timestamp fecha;
	private double precipitacion;
	private double sensacionRealTemp;
	private double temperatura;
	private double vientos;
	private String direccionViento;
	
	public long getIdeHistoricoClima() {
		return ideHistoricoClima;
	}

	public void setIdeHistoricoClima(long ideHistoricoClima) {
		this.ideHistoricoClima = ideHistoricoClima;
	}

	public long getIdeSitio() {
		return ideSitio;
	}
	
	public void setIdeSitio(long sitio) {
		this.ideSitio = sitio;
	}

	public long getIdeClima() {
		return ideClima;
	}

	public void setIdeClima(long climaActual) {
		this.ideClima = climaActual;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public double getPrecipitacion() {
		return precipitacion;
	}

	public void setPrecipitacion(double precipitacion) {
		this.precipitacion = precipitacion;
	}

	public double getSensacionRealTemp() {
		return sensacionRealTemp;
	}

	public void setSensacionRealTemp(double sensacionRealTemp) {
		this.sensacionRealTemp = sensacionRealTemp;
	}

	public double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}

	public double getVientos() {
		return vientos;
	}

	public void setVientos(double vientos) {
		this.vientos = vientos;
	}
	
	public String getDireccionViento() {
		return direccionViento;
	}

	public void setDireccionViento(String direccionViento) {
		this.direccionViento = direccionViento;
	}

	@Override
	public String toString() {
		return 	"{" + ideHistoricoClima + ", " + ideSitio + ", " + ideClima + ", " + fecha + ", " + precipitacion + ", " 
				+ sensacionRealTemp + ", " + temperatura + ", " + vientos + "}";
	}
}

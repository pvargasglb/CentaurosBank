/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.util;

/**
 *
 * @author paola.vargas
 */

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class MessageUtil {

	public static void info(String message) {
		factory(message, FacesMessage.SEVERITY_INFO);
	}

	public static void error(String message) {
		factory(message, FacesMessage.SEVERITY_ERROR);
	}

	public static void warn(String message) {
		factory(message, FacesMessage.SEVERITY_WARN);
	}

	public static void fatal(String message) {
		factory(message, FacesMessage.SEVERITY_FATAL);
	}

	private static void factory(String message, Severity fm) {
		FacesMessage mensaje = new FacesMessage();
		mensaje.setSeverity(fm);
		mensaje.setSummary(message);
		FacesContext.getCurrentInstance().addMessage(null, mensaje);
	}

}
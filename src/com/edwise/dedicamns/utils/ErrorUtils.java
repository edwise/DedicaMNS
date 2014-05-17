/**
 * 
 */
package com.edwise.dedicamns.utils;

import android.util.SparseArray;

/**
 * @author edwise
 * 
 */
public class ErrorUtils {
	// TODO ojo! como meterlo como i18n?

	private static final SparseArray<String> errorMessages = new SparseArray<String>();

	static {
		errorMessages.put(401, "Error en la conexión 401: revisa que tu usuario y password sean correctos");
		errorMessages.put(404, "Error en la conexión 404: la web de dedicaciones parece caida...");
		errorMessages.put(500, "Error en la conexión 500: la web está devolviendo un error 500...");

		errorMessages.put(-1, "Sin conexión a internet: activa tu WIFI o conexión de datos");
		errorMessages.put(-2, "Error en la conexión: pete que flipas!");
		errorMessages.put(-3, "Error en el formato de hora");
		errorMessages.put(-4, "Error en el borrado de actividad diaria");
	}

	public static String getMessageError(int idError) {
		return errorMessages.get(idError);
	}
}

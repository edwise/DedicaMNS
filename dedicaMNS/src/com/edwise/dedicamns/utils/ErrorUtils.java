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

    private static final SparseArray<String> errorMessages = new SparseArray<String>();

    static {
	errorMessages.put(-1, "Sin conexión a internet: activa tu WIFI o conexión de datos");
	errorMessages.put(401, "Error en la conexión: revisa que tu usuario y password sean correctos");
    }

    public static String getMessageError(int idError) {
	return errorMessages.get(idError);
    }
}

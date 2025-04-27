package com.tenpo.calculator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final long CACHE_DURATION_MINUTES = 30;
    public static final String CALCULATE_PATH = "/calculate";
    public static final String CACHE_PERCENTAGE_KEY = "percentage::value";
    public static final String GET_HISTORY_SUCCESS_MESSAGE = "Historial recuperado exitosamente";
    public static final String GET_HISTORY_ERROR_MESSAGE = "Error recuperando historial de llamadas";
    public static final String CALC_OPERATIONS_SUCCESS_MESSAGE = "Sumatoria y porcentaje, calculados exitosamente";
    public static final String ERROR_GETTING_PERCENTAGE = "Operación fallida. No se pudo obtener el porcentaje";

    public static final String ERROR_GETTING_PERCENTAGE_EXTERNAL_SERVICE_AND_CACHE = "Error obteniendo el porcentaje desde la cache y servicio externo.";
    public static final String LOG_GETTING_PERCENTAGE_EXTERNAL_SERVICE_SUBSCRIBE = "Consumiendo servicio externo de porcentaje";
    public static final String LOG_GETTING_PERCENTAGE_EXTERNAL_SERVICE_SUCCESS = "Servicio de porcentaje consultado exitosamente, {}";
    public static final String ERROR_GETTING_PERCENTAGE_EXTERNAL_SERVICE_ERROR = "Error al llamar al servicio externo.";
    public static final String LOG_GETTING_PERCENTAGE_EXTERNAL_SERVICE_ERROR = Constants.ERROR_GETTING_PERCENTAGE_EXTERNAL_SERVICE_ERROR.concat(" {}");

    public static final String LOG_NEW_PERCENTAGE_SAVED_CACHE = "Nuevo porcentaje guardado en caché, {}";
    public static final String LOG_GETTING_PERCENTAGE_CACHE_SUCCESS = "Porcentaje recuperado de la caché correctamente, {}";

    public static final String LOG_GETTING_HISTORY_ERROR = "Error obteniendo el historial de llamadas: {}";
    public static final String LOG_CALCULATION_REQUEST = "Solicitud Servicio Calcular: {}";
    public static final String LOG_CALCULATION_RESPONSE = "Respuesta Servicio Calcular: {}";
    public static final String LOG_CALCULATION_ERROR = "Error Servicio Calcular: {}";

    public static final String LOG_SAVE_HISTORY_SUCCESS = "Historial de llamada guardado exitosamente: {}";
    public static final String LOG_SAVE_HISTORY_ERROR = "Error Guardando Historial de Llamadas: {}";

    public static final String INTERNAL_ERROR_MESSAGE = "Ha ocurrido un error interno";
}

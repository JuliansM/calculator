package com.tenpo.calculator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final long CACHE_DURATION_MINUTES = 30;
    public static final String CALCULATE_PATH = "/calculate";
    public static final String GET_CALL_HISTORY_PATH = "/get-call-history";
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

    public static final String SWAGGER_INFO_TITLE = "Tenpo Calculator API";
    public static final String SWAGGER_INFO_DESCRIPTION = "Documentación de la API REST de Calculator API";
    public static final String SWAGGER_INFO_VERSION = "1.0.0";
    public static final String SWAGGER_STANDARD_RESPONSE_TITLE = "Objeto de respuesta estándar de la aplicación";
    public static final String SWAGGER_REQUEST_TITLE = "Carga útil de la solicitud para realizar un cálculo";
    public static final String SWAGGER_REQUEST_NUMBER1 = "Primer número a sumar";
    public static final String SWAGGER_REQUEST_NUMBER2 = "Segundo número a sumar";
    public static final String SWAGGER_REQUEST_NUMBER1_EXAMPLE = "10";
    public static final String SWAGGER_REQUEST_NUMBER2_EXAMPLE = "20";
    public static final String SWAGGER_CONTROLLER_TAG_NAME = "CalculationController";
    public static final String SWAGGER_CONTROLLER_TAG_DESCRIPTION = "API para realizar cálculos y recuperar el historial de llamadas";
    public static final String SWAGGER_CONTROLLER_OPERATION_CALCULATE_SUMMARY = "Calcula la suma de 2 números y el porcentaje dinámico";
    public static final String SWAGGER_CONTROLLER_OPERATION_CALCULATE_DESCRIPTION = "Calcula la suma de 2 números y el porcentaje dinámico";
    public static final String SWAGGER_CONTROLLER_OPERATION_GET_HISTORY_SUMMARY = "Obtener el historial de llamadas de cálculo";
    public static final String SWAGGER_CONTROLLER_OPERATION_GET_HISTORY_DESCRIPTION = "Recupera el historial de todas las llamadas de cálculo anteriores realizadas por el usuario";
    public static final String SWAGGER_CONTROLLER_API_RESPONSE_CALCULATE_200_DESCRIPTION = "Cálculo realizado con éxito";
    public static final String SWAGGER_CONTROLLER_API_RESPONSE_400_DESCRIPTION = "Solitud inválida";
    public static final String SWAGGER_CONTROLLER_API_RESPONSE_500_DESCRIPTION = "Error interno del servidor";
    public static final String SWAGGER_CONTROLLER_API_RESPONSE_200 = "200";
    public static final String SWAGGER_CONTROLLER_API_RESPONSE_400 = "400";
    public static final String SWAGGER_CONTROLLER_API_RESPONSE_500 = "500";
    public static final String SWAGGER_CONTROLLER_API_RESPONSE_GET_HISTORY_200_DESCRIPTION = "Historial de llamadas recuperado con éxito";
}

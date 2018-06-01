package br.wake_in_place.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import okhttp3.ResponseBody;

public class ErrorResponse {
    public static final String UPDATE_VERSION = "111";
    public static final String UPDATE_VERSION_TITLE = "Atualização";
    public static final String NETWORK_DISABLE = "222";
    public static final String NETWORK_DISABLE_TITLE = "Conexão";
    public static final String UNEXPECTED = "333";
    public static final String UNEXPECTED_TITLE = "Erro";
    public static final String SESSION = "444";
    public static final String SESSION_TITLE = "Sessão expirada";
    public static final String FAIL = "555";
    public static final String FAIL_TITLE = "Falha";
    private static String verifyConnection = "Verifique sua conexão e tente novamente.";
    private static String errorSession = "Sua sessão expirou. Por favor, faça o login novamente.";
    private static String errorMsg = "Ocorreu um erro inesperado. Por favor, tente novamente.";
    private static String errorJson = "Error to parse Gson";
    private static String errorProcessing = "Desculpe-nos, não foi possível processar a sua solicitação no momento.";
    private static String errorUpdateVersion = "A versão do seu aplicativo está desatualizada. Clique no botão abaixo para atualizar.";

    @Expose
    private String code = "";
    @Expose
    private int codeServer = 0;
    @Expose
    private String message = errorMsg;
    @Expose
    private String messageServer = errorMsg;






    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessageServer() {
        return messageServer;
    }

    public void setMessageServer(String messageServer) {
        this.messageServer = messageServer;
    }


    public int getCodeServer() {
        return codeServer;
    }

    public void setCodeServer(int codeServer) {
        this.codeServer = codeServer;
    }

    public static  ErrorResponse NETWORK_ERROR() {
        final ErrorResponse error = new ErrorResponse();
        error.code = NETWORK_DISABLE;
        error.message = verifyConnection;
        return error;
    }

    public static  ErrorResponse getResponseError(final ResponseBody responseBody, int serverCode) {
        ErrorResponse error;
        try {
            error = new Gson().fromJson(responseBody.string(), ErrorResponse.class);
            error.setMessageServer(error.getMessage());
        } catch (Exception e) {
            error = getGsonParseError();
        }
        if (error == null) {
            error = new ErrorResponse();
            error.setCode(ErrorResponse.UNEXPECTED);
            error.setMessageServer(errorMsg);
            error.setMessage(errorMsg);
        }
        error.setCodeServer(serverCode);
        return error;
    }



    public static  ErrorResponse getGsonParseError() {
        final ErrorResponse error = new ErrorResponse();
        error.setCode(errorJson);
        error.setMessage(errorProcessing);
        error.setMessageServer(errorProcessing);
        return error;
    }

    public static ErrorResponse getExceptionFail(final ResponseBody responseBody, int serverCode) {
        final ErrorResponse error = new ErrorResponse();
        error.setCode(UNEXPECTED);
        error.setMessage(errorProcessing);
        ErrorResponse errorServer;
        try {
            if(responseBody != null) {
                errorServer = new Gson().fromJson(responseBody.string(), ErrorResponse.class);
                error.setMessageServer(errorServer.message);

            }else{
                error.setMessageServer(errorProcessing);
            }
        } catch (Exception e) {
            error.setMessageServer(errorProcessing);
        }
        error.setCodeServer(serverCode);
        return error;
    }

    public static ErrorResponse getUpdateApplicationError() {
        final ErrorResponse error = new ErrorResponse();
        error.setCode(UPDATE_VERSION);
        error.setMessage(errorUpdateVersion);
        error.setMessageServer(errorUpdateVersion);
        return error;
    }

    public static ErrorResponse setSessionError(final ResponseBody responseBody, int serverCode) {
        final ErrorResponse error = new ErrorResponse();
        ErrorResponse errorServer;
        try {
            if(responseBody != null) {
                errorServer = new Gson().fromJson(responseBody.string(), ErrorResponse.class);
                error.setMessageServer(errorServer.message);
            }else{
                error.setMessageServer(errorSession);
            }
        } catch (Exception e) {
            error.setMessageServer(errorSession);
        }
        error.setCode(SESSION);
        error.setMessage(errorSession);
        error.setCodeServer(serverCode);
        return error;
    }
}
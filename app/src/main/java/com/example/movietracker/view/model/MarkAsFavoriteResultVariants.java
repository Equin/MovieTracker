package com.example.movietracker.view.model;

public enum MarkAsFavoriteResultVariants {

    UP_TO_DATE("Favorites up to date", 100),
    UPDATE_SUCCESSFUL("Favorites updated", 101),
    UPDATE_FAILED("The resource you requested could not be found.", 34),
    AUTHORIZATION_FAILED("Authentication failed: You do not have permissions to access the service.", 3),
    HTTP_401("Authentication failed: You do not have permissions to access the service.", 401),
    HTTP_404("Authentication failed: You do not have permissions to access the service.", 404),
    HTTP_201("Authentication failed: You do not have permissions to access the service.", 201);

    private String resultMessage;
    private int resultCode;

    MarkAsFavoriteResultVariants(String resultMessage, int resultCode ) {
        this.resultMessage = resultMessage;
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }

    public int getResultCode() {
        return this.resultCode;
    }

}

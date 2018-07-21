package controllers;

import play.api.http.HttpErrorHandler;

import javax.inject.Inject;

public class VaadinAssets extends Assets {

    @Inject
    public VaadinAssets(HttpErrorHandler errorHandler, AssetsMetadata meta) {
        super(errorHandler, meta);
    }
}

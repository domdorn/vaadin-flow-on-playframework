package org.vaadin.playintegration;

import com.vaadin.flow.component.PushConfiguration;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.internal.ReflectTools;
import com.vaadin.flow.server.BootstrapHandler;
import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.server.SystemMessages;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.Version;
import com.vaadin.flow.server.communication.AtmospherePushConnection;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.shared.communication.PushMode;
import elemental.json.Json;
import elemental.json.JsonObject;

import java.util.Locale;
import java.util.Optional;

public class PlayBootstrapHandler extends BootstrapHandler {
    private static final String CAPTION = "caption";
    private static final String MESSAGE = "message";
    private static final String URL = "url";
    private static final String CONTEXT_ROOT = "play.contextRoot";

    private final String contextRoot;

    public PlayBootstrapHandler(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    @Override
    protected BootstrapContext createAndInitUI(Class<? extends UI> uiClass,
                                               VaadinRequest request, VaadinResponse response,
                                               VaadinSession session) {
        UI ui = ReflectTools.createInstance(uiClass);
        request.setAttribute(CONTEXT_ROOT, contextRoot);
        ui.getInternals().setContextRoot(contextRoot + "/");

        PushConfiguration pushConfiguration = ui.getPushConfiguration();

        ui.getInternals().setSession(session);
        ui.setLocale(session.getLocale());

        BootstrapContext context = new PlayBootstrapContext(request, response,
                session, ui);

        Optional<Push> push = context
                .getPageConfigurationAnnotation(Push.class);

        DeploymentConfiguration deploymentConfiguration = context.getSession()
                .getService().getDeploymentConfiguration();
        PushMode pushMode = push.map(Push::value)
                .orElseGet(deploymentConfiguration::getPushMode);
        setupPushConnectionFactory(pushConfiguration, context);
        pushConfiguration.setPushMode(pushMode);
        pushConfiguration.setPushUrl(deploymentConfiguration.getPushURL());
        push.map(Push::transport).ifPresent(pushConfiguration::setTransport);

        // Set thread local here so it is available in init
        UI.setCurrent(ui);
        ui.doInit(request, session.getNextUIid());
        session.addUI(ui);

        // After init and adding UI to session fire init listeners.
        session.getService().fireUIInitListeners(ui);

        ui.getRouter().initializeUI(ui, request);

        return context;
    }

    protected static JsonObject getApplicationParameters(
            BootstrapContext context) {
        JsonObject appConfig = getApplicationParameters(context.getRequest(),
                context.getSession());

        appConfig.put(ApplicationConstants.UI_ID_PARAMETER,
                context.getUI().getUIId());
        return appConfig;
    }

    private static JsonObject getApplicationParameters(VaadinRequest request,
                                                       VaadinSession session) {
        VaadinService vaadinService = session.getService();
        DeploymentConfiguration deploymentConfiguration = session
                .getConfiguration();
        final boolean productionMode = deploymentConfiguration
                .isProductionMode();

        JsonObject appConfig = Json.createObject();

        appConfig.put(ApplicationConstants.FRONTEND_URL_ES6,
                deploymentConfiguration.getEs6FrontendPrefix());
        appConfig.put(ApplicationConstants.FRONTEND_URL_ES5,
                deploymentConfiguration.getEs5FrontendPrefix());

        if (!productionMode) {
            JsonObject versionInfo = Json.createObject();
            versionInfo.put("vaadinVersion", Version.getFullVersion());
            String atmosphereVersion = AtmospherePushConnection
                    .getAtmosphereVersion();
            if (atmosphereVersion != null) {
                versionInfo.put("atmosphereVersion", atmosphereVersion);
            }
            appConfig.put("versionInfo", versionInfo);
        }

        // Use locale from session if set, else from the request
        Locale locale = ServletHelper.findLocale(session, request);
        // Get system messages
        SystemMessages systemMessages = vaadinService.getSystemMessages(locale,
                request);
        if (systemMessages != null) {
            JsonObject sessExpMsg = Json.createObject();
            putValueOrNull(sessExpMsg, CAPTION,
                    systemMessages.getSessionExpiredCaption());
            putValueOrNull(sessExpMsg, MESSAGE,
                    systemMessages.getSessionExpiredMessage());
            putValueOrNull(sessExpMsg, URL,
                    systemMessages.getSessionExpiredURL());

            appConfig.put("sessExpMsg", sessExpMsg);
        }

        appConfig.put(ApplicationConstants.CONTEXT_ROOT_URL, String.valueOf(request.getAttribute(CONTEXT_ROOT) + "/"));

        if (!productionMode) {
            appConfig.put("debug", true);
        }

        if (deploymentConfiguration.isRequestTiming()) {
            appConfig.put("requestTiming", true);
        }

        appConfig.put("heartbeatInterval",
                deploymentConfiguration.getHeartbeatInterval());

        boolean sendUrlsAsParameters = deploymentConfiguration
                .isSendUrlsAsParameters();
        if (!sendUrlsAsParameters) {
            appConfig.put("sendUrlsAsParameters", false);
        }

        return appConfig;
    }

    private static void putValueOrNull(JsonObject object, String key,
                                       String value) {
        assert object != null;
        assert key != null;
        if (value == null) {
            object.put(key, Json.createNull());
        } else {
            object.put(key, value);
        }
    }


    protected class PlayBootstrapContext extends BootstrapContext {
        private JsonObject applicationParameters;

        public PlayBootstrapContext(VaadinRequest request, VaadinResponse response, VaadinSession session, UI ui) {
            super(request, response, session, ui);
        }


        @Override
        public JsonObject getApplicationParameters() {
            if (applicationParameters == null) {
                applicationParameters = PlayBootstrapHandler
                        .getApplicationParameters(this);
            }

            return applicationParameters;
        }

    }

}

package com.mobile.bonrix.bonrixappstore.utility;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

public class App extends Application {

    public static App app;
    private static String id = null;

    public static App getInstance() {

        if (app == null) {
            app = new App();
        }
        return app;
    }
    @Override
    public void onCreate() {
        super.onCreate();


        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .setDiskCacheEnabled(true)
                .build();
        Fresco.initialize(this, config);

    }

}

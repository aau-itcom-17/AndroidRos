package com.example.marcu.androidros;

import com.example.marcu.androidros.utils.Prefs;

public class Application extends android.app.Application {

    private Prefs prefs;
    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        prefs = new Prefs(this);

    }

    public static Application getApp() {
        return app;
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public void setPrefs(Prefs prefs) {
        this.prefs = prefs;
    }


}

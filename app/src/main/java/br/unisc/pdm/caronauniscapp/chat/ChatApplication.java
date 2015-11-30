package br.unisc.pdm.caronauniscapp.chat;

import com.firebase.client.Firebase;


/**
 * @author Diego, Gabriel, Rafael
 * @since 12/5/14
 *
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class ChatApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

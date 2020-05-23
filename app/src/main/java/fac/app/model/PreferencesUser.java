package fac.app.model;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by francoisledoyen on 27/01/2018.
 * Accès aux préferences de l'utlisateur
 */

public class PreferencesUser {

    private static final String KEY_PSEUDO  = "pseudo";
    private static final String KEY_PHONE  = "phone";
    private static final String KEY_MAIL  = "email";
    private SharedPreferences sharedPreferences;

    public PreferencesUser(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getPseudo(){
        return this.sharedPreferences.getString(KEY_PSEUDO, "");
    }

    public String getMail(){
        return this.sharedPreferences.getString(KEY_MAIL, "");
    }

    public String getPhone(){
        return this.sharedPreferences.getString(KEY_PHONE, "");
    }
}

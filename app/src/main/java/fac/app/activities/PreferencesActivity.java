package fac.app.activities;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import fac.app.R;

/**
 * Activté de gestion des préférences de l'utilisateur
 * utilisation de PreferenceFragment
 * **/
public class PreferencesActivity extends ActivityWithMenu{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_user);
        configureToolbar("Mon profil");
        configureDrawerLayout();
        configureNavigationView();
        getFragmentManager().beginTransaction().replace(R.id.preferenceUserToolBarContainer, new MyPreferenceFragment()).commit();
    }



    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            initSummary();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            initSummary();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePrefsSummary(findPreference(key));
        }

        /**
         * Recopie la préférence dans son summary
         * **/
        protected void updatePrefsSummary(Preference pref){
            if (pref == null )
                return;
            if (pref instanceof ListPreference){
                ListPreference listPreference = (ListPreference) pref;
                listPreference.setSummary(listPreference.getEntry());
            } else if (pref instanceof EditTextPreference){
                EditTextPreference editTextPreference = (EditTextPreference) pref;
                editTextPreference.setSummary(editTextPreference.getText());
            }
        }

        /**
         * intialise le summary de la préférence en fonction de son type
         * **/
        protected void initPrefsSummary(Preference p) {
            if (p instanceof PreferenceCategory) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) p;
                for (int i = 0; i < preferenceCategory.getPreferenceCount(); i++) {
                     initPrefsSummary(preferenceCategory.getPreference(i));
                }
            } else {
                updatePrefsSummary(p);
            }
	    }

	    /**
         * Initialise le summary de chaque préférence **/
	    protected void initSummary() {
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                initPrefsSummary(getPreferenceScreen().getPreference(i));
            }
	    }
    }

}

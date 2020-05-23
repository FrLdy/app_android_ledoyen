package fac.app.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import fac.app.activities.AnnonceActivity;
import fac.app.activities.ListeActivity;
import fac.app.activities.PreferencesActivity;
import fac.app.activities.SaveAnnonceActivity;
import fac.app.model.annonce.Annonce;
import fac.app.model.annonce.AnnonceStorage;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by francoisledoyen on 03/02/2018.
 * Gestion des différentes fonctionnalités acessibles depuis le menu (navigation drawer / toolbar)
 */

public class Menu {

    private View view;
    private Context context;
    private AnnonceStorage annonceStorage;
    public InternetConnexion internetConnexion;
    public OkHttpClient okHttpClient;



    public Menu(View view) {
        this.view = view;
        this.okHttpClient = new OkHttpClient();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        this.internetConnexion = new InternetConnexion(connectivityManager);
        this.context = view.getContext();
        this.annonceStorage = new AnnonceStorage(view.getContext());
    }

    /**
     * Requère l'API REST pour afficher une annonce aléatoire
     * **/
    public void displayRandomAnnonce() {
        final Intent intentAnnonce = new Intent(this.context, AnnonceActivity.class);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                this.annonceStorage.getUrlRandom(),
                null,
                new ResponseListenerDisplayAnnonce(this.annonceStorage, intentAnnonce, this.context),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayErrorAnnonce();
                    }
                });

        this.annonceStorage.requestQueueSingleton.addToRequestQueue(jsonObjectRequest);

    }

    public void displayAnnonce(String id) {
        final Intent intentAnnonce = new Intent(this.context, AnnonceActivity.class);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.annonceStorage.getUrlAnnonce(id), null,
                new ResponseListenerDisplayAnnonce(annonceStorage, intentAnnonce, context),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayErrorAnnonce();
                    }
                });

        this.annonceStorage.requestQueueSingleton.addToRequestQueue(jsonObjectRequest);

    }

    /**
     * Réquète l'API REST pour afficher la liste des annonces
     * **/
    public void displayListeAnnonces() {
        final Intent intentListeAnnonce = new Intent(this.context, ListeActivity.class);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.annonceStorage.getUrlListAll(), null,
                new ResponseListenerDisplayListeAnnonces(intentListeAnnonce, context, annonceStorage),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayErrorAnnonce();
                    }
                });

        this.annonceStorage.requestQueueSingleton.addToRequestQueue(jsonObjectRequest);
    }

    public void displayErrorAnnonce() {
        Toast.makeText(this.context, "Erreur : nous ne pouvons pas afficher l'annonce", Toast.LENGTH_LONG).show();
    }

    /**
     * Lance l'activité d'enregistrement d'une annonce
     * **/
    public void displaySaveAnnonce() {
        Intent intent = new Intent(context, SaveAnnonceActivity.class);
        context.startActivity(intent);
    }

    public void displayErrorInternet() {
        internetConnexion.displayErrorInternet(this.context);
    }

    /**
     * Lance l'activité de gestion des préférences de l'utilisateur
     * **/
    public void displayProfilActivity() {
        Intent intentProfil = new Intent(this.view.getContext(), PreferencesActivity.class);
        context.startActivity(intentProfil);
    }

    /**
     * Requète l'API REST pour enregistrer l'annonce**/
    public void saveAnnonce(final Annonce annonce) {
        final Intent intentAnnonce = new Intent(this.context, AnnonceActivity.class);

        RequestBody requestBody = annonceStorage.annonceToRequestBody(annonce);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(annonceStorage.getUrlSave())
                .post(requestBody)
                .build();
        if (!annonce.getImages().isEmpty()){
            okHttpClient.newCall(request).enqueue(new ResponseListenerDisplaySavePictures(intentAnnonce, annonceStorage, context, okHttpClient, annonce));
        } else {
            okHttpClient.newCall(request).enqueue(new ResponseListenerDisplayAnnonce(annonceStorage, intentAnnonce, context));
        }
    }
}

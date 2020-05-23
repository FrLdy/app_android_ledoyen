package fac.app.Controller;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;

import fac.app.model.annonce.Annonce;
import fac.app.model.annonce.AnnonceStorage;

/**
 * Created by francoisledoyen on 09/02/2018.
 * Listener affiche la liste des annonces retournée par l'API
 * Implémente Volley
 */

public class ResponseListenerDisplayListeAnnonces implements Response.Listener<JSONObject> {
     private Intent intent;
     private Context context;
     private AnnonceStorage annonceStorage;

    public ResponseListenerDisplayListeAnnonces(Intent intent, Context context, AnnonceStorage annonceStorage) {
        this.intent = intent;
        this.context = context;
        this.annonceStorage = annonceStorage;
    }

    @Override
    public void onResponse(JSONObject response) {
        ArrayList<Annonce> annonces = annonceStorage.buildListeAnnonce(annonceStorage.getResponseArray(response));
        intent.putExtra(Annonce.ANNONCE, annonces);
        context.startActivity(intent);
    }
}

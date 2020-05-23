package fac.app.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import fac.app.model.annonce.Annonce;
import fac.app.model.annonce.AnnonceStorage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by francoisledoyen on 09/02/2018.
 * Listener permettant d'afficher une annonce retournée par l'API
 * Implémente Volley
 * Implémente OkHttp
 */

public class ResponseListenerDisplayAnnonce implements com.android.volley.Response.Listener<JSONObject>, Callback {
    private AnnonceStorage annonceStorage;
    private Intent intent;
    private Context context;

    public ResponseListenerDisplayAnnonce(AnnonceStorage annonceStorage, Intent intent, Context context) {
        this.annonceStorage = annonceStorage;
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void onResponse(JSONObject response) {
        Annonce annonce = annonceStorage.buildAnnonce(annonceStorage.getResponseObject(response));
        if (annonce != null) {
            intent.putExtra(Annonce.ANNONCE, annonce);
            context.startActivity(intent);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("ERROR_DIS", e.toString());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            Log.d("POIU", jsonObject.toString());

            if (jsonObject.getBoolean(AnnonceStorage.KEY_SUCCESS)){
                Annonce annonce1 = annonceStorage.buildAnnonce(annonceStorage.getResponseObject(jsonObject));
                intent.putExtra(Annonce.ANNONCE, annonce1);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, Uri.encode(jsonObject.getString(AnnonceStorage.KEY_RESPONSE)), Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            Log.e("ERROR JSONObject", e.toString());
        }
    }
}

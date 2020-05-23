package fac.app.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import fac.app.model.annonce.Annonce;
import fac.app.model.annonce.AnnonceStorage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by francoisledoyen on 09/02/2018.
 * Listener permet d'enregistrer l'image d'une annonce sur l'API
 */

public class ResponseListenerDisplaySavePictures implements Callback {
    private Intent intent;
    private AnnonceStorage annonceStorage;
    private Context context;
    private OkHttpClient okHttpClient;
    private Annonce annonce;

    public ResponseListenerDisplaySavePictures(Intent intent, AnnonceStorage annonceStorage, Context context, OkHttpClient okHttpClient, Annonce annonce) {
        this.intent = intent;
        this.annonceStorage = annonceStorage;
        this.context = context;
        this.okHttpClient = okHttpClient;
        this.annonce = annonce;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("ERROR", e.toString());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());

            if (jsonObject.getBoolean(AnnonceStorage.KEY_SUCCESS)) {
                // annonce retournée après son enregistrement
                Annonce annonce1 = annonceStorage.buildAnnonce(annonceStorage.getResponseObject(jsonObject));

                // préparation de la requète d'ajout de l'image
                RequestBody requestBody = annonceStorage.prepareAddImage(annonce.getImages().get(0), annonce1.getId());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(annonceStorage.getUrlSave())
                        .post(requestBody)
                        .build();
                // enfile la requète d'ajout de l'image;
                okHttpClient.newCall(request).enqueue(new ResponseListenerDisplayAnnonce(annonceStorage, intent, context));
            }
        } catch (Exception e) {
            Log.e("ERROR JSONObject", e.toString());
        }
    }
}

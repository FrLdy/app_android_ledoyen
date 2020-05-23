package fac.app.model.annonce;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fac.app.Controller.RequestQueueSingleton;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by francoisledoyen on 02/02/2018.
 * Gestion des requètes avec l'API REST
 */

public class AnnonceStorage {
    public static final String APIKEY = "21506655";
    public static final String REF_APIKEY = "apikey";
    public static final String REF_METHOD = "method";
    public static final String REF_ID = "id";
    public static final String REF_PHOTO = "photo";
    public static final String METHOD_SAVE = "save";
    public static final String METHOD_RANDOM = "randomAd";
    public static final String METHOD_LISTALL = "listAll";
    public static final String METHOD_DETAILS = "details";
    public static final String METHOD_ADDIMAGE = "addImage";
private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    public static final String KEY_RESPONSE = "response";
    public static final String KEY_SUCCESS = "success";

    public RequestQueueSingleton requestQueueSingleton;
    public String url= "https://ensweb.users.info.unicaen.fr/android-api/";

    public AnnonceStorage(Context context) {
        this.requestQueueSingleton = RequestQueueSingleton.getInstanceUnique(context);
        this.requestQueueSingleton.startRequestQueue();
    }


    public JSONObject getResponseObject(JSONObject jsonObject){
        try {
            if(jsonObject.getBoolean(KEY_SUCCESS)){

                return jsonObject.getJSONObject(KEY_RESPONSE);

            }
        } catch (JSONException e){
            throw new RuntimeException("error : " + e);
        }
        return null;
    }

    public JSONArray getResponseArray(JSONObject jsonObject){
        try {
            if(jsonObject.getBoolean(KEY_SUCCESS)){

                return jsonObject.getJSONArray(KEY_RESPONSE);

            }
        } catch (JSONException e){
            throw new RuntimeException("error : " + e);
        }
        return null;
    }

    /**
     * retourne une instance d'Annonce construite depuis une JSON**/
    public Annonce buildAnnonce(JSONObject jsonObject){
        try {
                ArrayList<String> images = new ArrayList<>();
                JSONArray jsonArray = (JSONArray) jsonObject.get(AnnonceContract.KEY_PICTURES);

                for (int i=0;i<jsonArray.length();i++){
                    try {
                        images.add(jsonArray.getString(i));
                    } catch (JSONException e){

                    }
                }

                return new Annonce(
                        jsonObject.getString(AnnonceContract.KEY_ID),
                        jsonObject.getString(AnnonceContract.KEY_TITLE),
                        jsonObject.getString(AnnonceContract.KEY_DESCRIPTION),
                        jsonObject.getInt(AnnonceContract.KEY_PRICE),
                        jsonObject.getString(AnnonceContract.KEY_USERNAME),
                        jsonObject.getString(AnnonceContract.KEY_EMAIL),
                        jsonObject.getString(AnnonceContract.KEY_PHONE),
                        jsonObject.getString(AnnonceContract.KEY_CITY),
                        jsonObject.getString(AnnonceContract.KEY_POST_CODE),
                        images,
                        jsonObject.getInt(AnnonceContract.KEY_DATE));

        } catch (Exception e){
            return null;
        }
    }

    public Map<String, String> buildPost(Annonce annonce){
        Map<String, String> res = new HashMap<>();
        res.put(AnnonceContract.KEY_TITLE, annonce.getTitre());
        res.put(AnnonceContract.KEY_CITY, annonce.getVille());
        res.put(AnnonceContract.KEY_POST_CODE, annonce.getCp());
        res.put(AnnonceContract.KEY_PRICE, annonce.getPrix().toString());
        res.put(AnnonceContract.KEY_PHONE, annonce.getTelContact());
        res.put(AnnonceContract.KEY_EMAIL, annonce.getEmailContact());
        res.put(AnnonceContract.KEY_DESCRIPTION, annonce.getDescription());
        res.put(AnnonceContract.KEY_USERNAME, annonce.getPseudo());
        res.put(REF_APIKEY, APIKEY);
        res.put(REF_METHOD, METHOD_SAVE);

        return res;
    }

    public JSONObject annonceToJson(Annonce annonce){
        return new JSONObject(buildPost(annonce));
    }

    /**
     * retourne le Body d'une requète OkHttp pour enregistrer une annonce (sans son image)
     * **/
    public RequestBody annonceToRequestBody(Annonce annonce){
        return new FormBody.Builder()
                .add(AnnonceStorage.REF_APIKEY, AnnonceStorage.APIKEY)
                .add(AnnonceContract.KEY_TITLE, annonce.getTitre())
                .add(AnnonceContract.KEY_CITY, annonce.getVille())
                .add(AnnonceContract.KEY_POST_CODE, annonce.getCp())
                .add(AnnonceContract.KEY_PRICE, annonce.getPrix().toString())
                .add(AnnonceContract.KEY_PHONE, annonce.getTelContact())
                .add(AnnonceContract.KEY_EMAIL, annonce.getEmailContact())
                .add(AnnonceContract.KEY_DESCRIPTION, annonce.getDescription())
                .add(AnnonceContract.KEY_USERNAME, annonce.getPseudo())
                .add(REF_APIKEY, APIKEY)
                .add(REF_METHOD, METHOD_SAVE)
                .build();
    }

    /**
     * retourne le Body d'une requète OkHttp pour enregister l'image d'une annonce
     * **/
    public RequestBody prepareAddImage(String image, String id){
        Log.d("FILE", image);
        File file = new File(image).getAbsoluteFile();
        Log.d("PATH", file.toString());
        MultipartBody.Builder builder =  new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(REF_METHOD, METHOD_ADDIMAGE)
                .addFormDataPart(REF_APIKEY, APIKEY)
                .addFormDataPart(REF_ID, id)
                .addFormDataPart(REF_PHOTO, image, RequestBody.create(MEDIA_TYPE_PNG, file));

        return builder.build();
    }

/**
 * retourne une liste d'Annonces depuis une liste de JSON représentants des annonces
 * **/
    public ArrayList<Annonce> buildListeAnnonce(JSONArray jsonArray){
        ArrayList<Annonce> annonces = new ArrayList<>();
        try {
            for (int i=0;i<jsonArray.length();i++){
                    try {
                        annonces.add(this.buildAnnonce(jsonArray.getJSONObject(i)));
                    } catch (JSONException e){

                    }
                }
            return annonces;

        } catch (Exception e){
            return null;
        }
    }


    public String getUrlApk(){
        return this.url + "?" + REF_APIKEY + "=" + APIKEY;
    }

    public String getUrlRandom() {
        return getUrlApk() + "&" + REF_METHOD + "=" + METHOD_RANDOM;
    }

    public String getUrlListAll(){
        return getUrlApk() + "&" + REF_METHOD + "=" + METHOD_LISTALL;
    }

    public String getUrlAnnonce(String id){
        return getUrlApk() + "&" + REF_METHOD+ "=" + METHOD_DETAILS + "&" + REF_ID + "=" + id;
    }

    public String getUrl(){
        return this.url;
    }

    public String getUrlSave(){
        return getUrl();
    }
}

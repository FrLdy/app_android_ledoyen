package fac.app.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import fac.app.activities.AnnonceActivity;
import fac.app.R;
import fac.app.model.annonce.Annonce;
import fac.app.model.annonce.AnnonceStorage;

/**
 * Created by francoisledoyen on 03/02/2018.
 * Construit la carte à afficher dans la liste
 */

public class AnnonceViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewTitre;
    private TextView textViewPrix;
    private TextView textViewLocalisation;
    private String id;
    private ImageView imageView;
    private View view;
    private AnnonceStorage annonceStorage;



    public AnnonceViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.textViewTitre = itemView.findViewById(R.id.cardTitre);
        this.textViewLocalisation = itemView.findViewById(R.id.cardLocation);
        this.textViewPrix = itemView.findViewById(R.id.cardPrix);
        this.imageView = itemView.findViewById(R.id.cardImage);
        this.annonceStorage = new AnnonceStorage(view.getContext());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAnnonce();
            }
        });
    }


    public void bind(Annonce annonce){
        this.id = annonce.getId();
        textViewTitre.setText(annonce.getTitre());
        textViewPrix.setText(annonce.getPrix().toString() + "€");
        textViewLocalisation.setText(annonce.getCp() + " " + annonce.getVille());
        setImageView(annonce.getImages());
    }

    public void setImageView(ArrayList<String> images){
        if (images.isEmpty()){
            Picasso.with(this.view.getContext()).load(R.drawable.indispo).into(this.imageView);
        } else{
            Picasso.with(this.view.getContext()).load(images.get(0)).into(this.imageView);
        }
    }

    /**
     * Affiche l'annonce selectionnée
     * **/
    private void displayAnnonce(){

        final Intent intentAnnonce = new Intent(view.getContext(), AnnonceActivity.class);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.annonceStorage.getUrlAnnonce(this.id), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Annonce annonce = annonceStorage.buildAnnonce(annonceStorage.getResponseObject(response));
                        if (annonce != null){
                            intentAnnonce.putExtra(Annonce.ANNONCE, annonce);
                            view.getContext().startActivity(intentAnnonce);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        this.annonceStorage.requestQueueSingleton.addToRequestQueue(jsonObjectRequest);

    }
}

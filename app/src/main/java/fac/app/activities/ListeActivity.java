package fac.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import fac.app.R;
import fac.app.model.annonce.Annonce;
import fac.app.model.annonce.AnnonceAdapter;
/**
 * Activité montrant la liste des annonces
 * **/
public class ListeActivity extends ActivityWithMenu {

    public ArrayList<Annonce> annoncesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);


        Intent intent = getIntent();
        this.annoncesList = (ArrayList<Annonce>) intent.getSerializableExtra(Annonce.ANNONCE);
        configureToolbar("Liste des annonces");
        configureDrawerLayout();
        configureNavigationView();

        // Liste des annoncess
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAnnonces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // adapter d'annonce en card
        AnnonceAdapter annonceAdapter = new AnnonceAdapter(this.annoncesList);
        recyclerView.setAdapter(annonceAdapter);
    }

}

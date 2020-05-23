package fac.app.model.annonce;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fac.app.R;
import fac.app.activities.AnnonceViewHolder;

/**
 * Created by francoisledoyen on 03/02/2018.
 * Adpate une instance d'Annonce en carte pour la liste
 */

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceViewHolder> {

    private ArrayList<Annonce> list;


    public AnnonceAdapter(ArrayList<Annonce> list) {
        this.list = list;
    }

    @Override
    public AnnonceViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup,false);
        return new AnnonceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnnonceViewHolder annonceViewHolder, int position) {
        Annonce annonce = list.get(position);
        annonceViewHolder.bind(annonce);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

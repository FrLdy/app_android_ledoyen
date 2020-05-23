package fac.app.activities;

import android.os.Bundle;

/**
 * Activity de départ, elle est lancée au démarrage de l'app.
 * **/

public class MainActivity extends ActivityWithMenu {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menu.displayListeAnnonces();
    }
}

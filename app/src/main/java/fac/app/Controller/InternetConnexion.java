package fac.app.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by francoisledoyen on 02/02/2018.
 * Gestion de la connexion internet
 */

public class InternetConnexion {
    protected ConnectivityManager connectivityManager;
    protected NetworkInfo networkInfo;

    public InternetConnexion(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
        this.networkInfo = this.connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Retourne l'état de la connexion internet**/
    public boolean testConnexion(){
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Retourne le type de connexion internet
     * cellulaire ou wifi**/
    public int getConnexionType(){
        if (this.testConnexion()){
            return this.networkInfo.getType();
        } else {
            return -1;
        }
    }

    /**
     * Affiche un toast indiquant que la connexion à internet nécessaire**/
    public void displayErrorInternet(Context context){
        Toast.makeText(context, "Erreur : vous n'avez pas d'accès internet", Toast.LENGTH_LONG).show();
    }
}

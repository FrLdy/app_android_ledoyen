package fac.app.model.annonce;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by francoisledoyen on 02/02/2018.
 * Représente une annonce
 */

public class Annonce implements Serializable{

    public static final String ANNONCE = "annonce";
    public String id;
    public String titre;
    public String description;
    public Integer prix;
    public String pseudo;
    public String emailContact;
    public String telContact;
    public String ville;
    public String cp;
    public ArrayList<String> images;
    public Integer date;

    public Annonce(String id, String titre, String description, Integer prix, String pseudo, String emailContact, String telContact, String ville, String cp, ArrayList<String> images, Integer date) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.pseudo = pseudo;
        this.emailContact = emailContact;
        this.telContact = telContact;
        this.ville = ville;
        this.cp = cp;
        this.images = images;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrix() {
        return prix;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public String getTelContact() {
        return telContact;
    }

    public String getVille() {
        return ville;
    }

    public String getCp() {
        return cp;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Integer getDate() {
        return date;
    }

    @Override
    public String toString() {
        return this.getImages().toString();
    }
}

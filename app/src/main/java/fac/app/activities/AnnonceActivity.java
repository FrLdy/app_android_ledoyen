package fac.app.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fac.app.R;
import fac.app.model.PreferencesUser;
import fac.app.model.annonce.Annonce;

public class AnnonceActivity extends ActivityWithMenu {

    private TextView textViewTitre;
    private ViewPager viewPager;
    private TextView textViewPrix;
    private TextView textViewLocalisation;
    private TextView textViewDescription;
    private TextView textViewDatePublication;
    private TextView textViewVendeur;
    private Button buttonMail;
    private Button buttonPhone;
    private Button buttonPartager;
    private Annonce annonce;
    private PreferencesUser preferencesUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce);
        getElementOfActivity();
        this.preferencesUser = new PreferencesUser(this);

        configureToolbar("Annonce");
        configureDrawerLayout();
        configureNavigationView();

        Intent intent = getIntent();
        this.annonce = (Annonce) intent.getSerializableExtra(Annonce.ANNONCE);
        this.setContent(this.annonce);
        this.setImageView();
        this.setOnclickListenerCall();
        this.setOnclickListenerMail();
        this.setOnclickListenerShare();
    }
    /**
     * initialiser tous les éléments de la vue
     * **/
    private void getElementOfActivity(){
        textViewTitre = findViewById(R.id.AnnonceTitre);
        textViewDatePublication = findViewById(R.id.AnnonceDatePublication);
        textViewVendeur = findViewById(R.id.AnnonceAnnonceur);
        textViewPrix = findViewById(R.id.AnnoncePrix);
        viewPager = findViewById(R.id.AnnonceImageGallery);
        textViewLocalisation = findViewById(R.id.AnnonceLocalisation);
        textViewDescription = findViewById(R.id.AnnonceDescription);
        buttonMail = findViewById(R.id.AnnonceBoutonMail);
        buttonPhone = findViewById(R.id.AnnonceBoutonPhone);
        buttonPartager = findViewById(R.id.AnnonceBoutonPartager);
    }

    /**
     * @param annonce l'annonce à afficher
     * Remplir les différents champs de la vue avec attributs d'une instance d'Annonce
     * **/
    public void setContent(Annonce annonce){
        this.textViewDescription.setText(annonce.getDescription());
        this.textViewTitre.setText(annonce.getTitre());
        this.textViewVendeur.setText("Vendeur : " + annonce.getPseudo());
        this.textViewDatePublication.setText(formateDate(annonce.getDate()));
        this.textViewLocalisation.setText(annonce.getCp()+ " " + annonce.getVille());
        this.textViewPrix.setText(annonce.getPrix().toString() + '€');
    }

    /**
     * Affiche l'image de l'annonce
     * **/
    public void setImageView(){
        // les images sont dans un slider géré par l'imageAdapter
        ImageAdapter imageAdapter = new ImageAdapter(this, this.annonce.getImages());
        this.viewPager.setAdapter(imageAdapter);
        // si l'annonce ne comporte qu'une image le slider n'est pas actif
        if (this.annonce.getImages().size()==1) {
            this.viewPager.setCurrentItem(0, false);
        }
    }

    /**
     * Permet ouvre l'intent mail avec un email type
     * **/
    public void sendMail(String message, Uri pj, String objet, String destinataire){
        String mail = "mailto:";

        if (destinataire != null){
            mail += destinataire;
        }

        mail += "?&subject=" + objet;
        mail += "&body=" + message;

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        if (pj != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, pj);
        }
        emailIntent.setData(Uri.parse(mail));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e){
            Toast.makeText(this, "L'application Mail n'est pas accessible", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * ouvre intent mail pour contacter le vendeur de l'annonce
     * **/
    public void contactVendorMail(){
        String body ="Bonjour, \n je suis intéressé par votre annonce.\n";
        body += "Vous pouvez me contacter par téléphone ou email : ";
        body += preferencesUser.getPhone() + " / " + preferencesUser.getMail();
        String objet = "[Annonce " + annonce.getTitre() + "]";
        sendMail(Uri.encode(body),null, Uri.encode(objet), Uri.encode(annonce.getEmailContact()));
    }

    /**
     * ouvre une intent dialogue avec le numéro de l'annonceur pour le contacter
     * **/
    public void call(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + this.annonce.getTelContact()));
        startActivity(intent);
    }

    /**
     * ouvre une intent mail pour partager l'annonce par email
     * **/
    public void shareMail(){
        String body = this.annonce.getTitre() + "\n";
        body += this.annonce.getPrix()+"€\n";
        body += "Vendeur : " + this.annonce.getPseudo() + "\n";
        body += "\t" + this.annonce.getTelContact();
        body += "\t" + this.annonce.getEmailContact();
        body += this.annonce.getVille() + " - " + this.annonce.getCp() + "\n";


        String objet = "Regarde cette annonce !";
        sendMail(Uri.encode(body),null, Uri.encode(objet), null);
    }

    /**
     * ouvre intent sms pour partager l'annonce par sms
     * **/
    public void shareSMS(){
        String body = this.annonce.getTitre() + "\n";
        body += this.annonce.getPrix()+"€\n";
        body += "Vendeur : " + this.annonce.getPseudo() + "\n";
        body += "\t" + this.annonce.getTelContact();
        body += "\t" + this.annonce.getEmailContact();
        body += this.annonce.getVille() + " - " + this.annonce.getCp() + "\n";
        sendSMS(body, null);
    }

    /**
     * ouvre une instance sms avec une sms préformaté
     * **/
    public void sendSMS(String message, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"));
        intent.putExtra("sms_body", message);
        if (attachment != null){
            intent.putExtra(Intent.EXTRA_STREAM, attachment);
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            Toast.makeText(this, "L'application SMS n'est pas accessible", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Ajoute un écouteur au bouton d'appel
     * **/
    private void setOnclickListenerCall(){
        this.buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
    }

    /**
     * Ajoute un écouteur au bouton mail
     * **/
    private void setOnclickListenerMail(){
        this.buttonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactVendorMail();
            }
        });
    }

    /**
     * ajoute un écouteur au bouton partager
     * ouvre une boite de dialogue : mail ou sms**/
    private void setOnclickListenerShare(){
        this.buttonPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogShare();
            }
        });
    }

    private void setDialogShare(){
        final CharSequence[] items = { "Mail", "SMS",
                "Annuler" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Partager");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Mail")) {
                    shareMail();
                } else if (items[item].equals("SMS")) {
                    shareSMS();
                } else if (items[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Formatage du timestamp
     * **/
    private String formateDate(Integer date){
        long timestamp = date.longValue();
        Date date1 = new Date(timestamp*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'Publié le 'dd/MM/yyyy '\nà' HH 'h' mm", Locale.FRANCE);
        return simpleDateFormat.format(date1);
    }

}

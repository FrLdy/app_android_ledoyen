package fac.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import fac.app.R;
import fac.app.model.PreferencesUser;
import fac.app.model.annonce.Annonce;
/**
 * Activité permettant de sauvegarder une annonce
 * **/
public class SaveAnnonceActivity extends ActivityWithMenu {

    private final static int REQUEST_IMAGE_CAPTURE = 2;
    private final static int RESULT_LOAD_IMAGE = 1;
    private TextView textViewTitre;
    private TextView textViewPrix;
    private TextView textViewDescription;
    private TextView textViewVille;
    private TextView textViewCP;
    private Button buttonSave;
    private Button buttonCancel;
    private Button buttonAddImage;
    private ImageView imageView;
    private String imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_annonce);
        getElementOfActivity();
        configureToolbar("Déposer une annonce");
        configureDrawerLayout();
        configureNavigationView();
        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.saveAnnonce(buildAnonce());
            }
        });

        this.buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    /**Démarre activité vers la galerie d'images de l'appareil**/
    protected void setGalleryIntent(){
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    /**
     * Démarre l'activité vers la caméra de l'appareil
     * **/
    protected void setCameraIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Traitement de l'image de la galery ou celle prise directement depuis l'app.
     * **/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                imageUri = getRealPathFromURI(this, selectedImage);
                this.imageView.setImageBitmap(BitmapFactory.decodeFile(imageUri));

            } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri tmp = getImageUri(getApplicationContext(), imageBitmap);
                imageUri = getRealPathFromURI(this, tmp);
                this.imageView.setImageBitmap(imageBitmap);
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("ERRRRROOOORRR", e.toString());
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Récupére la véritable adresse d'une image dans la mémoire de l'appareil
     * **/
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Retourne l'URI d'une image bitMap
     * **/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void getElementOfActivity(){
        buttonCancel = findViewById(R.id.saveCancel);
        buttonSave = findViewById(R.id.saveSend);
        buttonAddImage = findViewById(R.id.saveSelectImage);
        textViewTitre = findViewById(R.id.saveTitre);
        textViewPrix = findViewById(R.id.savePrix);
        textViewDescription = findViewById(R.id.saveDescription);
        textViewVille = findViewById(R.id.saveVille);
        textViewCP = findViewById(R.id.saveCP);
        imageView = findViewById(R.id.saveImageView);
        imageUri = null;
    }

    /**
     * retourne une instance d'objet avec les informations renseignées
     * **/
    private Annonce buildAnonce(){
        PreferencesUser preferencesUser = new PreferencesUser(this);
        String prix = this.textViewPrix.getText().toString();
        if (prix.equals("")){
            prix = "0";
        }
        ArrayList<String> images = new ArrayList<>();

        if (imageUri != null){
            images.add(imageUri);
        }

        Annonce annonce = new Annonce(
                null,
                this.textViewTitre.getText().toString(),
                this.textViewDescription.getText().toString(),
                Integer.parseInt(prix),
                preferencesUser.getPseudo(),
                preferencesUser.getMail(),
                preferencesUser.getPhone(),
                this.textViewVille.getText().toString(),
                this.textViewCP.getText().toString(),
                images,
                null);

        return annonce;
    }

    /**
     * Teste la permission d'accès pour le galerie d'images
     * **/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("PERMISSION","Permission: "+permissions[0]+ "was "+grantResults[0]);
            setGalleryIntent();
            //resume tasks needing this permission
        }
    }

    /**
     * Demande à l'utilisateur si l'accès aux images est autorisée
     * **/
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSIONS","Permission is granted");
                setGalleryIntent();
                return true;
            } else {

                Log.v("PERMISSIONS","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSIONS","Permission is granted");
            setGalleryIntent();
            return true;
        }
    }

    /**
     * Affiche la boite de dialogue pour ajouter une image**/
    private void selectImage() {
        final String galerie = "Depuis la galerie";
        final String camera = "Prendre une photo";
        final String annuler = "Annuler";
        final CharSequence[] items = { camera, galerie,
                annuler };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter une photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(camera)) {
                    setCameraIntent();
                } else if (items[item].equals(galerie)) {
                    isStoragePermissionGranted();
                } else if (items[item].equals(annuler)) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}

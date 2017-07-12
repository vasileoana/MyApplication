package com.google.android.myapplication.Activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.myapplication.DataBase.Files.ReadCategories;
import com.google.android.myapplication.DataBase.Files.ReadIngredients;
import com.google.android.myapplication.DataBase.Files.ReadRatings;
import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Model.Category;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListIngredients.ListViewAdapter;
import com.google.android.myapplication.Utilities.Ocr.OcrUtility;
import com.google.android.myapplication.Utilities.Ocr.SearchThread;


public class OcrActivity extends Activity {

    int REQUEST_CAMERA = 0, SELECT_FILE = 1, PIC_CROP=2;
    ImageButton btnSelect, btnShow, btnOcr;
    ImageView ivImage;
    static Bitmap bm = null;
    String userChosenTask;
    TextView scanResults, tvIndicatii;
    TextRecognizer detector;
    static final String LOG_TAG = "Text API";
    ArrayList<String> ingredients;
    int idUser = 0;
    String tipUtilizator;
    List<Category> categoryList;
    Uri picUri;
    RatingMethods ratingMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        ratingMethods = new RatingMethods();
        btnSelect = (ImageButton) findViewById(R.id.btnSelectPhoto);
        btnShow = (ImageButton) findViewById(R.id.btnShow);
        btnOcr = (ImageButton) findViewById(R.id.btnOcr);
        btnShow.setVisibility(View.GONE);
        btnOcr.setVisibility(View.GONE);
        btnSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);
        scanResults = (EditText) findViewById(R.id.results);
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        ingredients = new ArrayList<>();
        tipUtilizator = getIntent().getExtras().getString("tipUtilizator");
        if (!tipUtilizator.equals("anonim")) {

            idUser = getIntent().getExtras().getInt("userId");
        }
        tvIndicatii = (TextView) findViewById(R.id.tvIndicatii);

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case OcrUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Toast.makeText(this, "Permisiunea nu a fost acordata", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Realizeaza fotografie", "Alege din galerie",
                "Iesire"};

        AlertDialog.Builder builder = new AlertDialog.Builder(OcrActivity.this);
        builder.setTitle("Adauga fotografie!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = OcrUtility.checkPermission(OcrActivity.this);

                if (items[item].equals("Realizeaza fotografie")) {
                    userChosenTask = "Realizeaza fotografie";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Alege din galerie")) {
                    userChosenTask = "Alege din galerie";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Iesire")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if(requestCode == PIC_CROP){
//get the returned data
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                ivImage.setImageBitmap(thePic);
            }
        }
        btnOcr.setVisibility(View.VISIBLE);
        tvIndicatii.setText("Obtine textul din poza si valideaza-l!");
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        picUri = data.getData();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //performCrop();
        ivImage.setImageBitmap(thumbnail);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                picUri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
       // performCrop();

    }

    public void Ocr(View view) {
        Bitmap bitmap;
        if (ivImage.getDrawable() != null) {
            bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
            try {
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = detector.detect(frame);
                    String blocks = "";
                    String lines = "";
                    for (int index = 0; index < textBlocks.size(); index++) {
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue();
                        for (Text line : tBlock.getComponents()) {
                            String cratima = line.getValue().substring(line.getValue().length() - 1);

                            if (cratima.equals("-")) {
                                lines = lines + line.getValue().substring(0, line.getValue().length() - 2);
                            } else {
                                lines = lines + " "+ line.getValue();

                            }

                        }

                        tvIndicatii.setText("Aflati nota fiecarui ingredient!");
                    }
                    if (textBlocks.size() == 0) {
                        scanResults.setText("Incercati o poza mai clara!");
                    } else {
                        scanResults.setText(scanResults.getText() + lines + " ");

                    }
                } else {
                    scanResults.setText("Incercati cu o noua poza!");
                }
                scanResults.setVisibility(View.VISIBLE);
                btnShow.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }


    public void Show(View view) {
        String text = scanResults.getText().toString();
        //am facut un vector de cuvinte
        String[] vector = text.replaceAll("'", "").replaceAll("[()+]", " ").replaceAll("!", "l").replaceAll(" ", ",").split("[()#,.:;]");
        for (String ing : vector) {
            if (!ing.isEmpty())
                ingredients.add(ing.trim());
        }
        final Intent i = new Intent(getApplicationContext(), ListIngredientsActivity.class);
        i.putStringArrayListExtra("list", ingredients);
        if (!tipUtilizator.equals("anonim")) {
            i.putExtra("userId", idUser);
        }
        i.putExtra("tipUtilizator", tipUtilizator);
        startActivity(i);


    }

    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop

            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}

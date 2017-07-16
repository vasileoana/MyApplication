package com.google.android.myapplication.Activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

         int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);



            List<String> permissions = new ArrayList<String>();
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            }

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)  {


                } else {
                    Toast.makeText(this, "Permis denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void selectImage() {

        int REQUEST_CODE = 1;
        //ActivityCompat.requestPermissions(OcrActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
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
        File myFile = getFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(myFile));
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
                String path = "file:///sdcard/camera_app/imageFileCrop.jpg";
                Uri uri = Uri.parse(path);
                Bitmap bit = null;
                try {
                    bit = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivImage.setImageBitmap(bit);


            }
        }
        btnOcr.setVisibility(View.VISIBLE);
        tvIndicatii.setText("Obtine textul din poza si valideaza-l!");
    }

    private void onCaptureImageResult(Intent data) {
        //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

       //ivImage.setImageBitmap(thumbnail);
        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

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

        ivImage.setImageBitmap(thumbnail);
        //performCrop();*/
        String path = "file:///sdcard/camera_app/imageFile.jpg";
        /*Drawable dr = Drawable.createFromPath(path);
        ivImage.setImageDrawable(dr);*/
        Uri uri = Uri.parse(path);
        Bitmap bit = null;
        try {
            bit = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //bit.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        //ivImage.setImageBitmap(bit);
        performCrop(null);
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
        performCrop(picUri);

    }

    public void Ocr(View view) {
        Bitmap bitmap;
        Bitmap bitmap2 = null;
        if (ivImage.getDrawable() != null) {
            bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
            Uri uri = Uri.parse("file:///sdcard/camera_app/imageFile.jpg");
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (detector.isOperational() && bitmap2 != null) {
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

    private void performCrop(Uri uri){
        try {
            //call the standard crop action intent (the user device may not support it)
            /*Intent cropIntent = new Intent("com.android.camera.action.CROP");
            Uri uri = Uri.parse("file:///sdcard/camera_app/imageFile.jpg");
            //indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop

            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);*/

            Intent intent = new Intent("com.android.camera.action.CROP");
            Uri uriInitial = null;
            if (uri == null) {
                uriInitial = Uri.parse("file:///sdcard/camera_app/imageFile.jpg");
            } else {
                uriInitial = uri;
            }
            File myFile = getFileCrop();
            intent.setDataAndType(uriInitial, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(myFile));
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private File getFile() {
        File folder = new File("sdcard/camera_app");

        if (!folder.exists()) {
            folder.mkdir();
        }

        File imgFile = new File(folder, "imageFile.jpg");
        return imgFile;

    }

    private File getFileCrop() {
        File folder = new File("sdcard/camera_app");

        if (!folder.exists()) {
            folder.mkdir();
        }

        File imgFile = new File(folder, "imageFileCrop.jpg");
        return imgFile;

    }
}


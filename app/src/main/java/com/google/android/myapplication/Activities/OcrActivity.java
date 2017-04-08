package com.google.android.myapplication.Activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Ocr.OcrUtility;


public class OcrActivity extends Activity {

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect, btnShow;
    ImageView ivImage;
    static Bitmap bm = null;
    String userChosenTask;
    static Bitmap thumbnail;
    TextView scanResults;
    TextRecognizer detector;
    static final String LOG_TAG = "Text API";
    ArrayList<String> ingredients;
    int idUser = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnShow = (Button) findViewById(R.id.btnShow);
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
        idUser = getIntent().getExtras().getInt("userId");

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
                    //de compeltat
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(OcrActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = OcrUtility.checkPermission(OcrActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
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
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
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
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);

    }

    public void Ocr(View view) {
        Bitmap bitmap;
        bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
        try {
            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = detector.detect(frame);
                String blocks = "";
                String lines = "";
                String words = "";
                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue();
                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines = lines + line.getValue();
                        //  for (Text element : line.getComponents()) {
                        //extract scanned text words here
                        //   words = words + element.getValue() + ", ";
                        // }
                    }
                    scanResults.setVisibility(View.VISIBLE);
                }
                if (textBlocks.size() == 0) {
                    scanResults.setText("Scan Failed: Found nothing to scan");
                } else {
                    //  scanResults.setText(scanResults.getText() + "Blocks: " + "\n");
                    //  scanResults.setText(scanResults.getText() + blocks + "\n");
                    //  scanResults.setText(scanResults.getText() + "---------" + "\n");
                    // scanResults.setText(scanResults.getText() + "Lines: " + "\n");
                    scanResults.setText(scanResults.getText() + lines);
                    //  scanResults.setText(scanResults.getText() + "---------" + "\n");
                    // scanResults.setText(scanResults.getText() + "Words: " + "\n");
                    // scanResults.setText(scanResults.getText() + words + "\n");
                    // scanResults.setText(scanResults.getText() + "---------" + "\n");
                }
            } else {
                scanResults.setText("Could not set up the detector!");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                    .show();
            Log.e(LOG_TAG, e.toString());
        }

    }


    public void Show(View view) {
        String text = scanResults.getText().toString();
        String[] vector = text.split("[,.:;-]");
        for (String ing : vector) {
                ingredients.add(ing.replaceAll("'", " ").trim());
        }
        Intent i = new Intent(getApplicationContext(), ListIngredientsActivity.class);
        i.putStringArrayListExtra("list", ingredients);
        i.putExtra("userId", idUser);
        startActivity(i);
    }
}

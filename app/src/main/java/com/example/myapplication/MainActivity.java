package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private TessBaseAPI m_tess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageView();
        try {
            prepareLanguageDir();
            m_tess = new TessBaseAPI();
            m_tess.init(getFilesDir()+"", "vie");
        } catch (Exception e) {
            // Logging here
        }
    }

    private void initImageView() {
        ImageView imgView = (ImageView) findViewById(R.id.img_input);
        Bitmap input = BitmapFactory.decodeResource(getResources(), R.drawable.acb1);
        imgView.setImageBitmap(input);
    }
    // copy file from assets to another folder due to accessible
    private void copyFile() throws IOException {
        try {
            String filepath = getFilesDir()+ "/tessdata/vie.traineddata";
            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/vie.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }

            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareLanguageDir() throws IOException {
        File dir = new File(getFilesDir() + "/tessdata");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File trainedData = new File(getFilesDir() + "/tessdata/vie.traineddata");
        if (!trainedData.exists()) {
            copyFile();
        }
    }
    public void doRecognize(View view) {
        if (m_tess == null) {
            return;
        }
        try {
            //gán bitmap vào m_tess
            m_tess.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.acb1));
            String result = m_tess.getUTF8Text();
            TextView resultView = (TextView) findViewById(R.id.txt_result);
            resultView.setText(result);
        } catch (Exception e) {
            // Do what you like here...
        }
    }
}

package com.didi.android.tun2socks;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.didi.android.tun2socks.app.R;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NativeLib nativeLib = new NativeLib();
                String textDir = getApplication().getFilesDir().getAbsolutePath();
                File file = new File(textDir + "/sock_path");
                try {
                    if (!file.exists()) {
                        boolean ok = file.createNewFile();
                        Log.e("TEST","createNewFile() "+file.getAbsolutePath()+" is "+ok);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                String text = nativeLib.stringFromJNI(textDir);
//                test.setText((test.getText().toString() + text));
                nativeLib.test(getActivity());
            }
        });

    }

    private Activity getActivity() {
        return this;
    }
}
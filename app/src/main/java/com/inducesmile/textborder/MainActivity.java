package com.inducesmile.textborder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.inducesmile.textborder.custom.TextBorderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextBorderView textBorderView = (TextBorderView)findViewById(R.id.text_in_center);
        textBorderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textValue = textBorderView.getText().toString();
            }
        });
    }
}

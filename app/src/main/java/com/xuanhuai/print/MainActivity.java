package com.xuanhuai.print;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xuanhuai.print.cprint.SeachBluetooth;
import com.xuanhuai.print.htmlprint.HtmlMainActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn1,btn2,btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                startActivity(new Intent(this, SeachBluetooth.class));
                break;
            case R.id.button1:
                startActivity(new Intent(this, HtmlMainActivity.class));
                break;
            default:
                break;
        }
    }
}

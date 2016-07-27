package com.example.aplex.jnidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void sayHelloToc(View view){
        JniNative.sayHelloToC("Hello");
    }
    public void HelloFromC(View view){
        Toast.makeText(MainActivity.this, JniNative.HelloFromC(), Toast.LENGTH_SHORT).show();
    }
}

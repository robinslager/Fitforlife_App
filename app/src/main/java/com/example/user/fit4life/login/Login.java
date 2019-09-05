package com.example.user.fit4life.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.user.fit4life.R;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private Button loginbtn;
    private EditText passfield, emailfield;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // shows main activity
        setContentView(R.layout.activity_main);
        // making toolbar and not making it part of the app.
        // and making title color green
        Toolbar actionbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(actionbar);
        loginbtn = findViewById(R.id.Login_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        actionbar.setTitleTextColor(getColor(R.color.toolbar_title_color));
        actionbar.bringToFront();
        passfield = findViewById(R.id.login_password);
        emailfield = findViewById(R.id.login_email);
        loginbtn.setOnClickListener(loginevnt);
    }

    private View.OnClickListener loginevnt = new View.OnClickListener() {
        public void onClick(View v) {
            String email = emailfield.getText().toString();
            String pass = passfield.getText().toString();
            login_backend login = new login_backend(context, pass);


            if (email.isEmpty() || pass.isEmpty()) {
                if (email.isEmpty() && !pass.isEmpty()) {
                    emailfield.setHint("Please insert email");
                    passfield.setText(pass);
                } else if (!email.isEmpty() && pass.isEmpty()) {
                    emailfield.setText(email);
                    passfield.setHint("please insert password");
                } else {
                    emailfield.setHint("Please insert email");
                    passfield.setHint("please insert password");
                }
            } else {
                    login.execute(email, pass);
            }
        }




    };




}



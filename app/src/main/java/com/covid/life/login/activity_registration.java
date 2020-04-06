package com.covid.life.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.covid.life.MainActivity;
import com.covid.life.R;
import com.covid.life.form.activity_paciente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_registration extends AppCompatActivity {
    private EditText emailTextView, passwordTextView, txtNombres,txtApellidos,txtCedula;
    private CheckBox cbTerminos;
    private Button Btn;
    public ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private String[] datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        datos = new String[4];
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);

        txtCedula = findViewById(R.id.cedula);
        txtApellidos = findViewById(R.id.apellidos);
        txtNombres = findViewById(R.id.nombres);
        cbTerminos = findViewById(R.id.cbTerminos);

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(cbTerminos.isChecked())
                    registerNewUser();
                else {
                    Toast.makeText(getApplicationContext(),
                                    "¿Acepta los términos y condiciones?",
                            Toast.LENGTH_LONG)
                            .show();
                }


            }
        });

    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        if (TextUtils.isEmpty(txtCedula.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese su cedula !!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(txtNombres.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese sus nombres!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(txtApellidos.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese sus apeliidos!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese su email !!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese su contraseña !!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }


        progressbar.setVisibility(View.GONE);
        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Bienvenido !!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(activity_registration.this,
                                    activity_paciente.class);
                            intent.putExtra("datos",obtenerDatosRegistro());
                            startActivity(intent);
                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registro fallido !!"
                                            + " Intentalo mas tarde",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private String [] obtenerDatosRegistro(){
        datos[0] = txtCedula.getText().toString();
        datos[1] = txtNombres.getText().toString();
        datos[2] = txtApellidos.getText().toString();
        datos[3] = emailTextView.getText().toString();
        return datos;
    }





}

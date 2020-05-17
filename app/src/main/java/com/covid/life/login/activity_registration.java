package com.covid.life.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.MainActivity;
import com.covid.life.R;
import com.covid.life.form.activity_paciente;
import com.covid.life.menu.menu_pacientes;
import com.covid.life.models.Paciente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class activity_registration extends AppCompatActivity {
    private EditText emailTextView, passwordTextView, txtNombres,txtApellidos,txtCedula;
    private CheckBox cbTerminos;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private String[] datos;
    private String email, password;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Paciente paciente=new Paciente();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                /*Intent intent
                        = new Intent(activity_registration.this,
                        activity_paciente.class);
                startActivity(intent);*/
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


        email = emailTextView.getText().toString().trim();
        password = passwordTextView.getText().toString().trim();

        if (TextUtils.isEmpty(txtCedula.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese su numero de cédula  !!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(validarCedula(txtCedula.getText().toString())==FALSE){
            Toast.makeText(getApplicationContext(),
                    "Numero de cédula incorecta !!",
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
                    "Ingrese sus apellidos!!",
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

        progressbar.setVisibility(View.VISIBLE);
        Btn.setEnabled(false);
        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            guardarPreferencias();
                            guardarDatos();
                            Toast.makeText(getApplicationContext(),
                                    "Bienvenido !!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                            finish();
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Btn.setEnabled(true);
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
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

    private void guardarDatos(){
        paciente.setCorreo(email);
        paciente.setCedula(txtCedula.getText().toString());
        paciente.setNombres(txtNombres.getText().toString());
        paciente.setApellidos(txtApellidos.getText().toString());

        String uid = mAuth.getCurrentUser().getUid();
        db.collection("paciente")
                .document(uid)
                .set(paciente)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error: "+e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        progressbar.setVisibility(View.GONE);
                        return;
                    }
                });

    }

    private boolean validarCedula(String x){
        int suma = 0;
        if (x.length() == 9) {
            System.out.println("Ingrese su cedula de 10 digitos");
            return false;
        } else {
            int a[] = new int[x.length() / 2];
            int b[] = new int[(x.length() / 2)];
            int c = 0;
            int d = 1;
            for (int i = 0; i < x.length() / 2; i++) {
                a[i] = Integer.parseInt(String.valueOf(x.charAt(c)));
                c = c + 2;
                if (i < (x.length() / 2) - 1) {
                    b[i] = Integer.parseInt(String.valueOf(x.charAt(d)));
                    d = d + 2;
                }
            }
            for (int i = 0; i < a.length; i++) {
                a[i] = a[i] * 2;
                if (a[i] > 9) {
                    a[i] = a[i] - 9;
                }
                suma = suma + a[i] + b[i];
            }
            int aux = suma / 10;
            int dec = (aux + 1) * 10;
            if ((dec - suma) == Integer.parseInt(String.valueOf(x.charAt(x.length() - 1))))
                return true;
            else if (suma % 10 == 0 && x.charAt(x.length() - 1) == '0') {
                return true;
            } else {
                return false;
            }
        }
    }

    public void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);

        emailTextView.setText(email);
        passwordTextView.setText(password);

        editor.commit();
    }






}

package com.tesis.profesionesapp.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tesis.profesionesapp.R;
import com.tesis.profesionesapp.modelo.SetGetRegistroUsuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    EditText editTextEmail, editTextContraseña;
    TextView textViewRegistro, textViewRegistroSocio;
    Button btnInicio;
    SetGetRegistroUsuario aptrSetGetRegistro =new SetGetRegistroUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewRegistro = (TextView) findViewById(R.id.idRegistroLogin);
        textViewRegistro.setOnClickListener(this);

        textViewRegistroSocio = (TextView) findViewById(R.id.idRegistroLoginSocio);
        textViewRegistroSocio.setOnClickListener(this);

        btnInicio = (Button) findViewById(R.id.idBtnInicio);
        btnInicio.setOnClickListener(this);

        aptrSetGetRegistro.setPreguntaActual(10);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.idRegistroLogin:
                Intent intentContainerRegistro=new Intent(this,RegistroUsuarioActivity.class);
                startActivity(intentContainerRegistro);
                break;

            case R.id.idRegistroLoginSocio:
                Intent intentContainerRegistroSocio=new Intent(this,RegistroUsuarioActivity.class);
                intentContainerRegistroSocio.putExtra("pk_id_PreguntaActual",Integer.toString(aptrSetGetRegistro.getPreguntaActual()));
                startActivity(intentContainerRegistroSocio);
                break;
            case R.id.idBtnInicio:
                Intent intentContainerInicio=new Intent(this,InicioActivity.class);
                startActivity(intentContainerInicio);
                break;

        }

    }
}
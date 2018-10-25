package com.tesis.profesionesapp.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tesis.profesionesapp.R;
import com.tesis.profesionesapp.modelo.ConexionDB;
import com.tesis.profesionesapp.modelo.SetGetRegistroUsuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfesionesActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String pk_id_usuario=null;
    Spinner spinnerProfesiones;
    Button btnSiguienteProfesion;
    ArrayAdapter<String> comboAdapter;
    int banProfOtro=0;

    EditText profesionIngresada;
    Statement pstmt;

    SetGetRegistroUsuario aptrSetGetRegUsu=new SetGetRegistroUsuario();


    List<String> listaProfesionesSql = new ArrayList<String>();
    ConexionDB conn=new ConexionDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesiones);


        profesionIngresada=(EditText) findViewById(R.id.idEditTextNuevaProfesion);
        btnSiguienteProfesion=(Button) findViewById(R.id.idButtonSiguienteProfesion);
        btnSiguienteProfesion.setOnClickListener(this);
        profesionIngresada.setVisibility(View.INVISIBLE);




        Bundle pk_id_usuarioIntent = this.getIntent().getExtras();
        if(pk_id_usuarioIntent !=null){
            Log.i("Sesion", "sesion iniciada correctamente entro al if es diferente de null  ProfesionesActivity ............: "+pk_id_usuarioIntent.getString("pk_id_usuario"));
            pk_id_usuario = pk_id_usuarioIntent.getString("pk_id_usuario");
        }

        conexionProfesiones();
        spinnerProfesiones=(Spinner) findViewById(R.id.idSpinnerProfesiones);
        spinnerProfesiones.setOnItemSelectedListener(this);
        listaProfesionesSql.add("Otro");
        Collections.addAll(listaProfesionesSql);
        comboAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaProfesionesSql);
        spinnerProfesiones.setAdapter(comboAdapter);


    }

    public void conexionProfesiones()
    {
        try {
            String queryRes = "select prof_profesion from tbl_profesiones";
            pstmt = conn.conexionBD().createStatement();
            ResultSet resultado = pstmt.executeQuery(queryRes);
            while (resultado.next()) {
                listaProfesionesSql.add(resultado.getString(1));
            }

            int contador=0;
            while(contador<listaProfesionesSql.size())
            {
                Log.i("Sesion", "sesion iniciada correctamente entro al iterador  ............: "+contador+" : "+listaProfesionesSql.get(contador)+" --/-- ");
                contador++;
            }
        } catch (SQLException e) {
        }

    }

    @Override
    public void onClick(View v) {
        if(banProfOtro==1)
        {
            String profesion=profesionIngresada.getText().toString();
             if(profesion.isEmpty()||profesion.trim().equals(""))
             {
                 //reenfocar al campo vacio
                 profesionIngresada.setFocusable(true);
                 Toast.makeText(this, "Ingrese una profesión.", Toast.LENGTH_SHORT).show();
                 Log.i("Sesion", "sesion iniciada correctamente entro al iterador  ............: entro al if");
             }
             else
             {
                 //Inserccion de datos
                 Intent intentContainerProfesiones=new Intent(this,RegistroUsuarioActivity.class);
                 intentContainerProfesiones.putExtra("strProfesion",profesionIngresada.getText());
                 intentContainerProfesiones.putExtra("pk_id_PreguntaActual","6");
                 intentContainerProfesiones.putExtra("pk_id_usuarioProf",pk_id_usuario);

                 startActivity(intentContainerProfesiones);
             }
        }
        else if(banProfOtro==0)
        {
            //Inserccion de datos

            Intent intentContainerProfesiones=new Intent(this,RegistroUsuarioActivity.class);
            intentContainerProfesiones.putExtra("strProfesion",profesionIngresada.getText());
            intentContainerProfesiones.putExtra("pk_id_usuarioProf",pk_id_usuario);
            startActivity(intentContainerProfesiones);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String profesionSeleccionada = listaProfesionesSql.get(position);
        if(profesionSeleccionada.equals("Otro"))
        {
            profesionIngresada.setVisibility(View.VISIBLE);
            banProfOtro=1;
        }
        else
        {
            profesionIngresada.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Nombre profesion: " + profesionSeleccionada, Toast.LENGTH_SHORT).show();
            aptrSetGetRegUsu.setProfesionSeleccionada(profesionSeleccionada);
            banProfOtro=0;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

package com.tesis.profesionesapp.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class RegistroUsuarioActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ConexionDB conn=new ConexionDB();
    TextView pregunta;
    EditText etPassword,etPasswordConfirmacion;
    AutoCompleteTextView respuesta;
    Button siguiente;
    Spinner idSpinnerProfesion;
    ArrayAdapter<String> comboAdapter;
    List<String> listaProfesionesSql = new ArrayList<String>();
    int banProfOtro=0;

    SetGetRegistroUsuario aptrSetGetRegistro=new SetGetRegistroUsuario();
    String pk_id_PreguntaActual=null;
    String pk_id_usuarioProfe=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);



        Bundle pk_id_PreguntaActualIntent = this.getIntent().getExtras();
        if(pk_id_PreguntaActualIntent != null){
            Log.i("Sesion", "sesion iniciada correctamente entro al if es diferente de null  ProfesionesActivity ............: "+pk_id_PreguntaActualIntent.getString("pk_id_PreguntaActual"));
            pk_id_PreguntaActual = pk_id_PreguntaActualIntent.getString("pk_id_PreguntaActual");
            aptrSetGetRegistro.setPreguntaActual(Integer.parseInt(pk_id_PreguntaActual));

        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();//registro de permiso de acceso
        StrictMode.setThreadPolicy(policy);

        pregunta = (TextView) findViewById(R.id.idtextViewPregunta);
        respuesta = (AutoCompleteTextView) findViewById(R.id.idtextViewRespuesta);

        etPassword=(EditText) findViewById(R.id.idEditTextPassword);
        etPasswordConfirmacion=(EditText) findViewById(R.id.idEditTextPasswordVerificacion);

        siguiente = (Button) findViewById(R.id.idButtonSiguiente);
        siguiente.setOnClickListener(this);

        Log.i("Sesion", "sesion iniciada correctamente entro al if es diferente de null  entroooooooooooo ............: "+aptrSetGetRegistro.getPreguntaActual());
        Log.i("Sesion", "sesion iniciada correctamente  1");

        etPassword.setVisibility(View.GONE);
        etPasswordConfirmacion.setVisibility(View.GONE);

        PreguntaActual(aptrSetGetRegistro.getPreguntaActual());
        respuesta.requestFocus();

    }

    public void PreguntaActual(int preguntaActual) {
        Statement pstmt;
        try {
                if(preguntaActual==10)
                {
                    etPassword.setVisibility(View.VISIBLE);
                    etPasswordConfirmacion.setVisibility(View.VISIBLE);
                }
                else if(preguntaActual==11)
                {
                    consultaProfesiones();
                    idSpinnerProfesion=(Spinner) findViewById(R.id.idSpinnerProfesiones);
                    idSpinnerProfesion.setOnItemSelectedListener(this);
                    listaProfesionesSql.add("Otro");
                    Collections.addAll(listaProfesionesSql);
                    comboAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaProfesionesSql);
                    idSpinnerProfesion.setAdapter(comboAdapter);
                    idSpinnerProfesion.setVisibility(View.VISIBLE);

                }

                if(preguntaActual!=11){ respuesta.setVisibility(View.VISIBLE);}
               /* else if(preguntaActual==6)
                {
                    Bundle pk_id_UsuarioProfIntent = this.getIntent().getExtras();
                    if(pk_id_UsuarioProfIntent != null) {
                        pk_id_usuarioProfe = pk_id_UsuarioProfIntent.getString("pk_id_usuarioProf");
                        aptrSetGetRegistro.setPk_id_usuario(Integer.parseInt(pk_id_usuarioProfe));
                    }
                }
                */

            String queryRes = "Select pk_id_pregunta,pregunta,pregunta_siguiente from tbl_Registro WHERE pk_id_pregunta='"+preguntaActual+"'";
            pstmt = conn.conexionBD().createStatement();
            ResultSet resultado = pstmt.executeQuery(queryRes);
            if (resultado.next()) {
                aptrSetGetRegistro.setPreguntaActual(Integer.parseInt(resultado.getString(1)));
                pregunta.setText(resultado.getString(2));
                aptrSetGetRegistro.setIntPregSiguiente(Integer.parseInt(resultado.getString(3)));
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Error no existe", Toast.LENGTH_SHORT).show();
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////

    public void agregarRespuesta(int preguntaActual) throws java.sql.SQLException {

        Statement pstmt;
        String resulPassMD5=null;
        Log.i("Sesion", "sesion iniciada correctamente ..............................PreguntaActualAgregarRespuesta: "+preguntaActual);

        try {
            //Log.i("Sesion", "sesion iniciada correctamente  0: "+preguntaActual);

            if(preguntaActual==10) {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String insertDatos = "insert into tbl_usuario(usua_usuario)values('" + respuesta.getText().toString() + "')";
                pstmt.executeUpdate(insertDatos, Statement.RETURN_GENERATED_KEYS);
                ResultSet keyset = pstmt.getGeneratedKeys();
                if (keyset.next())
                {
                    aptrSetGetRegistro.setLastIdPregunta(Integer.parseInt(keyset.getString(1)));
                    respuesta.setText("");
                }
                aptrSetGetRegistro.setPk_id_usuario(aptrSetGetRegistro.getLastIdPregunta());
                String actualizaDatos="update tbl_usuario set usua_contrasena = '"  + etPassword.getText().toString()+"' where pk_id_usuario ="+aptrSetGetRegistro.getLastIdPregunta();
                pstmt.executeUpdate(actualizaDatos);

                String queryPasswordMD5 = "Select CONVERT(VARCHAR(32), HashBytes('MD5', '"+etPassword.getText().toString()+"'), 2) as MD5Hash";
                ResultSet resultadoPassMD5 = pstmt.executeQuery(queryPasswordMD5);
                if (resultadoPassMD5.next()) {
                    resulPassMD5=resultadoPassMD5.getString(1);
                }

                String actualizaDatosPassword="update tbl_usuario set usua_password =('"  + resulPassMD5 +"') where pk_id_usuario ="+aptrSetGetRegistro.getLastIdPregunta();
                Log.i("Sesion", "sesion iniciada correctamente ..............................actualizaDatosPassword: "+actualizaDatosPassword);
                pstmt.executeUpdate(actualizaDatosPassword);


                //////////////////////////// cambiar el valor de nivel al que le corresponda cuando el usuario haga login
                String actualizaDatosNivel="update tbl_usuario set usua_nivel = '"  + 2 +"' where pk_id_usuario ="+aptrSetGetRegistro.getLastIdPregunta();
                pstmt.executeUpdate(actualizaDatosNivel);

                String actualizaDatosCuentaActiva="update tbl_usuario set usua_cuenta_activa = '"  + 1 +"' where pk_id_usuario ="+aptrSetGetRegistro.getLastIdPregunta();
                pstmt.executeUpdate(actualizaDatosCuentaActiva);


                String insertDatosUsuario = "insert into tbl_usuario_datosPersonales(fk_usda_pk_id_usuario)values('" + aptrSetGetRegistro.getLastIdPregunta() + "')";
                pstmt.executeUpdate(insertDatosUsuario, Statement.RETURN_GENERATED_KEYS);
                ResultSet foreignkeyset = pstmt.getGeneratedKeys();
                if (foreignkeyset.next())
                {
                    aptrSetGetRegistro.setPk_id_usda(Integer.parseInt(foreignkeyset.getString(1)));
                }
                etPassword.setVisibility(View.GONE);
                etPasswordConfirmacion.setVisibility(View.GONE);
            }

            else if(preguntaActual==1) {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String insertDatos = "insert into tbl_DatosPersonales(dape_Nombre)values('" + respuesta.getText().toString() + "')";
                pstmt.executeUpdate(insertDatos, Statement.RETURN_GENERATED_KEYS);
                ResultSet keyset = pstmt.getGeneratedKeys();
                if (keyset.next())
                {
                    aptrSetGetRegistro.setLastIdPregunta(Integer.parseInt(keyset.getString(1)));
                    respuesta.setText("");
                }
                String actualizausuario_datosPersonales="update tbl_usuario_datosPersonales set fk_usda_pk_id_datosPersonales = '"  + aptrSetGetRegistro.getLastIdPregunta()+"' where pk_id_usda ="+aptrSetGetRegistro.getPk_id_usda();
                Log.i("Sesion", "sesion iniciada correctamente ..............................actualizausuario_datosPersonales___: "+actualizausuario_datosPersonales);
                pstmt.executeUpdate(actualizausuario_datosPersonales);

                String insertdatosPersonales_profesionales = "insert into tbl_datosPersonales_profesionales(fk_dapr_pk_id_datosPersonales)values('" + aptrSetGetRegistro.getLastIdPregunta() + "')";
                Log.i("Sesion", "sesion iniciada correctamente ..............................insertdatosPersonales_profesionales: "+insertdatosPersonales_profesionales);
                pstmt.executeUpdate(insertdatosPersonales_profesionales, Statement.RETURN_GENERATED_KEYS);
                ResultSet foreignkeyset = pstmt.getGeneratedKeys();
                if (foreignkeyset.next())
                {
                    aptrSetGetRegistro.setPk_id_dapr(Integer.parseInt(foreignkeyset.getString(1)));
                }

                String insertdatosPersonales_consultorio = "insert into tbl_datosPersonales_consultorio(fk_daco_pk_id_datosPersonales)values('" + aptrSetGetRegistro.getLastIdPregunta() + "')";
                pstmt.executeUpdate(insertdatosPersonales_consultorio, Statement.RETURN_GENERATED_KEYS);
                ResultSet foreignkeyset_daco = pstmt.getGeneratedKeys();
                if (foreignkeyset_daco.next())
                {
                    aptrSetGetRegistro.setPk_id_daco(Integer.parseInt(foreignkeyset_daco.getString(1)));
                }
            }
            else if(preguntaActual==2)
            {
                pstmt = (Statement) conn.conexionBD().createStatement();

                String actualizaDatos="update tbl_DatosPersonales set dape_ApellidoPaterno = '"  + respuesta.getText().toString()+"' where pk_id_datosPersonales ="+aptrSetGetRegistro.getLastIdPregunta();
                pstmt.executeUpdate(actualizaDatos);
                Toast.makeText(this, "Exito", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Agredado Exito", Toast.LENGTH_SHORT).show();
                respuesta.setText("");
            }
            else if(preguntaActual==3)
            {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String actualizaDatos="update tbl_DatosPersonales set dape_ApellidoMaterno = '"  + respuesta.getText().toString()+"' where pk_id_datosPersonales ="+aptrSetGetRegistro.getLastIdPregunta();
                pstmt.executeUpdate(actualizaDatos);
                Toast.makeText(this, "Exito", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Agredado con Exito", Toast.LENGTH_SHORT).show();
                respuesta.setText("");
            }
            else if(preguntaActual==4)
            {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String actualizaDatos="update tbl_DatosPersonales set dape_correo = '"  + respuesta.getText().toString()+"' where pk_id_datosPersonales ="+aptrSetGetRegistro.getLastIdPregunta();
                pstmt.executeUpdate(actualizaDatos);
                Toast.makeText(getApplicationContext(), "Agredado con Exito", Toast.LENGTH_SHORT).show();
                respuesta.setText("");
            }
            else if(preguntaActual==5)
            {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String actualizaDatos="update tbl_DatosPersonales set dape_numeroCelular = '"  + respuesta.getText().toString()+"' where pk_id_datosPersonales ="+aptrSetGetRegistro.getLastIdPregunta();
                Log.i("Sesion", "sesion iniciada correctamente  0............: "+actualizaDatos);
                pstmt.executeUpdate(actualizaDatos);
                Toast.makeText(getApplicationContext(), "Agredado con Exito", Toast.LENGTH_SHORT).show();
                respuesta.setText("");
            }

            if(preguntaActual==6) {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String insertDatos = "insert into tbl_DatosProfesionales(dapr_NoCedula)values('" + respuesta.getText().toString() + "')";
                pstmt.executeUpdate(insertDatos, Statement.RETURN_GENERATED_KEYS);
                ResultSet keyset = pstmt.getGeneratedKeys();
                if (keyset.next())
                {
                    aptrSetGetRegistro.setPk_id_daprofe(Integer.parseInt(keyset.getString(1)));
                    respuesta.setText("");
                }
                String actualizaDatosPersonlaesProfesionales="update tbl_datosPersonales_profesionales set fk_dapr_pk_id_datosProfesionales = '"  + aptrSetGetRegistro.getPk_id_daprofe()+"' where pk_id_dapr ="+aptrSetGetRegistro.getPk_id_dapr();
                Log.i("Sesion", "sesion iniciada correctamente ..............................actualizaDatosPersonlaesProfesionales: "+actualizaDatosPersonlaesProfesionales);
                pstmt.executeUpdate(actualizaDatosPersonlaesProfesionales);

                String actualizaDatosProfesionales_profesiones="update tbl_datosProfesionales_profesiones set fk_prpr_pk_id_datosProfesionales = '"  + aptrSetGetRegistro.getPk_id_daprofe()+"' where pk_id_prpr ="+aptrSetGetRegistro.getPk_id_prpr();
                Log.i("Sesion", "sesion iniciada correctamente ..............................actualizaDatosProfesionales_profesiones: "+actualizaDatosProfesionales_profesiones);
                pstmt.executeUpdate(actualizaDatosProfesionales_profesiones);
            }

            else if(preguntaActual==7)
            {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String actualizaDatos="update tbl_DatosProfesionales set dapr_Cv = '"  + respuesta.getText().toString()+"' where pk_id_datosProfesionales ="+aptrSetGetRegistro.getPk_id_daprofe();
                Log.i("Sesion", "sesion iniciada correctamente  0............: "+actualizaDatos);
                pstmt.executeUpdate(actualizaDatos);
                Toast.makeText(getApplicationContext(), "Agredado con Exito", Toast.LENGTH_SHORT).show();
                respuesta.setText("");
            }
            if(preguntaActual==8) {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String insertDatos = "insert into tbl_DatosConsultorio(daco_numeroParticular)values('" + respuesta.getText().toString() + "')";
                pstmt.executeUpdate(insertDatos, Statement.RETURN_GENERATED_KEYS);
                ResultSet keyset = pstmt.getGeneratedKeys();
                if (keyset.next())
                {
                    aptrSetGetRegistro.setLastIdPregunta(Integer.parseInt(keyset.getString(1)));
                    respuesta.setText("");
                }

                String actualizaDatosPersonlaesConsultorio="update tbl_datosPersonales_consultorio set fk_daco_pk_id_datosConsultorio = '"  + aptrSetGetRegistro.getLastIdPregunta()+"' where pk_id_daco ="+aptrSetGetRegistro.getPk_id_daco();
                Log.i("Sesion", "sesion iniciada correctamente ..............................actualizaDatosPersonlaesConsultorio: "+actualizaDatosPersonlaesConsultorio);
                pstmt.executeUpdate(actualizaDatosPersonlaesConsultorio);
            }

            else if(preguntaActual==9)
            {
                pstmt = (Statement) conn.conexionBD().createStatement();
                String actualizaDatos="update tbl_DatosConsultorio set daco_direccion = '"  + respuesta.getText().toString()+"' where pk_id_datosConsultorio ="+aptrSetGetRegistro.getLastIdPregunta();
                Log.i("Sesion", "sesion iniciada correctamente  0............: "+actualizaDatos);
                pstmt.executeUpdate(actualizaDatos);
                Toast.makeText(getApplicationContext(), "Agredado con Exito", Toast.LENGTH_SHORT).show();
                respuesta.setText("");
            }
            else if(preguntaActual==11)
            {
                if(banProfOtro==1)
                {
                    String profesion=respuesta.getText().toString();
                    if(profesion.isEmpty()||profesion.trim().equals(""))
                    {
                        //reenfocar al campo vacio
                        respuesta.setFocusable(true);
                        Toast.makeText(this, "Ingrese una profesión.", Toast.LENGTH_SHORT).show();
                        Log.i("Sesion", "sesion iniciada correctamente entro al iterador  ............: otroooooooooooooooo esta vaciio profesion");
                    }
                    else
                    {
                        //Inserccion de datos
                        pstmt = (Statement) conn.conexionBD().createStatement();
                        String insertDatosProfesiones = "insert into tbl_profesiones(prof_profesion)values('" + respuesta.getText().toString() + "')";
                        pstmt.executeUpdate(insertDatosProfesiones, Statement.RETURN_GENERATED_KEYS);
                        ResultSet keyset = pstmt.getGeneratedKeys();
                        if (keyset.next())
                        {
                            aptrSetGetRegistro.setLastIdPregunta(Integer.parseInt(keyset.getString(1)));
                            respuesta.setText("");
                        }

                        String insertDatosProfesionales_profesiones = "insert into tbl_datosProfesionales_profesiones(fk_prpr_pk_id_profesion)values('" +aptrSetGetRegistro.getLastIdPregunta()+ "')";
                        pstmt.executeUpdate(insertDatosProfesionales_profesiones, Statement.RETURN_GENERATED_KEYS);
                        ResultSet keysetprpr = pstmt.getGeneratedKeys();
                        if (keysetprpr.next())
                        {
                            aptrSetGetRegistro.setPk_id_prpr(Integer.parseInt(keysetprpr.getString(1)));
                        }

                    }
                }
                else
                {
                    int profesionSeleccionada=0;
                    String queryProfesion = "select pk_id_profesion from tbl_profesiones where prof_profesion='"+aptrSetGetRegistro.getProfesionSeleccionada()+"'";
                    pstmt = conn.conexionBD().createStatement();
                    ResultSet resultadoProfesion = pstmt.executeQuery(queryProfesion);
                    if (resultadoProfesion.next()) {
                        profesionSeleccionada=Integer.parseInt(resultadoProfesion.getString(1));
                    }

                    String insertDatosProfesionales_profesiones = "insert into tbl_datosProfesionales_profesiones(fk_prpr_pk_id_profesion)values('" +profesionSeleccionada+ "')";
                    pstmt.executeUpdate(insertDatosProfesionales_profesiones, Statement.RETURN_GENERATED_KEYS);
                    ResultSet keysetprpr = pstmt.getGeneratedKeys();
                    if (keysetprpr.next())
                    {
                        aptrSetGetRegistro.setPk_id_prpr(Integer.parseInt(keysetprpr.getString(1)));
                    }


                }



                idSpinnerProfesion.setVisibility(View.GONE);
                respuesta.setText("");
                //limpiar




            }
            else if(preguntaActual==-1)
            {


                Toast.makeText(getApplicationContext(), "Termino de registrarse bienvenido a su plataforma.", Toast.LENGTH_SHORT).show();

                Intent intentContainerInicio=new Intent(this,InicioActivity.class);
                Log.i("Sesion", "sesion iniciada correctamente entro a 0 en inicio enviar valor intent  ............: "+aptrSetGetRegistro.getPk_id_usuario());
                intentContainerInicio.putExtra("pk_id_usuario",Integer.toString(aptrSetGetRegistro.getPk_id_usuario()));
                startActivity(intentContainerInicio);
            }


        } catch (SQLException e) {

            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultaProfesiones()
    {
        Statement pstmt;
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
        try {
            Log.i("Sesion", "sesion iniciada correctamente ..............................PreguntaActual Onclick: "+aptrSetGetRegistro.getIntPregSiguiente());


            agregarRespuesta(aptrSetGetRegistro.getPreguntaActual());
            if(aptrSetGetRegistro.getIntPregSiguiente()!=-1){
                PreguntaActual(aptrSetGetRegistro.getIntPregSiguiente());}
            else{
                aptrSetGetRegistro.setPreguntaActual(-1);
                agregarRespuesta(aptrSetGetRegistro.getPreguntaActual());}

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String profesionSeleccionada = listaProfesionesSql.get(position);
        if(profesionSeleccionada.equals("Otro"))
        {
            respuesta.setVisibility(View.VISIBLE);
            banProfOtro=1;
        }
        else
        {
            respuesta.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Nombre profesion: " + profesionSeleccionada, Toast.LENGTH_SHORT).show();
            aptrSetGetRegistro.setProfesionSeleccionada(profesionSeleccionada);
            banProfOtro=0;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////




}

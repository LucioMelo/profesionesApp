package com.tesis.profesionesapp.controlador;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tesis.profesionesapp.R;
import com.tesis.profesionesapp.modelo.ConexionDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InicioActivity extends AppCompatActivity {
    TextView nombreUsuario,idTextViewProfesion,idtextViewUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        String pk_id_usuario=null;

        nombreUsuario=(TextView) findViewById(R.id.id_textView_nombreUsuario);
        idTextViewProfesion=(TextView) findViewById(R.id.idTextViewProfesion);
        idtextViewUsuario=(TextView) findViewById(R.id.idtextViewUsuario);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            Log.i("Sesion", "sesion iniciada correctamente entro al if es diferente de null  ............: "+parametros.getString("pk_id_usuario"));
            pk_id_usuario = parametros.getString("pk_id_usuario");
            nombreUsuario.setText("Usuario: "+pk_id_usuario);
        }

        ConexionDB conn=new ConexionDB();

        Statement pstmt;
        try {
            /*String queryRes = "SELECT usua_usuario,dape_nombre,dapr_NoCedula,prof_profesion from tbl_usuario usua \n" +
                    "INNER JOIN tbl_usuario_datosPersonales usda on usua.pk_id_usuario=usda.fk_usda_pk_id_usuario\n" +
                    "INNER JOIN tbl_DatosPersonales dape on dape.pk_id_datosPersonales=usda.fk_usda_pk_id_datosPersonales\n" +
                    "INNER JOIN tbl_datosPersonales_profesionales dapr on dapr.fk_dapr_pk_id_datosPersonales=dape.pk_id_datosPersonales\n" +
                    "inner JOIN tbl_DatosProfesionales dapro on dapro.pk_id_datosProfesionales=dapr.fk_dapr_pk_id_datosProfesionales\n" +
                    "INNER JOIN tbl_datosProfesionales_profesiones prpr on prpr.fk_prpr_pk_id_datosProfesionales=dapro.pk_id_datosProfesionales\n" +
                    "INNER JOIN tbl_profesiones prof on prof.pk_id_profesion=prpr.fk_prpr_pk_id_profesion\n" +
                    "WHERE usua.pk_id_usuario='"+pk_id_usuario+"'";

                    */


            String queryRes = "SELECT usua_usuario,dape_nombre,dapr_NoCedula from tbl_usuario usua \n" +
                    "INNER JOIN tbl_usuario_datosPersonales usda on usua.pk_id_usuario=usda.fk_usda_pk_id_usuario\n" +
                    "INNER JOIN tbl_DatosPersonales dape on dape.pk_id_datosPersonales=usda.fk_usda_pk_id_datosPersonales\n" +
                    "INNER JOIN tbl_datosPersonales_profesionales dapr on dapr.fk_dapr_pk_id_datosPersonales=dape.pk_id_datosPersonales\n" +
                    "inner JOIN tbl_DatosProfesionales dapro on dapro.pk_id_datosProfesionales=dapr.fk_dapr_pk_id_datosProfesionales\n" +
                    "WHERE usua.pk_id_usuario='"+pk_id_usuario+"'";


            pstmt = conn.conexionBD().createStatement();
            ResultSet resultado = pstmt.executeQuery(queryRes);
            if (resultado.next()) {
                nombreUsuario.setText("Bienvenido: "+resultado.getString(1));
                idtextViewUsuario.setText(resultado.getString(2));
                idTextViewProfesion.setText(resultado.getString(3));
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Error no existe", Toast.LENGTH_SHORT).show();
        }

    }

}


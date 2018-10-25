package com.tesis.profesionesapp.modelo;

/**
 * Created by Lucio on 16/10/2018.
 */

public class SetGetRegistroUsuario {
    int intPregSiguiente;
    int preguntaActual;
    int lastIdPregunta;
    int pk_id_usda;
    int pk_id_dapr;
    int pk_id_daco;
    int pk_id_usuario;
    int pk_id_prpr;
    int pk_id_daprofe;
    String profesionSeleccionada;

    public int getPk_id_daprofe() {
        return pk_id_daprofe;
    }

    public void setPk_id_daprofe(int pk_id_daprofe) {
        this.pk_id_daprofe = pk_id_daprofe;
    }

    public int getPk_id_prpr() {
        return pk_id_prpr;
    }

    public void setPk_id_prpr(int pk_id_prpr) {
        this.pk_id_prpr = pk_id_prpr;
    }



    public String getProfesionSeleccionada() {return profesionSeleccionada;}
    public void setProfesionSeleccionada(String profesionSeleccionada) {this.profesionSeleccionada = profesionSeleccionada;}

    public int getPk_id_usuario() {return pk_id_usuario;}
    public void setPk_id_usuario(int pk_id_usuario) {this.pk_id_usuario = pk_id_usuario;}

    public int getPk_id_daco() {return pk_id_daco;}
    public void setPk_id_daco(int pk_id_daco) {this.pk_id_daco = pk_id_daco;}

    public int getPk_id_dapr() {return pk_id_dapr;}
    public void setPk_id_dapr(int pk_id_dapr) {this.pk_id_dapr = pk_id_dapr;}

    public int getPk_id_usda() {return pk_id_usda;}
    public void setPk_id_usda(int pk_id_usda) {this.pk_id_usda = pk_id_usda;}

    public int getLastIdPregunta() {return lastIdPregunta;}
    public void setLastIdPregunta(int lastIdPregunta) {this.lastIdPregunta = lastIdPregunta;}

    public int getPreguntaActual(){return preguntaActual;}
    public void setPreguntaActual(int preguntaActual) {
        this.preguntaActual = preguntaActual;
    }

    public int getIntPregSiguiente() {
        return intPregSiguiente;
    }
    public void setIntPregSiguiente(int intPregSiguiente) {this.intPregSiguiente = intPregSiguiente;}




}
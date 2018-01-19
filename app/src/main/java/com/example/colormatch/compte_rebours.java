package com.example.colormatch;

/**
 * Created by soumah on 28/12/2015.
 */
public class compte_rebours {

    private long val; // valeur en milliseconde du timer
    private long timer; // valeur du timer en milliseconde en fonction de quand est lancé le timer
    private long temps_pause; // sauvegarde du temps restant du timer pour les pauses

    public compte_rebours(int k){ // convertis le temps passé en paramètre
        val = k*1000;
    }

    public void start(){ // lancement du timer
        timer = System.currentTimeMillis() + val; // on récupère le temps présent en milliseconde à qui on ajoute la durée du timer
    }

    public boolean finish(){ // on vérifie si le temps présent n'a pas dépassé le timer
        return System.currentTimeMillis() > timer ? true : false;
    }

    public long temps(){ // retourne le temps restant en milliseconde
        return (timer - System.currentTimeMillis());
    }

    public void pause(){ // sauvegarde le temps restant
        temps_pause = temps();
    }

    public void resume(){ // on récupère le temps présent en milliseconde à qui on ajoute le temps restant (pour les sorties de pause)
        timer = System.currentTimeMillis() + temps_pause;
    }

    public void m_case(int d){// soustraire du temps de la mauvaise case au temps du compte à rebours
        timer -= d*1000;
    }
}



package com.example.colormatch;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class ColorMatch {
	private int Tab[][];  // grille du jeu
	private int Points[]; // les différentes valeur de points
	private int point_Joueur; // le nombre de points que le joueur a accumulé
	private int nombre_Blanc = 0; // nombre de case vide au départ
	
	public ColorMatch(Context contex){
		Resources res = contex.getResources();
		Tab = new int[10][14];
		Points = new int[5];
		nombre_Blanc = res.getInteger(R.integer.nombre_de_blanc);
		point_Joueur = 0;
		Points[0] = 0;
		Points[1] = 0;
		Points[2] = res.getInteger(R.integer.paire);
		Points[3] = res.getInteger(R.integer.triplet);
		Points[4] = res.getInteger(R.integer.quadruplet);
		init_Tab();
	}
	public boolean c_ident(int x, int y){ // vérifie si oui ou non il y a des couleurs identiques
		int c_case[] = new int[8]; // chaque case correspond à une couleurs
		c_case[color_MoinX(x, y)[2]]++;
		c_case[color_PlusX(x, y)[2]]++;
		c_case[color_MoinY(x, y)[2]]++;
		c_case[color_PlusY(x, y)[2]]++;
		for(int i = 1; i < 8; i++) //on parcourt le tableau pour vérifier si une couleur est à plus d'un exemplaire
			if(c_case[i] > 1) return true; // si oui on retourne true
		return false;
	}
	
	public boolean tableau_Fin(){ // vérifie si le tableau est encore jouable
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 14; y++){
				if(c_ident(x, y)){
					return false;
				}
			}
		}
		return true;
	}
	
	public int supp_case(int x, int y){ // supprime les couleurs en paire, triplet et quadruplet autoure du point (x,y)
		int res = 0;
		if(Tab[x][y] == 0){
			int c_case[][] = new int[4][3]; // tableau qui permet ede sauvegarder les coordonnées et la valeur des 4 cases autoure du point (x,y)
			int c_case2[] = new int[8]; //  chaque case correspond à une couleurs
			c_case[0] = color_MoinX(x, y); // on récupère les coordonées et la couleurs sur l'axe - X
			c_case[1] = color_PlusX(x, y); // on récupère les coordonées et la couleurs sur l'axe + X
			c_case[2] = color_MoinY(x, y); // on récupère les coordonées et la couleurs sur l'axe - Y
			c_case[3] = color_PlusY(x, y); // on récupère les coordonées et la couleurs sur l'axe + Y
			c_case2[c_case[0][2]]++; // on incrémente la couleur de la 1ere case
			c_case2[c_case[1][2]]++; // on incrémente la couleur de la 2eme case
			c_case2[c_case[2][2]]++; // on incrémente la couleur de la 3eme case
			c_case2[c_case[3][2]]++; // on incrémente la couleur de la 4eme case
			for(int i = 1; i < 8; i++) // on parcourt le tableau de couleurs
				if(c_case2[i] > 1){ // si une couleur est représentée plus d'une fois on les supprimes
					point_Joueur += Points[c_case2[i]]; // on incrémente le nombre de points du joueur
					for(int z = 0 ; z < 4; z++) // on les supprimes
						if(c_case[z][2] == i){
							Tab[c_case[z][0]][c_case[z][1]] = 0;
							res++;
						}	
				}
		}
		Log.e("Nombres de points", ""+point_Joueur);
		return res;
	}
	
	private int[] color_MoinX(int x, int y){// récupère les coordonées et la couleurs sur l'axe - X
		int c_case[] = new int[3];
		x--;
		while(x >= 0){
			if(Tab[x][y] != 0){
				c_case[0] = x;
				c_case[1] = y;
				c_case[2] = Tab[x][y];
				return c_case;
			}
			x--;
		}
		return c_case;
	}
	private int[] color_PlusX(int x, int y){ // récupère les coordonées et la couleurs sur l'axe + X
		int c_case[] = new int[3];
		x++;
		while(x < 10){
			if(Tab[x][y] != 0){
				c_case[0] = x;
				c_case[1] = y;
				c_case[2] = Tab[x][y];
				return c_case;
			}
			x++;
		}
		return c_case;
	}
	private int[] color_MoinY(int x, int y){// récupère les coordonées et la couleurs sur l'axe - Y
		int c_case[] = new int[3];
		y--;
		while(y >= 0){
			if(Tab[x][y] != 0){
				c_case[0] = x;
				c_case[1] = y;
				c_case[2] = Tab[x][y];
				return c_case;
			}
			y--;
		}
		return c_case;
	}
	private int[] color_PlusY(int x, int y){ // récupère les coordonées et la couleurs sur l'axe + Y
		int c_case[] = new int [3];
		y++;
		while(y < 14){
			if(Tab[x][y] != 0){
				c_case[0] = x;
				c_case[1] = y;
				c_case[2] = Tab[x][y];
				return c_case;
			}
			y++;
		}
		return c_case;
	}
	public void init_Tab(){ // initialisation de la grille
		ArrayList<int[]> c_case = new ArrayList<int[]>(); // liste qui va contenir tous les vides possibles pour le début de partie
		int c_case2[] = new int[2];
		for(int y = 0 ; y < 14; y++) // remplissage de la grille 
			for(int x = 0; x < 10; x++)
				Tab[x][y] = (int) ((Math.random() * 7) + 1);
		for(int y = 0 ; y < 14; y++){ // on récupère tous les vides possible
			for(int x = 0; x < 10; x++){
				if(c_ident(x,y)) {
					c_case.add(new int[]{x,y});
				}
			}
		}
		for(int i = 0; i < nombre_Blanc; i++){ // on en supprime "nombreDeBlanc"
			int z = (int) ((Math.random() * c_case.size()-1));
			c_case2 = c_case.get(z);
			Tab[c_case2[0]][c_case2[1]] = 0;
			c_case.remove(z);
		}
	}
	
	public int get_Tab(int x, int y){
		return Tab[x][y];
	}
	public void Affiche_Tab(){
		for(int y = 0 ; y < 14; y++){
			for(int x = 0; x < 10; x++){
				System.out.print(Tab[x][y] + " ");
			}
			System.out.println();
		}
	}
	public int getPoints(){
		return point_Joueur;
	} //retourne le nombre de points cumulés par le joueur

	public void Renitialise_Points(){
		point_Joueur = 0;
	}  //remet à zero les points gagné par le joueur
}

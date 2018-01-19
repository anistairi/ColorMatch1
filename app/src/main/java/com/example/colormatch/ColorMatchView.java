package com.example.colormatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
public class ColorMatchView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

	private MediaPlayer loop = null;
	private Paint pain;
	private Bitmap[] img_colors; //tableau qui contient les images des différentes couleurs
	private Bitmap[] color_BackGround; // tableau qui contient les images des différentes couleurs du fond de la grille
	private Bitmap top_background; // image du dessus de la grille
	private Canvas canv = null;
	private Resources res_ColorsMatch;
	private Context color_Context;
	private Thread thread;
	private SurfaceHolder holder;
	private ColorMatch colorMatch;
	private int img;
	private int border_left; // taille de la bordure de gauche
	private int border_top; // taille de la bordure du haut
	private int m_case = 0; // m_case quand on clique que une mauvaise case
	private static int border_color = 4; // bordure entre les cases de la grille et la couleur
	private compte_rebours t; // classe pour gérer le temps
	private boolean pause  = false; // indique si le jeu est en pause
	private boolean fini = false; // permet d'indiquer quand le thread doit sortire de sa boucle
	private boolean pause_Son = false;
	

	
	public ColorMatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.e(">>> ColorMatchView", " ColorMatchView() : begin");
		
		holder = getHolder();
		holder.addCallback(this);
		
		color_Context  = context;
		res_ColorsMatch = color_Context.getResources();
		colorMatch   = new ColorMatch(color_Context);
		m_case = res_ColorsMatch.getInteger(R.integer.m_caseTemps);
		thread = new Thread(this);
		t = new compte_rebours(res_ColorsMatch.getInteger(R.integer.timeGame)); // création de la class compte_rebours avec comme paramètre le temps en seconde du compte_rebours
		setFocusable(true);
		Log.e(">>> ColorMatchView", " ColorMatchView() : end");
	}
	
	public void refresh(Canvas canv){
		int ll = 0; // permet un décalage des couleurs de la grille
		for(int y = 0; y < 14 ; y++){ // double boucles qui parcourt la grille 
			for(int x = 0; x < 10; x++){
				canv.drawBitmap(color_BackGround[ll%2],img*x+border_left,img*y+border_top,null); // dessine les cases de la grille
				if(colorMatch.get_Tab(x, y) != 0){ // si la case est non vide, dessine la couleurs qui correspond à la valeur de la case
					canv.drawBitmap(img_colors[colorMatch.get_Tab(x, y) -1],(img*x)+border_left+border_color,(img*y)+border_color+border_top,null);
				}
				ll++;
			}
		ll++;
		}
	}
	
	public void init(){
		Log.e(">>> ColorMatchView", "init() : begin");
		initData();
		loadImage();
		pain.setTextSize(border_top);
		pain.setColor(Color.WHITE);
		runthread();
		
		Log.e(">>> ColorMatchView", "init() : end");
	}
	
	public void runthread(){
		if(thread != null){ 
			fini = true; // termine le thread en cours
			while(thread.isAlive()){}
		}
		if ((thread!=null) && (!thread.isAlive())) { 
			thread = new Thread(this);
			if(pause) // si l'application est considérée comme étant en pause on relance le compte_rebours
				t.resume();
			else // sinon on reset le compte_rebours
				t.start();
			fini = false; // on met à false la condition d'arrêt de la boucle du run
			pause = false; // on est plus en pause
			thread.start();

			
		}
	
	}
	
	void initData(){
		int imgw = getWidth() / 10; // taille des cases en fonction de la largeur l'écran 
		int imgh = getHeight() / 14; // taille des cases en fonction de la hauteur de l'écran
		img = imgw < imgh ? imgw : imgh; // on prend la plus petite valeur
		border_left = (getWidth() - (img * 10))/2; // bordure à gauche de l'écran
		border_top = getHeight() - img*14; // bordure en haut de l'écran
	}
	
	public void loadImage(){ // chargement des images
		Log.i(">>> ColorMatchView", "loadImage() : begin");
		Bitmap color = BitmapFactory.decodeResource(res_ColorsMatch, R.drawable.img_colors);
		Bitmap init_colors = BitmapFactory.decodeResource(res_ColorsMatch, R.drawable.init_colors);
		Bitmap cbt =  BitmapFactory.decodeResource(res_ColorsMatch, R.drawable.top_background);
		color = Bitmap.createScaledBitmap(color, (img*7)-border_color*7*2, img-(border_color*2), true); // on redimensionne l'image
		init_colors= Bitmap.createScaledBitmap(init_colors, img*2, img, true); // on redimensionne l'image
		top_background  = Bitmap.createScaledBitmap(cbt,getWidth(),border_top,true);
		for(int i = 0; i < 7; i++){
			img_colors[i] = Bitmap.createBitmap(color,i*color.getWidth()/7,0,color.getWidth()/7,color.getWidth()/7); // on découpe l'image dans le tableau colors[]
		}
		color_BackGround[0] = Bitmap.createBitmap(init_colors,0*init_colors.getWidth()/2,0,init_colors.getWidth()/2,init_colors.getWidth()/2);//on découpe l'image
		color_BackGround[1] = Bitmap.createBitmap(init_colors,1*init_colors.getWidth()/2,0,init_colors.getWidth()/2,init_colors.getWidth()/2);//on découpe l'image
		Log.i(">>> ColorMatchView", "loadImage() : end");
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub		
		init();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		img_colors = new Bitmap[7];
		color_BackGround = new Bitmap[2];
		pain = new Paint();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub	
		Log.e(">>> run", "start");
		canv = null;
		while(!fini){
			try {
				Thread.sleep(40);	
				try{
					canv = holder.lockCanvas(null);
					refresh(canv); // on dessine la grille
					canv.drawBitmap(top_background,0,0,null); // on dessine le score et le temps
					canv.drawText(" "+(int)(t.temps()/1000), 0, border_top-10, pain);
					canv.drawText(colorMatch.getPoints()+" ", getWidth()/2, border_top-10, pain);
				}
				finally{
					if(canv != null)
						holder.unlockCanvasAndPost(canv);
				}
				} catch (InterruptedException e) {
					//Log.e("-> RUN <-", "PB DANS RUN : " + e.getMessage());
				}	
			if(t.finish() || colorMatch.tableau_Fin()){ // fin de game quand le temps est écoulé ou que la grille n'a plus de solution
				finGame();
			}
		}
		Log.e(">>> run", "stop");
	}
	
	public void stopthread(){ // pour stopper le thread 
		fini = true;
		pause = true;
		t.pause();
	}
	
	public void finGame(){
		Log.e(">>> finGame", "start");
		fini = true;
		pause = true;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if(event.getX() > border_left && event.getX() < (border_left+img*10) && event.getY() > border_top && !pause){
			Log.i(">>> ColorMatchView"," onTouchEvent");
			int x = (int)((event.getX()-border_left)/img); // on récupère les coordonnées x et y
			int y = (int)(event.getY()-border_top)/img;
			Log.i(">>> ColorMatchView"," x : y (" + x + " : " + y + ")");			
			if(colorMatch.supp_case(x, y) > 0){ // on supprime si possible les couleurs qui faut
				jouerson(R.raw.beep1); // si des couleurs on été supprimées on lance le son numéro 1
			}
			else{
				jouerson(R.raw.beep2); // si aucune des couleurs a été supprimées on lance le son numéro 2
				t.m_case(m_case); // on met un m_case de temps au compte_rebours
			}
		}
		else if(pause){ // si l'application est en pause et que l'on clique sur l'écran un popup souvre
			popup();
		}
		return super.onTouchEvent(event);
	}
	
	public void regame(){ // relance une partie
		stopthread(); // arrêt du thread en cours
		colorMatch.init_Tab(); // nouvelle grille de jeu
		colorMatch.Renitialise_Points(); // on reset les points
		pause = false; // on désactive la pause (permet aussi de reset le compte_rebours dans le run)
		runthread(); // on lance un nouveau thread
		
	}
	public void leavepause(){ // pour quitter la pause on lance un nouveau thread
		runthread(); 
	}
	public void pause(){ // pour mettre une pause on stop le thread en cours et on indique au compte_rebours qu'il doit sauvegarder son état
		stopthread();
		t.pause();
	}
	
	public void resumeGame(){ // appelée de l'activity pour indique qu'il y a eu pause
		pause = true;
	}
	
	public boolean thread_Stop(){
		return thread.isAlive() ? false : true;
	}
	
	public void jouerson(int i){ 
		if(!pause_Son){
		if(loop != null) {
	        loop.stop();
	        loop.release();
	    }
	    loop = MediaPlayer.create(color_Context, i);
	    loop.start();
		}
	}
	
	public void pause_son(){ // active et désactive le son
		pause_Son = !pause_Son;
	}
	public void popup(){
		AlertDialog.Builder p = new AlertDialog.Builder(color_Context);
		p.setMessage("Vous avez fait " + colorMatch.getPoints() + " points !");
		p.setCancelable(true);
		p.setPositiveButton("Recommencer", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				regame();
			}
		} );
		AlertDialog alert = p.create();
		alert.show();
	}
	
}




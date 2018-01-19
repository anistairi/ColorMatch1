package com.example.colormatch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MenuActivity extends Activity {
	private MenuActivity menu_activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		menu_activity = this;
		
		final View btn1 = findViewById(R.id.button1);
		final View btn2 = findViewById(R.id.button2);
		
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent g = new Intent(menu_activity, com.example.colormatch.Activity_jeu.class);
				startActivity(g);
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu_activity.finish();
			}
		});
		
		
	}
}

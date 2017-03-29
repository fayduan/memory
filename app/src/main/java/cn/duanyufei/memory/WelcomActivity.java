package cn.duanyufei.memory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import cn.duanyufei.util.UpdateTask;

public class WelcomActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);          
        setContentView(R.layout.activity_welcome);

        new UpdateTask(this).update();

        new Handler().postDelayed(new Runnable() {            
            @Override  
            public void run() {  
                Intent intent = new Intent(WelcomActivity.this, MainActivity.class);  
                startActivity(intent);  
                WelcomActivity.this.finish();  
            }  
        }, 700);  
	}
	
}

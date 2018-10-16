package ir.pepotec.app.bluetoothremote;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActivityLuncher extends AppCompatActivity {

    LinearLayout img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);
        img9 = findViewById(R.id.img9);
        img10 = findViewById(R.id.img10);

        (findViewById(R.id.luncher)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startAnimation();
            }
        });

        startAnimation();
    }

    private void startAnimation() {

        img1.setScaleX(0);
        img1.setScaleY(0);
        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(img1,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(img1,"scaleY",0,1.5f,1);
        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(scaleX1,scaleY1);

        img2.setScaleX(0);
        img2.setScaleY(0);
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(img2,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(img2,"scaleY",0,1.5f,1);
        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(scaleX2,scaleY2);

        img3.setScaleX(0);
        img3.setScaleY(0);
        ObjectAnimator scaleX3 = ObjectAnimator.ofFloat(img3,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY3 = ObjectAnimator.ofFloat(img3,"scaleY",0,1.5f,1);
        AnimatorSet set3 = new AnimatorSet();
        set3.playTogether(scaleX3,scaleY3);

        img4.setScaleX(0);
        img4.setScaleY(0);
        ObjectAnimator scaleX4 = ObjectAnimator.ofFloat(img4,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY4 = ObjectAnimator.ofFloat(img4,"scaleY",0,1.5f,1);
        AnimatorSet set4 = new AnimatorSet();
        set4.playTogether(scaleX4,scaleY4);

        img5.setScaleX(0);
        img5.setScaleY(0);
        ObjectAnimator scaleX5 = ObjectAnimator.ofFloat(img5,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY5 = ObjectAnimator.ofFloat(img5,"scaleY",0,1.5f,1);
        AnimatorSet set5 = new AnimatorSet();
        set5.playTogether(scaleX5,scaleY5);

        img6.setScaleX(0);
        img6.setScaleY(0);
        ObjectAnimator scaleX6 = ObjectAnimator.ofFloat(img6,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY6 = ObjectAnimator.ofFloat(img6,"scaleY",0,1.5f,1);
        AnimatorSet set6 = new AnimatorSet();
        set6.playTogether(scaleX6,scaleY6);

        img7.setScaleX(0);
        img7.setScaleY(0);
        ObjectAnimator scaleX7 = ObjectAnimator.ofFloat(img7,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY7 = ObjectAnimator.ofFloat(img7,"scaleY",0,1.5f,1);
        AnimatorSet set7 = new AnimatorSet();
        set7.playTogether(scaleX7,scaleY7);

        img8.setScaleX(0);
        img8.setScaleY(0);
        ObjectAnimator scaleX8 = ObjectAnimator.ofFloat(img8,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY8 = ObjectAnimator.ofFloat(img8,"scaleY",0,1.5f,1);
        AnimatorSet set8 = new AnimatorSet();
        set8.playTogether(scaleX8,scaleY8);

        img9.setScaleX(0);
        img9.setScaleY(0);
        ObjectAnimator scaleX9 = ObjectAnimator.ofFloat(img9,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY9 = ObjectAnimator.ofFloat(img9,"scaleY",0,1.5f,1);
        AnimatorSet set9 = new AnimatorSet();
        set9.playTogether(scaleX9,scaleY9);

        img10.setScaleX(0);
        img10.setScaleY(0);
        ObjectAnimator scaleX10 = ObjectAnimator.ofFloat(img10,"scaleX",0,1.5f,1);
        ObjectAnimator scaleY10 = ObjectAnimator.ofFloat(img10,"scaleY",0,1.5f,1);
        AnimatorSet set10= new AnimatorSet();
        set10.playTogether(scaleX10,scaleY10);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(set1,set2,set3,set4,set5,set6,set7,set8,set9,set10);

        animatorSet.setStartDelay(500);

        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ActivityLuncher.this.finish();
                startActivity(new Intent(ActivityLuncher.this,ActivityMain.class));
            }
        });

    }
}

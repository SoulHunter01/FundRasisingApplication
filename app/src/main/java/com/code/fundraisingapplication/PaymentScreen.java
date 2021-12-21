package com.code.fundraisingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PaymentScreen extends AppCompatActivity {
    TextView title_of_goal;
    TextView target_of_goal;
    EditText YourContribution;
    Button paynow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        Intent intent=getIntent();
        String title=intent.getStringExtra("Title");
        String target=intent.getStringExtra("Target");
        title_of_goal=findViewById(R.id.title_of_goal);
        title_of_goal.setText(title);
        target_of_goal=findViewById(R.id.targetamount_of_goal);
        target_of_goal.setText(target);
        YourContribution=findViewById(R.id.YourContribution);
        paynow=findViewById(R.id.paynow);


        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("GoalInformation")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String getTitle= (String) document.getData().get("Title");

                                        if(getTitle.equals(title) ){
                                                int target_int=Integer.parseInt(target_of_goal.getText().toString());
                                                int contribution=Integer.parseInt(YourContribution.getText().toString());
                                                int amount=target_int-contribution;
                                                db.collection("GoalInformation").document(document.getId())
                                                        .update("TargetAmount",String.valueOf(amount));

                                                target_of_goal.setText(String.valueOf(amount));

                                                Intent intent1=new Intent(PaymentScreen.this,RecyclerViewSpecificItem.class);
                                                setResult(RESULT_OK,intent1);
                                                finish();

                                        }


                                    }
                                } else {
                                    Log.w("TAGXAXAXAXA", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });





    }
}
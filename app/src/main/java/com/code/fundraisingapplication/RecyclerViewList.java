package com.code.fundraisingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "XA1212";
    RecyclerView rv;
    ArrayList<Upload> ls;
    EditText sh;
    Spinner spinner;
    public static String[] paths = {"All","Education","Climate Change","Medical Support"};

    public static String goalname;
    RecyclerViewAdapter adapter;
    RecyclerViewAdapter adapter1;
    private DatabaseReference mDatabaseRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_list);
        rv=findViewById(R.id.rv);
        ls=new ArrayList<>();
        spinner = (Spinner)findViewById(R.id.spinner);
        sh = (EditText) findViewById(R.id.sh);
        sh.setText(goalname);

        sh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    filter(editable.toString());
            }
        });

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paths));



        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);




        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ls.clear();
                for(DataSnapshot postSnapshot:snapshot.getChildren()){
                        Upload upload=postSnapshot.getValue(Upload.class);
                        ls.add(upload);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(RecyclerViewList.this, 2);
                        rv.setLayoutManager(mLayoutManager);
                        adapter = new RecyclerViewAdapter(ls, RecyclerViewList.this);
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecyclerViewList.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });









    }

    private void filter(String text) {
        ArrayList<Upload> filteredList = new ArrayList<>();

        for (Upload item : ls) {
            if (item.getmName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    private void filter2(String text) {
        ArrayList<Upload> filteredList = new ArrayList<>();

        for (Upload item : ls) {
            if (item.getmCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter1 = new RecyclerViewAdapter(ls,RecyclerViewList.this);
        rv.setAdapter(adapter1);
        adapter1.filterList(filteredList);
        adapter1.notifyDataSetChanged();
    }

    private void filter3(String text) {
        ArrayList<Upload> filteredList = new ArrayList<>();

        for (Upload item : ls) {
                filteredList.add(item);
        }

        adapter1 = new RecyclerViewAdapter(ls,RecyclerViewList.this);
        rv.setAdapter(adapter1);
        adapter1.filterList(filteredList);
        adapter1.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       // Toast.makeText(RecyclerViewList.this,paths[i],Toast.LENGTH_LONG).show();
//                if (i == 0) {
//                    filter2("Education");
//                } else if (i == 1) {
//                    filter2("Climate Change");
//                } else if (i == 3) {
//                    filter2("Medical Support");
//                }
        if (paths[i] != "All") {
            filter2(paths[i]);
        } else {
            filter3(paths[i]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
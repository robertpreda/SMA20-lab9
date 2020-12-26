package com.upt.cti.smartwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

// lab is deprecated

public class ListActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private int currentMonth;
    private List<Payment> paymentList = new ArrayList<>();

    private TextView tStatus;
    private Button bPrevious, bNext;
    private FloatingActionButton fabAdd;
    private ListView listPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        tStatus = findViewById(R.id.tStatus);
        bPrevious = findViewById(R.id.bPrevious);
        bNext = findViewById(R.id.bNext);
        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPaymentDialogActivity.class);
                AppState.get().setCurrentPayment(null);

                startActivity(intent);
            }
        });


        listPayments = findViewById(R.id.listPayments);
        final PaymentAdapter adapter = new PaymentAdapter(this, R.layout.item_payment, paymentList); // crapa asta, copy paste din lab
        listPayments.setAdapter(adapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        listPayments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppState.get().setCurrentPayment(paymentList.get(position));
                startActivity(new Intent(getApplicationContext(), AddPaymentDialogActivity.class));
            }
        });

        currentMonth = Month.monthFromTimestamp(AppState.getCurrentTimeDate());


        databaseReference = database.getReference();
        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    if(currentMonth == Month.monthFromTimestamp(snapshot.getKey())){
                        Payment payment = snapshot.getValue(Payment.class);
                        payment.timestamp = snapshot.getKey();

                        paymentList.add(payment);
                        adapter.notifyDataSetChanged();

                        tStatus.setText("Found " + paymentList.size() + " payments for " + Month.intToMonthName(currentMonth));

                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public enum Month{
        January, February, March, April, May, June, July, August, September, October, November, December;
        public static int monthNameToInt(Month month){
            return month.ordinal();

        }
        public static Month intToMonthName(int index){
            return Month.values()[index];
        }
        public static int monthFromTimestamp(String timestamp){
            int month = Integer.parseInt(timestamp.substring(5,7));
            return month - 1;
        }
    }

//    public void fabClicked(View view){
//
//    }

    public void clicked(View view){
        switch(view.getId()){
            case R.id.bNext:
                currentMonth += 1;
                currentMonth %= 12; // so it remains in [0, 11]
                break;
            case R.id.bPrevious:
                currentMonth -= 1;
                currentMonth %= 12;
                break;
        }
    }
}
package com.example.fyp.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.Models.NewOrderModel;
import com.example.fyp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class NewOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewOrder.this));
        databaseReference= FirebaseDatabase.getInstance().getReference("ConfirmOrder");

    }
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerOptions<NewOrderModel> options=new FirebaseRecyclerOptions.Builder<NewOrderModel>().setQuery(databaseReference,NewOrderModel.class).build();
        FirebaseRecyclerAdapter<NewOrderModel,OrderViewHolder> adapter=new FirebaseRecyclerAdapter<NewOrderModel, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull NewOrderModel newOrderModel) {
                orderViewHolder.name.setText(newOrderModel.getCname());
                orderViewHolder.emailuser.setText(newOrderModel.getEmail());
                orderViewHolder.phone.setText(newOrderModel.getPhoneNum());
                orderViewHolder.address.setText(newOrderModel.getAddress());
                orderViewHolder.txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(NewOrder.this,NewOrderProduct.class);
                        startActivity(intent);
                    }
                });
                orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(NewOrder.this);
                        builder.setTitle("Have you shipped this Product ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i==0){
                                    //String uid=databaseReference.getKey();
                                    RemoveOrder();
                                }
                                else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_cardview,parent,false);
                return new OrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void RemoveOrder(){
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid).removeValue();
    }

}
class OrderViewHolder extends RecyclerView.ViewHolder{
    TextView name,emailuser,phone,address;
    TextView txt;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        name=(TextView) itemView.findViewById(R.id.name_user);
        emailuser=(TextView) itemView.findViewById(R.id.email_order);
        phone=(TextView) itemView.findViewById(R.id.phoneNum);
        address=(TextView) itemView.findViewById(R.id.address_order);
        txt=(TextView) itemView.findViewById(R.id.btn_new);
    }
}

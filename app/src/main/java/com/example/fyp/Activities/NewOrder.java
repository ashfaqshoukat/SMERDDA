package com.example.fyp.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class NewOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ORDERINFO,OrderViewHolder> adapter = null;
    DatabaseReference databaseReference,databaseReference2;
    TextView orderdetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewOrder.this));
        databaseReference= FirebaseDatabase.getInstance().getReference("Order").child(FirebaseAuth.getInstance().getUid());

    }
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerOptions<ORDERINFO> options=new FirebaseRecyclerOptions.Builder<ORDERINFO>().setQuery(databaseReference,ORDERINFO.class).build();

        adapter=new FirebaseRecyclerAdapter<ORDERINFO, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull ORDERINFO newOrderModel) {
                if(newOrderModel.getStatus()!=3) {
                    orderViewHolder.name.setText(newOrderModel.getUserFName());
                    orderViewHolder.emailuser.setText(newOrderModel.getUserEmail());
                    orderViewHolder.phone.setText(newOrderModel.getUserPhoneNbr());
                    orderViewHolder.address.setText(newOrderModel.getUserAddress());
                    orderViewHolder.txt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        Intent intent=new Intent(NewOrder.this,NewOrderProduct.class);
//                        startActivity(intent);
                        }
                    });
                    orderViewHolder.ordeDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NewOrder.this, OrderDetail.class);
                            intent.putExtra("orderinfo", new Gson().toJson(newOrderModel));
                            startActivity(intent);

                        }
                    });
                }

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
    TextView txt,ordeDetail;
    CardView cardView;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        name=(TextView) itemView.findViewById(R.id.name_user);
        emailuser=(TextView) itemView.findViewById(R.id.email_order);
        phone=(TextView) itemView.findViewById(R.id.phoneNum);
        address=(TextView) itemView.findViewById(R.id.address_order);
        txt=(TextView) itemView.findViewById(R.id.btn_new);
        ordeDetail=itemView.findViewById(R.id.orderdetail);
        cardView=itemView.findViewById(R.id.cardview);
    }
}

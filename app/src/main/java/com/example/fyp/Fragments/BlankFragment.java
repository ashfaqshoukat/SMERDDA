package com.example.fyp.Fragments;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fyp.Activities.NewOrder;
import com.example.fyp.Activities.OrderDetail;
import com.example.fyp.Adapters.NotificatioListAdapter;
import com.example.fyp.Adapters.ViewHolder;
import com.example.fyp.Models.NOTIFICATIONS;
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    FirebaseRecyclerAdapter<NOTIFICATIONS, ViewHolder> adapter = null;
    private RecyclerView recyclerView;
    private List<NOTIFICATIONS> notificationsList=new ArrayList<>();
    private  DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Notification").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    public BlankFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        getAllNotification();
        return view;
    }


    private  void getAllNotification(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationsList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    NOTIFICATIONS notifications=dataSnapshot1.getValue(NOTIFICATIONS.class);
                    notificationsList.add(notifications);
                }

                setUpRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new NotificatioListAdapter(getContext(),notificationsList));
    }

    private void fetch() {
        FirebaseRecyclerOptions<NOTIFICATIONS> options=new FirebaseRecyclerOptions.Builder<NOTIFICATIONS>().setQuery(databaseReference,NOTIFICATIONS.class).build();

        adapter=new FirebaseRecyclerAdapter<NOTIFICATIONS, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull NOTIFICATIONS model) {

                holder.setTxtTitle(model.getTitle());
              holder.setImage(model.getImage());
              holder.setTxtDate(model.getTime());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_cardview,parent,false);
                return new ViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
//        DatabaseReference query = FirebaseDatabase.getInstance()
//                .getReference("Notification")
//                .child(FirebaseAuth.getInstance().getUid());
//
//        FirebaseRecyclerOptions<NOTIFICATIONS> options =new FirebaseRecyclerOptions.Builder<NOTIFICATIONS>().setQuery(query,NOTIFICATIONS.class).build();
//        adapter = new FirebaseRecyclerAdapter<NOTIFICATIONS, ViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(ViewHolder holder, final int position, NOTIFICATIONS model) {
//              holder.setTxtTitle(model.getTitle());
//              holder.setImage(model.getImage());
//              holder.setTxtDate(model.getTime());
//                holder.root.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_item, parent, false);
//
//                return new ViewHolder(view);
//            }
//
//
//
//        };
//        recyclerView.setAdapter(adapter);
    }


}

package com.example.fyp.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp.Adapters.CompanyAdapter;
import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    RecyclerView recyclerView;
    CompanyAdapter companyAdapter;
    ArrayList<COMPANYINFO> COMPANYINFOList;

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        COMPANYINFOList = new ArrayList<>();
        getAllUsers();
        return view;
    }


    private void getAllUsers() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CompanyInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                COMPANYINFOList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    COMPANYINFO companyInfo = dataSnapshot1.getValue(COMPANYINFO.class);
                    companyInfo.setCompanyId(dataSnapshot1.getKey());
                    COMPANYINFOList.add(companyInfo);
                    //if (companyInfo.getId().equals(firebaseUser.getUid())){
                    //COMPANYINFOList.add(companyInfo);
                    // }


                }
                companyAdapter = new CompanyAdapter(getActivity(), COMPANYINFOList);
                recyclerView.setAdapter(companyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
/*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.item1){
            Toast.makeText(getActivity(),"Setting",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.item2){
            Toast.makeText(getActivity(),"About",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }*/
}

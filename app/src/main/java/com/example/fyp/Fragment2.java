package com.example.fyp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {


    private COMMENT comment;
    double rating=0;
    AdapterUsers adapterUsers;

    private List<Rating> ratingList=new ArrayList<>();
    private List<CompanyInfo> companyInfos=new ArrayList<>();
    private RecyclerView recyclerView;
    List<CompanyInfo> companyInfoList = new ArrayList<>();


    public Fragment2() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment2, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getAllCompanyRating();
        return  view;
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

    //5
    //4

    int count=0;
    private  void getAllCompanyRating() {
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot comapny:dataSnapshot.child("Feedback").getChildren()){
                    for(DataSnapshot eachCompany:comapny.getChildren()){
                        comment=eachCompany.getValue(COMMENT.class);
                        rating=rating+comment.getRating();
                        count++;
                    }

                    double avrrating=rating/count;
                    Rating rating1=new Rating();
                    rating1.setName(comment.getComapanyId());
                    rating1.setRating(avrrating);
                    ratingList.add(rating1);
                    count=0;
                }
                if(ratingList!=null && ratingList.size()>0)
                setUpRecyclerView(ratingList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerView(List<Rating> list) {

        Collections.sort(list, new Comparator<Rating>() {
            @Override


            public int compare(Rating c1, Rating c2) {
                if (c1.getRating() <c2.getRating()) return 1;
                if (c1.getRating() > c2.getRating()) return -1;
                return 0;
            }});

            FirebaseDatabase.getInstance().getReference().child("CompanyInfo").child(list.get(0).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    companyInfoList.add(dataSnapshot.getValue(CompanyInfo.class));
                    adapterUsers = new AdapterUsers(getActivity(), companyInfoList);
                    recyclerView.setAdapter(adapterUsers);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }

}

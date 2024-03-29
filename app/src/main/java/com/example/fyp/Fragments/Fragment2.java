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
import com.example.fyp.Models.COMMENT;
import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.Models.RATING;
import com.example.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {


    private COMMENT comment;
    double rating = 0;
    CompanyAdapter companyAdapter;

    private List<RATING> ratingList = new ArrayList<>();
    private List<COMPANYINFO> COMPANYINFOS = new ArrayList<>();
    private List<COMPANYINFO> companyList = new ArrayList<>();

    private RecyclerView recyclerView;
    List<COMPANYINFO> COMPANYINFOList = new ArrayList<>();


    public Fragment2() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
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


    int count = 0;

    private void getAllCompanyRating() {
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ratingList.clear();
                COMPANYINFOList.clear();
                for (DataSnapshot comapny : dataSnapshot.child("Feedback").getChildren()) {
                    for (DataSnapshot eachCompany : comapny.getChildren()) {
                        comment = eachCompany.getValue(COMMENT.class);
                        rating = rating + comment.getRating();
                        count++;
                    }

                    double avrrating = rating / count;
                    RATING rating1 = new RATING();
                    rating1.setName(comment.getComapanyId());
                    rating1.setRating(avrrating);
                    ratingList.add(rating1);
                    count = 0;
                    rating = 0;
                }
                if (ratingList != null && ratingList.size() > 0)
                    setUpRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllCompany() {
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companyList.clear();
                COMPANYINFOList.clear();
                for (DataSnapshot comapny : dataSnapshot.child("CompanyInfo").getChildren()) {
                    COMPANYINFO info=comapny.getValue(COMPANYINFO.class);
                    info.setCompanyId(comapny.getKey());
                    companyList.add(info);
                }
                setUpRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerView() {

        Collections.sort(ratingList, new Comparator<RATING>() {
            @Override


            public int compare(RATING c1, RATING c2) {
                if (c1.getRating() < c2.getRating()) return 1;
                if (c1.getRating() > c2.getRating()) return -1;
                return 0;
            }
        });

        for (int i = 0; i < ratingList.size(); i++) {
            for (int j = 0; j < companyList.size(); j++) {
                if(ratingList.get(i).getName().equalsIgnoreCase(companyList.get(j).getCompanyId())){
                    COMPANYINFOList.add(companyList.get(j));
                    break;
                }
            }
        }


        companyAdapter = new CompanyAdapter(getActivity(), COMPANYINFOList);
        recyclerView.setAdapter(companyAdapter);



    }

    @Override
    public void onResume() {
        super.onResume();
        getAllCompanyRating();
        getAllCompany();

    }
}

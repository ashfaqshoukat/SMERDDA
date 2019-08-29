package com.example.fyp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.Activities.CustomerOrder;
import com.example.fyp.Activities.OrderDetail;

import com.example.fyp.Activities.ShopActivity;
import com.example.fyp.Models.GALLERY;
import com.example.fyp.Models.ORDERINFO;
import com.example.fyp.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private Context mContext;
    private ArrayList<ORDERINFO> mGallery;
    public OrderListAdapter(Context context, List<ORDERINFO> galleryList){
        mContext=context;
        mGallery= (ArrayList<ORDERINFO>) galleryList;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_order_item,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int position) {
        ORDERINFO newOrderModel=mGallery.get(position);
        orderViewHolder.productname.setText(newOrderModel.getProductName());
        orderViewHolder.price.setText("$ "+newOrderModel.getProductPrice());
        orderViewHolder.qty.setText(newOrderModel.getProductQty()+" Qty");
        orderViewHolder.tottalprice.setText("$ "+newOrderModel.getProductPrice()*newOrderModel.getProductQty()+"");
        Picasso.get().load(newOrderModel.getProductImage()).placeholder(mContext.getDrawable(R.drawable.smerdapng)).into(orderViewHolder.image);

        orderViewHolder.ordeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, OrderDetail.class);
                intent.putExtra("orderinfo",new Gson().toJson(newOrderModel));
               mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mGallery.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView productname,qty,price,tottalprice;
        RoundedImageView image;
        TextView txt,ordeDetail;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productname=itemView.findViewById(R.id.productname);
            qty =itemView.findViewById(R.id.qty);
            price=itemView.findViewById(R.id.productPrice);
            tottalprice=itemView.findViewById(R.id.totalPrice);
            image=itemView.findViewById(R.id.image);
            ordeDetail=itemView.findViewById(R.id.orderid);
        }
    }
}

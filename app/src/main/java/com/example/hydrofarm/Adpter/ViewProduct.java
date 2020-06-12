package com.example.hydrofarm.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hydrofarm.Model.ViewProductData;
import com.example.hydrofarm.R;

import java.util.List;

public class ViewProduct extends RecyclerView.Adapter<ViewProduct.viewholder>
{
    List<ViewProductData> data;
    Context context;
    public ViewProduct(List<ViewProductData> data,Context context)
    {
        this.data=data;
        this.context=context;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_products_view,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position)
    {


        Glide.with(context).load("http://192.168.1.46/hydrofarm/images/"+data.get(position).getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.hydrofarm_logo)
                .into(holder.productImage);
        holder.name.setText(data.get(position).getName());
        holder.price.setText(data.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class viewholder extends RecyclerView.ViewHolder
    {

        TextView name,price;
        ImageView productImage;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.main_product_view_Product_title_text_view);
            price=itemView.findViewById(R.id.main_product_view_Product_price_text_view);
            productImage=itemView.findViewById(R.id.main_product_view_Product_image_viewer);
        }
    }
}

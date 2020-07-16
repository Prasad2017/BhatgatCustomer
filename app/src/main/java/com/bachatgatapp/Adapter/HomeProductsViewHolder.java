package com.bachatgatapp.Adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bachatgatapp.Model.Product;
import com.bachatgatapp.R;

import java.util.List;

public class HomeProductsViewHolder  extends RecyclerView.ViewHolder {

    ImageView image;
    public ImageView image1;
    ImageView delete;
    TextView productName;
    TextView price;
    TextView actualPrice;
    public TextView productName1;
    public TextView price1;
    public TextView actualPrice1;
    TextView discountPercentage;
    public TextView discountPercentage1;
    public CardView cardView;
    public CardView cardView1;

    public HomeProductsViewHolder(final Context context, View itemView, List<Product> productList) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.productImage);
        image1 = (ImageView) itemView.findViewById(R.id.productImage1);
        delete = (ImageView) itemView.findViewById(R.id.delete);
        productName = (TextView) itemView.findViewById(R.id.productName);
        price = (TextView) itemView.findViewById(R.id.price);
        actualPrice = (TextView) itemView.findViewById(R.id.actualPrice);
        productName1 = (TextView) itemView.findViewById(R.id.productName1);
        price1 = (TextView) itemView.findViewById(R.id.price1);
        actualPrice1 = (TextView) itemView.findViewById(R.id.actualPrice1);
        discountPercentage = (TextView) itemView.findViewById(R.id.discountPercentage);
        discountPercentage1 = (TextView) itemView.findViewById(R.id.discountPercentage1);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        cardView1 = (CardView) itemView.findViewById(R.id.cardView1);

    }
}

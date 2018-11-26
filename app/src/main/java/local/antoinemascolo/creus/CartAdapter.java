package local.antoinemascolo.creus;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

class CartAdapter extends RecyclerView.Adapter<local.antoinemascolo.creus.CartAdapter.MyViewHolder> {

    ArrayList<Object> objet;

    public CartAdapter(ArrayList<Object> objet){this.objet = objet;}

    @Override
    public local.antoinemascolo.creus.CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_cell, parent, false);
        return new local.antoinemascolo.creus.CartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(local.antoinemascolo.creus.CartAdapter.MyViewHolder holder, int position) {
        Object obj = objet.get(position);
        holder.display(obj);
    }

    @Override
    public int getItemCount() {
        return objet.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        private final TextView nom;
        private final TextView prix;
        private final TextView total;
        private final TextView qty;
        private final ImageView img;
        private final Button plus;
        private final Button minus;


        private Object currentObject;

        public MyViewHolder(final View itemView) {
            super(itemView);

            nom = ((TextView) itemView.findViewById(R.id.textNameC));
            prix = ((TextView) itemView.findViewById(R.id.textprixunite));
            total = ((TextView) itemView.findViewById(R.id.textPriceTotal));
            qty = ((TextView) itemView.findViewById(R.id.textQtyC));
            img = ((ImageView) itemView.findViewById(R.id.image));
            plus = ((Button) itemView.findViewById(R.id.buttonCartPlus));
            minus = ((Button) itemView.findViewById(R.id.buttonCartMoins));

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.myCart.addItem(currentObject.getItemName(),1);
                    CartActivity.grandTotal.setText(String.format("%.2f", MainActivity.myCart.getBalance()) + "$");
                    notifyDataSetChanged();
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.myCart.removeItem(currentObject.getItemName(),1);
                    CartActivity.grandTotal.setText(String.format("%.2f", MainActivity.myCart.getBalance())+ "$");
                    notifyDataSetChanged();
                }

            });
        }

        public void display(Object objet) {
            currentObject = objet;

            Log.e("Display", "item is NOT null");
            nom.setText(objet.getItemName());
            img.setImageResource(objet.getItemImage());
            prix.setText(String.format("%.2f", objet.getItemPrice()));
            total.setText(String.format("%.2f", objet.getItemPrice() * objet.getQtyLeft()));
            qty.setText(Integer.toString(objet.getQtyLeft()));
            Log.e("Display", "Qty: " + objet.getQtyLeft());
        }
    }
}

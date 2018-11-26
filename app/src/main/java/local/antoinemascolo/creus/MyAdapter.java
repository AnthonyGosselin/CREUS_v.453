package local.antoinemascolo.creus;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<local.antoinemascolo.creus.MyAdapter.MyViewHolder> {

    ArrayList<Object> objet;

    public MyAdapter(ArrayList<Object> objet){this.objet = objet;}

    @Override
    public local.antoinemascolo.creus.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new local.antoinemascolo.creus.MyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(local.antoinemascolo.creus.MyAdapter.MyViewHolder holder, int position) {
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
        private final TextView qty;
        private final ImageView img;


        private Object currentObject;

        public MyViewHolder(final View itemView) {
            super(itemView);

            nom = ((TextView) itemView.findViewById(R.id.textName));
            prix = ((TextView) itemView.findViewById(R.id.textPrice));
            qty = ((TextView) itemView.findViewById(R.id.textQty));
            img = ((ImageView) itemView.findViewById(R.id.image));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addToCart = new Intent(view.getContext(), PopUpActivity.class);//OUINNNNNN
                    addToCart.putExtra("nom", nom.getText().toString());
                    addToCart.putExtra("description", currentObject.getDescription());
                    view.getContext().startActivity(addToCart);
                }
            });
        }

        public void display(Object objet) {
            currentObject = objet;
            nom.setText(objet.getItemName());
            img.setImageResource(objet.getItemImage());
            prix.setText(String.format("%.2f", objet.getItemPrice()));
            qty.setText(Integer.toString(objet.getQtyLeft()));
        }
    }
}

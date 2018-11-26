package local.antoinemascolo.creus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Object> objList = new ArrayList<>(MainActivity.allItems.values());
        rv.setAdapter(new MyAdapter(objList));

        Button cart = (Button) findViewById(R.id.buttonCart);

        cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ShopActivity.this, CartActivity.class));
            }
        });

    }

   /* @Override
    protected void onStart(){
        super.onStart();
        Log.e("MainActivity", "On Start fired");
    }*/
}

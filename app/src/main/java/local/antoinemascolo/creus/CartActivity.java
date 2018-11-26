package local.antoinemascolo.creus;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private final String TAG = "Cart";

    private Button pay;
    public static TextView grandTotal;

    private MediaPlayer goodsound;
    private MediaPlayer error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        MainActivity.myCart.UpdateNullItems(); //Pour les items a 0

        final RecyclerView rv = (RecyclerView) findViewById(R.id.cartList);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Object> objList = new ArrayList<>(MainActivity.myCart.getCartItems().values());
        CartAdapter cartAdapter = new CartAdapter(objList);
        rv.setAdapter(cartAdapter);
        //cartAdapter.notifyDataSetChanged();

        pay = (Button) findViewById(R.id.buttonPayer);
        grandTotal = (TextView) findViewById(R.id.grandTotal);
        grandTotal.setText(String.format("%.2f", MainActivity.myCart.getBalance()) + "$");
        goodsound = MediaPlayer.create(this, R.raw.goodbeep);
        error = MediaPlayer.create(this, R.raw.error);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "On Clicked");
                if (MainActivity.myCart.getBalance() > 0) {
                    if (MainActivity.currInputStream == null) {
                        Log.e(TAG, "No input stream for payment!");
                        Toast.makeText(getApplicationContext(), "Veuillez vous connecter au CREUS", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Veuillez passez votre carte pour payer", Toast.LENGTH_LONG).show();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Getting ready to read card");

                                //Envoie de la commande pour la carte
                                String strCom = "S";
                                MainActivity.mBluetoothConnection.write(strCom.getBytes(Charset.forName("UTF-8")));

                                Log.d(TAG, "Sent 'S' ");

                                //Scan pour carte
                                Boolean gotCard = false;

                                while (!gotCard) {
                                    // Read from the InputStream
                                    Log.d(TAG, "Is connected to inputStream");

                                    String incomingMessage = bluetoothRead();

                                    if (!incomingMessage.matches("(.*)Error(.*)")) {
                                        // if (incomingMessage.matches("(.*)C(.*)")) {
                                        //Commande de compte qui paye
                                        gotCard = true;
                                        //Integer.parseInt(incomingMessage.replaceAll("[\\D]", ""));
                                        Log.d(TAG, incomingMessage);
                                        int numCarte = Integer.parseInt(incomingMessage);

                                        Log.d(TAG, "Payment with account: #" + numCarte);
                                        Account account = MainActivity.accounts.get(numCarte);
                                        Boolean payed = account.pay(MainActivity.myCart.getBalance());
                                        if (payed) {
                                            Log.d(TAG, "Payment validated");
                                            MainActivity.myCart.checkOut();
                                            MainActivity.writeDataToFileInventory(MainActivity.allItems);
                                            MainActivity.writeDataToFileAccount(MainActivity.accounts);
                                            Toast.makeText(getApplicationContext(), "Succès de l'achat. Merci :)", Toast.LENGTH_LONG).show();
                                            goodsound.start();
                                            startActivity(new Intent(CartActivity.this, ShopActivity.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Balance insuffisante :(", Toast.LENGTH_LONG).show();
                                            error.start();
                                        }
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Échec du paiement", Toast.LENGTH_LONG).show();
                                        Log.e(TAG, "Payment failed: nothing charged");
                                    }
                                }
                            }
                        }, 2000);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Votre panier est vide!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String bluetoothRead(){
        int bytes; // bytes returned from read()
        byte[] buffer = new byte[1024];  // buffer store for the stream
        String incomingMessage = "Error";
        Log.d(TAG, "Bluetooth read start");

        try{
            if (MainActivity.currInputStream.available() == 0) {
                Log.d(TAG, "IS AVAILABLE");
                bytes = MainActivity.currInputStream.read(buffer);
                incomingMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "Card msg received: " + incomingMessage);
            }
        } catch (IOException e) {
            Log.e(TAG, "write: Error receiving card. " + e.getMessage());
        }
        return incomingMessage;
    }
}

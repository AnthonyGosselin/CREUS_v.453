package local.antoinemascolo.creus;

import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class Cart{

    private HashMap<String,Object> cartItems = new HashMap<>();
    private HashMap<String,Object> allItems;

    public Cart(HashMap<String,Object> itemList){
        this.allItems = itemList; //Get access to the inventory
    }

    public boolean addItem(String itemName, int amount){
        Object itemReference = allItems.get(itemName);

        if (itemReference == null || itemReference.getQtyLeft() <= 0){
            Log.e("Cart", "Error adding " + itemName + " to cart: this item doesn't exist in stock!");
            return false;
        }

        Log.d("CartClass", Integer.toString(itemReference.getQtyLeft()));

        int availableAmount = itemReference.getQtyLeft();

        //Are there already items like this one in the cart?
        int currAmountInCart = 0;
        int newAmount;
        Object itemInCart = cartItems.get(itemName);
        if (itemInCart != null){
            currAmountInCart = itemInCart.getQtyLeft();
            newAmount = currAmountInCart + amount;
            if ( availableAmount < newAmount ){
                Log.e("Cart","Cannot add " + itemName + " to cart (missing " + (newAmount - availableAmount) + " in stock)");
                return false;
            }
            cartItems.get(itemName).setQtyLeft(newAmount);
        }else{
            newAmount = currAmountInCart + amount;
            Object newItem = new Object(itemName, itemReference.getItemPrice(), newAmount, itemReference.getItemImage(),itemReference.getDescription(), itemReference.getItemPlace());
            cartItems.put(itemName, newItem);
            Log.e("Cart","Added: " + amount + " " + itemName + " to cart (new total: " + newAmount + ")");

        }

        return true;
    }

    public void removeItem(String itemName, int amount){
        Object itemInCart = cartItems.get(itemName);

        if (itemInCart != null){
            int newAmount = (itemInCart.getQtyLeft() - amount);
            if (newAmount < 0){ newAmount = 0; }

            if (newAmount == 0){
                //cartItems.remove(itemName);
                cartItems.get(itemName).setQtyLeft(newAmount);
                Log.e("Cart","Removed all " + itemName + " items from cart");
            }
            else{
                cartItems.get(itemName).setQtyLeft(newAmount);
                Log.e("Cart","Removed " + amount + " " + itemName + " items from cart (new total: " + newAmount + ")");
            }
        }
        else{
            Log.e("Cart","Did not remove " + itemName + " item because it was never in the cart!");
        }
    }

    public double getBalance(){
        double balance = 0;

        for (String itemName : cartItems.keySet()){
            Object item = cartItems.get(itemName);
            balance += (item.getItemPrice() * item.getQtyLeft());
        }

        Log.e("Cart","New cart balance: " + balance);
        return balance;
    }

    public void checkOut(){
        Log.e("Cart","----------Purchase receipt----------");
        for (String itemName : cartItems.keySet()){

            Object itemInCart = cartItems.get(itemName);
            Object itemInStock = allItems.get(itemName);

            int currQty = itemInStock.getQtyLeft();
            int cartQty = itemInCart.getQtyLeft();
            int newQtyLeft = currQty - cartQty;
            itemInStock.setQtyLeft(newQtyLeft);

            Log.e("Cart","Purchased " + cartQty + " " + itemName + " items");
        }

        cartItems.clear(); //Empty cart
        Log.e("Cart","------------------------------------");
    }

    public HashMap<String, Object> getCartItems() {
        return cartItems;
    }

    public void setCartItems(HashMap<String, Object> cartItems) {
        this.cartItems = cartItems;
    }

    public Object getObject(String itemName){
        Object itemInCart = cartItems.get(itemName);
        return itemInCart;
    }

    public void UpdateNullItems(){
        //Update for items at qty = 0
        for (String name : cartItems.keySet()){
           // Log.d("Cart", "Item: " + name);

            if (getObject(name).getQtyLeft() == 0){
                //Log.d("Cart", "Item at 0!");
                MainActivity.myCart.getCartItems().remove(name);
            }
            else{
                //Log.d("Cart", name + " left: " + getObject(name).getQtyLeft());
            }
        }
    }
}


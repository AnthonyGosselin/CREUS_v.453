package local.antoinemascolo.creus;

public class Object {

    private String itemName;
    private double itemPrice;
    private int qtyLeft;
    private int itemImage;
    private String description;
    private int place;

    public Object(String itemName, double itemPrice, int qtyLeft, int itemImage,String description, int pl) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.qtyLeft = qtyLeft;
        this.itemImage = itemImage;
        this.description = description;
        this.place = pl;
    }

    //Name
    public void setItemName(String newName){itemName = newName;}
    public String getItemName(){return itemName;}
    //Price
    public void setItemPrice(double newPrice){itemPrice = newPrice;}
    public double getItemPrice(){return itemPrice;}
    //Qty
    public void setQtyLeft(int newQty){qtyLeft = newQty;}
    public int getQtyLeft(){return qtyLeft;}
    //Img
    public void setItemImage(int img){itemImage = img;}
    public int getItemImage(){return itemImage;}
    //description

    public void setItemPlace(int pl) {place = pl;}
    public int getItemPlace() {return place;};

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}


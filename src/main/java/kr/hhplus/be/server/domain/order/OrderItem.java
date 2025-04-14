package kr.hhplus.be.server.domain.order;

public class OrderItem {

    private Long id;
    private Order order;
    private Long itemId;
    private Integer price;
    private Integer quantity;

    public static OrderItem of(Long itemId, Integer price, Integer quantity) {
        return new OrderItem(itemId, price, quantity);
    }

    public OrderItem(Long itemId, Integer price, Integer quantity) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getItemId() {
        return itemId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    protected void setOrder(Order order){
        this.order = order;
    }

    public Integer calculatePrice(){
        return price * quantity;
    }
}

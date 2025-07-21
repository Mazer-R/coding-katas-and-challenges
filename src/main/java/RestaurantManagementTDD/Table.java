package RestaurantManagementTDD;

import java.util.LinkedHashMap;
import java.util.Map;

public class Table {
    private int guestNumber;
    private Map<String, String> clientsAndOrders = new LinkedHashMap<>();

    public Table(int tableGuests){
        this.guestNumber = tableGuests;
    }


    public String getLastOrder(){
        var ordersArray=clientsAndOrders.values().toArray();
        return ordersArray[ordersArray.length-1].toString();
    }

    public String getOrders(){
        var stringBuilder = new StringBuilder();
        var ordersArray= clientsAndOrders.values().toArray();
        var length = ordersArray.length;
        for(int i = 0;i<length;i++){
            if((length-1)==i)
                stringBuilder.append(ordersArray[i]);
            else
                stringBuilder.append(ordersArray[i]).append(", ");
            }
            return stringBuilder.toString();
        }

    public void registerOrder(String clientAndOrder) {
        var clientOrderSplit = clientAndOrder.split(":");
        var client = clientOrderSplit[0].trim();
        var orderToValidate = clientOrderSplit[1].trim();
        String order;
        if (orderToValidate.equalsIgnoreCase("SAME")){
            order = getLastOrder();
        }else{
            order = orderToValidate;
        }
        clientsAndOrders.put(client,order);
    }
    public boolean hasPendingOrders(){
        var ordersArray= clientsAndOrders.values().toArray();
        return (ordersArray.length)<(guestNumber);
    }
    public void validateOrders(){
        var forTwoList = new java.util.ArrayList<>(
                clientsAndOrders.values().stream().filter(
                        x -> (x.contains("para dos"))).toList());

        while(!forTwoList.isEmpty()){
        var toSearch = forTwoList.removeLast();
            if (forTwoList.remove(toSearch)){
                continue;
            }else {
                throw new IllegalStateException("El pedido '" + toSearch + "' requiere dos clientes que lo soliciten");
            }
        }
    }


}

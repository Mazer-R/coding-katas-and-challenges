package RestaurantManagementTDD;

import java.util.HashMap;
import java.util.Map;
/**
My solution to the RestaurantManagerTest.

Given a predefined test class (see RestaurantManagerTest),
the goal was to develop a working solution within a limited time (~1 hour).

Further improvements could include input validation, refactoring methods
like "validateOrders", breaking logic into smaller units, and introducing
supporting classes such as a Product model.
*/


public class RestaurantManager {
    private Map<Integer, Table> tableList = new HashMap<>();

    public int createTable(int guestsNumber){
        Table table = new Table(guestsNumber);
        var i = 1;
        while (tableList.containsKey(i)){
            i++;
        }
        tableList.put(i, table);
        return i;
    }
    public void order(String order, int tableNumber){
        var table = getTable(tableNumber);
        table.registerOrder(order);
    }
    public String generateOrder(int tableId){
        var table = getTable(tableId);
        table.validateOrders();
        if (table.hasPendingOrders())
            throw new IllegalStateException("No todos los clientes han realizado su pedido");
        return table.getOrders();
    }

    private Table getTable(int tableId){
        return tableList.get(tableId);

    }
}

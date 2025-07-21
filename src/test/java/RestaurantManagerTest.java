import RestaurantManagementTDD.RestaurantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantManagerTest {
/**
    This test suite is designed to guide the iterative development of a RestaurantManager system.

    The objective is to implement a class that manages customer orders in a restaurant setting.
    Development is done test-by-test, validating functionality step by step as follows:

    - Initially, the system should support assigning clients to tables and storing individual orders.
    - Then, it must validate that all expected guests at a table have placed an order before generating it.
    - If a client orders "Same", the system must interpret it as repeating the previous guest’s order.
    - If a guest orders a product marked as "para dos" (means "for two") (e.g., "Paella para dos"), the system must ensure that
      exactly one other guest at the same table has also ordered the same item; otherwise, it should throw an error.

    The tests reflect this incremental development, increasing in complexity and rule validation as the implementation evolves.

    This implementation focuses solely on business logic; persistence, UI, and concurrency are out of scope.
    The approach follows TDD and clean design principles, emphasizing encapsulation and business rule enforcement.
 */

    private RestaurantManager restaurantManager;

    @BeforeEach
    public void setUp() {
        restaurantManager = new RestaurantManager();
    }

    /**
     * Verifies that multiple clients can place individual orders
     * at the same table, and the combined order is returned in insertion order.
     */

    @Test
    public void testSingleTableMultipleClients() {
        int mesaId = restaurantManager.createTable(2);

        restaurantManager.order("Alice : Cerveza", mesaId);
        restaurantManager.order("Bob : Tapa de queso", mesaId);

        String result = restaurantManager.generateOrder(mesaId);
        assertEquals("Cerveza, Tapa de queso", result);
    }

    /**
     * Verifies that multiple tables can be managed independently,
     * each storing and generating its own set of client orders.
     */

    @Test
    public void testMultipleTablesDifferentClients() {
        int mesa1 = restaurantManager.createTable(2);
        int mesa2 = restaurantManager.createTable(1);

        restaurantManager.order("Carlos : Vino", mesa1);
        restaurantManager.order("Alice : Coca-Cola", mesa1);

        restaurantManager.order("Diana : Agua", mesa2);

        String result1 = restaurantManager.generateOrder(mesa1);
        String result2 = restaurantManager.generateOrder(mesa2);
        assertEquals("Vino, Coca-Cola", result1);
        assertEquals("Agua", result2);

    }

    /**
     * Tests the "Same" functionality, where a client can request
     * to repeat the most recent order made at the table.
     */

    @Test
    public void testSameOrderFunctionality() {
        int mesaId = restaurantManager.createTable(2);

        restaurantManager.order("Laura : Mojito", mesaId);

        restaurantManager.order("Paco : Same", mesaId);

        restaurantManager.order("Pablo : Cerveza", mesaId);

        String result = restaurantManager.generateOrder(mesaId);
        assertEquals("Mojito, Mojito, Cerveza", result);
    }
    /**
     * Ensures that an order cannot be generated if not all guests
     * at the table have submitted their orders. It should throw an exception.
     */

    @Test
    public void  testGenerateOrderFailsIfNotAllClientsOrdered() {
        int mesaId = restaurantManager.createTable(3);

        restaurantManager.order("Ana : Agua", mesaId);
        restaurantManager.order("Luis : Vino", mesaId);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            restaurantManager.generateOrder(mesaId);
        });

        assertEquals("No todos los clientes han realizado su pedido", exception.getMessage());

        restaurantManager.order("Mario : Tónica", mesaId);

        String result = restaurantManager.generateOrder(mesaId);
        assertEquals("Agua, Vino, Tónica", result);
    }

    /**
     * Validates the "for two" rule: a product labeled as "para dos"
     * must be ordered by exactly two clients to be accepted.
     */

    @Test
    public void testForTwoRequiresTwoOrders() {
        int mesaId = restaurantManager.createTable(2);

        restaurantManager.order("Eva : Paella para dos", mesaId);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            restaurantManager.generateOrder(mesaId);
        });

        assertEquals("El pedido 'Paella para dos' requiere dos clientes que lo soliciten", exception.getMessage());

        restaurantManager.order("Oscar : Paella para dos", mesaId);

        String result = restaurantManager.generateOrder(mesaId);
        assertEquals("Paella para dos, Paella para dos", result);
    }

    /**
     * Verifies that a client can change their order by submitting
     * a new one before the final order is generated. The last one prevails.
     */
    @Test
    public void testGuestChangeORder() {
        int mesaId = restaurantManager.createTable(2);

        restaurantManager.order("Lucía : Agua", mesaId);
        restaurantManager.order("Paco : Calamares", mesaId);
        restaurantManager.order("Raúl : Vino", mesaId);


        restaurantManager.order("Lucía : Cerveza", mesaId);

        restaurantManager.order("Raúl : Mojito", mesaId);

        String result = restaurantManager.generateOrder(mesaId);
        assertEquals("Cerveza, Calamares, Mojito", result);
    }

}

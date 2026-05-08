import model.Product;
import service.InventoryService;
public class MainTest {

    public static void main(String[] args) {

        InventoryService service = new InventoryService();

        service.addProduct(new Product(1, "Laptop", 50000, 3));
        service.addProduct(new Product(2, "Mouse", 500, 10));

        System.out.println("ALL:");
        service.getAll().forEach(System.out::println);

        System.out.println("\nLOW STOCK:");
        service.lowStock().forEach(System.out::println);
    }
}
//import model.Product;
//import service.InventoryService;

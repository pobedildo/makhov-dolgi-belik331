import db.DB;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("=== ПОДКЛЮЧЕНИЯ ===");
        DB.testConnection();
    }
}
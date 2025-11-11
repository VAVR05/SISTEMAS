public class BancoConcurrente {
    public static void main(String[] args) {
        CuentaBancaria cuentaCompartida = new CuentaBancaria(10000000); 

        Cajero cajero1 = new Cajero("Cajero 1", cuentaCompartida);
        Cajero cajero2 = new Cajero("Cajero 2", cuentaCompartida);
        Cajero cajero3 = new Cajero("Cajero 3", cuentaCompartida);

        // Inicializar los hilos
        cajero1.start();
        cajero2.start();
        cajero3.start();
        // Esperar a que los hilos terminen
        try {
            cajero1.join();
            cajero2.join();
            cajero3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nSaldo final en la cuenta: $" + cuentaCompartida.getSaldo());
    }
}

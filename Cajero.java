import java.util.Random;

public class Cajero extends Thread {
    private CuentaBancaria cuenta;
    private String nombre;
    private Random random = new Random();

    public Cajero(String nombre, CuentaBancaria cuenta) {
        this.nombre = nombre;
        this.cuenta = cuenta;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            int tipoOperacion = random.nextInt(2); // 0 = retiro, 1 = depósito
            int cantidad = random.nextInt(5000000) + 1000000; // entre 1 y 6 millones

            if (tipoOperacion == 0) {
                cuenta.retirar(cantidad, nombre);
            } else {
                cuenta.depositar(cantidad, nombre);
            }

            try {
                Thread.sleep(random.nextInt(1000)); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

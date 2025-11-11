public class CuentaBancaria {
    private int saldo;

    public CuentaBancaria(int saldoInicial) {
        this.saldo = saldoInicial;
    }

    public synchronized void retirar(int cantidad, String cajero) {
        if (saldo >= cantidad) {
            System.out.println(cajero + " está retirando $" + cantidad);
            try {
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            saldo -= cantidad;
            System.out.println(cajero + " RETIRO COMPLETO. Saldo restante: $" + saldo);
        } else {
            System.out.println(cajero + " intentó retirar $" + cantidad + " pero no hay suficiente saldo.");
        }
    }

    public synchronized void depositar(int cantidad, String cajero) {
        System.out.println(cajero + " está depositando $" + cantidad);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        saldo += cantidad;
        System.out.println(cajero + " DEPOSITO COMPLETO. Saldo actual: $" + saldo);
    }

    public int getSaldo() {
        return saldo;
    }
}

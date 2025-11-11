
# INTEGRANTES: JACOB SALCEDO, VANESSA VILLAFAÑE

# Concurrencia

Es la capacidad de un sistema para **hacer varias tareas al mismo tiempo**.
Por ejemplo, en un banco real, distintos cajeros pueden atender a diferentes personas al mismo tiempo.
En el programa, cada cajero es un "hilo" que trabaja de forma simultánea.

**Ejemplo visual:**
```
Cajero 1 → [retiro] [depósito] [retiro] ...
Cajero 2 → [depósito] [retiro] [depósito] ...
Cajero 3 → [retiro] [retiro] [depósito] ...
```

# Proyecto: Banco 

Este proyecto simula el funcionamiento de un banco cuando **varios cajeros trabajan al mismo tiempo** sobre una misma cuenta.
La idea es mostrar qué pasa cuando varias personas intentan **retirar o depositar dinero al mismo tiempo**, y cómo se puede controlar eso para que el dinero no se pierda ni se duplique por error.

El programa está hecho en **Java** y utiliza una técnica llamada *concurrencia*, que significa que **varias acciones pueden ocurrir al mismo tiempo**.

---

## Estructura del proyecto

El programa está compuesto por **tres clases principales**, cada una con una función diferente:

---

### 1. `CuentaBancaria.java`

Esta clase representa una **cuenta de banco**, con un saldo que puede aumentar o disminuir.

#### Declaración de la clase y atributos

```java
public class CuentaBancaria {
    private int saldo;

    public CuentaBancaria(int saldoInicial) {
        this.saldo = saldoInicial;
    }
```

**EXPLICACIÓN:**
- `private int saldo`: Guarda la cantidad de dinero disponible en la cuenta. Es privado para que nadie pueda modificarlo directamente.
-  `CuentaBancaria(int saldoInicial)` inicializa la cuenta con un monto inicial (por ejemplo, 10 millones de pesos).

#### Método para retirar dinero

```java
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
```

- `synchronized`: Esta palabra clave es **la más importante**. Garantiza que solo un cajero a la vez pueda ejecutar este método. Si otro cajero intenta retirar al mismo tiempo, debe esperar su turno.
- `if (saldo >= cantidad)`: Verifica que haya suficiente dinero antes de permitir el retiro.
- `Thread.sleep(500)`: Simula el tiempo que toma procesar la transacción (medio segundo).
- `saldo -= cantidad`: Resta la cantidad retirada del saldo total.
- Si no hay suficiente dinero, muestra un mensaje de rechazo.

#### Método para depositar dinero

```java
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
```

- También usa `synchronized` para evitar que dos depósitos se hagan simultáneamente y causen errores.
- `saldo += cantidad`: Suma la cantidad depositada al saldo total.
- El proceso es similar al retiro, pero más simple porque siempre se puede depositar.

---

### 2. `Cajero.java`

Esta clase representa un **cajero automático** o una persona atendiendo en ventanilla.

#### Declaración de la clase

```java
public class Cajero extends Thread {
    private CuentaBancaria cuenta;
    private String nombre;
    private Random random = new Random();

    public Cajero(String nombre, CuentaBancaria cuenta) {
        this.nombre = nombre;
        this.cuenta = cuenta;
    }
```

**Explicación:**
- `extends Thread`: Convierte al cajero en un "hilo" que puede trabajar en paralelo con otros cajeros.
- `CuentaBancaria cuenta`: Cada cajero tiene acceso a la cuenta compartida.
- `Random random`: Genera números aleatorios para simular operaciones impredecibles.

#### Método run (lo que hace el cajero)

```java
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
```

- `for (int i = 0; i < 5; i++)`: Cada cajero realiza 5 operaciones en total.
- `random.nextInt(2)`: Genera 0 o 1 aleatoriamente. Si es 0, retira; si es 1, deposita.
- `random.nextInt(5000000) + 1000000`: Genera una cantidad aleatoria entre 1 y 6 millones de pesos.
- `Thread.sleep(random.nextInt(1000))`: Pausa entre 0 y 1 segundo antes de la siguiente operación, simulando el tiempo entre clientes.

---

### 3. `BancoConcurrente.java`

Esta es la **clase principal**, donde todo comienza.

**EXPLICACIÓN:**

```java
public class BancoConcurrente {
    public static void main(String[] args) {
        CuentaBancaria cuentaCompartida = new CuentaBancaria(10000000); 

        Cajero cajero1 = new Cajero("Cajero 1", cuentaCompartida);
        Cajero cajero2 = new Cajero("Cajero 2", cuentaCompartida);
        Cajero cajero3 = new Cajero("Cajero 3", cuentaCompartida);

        cajero1.start();
        cajero2.start();
        cajero3.start();

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
```

1. **Crear la cuenta compartida:**
   ```java
   CuentaBancaria cuentaCompartida = new CuentaBancaria(10000000);
   ```
   Se crea una única cuenta con 10 millones de pesos que todos los cajeros usarán.

2. **Crear los cajeros:**
   ```java
   Cajero cajero1 = new Cajero("Cajero 1", cuentaCompartida);
   ```
   Cada cajero recibe un nombre y la referencia a la cuenta compartida.

3. **Iniciar los hilos:**
   ```java
   cajero1.start();
   ```
   El método `start()` activa cada cajero para que comience a trabajar en paralelo.

4. **Esperar a que terminen:**
   ```java
   cajero1.join();
   ```
   El método `join()` hace que el programa principal espere a que cada cajero termine sus 5 operaciones antes de continuar.

5. **Mostrar resultado final:**
   ```java
   System.out.println("\nSaldo final en la cuenta: $" + cuentaCompartida.getSaldo());
   ```
   Imprime el saldo final después de todas las operaciones.

---

---

## Ejemplo de salida

```
Cajero 1 está depositando $2345000
Cajero 2 está retirando $1500000
Cajero 1 DEPOSITO COMPLETO. Saldo actual: $12345000
Cajero 3 está depositando $3000000
Cajero 2 RETIRO COMPLETO. Saldo restante: $10845000
...
Saldo final en la cuenta: $10500000
```

---

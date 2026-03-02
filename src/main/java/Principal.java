import java.util.*;

/**
 * Convertidor de Monedas usando la API ExchangeRate
 *
 * Permite convertir cantidades entre diferentes monedas obteniendo
 * tasas de cambio en tiempo real desde exchangerate-api.com
 *
 * @author Anderson Coello
 * @version 1.0
 * @since 2026-03-02
 */
public class Principal {

    private static ConsultaAPI api = new ConsultaAPI();
    private static Scanner sc = new Scanner(System.in);
    private static Map<String, Double> monedasDisponibles;
    private static List<String> codigosMonedas;

    /**
     * Método principal que ejecuta la aplicación.
     *
     * Carga las monedas disponibles y permite al usuario realizar
     * conversiones de forma repetida hasta que decida salir.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   Convertidor de Monedas - API ExchangeRate   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            System.out.println("Cargando monedas disponibles...\n");
            cargarMonedas();

            int salir = 2;
            while (salir == 2) {
                System.out.println("════════════════════════════════════════");
                System.out.println("SELECCIONA LA MONEDA DE ORIGEN:");
                System.out.println("════════════════════════════════════════");
                String monedaOrigen = seleccionarMoneda();

                System.out.println("\nCuanto quieres convertir?");
                double cantidad = obtenerCantidadValida();

                System.out.println("\n════════════════════════════════════════");
                System.out.println("SELECCIONA LA MONEDA DE DESTINO:");
                System.out.println("════════════════════════════════════════");
                String monedaDestino = seleccionarMoneda();

                double resultado = api.convertir(cantidad, monedaOrigen, monedaDestino);

                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.printf("║ %.2f %s = %.2f %s%n", cantidad, monedaOrigen, resultado, monedaDestino);
                System.out.println("╚════════════════════════════════════════╝\n");

                salir = obtenerOpcionSalida();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            sc.close();
            System.out.println("\nGracias por usar el convertidor de monedas!");
        }
    }

    /**
     * Solicita al usuario que ingrese 1 para salir o 2 para continuar.
     *
     * @return 1 para salir, 2 para continuar
     */
    private static int obtenerOpcionSalida() {
        int opcion = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.println("Digite 1 para salir o 2 para otra conversion");
                System.out.print("Opcion (1 o 2): ");
                String entrada = sc.nextLine().trim();

                if (entrada.isEmpty()) {
                    System.out.println("Debes ingresar un numero.\n");
                    continue;
                }

                opcion = Integer.parseInt(entrada);

                if (opcion != 1 && opcion != 2) {
                    System.out.println("Solo puedes ingresar 1 o 2.\n");
                    continue;
                }

                valido = true;

            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingresa solo numeros (1 o 2).\n");
            }
        }

        return opcion;
    }

    /**
     * Carga todas las monedas disponibles desde la API.
     */
    private static void cargarMonedas() {
        monedasDisponibles = api.obtenerTodasLasMonedas();
        codigosMonedas = new ArrayList<>(monedasDisponibles.keySet());
        System.out.println("Se cargaron " + codigosMonedas.size() + " monedas disponibles\n");
    }

    /**
     * Muestra el menu de monedas y captura la seleccion del usuario.
     *
     * @return codigo de la moneda seleccionada
     */
    private static String seleccionarMoneda() {
        mostrarMonedasNumeradas();
        int indice = obtenerIndiceValido();
        String monedaSeleccionada = codigosMonedas.get(indice);
        System.out.println("Seleccionaste: " + monedaSeleccionada);
        return monedaSeleccionada;
    }

    /**
     * Muestra todas las monedas disponibles con indices numerados.
     */
    private static void mostrarMonedasNumeradas() {
        for (int i = 0; i < codigosMonedas.size(); i++) {
            String codigo = codigosMonedas.get(i);
            System.out.printf("%3d. %s%n", i + 1, codigo);
        }
    }

    /**
     * Obtiene un indice valido dentro del rango de monedas disponibles.
     *
     * Valida que la entrada sea un numero en el rango correcto.
     *
     * @return indice de la moneda seleccionada (0-based)
     */
    private static int obtenerIndiceValido() {
        int opcion = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print("\nIngresa el numero de la moneda (1-" + codigosMonedas.size() + "): ");
                String entrada = sc.nextLine().trim();

                if (entrada.isEmpty()) {
                    System.out.println("Debes ingresar un numero.");
                    continue;
                }

                opcion = Integer.parseInt(entrada);

                if (opcion < 1 || opcion > codigosMonedas.size()) {
                    System.out.println("El numero debe estar entre 1 y " + codigosMonedas.size());
                    continue;
                }

                valido = true;

            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingresa solo numeros.");
            }
        }

        return opcion - 1;
    }

    /**
     * Obtiene una cantidad valida del usuario para la conversion.
     *
     * Valida que la entrada sea un numero decimal positivo.
     *
     * @return cantidad a convertir
     */
    private static double obtenerCantidadValida() {
        double cantidad = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print("Ingresa la cantidad: ");
                String entrada = sc.nextLine().trim();

                if (entrada.isEmpty()) {
                    System.out.println("Debes ingresar una cantidad.");
                    continue;
                }

                cantidad = Double.parseDouble(entrada);

                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a 0.");
                    continue;
                }

                valido = true;

            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingresa un numero valido.");
            }
        }

        return cantidad;
    }
}
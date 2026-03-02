import java.util.*;

public class Principal {

    private static ConsultaAPI api = new ConsultaAPI();
    private static Scanner sc = new Scanner(System.in);
    private static Map<String, Double> monedasDisponibles;
    private static List<String> codigosMonedas;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   Convertidor de Monedas - API ExchangeRate   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            // Cargar monedas UNA SOLA VEZ (fuera del while)
            System.out.println("⏳ Cargando monedas disponibles...\n");
            cargarMonedas();

            int salir = 2;
            while (salir == 2) {
                // Obtener moneda origen
                System.out.println("════════════════════════════════════════");
                System.out.println("1️⃣  SELECCIONA LA MONEDA DE ORIGEN:");
                System.out.println("════════════════════════════════════════");
                String monedaOrigen = seleccionarMoneda();

                // Obtener cantidad
                System.out.println("\n💰 ¿Cuánto quieres convertir?");
                double cantidad = obtenerCantidadValida();

                // Obtener moneda destino
                System.out.println("\n════════════════════════════════════════");
                System.out.println("2️⃣  SELECCIONA LA MONEDA DE DESTINO:");
                System.out.println("════════════════════════════════════════");
                String monedaDestino = seleccionarMoneda();

                // Realizar conversión
                double resultado = api.convertir(cantidad, monedaOrigen, monedaDestino);

                // Mostrar resultado
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.printf("║ %.2f %s = %.2f %s%n", cantidad, monedaOrigen, resultado, monedaDestino);
                System.out.println("╚════════════════════════════════════════╝\n");

                // Obtener opción de salida
                salir = obtenerOpcionSalida();
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        } finally {
            sc.close();
            System.out.println("\n👋 ¡Gracias por usar el convertidor de monedas!");
        }
    }

    /**
     * Valida que el usuario ingrese 1 o 2
     */
    private static int obtenerOpcionSalida() {
        int opcion = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.println("Digite 1 si quiere salir del programa y 2 para continuar con otra conversión");
                System.out.print("Tu opción (1 o 2): ");
                String entrada = sc.nextLine().trim();  // ✅ Usa nextLine()

                if (entrada.isEmpty()) {
                    System.out.println("Debes ingresar un número.\n");
                    continue;
                }

                opcion = Integer.parseInt(entrada);

                if (opcion != 1 && opcion != 2) {
                    System.out.println("Solo puedes ingresar 1 o 2.\n");
                    continue;
                }

                valido = true;

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debes ingresar solo números (1 o 2).\n");
            }
        }

        return opcion;
    }

    /**
     * Carga todas las monedas disponibles desde la API
     */
    private static void cargarMonedas() {
        monedasDisponibles = api.obtenerTodasLasMonedas();
        codigosMonedas = new ArrayList<>(monedasDisponibles.keySet());
        System.out.println("✅ Se cargaron " + codigosMonedas.size() + " monedas disponibles\n");
    }

    /**
     * Muestra el menú de monedas numerado y devuelve la selección del usuario
     */
    private static String seleccionarMoneda() {
        mostrarMonedasNumeradas();
        int indice = obtenerIndiceValido();
        String monedaSeleccionada = codigosMonedas.get(indice);
        System.out.println("✅ Seleccionaste: " + monedaSeleccionada);
        return monedaSeleccionada;
    }

    /**
     * Muestra todas las monedas con índices numerados
     */
    private static void mostrarMonedasNumeradas() {
        for (int i = 0; i < codigosMonedas.size(); i++) {
            String codigo = codigosMonedas.get(i);
            System.out.printf("%3d. %s%n", i + 1, codigo);
        }
    }

    /**
     * Obtiene un índice válido del usuario (entre 1 y cantidad de monedas)
     */
    private static int obtenerIndiceValido() {
        int opcion = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print("\nIngresa el número de la moneda (1-" + codigosMonedas.size() + "): ");
                String entrada = sc.nextLine().trim();

                if (entrada.isEmpty()) {
                    System.out.println("Debes ingresar un número.");
                    continue;
                }

                opcion = Integer.parseInt(entrada);

                if (opcion < 1 || opcion > codigosMonedas.size()) {
                    System.out.println("El número debe estar entre 1 y " + codigosMonedas.size());
                    continue;
                }

                valido = true;

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debes ingresar solo números.");
            }
        }

        return opcion - 1;
    }

    /**
     * Obtiene una cantidad válida del usuario (número decimal positivo)
     */
    private static double obtenerCantidadValida() {
        double cantidad = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print("Ingresa la cantidad: ");
                String entrada = sc.nextLine().trim();

                if (entrada.isEmpty()) {
                    System.out.println("  Debes ingresar una cantidad.");
                    continue;
                }

                cantidad = Double.parseDouble(entrada);

                if (cantidad <= 0) {
                    System.out.println("  La cantidad debe ser mayor a 0.");
                    continue;
                }

                valido = true;

            } catch (NumberFormatException e) {
                System.out.println(" Entrada inválida. Ingresa un número válido.");
            }
        }

        return cantidad;
    }
}
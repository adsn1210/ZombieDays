package ucjc.ev2.model;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;


//Clase para la creacion del Laberinto usando DFS como algoritmo
public class Laberinto {
    //Creamos un laberinto base por defecto.
    public static final int[][] MAPA_POR_DEFECTO = {
            //1 = Pared 0 = Camino
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private static final int TAMANO_CELDA_POR_DEFECTO = 32;//cada celda es de 32x32
    private static final int PARED = 1;
    private static final int CAMINO = 0;
    private static final int MIN_DIMENSION = 5;//Usado en el DFS para asegurar que el laberinto tenga un minimo

    private final int[][] mapa;
    private final int tamanoCelda;
    private final Point entrada;
    private final Point salida;

    //Constructores
    public Laberinto() {
        this(MAPA_POR_DEFECTO, TAMANO_CELDA_POR_DEFECTO);
    }

    public Laberinto(int[][] mapa, int tamanoCelda) {
        this.mapa = mapa;
        this.tamanoCelda = tamanoCelda;
        this.entrada = localizarEntrada(mapa);
        this.salida = localizarSalida(mapa);
    }

    //ESTE ALGORITMO LO HA GENERADO LA IA, EL ALGORITMO DFS(Depth-First Search)

    public Laberinto(int filas, int columnas) {
        this(usarDFS(filas, columnas), TAMANO_CELDA_POR_DEFECTO);
    }

    /**
     * Determina si el área indicada colisiona con una pared del laberinto.
     * Las paredes se representan con valor 1 en la matriz del laberinto.
     */
    public boolean esPared(java.awt.Rectangle area) {
        if (area == null) {
            return false;
        }
        int columnaMin = Math.floorDiv(area.x, tamanoCelda);
        int columnaMax = Math.floorDiv(area.x + area.width - 1, tamanoCelda);
        int filaMin = Math.floorDiv(area.y, tamanoCelda);
        int filaMax = Math.floorDiv(area.y + area.height -1, tamanoCelda);

        for (int fila = filaMin; fila <= filaMax;fila++){
            for (int columna = columnaMin; columna <= columnaMax; columna++){
                if (esCeldaPared(fila,columna)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean esCeldaPared(int fila, int columna){
        if (fila < 0 || fila >= mapa.length){
            return true;
        }
        if (columna < 0 || columna >= mapa[0].length){
            return true;
        }
        return mapa[fila][columna] == PARED;
    }
    public int[][] getMapa() {
        return mapa;
    }

    public int getTamanoCelda() {
        return tamanoCelda;
    }

    public Point getEntradaCelda() {
        return new Point(entrada);
    }

    public Point getSalidaCelda() {
        return new Point(salida);
    }

    public Point getEntradaEnPixeles() {
        return new Point(entrada.x * tamanoCelda, entrada.y * tamanoCelda);
    }

    public Point getSalidaEnPixeles() {
        return new Point(salida.x * tamanoCelda, salida.y * tamanoCelda);
    }private static int[][] usarDFS(int filas, int columnas) {
        int filasAjustadas = ajustarDimension(filas);
        int columnasAjustadas = ajustarDimension(columnas);
        int[][] mapa = inicializarParedes(filasAjustadas, columnasAjustadas);

        int filaInicio = 1;
        int columnaInicio = 1;
        Random random = new Random();
        Stack<int[]> pila = new Stack<>();
        pila.push(new int[] { filaInicio, columnaInicio });
        mapa[filaInicio][columnaInicio] = CAMINO;

        while (!pila.isEmpty()) {
            int[] actual = pila.peek();
            List<int[]> vecinos = obtenerVecinosNoVisitados(actual[0], actual[1], mapa);
            if (vecinos.isEmpty()) {
                pila.pop();
                continue;
            }

            Collections.shuffle(vecinos, random);
            int[] siguiente = vecinos.get(0);

            int filaPared = actual[0] + (siguiente[0] - actual[0]) / 2;
            int columnaPared = actual[1] + (siguiente[1] - actual[1]) / 2;
            mapa[filaPared][columnaPared] = CAMINO;
            mapa[siguiente[0]][siguiente[1]] = CAMINO;

            pila.push(siguiente);
        }

        mapa[filaInicio][0] = CAMINO;
        mapa[filasAjustadas - 2][columnasAjustadas - 1] = CAMINO;
        return mapa;
    }

    private static Point localizarEntrada(int[][] mapa) {
        return localizarCeldaEnBorde(mapa, true);
    }

    private static Point localizarSalida(int[][] mapa) {
        return localizarCeldaEnBorde(mapa, false);
    }

    private static Point localizarCeldaEnBorde(int[][] mapa, boolean primera) {
        List<Point> caminosEnBorde = new ArrayList<>();

        for (int fila = 0; fila < mapa.length; fila++) {
            if (mapa[fila][0] == CAMINO) {
                caminosEnBorde.add(new Point(0, fila));
            }
            if (mapa[fila][mapa[0].length - 1] == CAMINO) {
                caminosEnBorde.add(new Point(mapa[0].length - 1, fila));
            }
        }

        for (int columna = 0; columna < mapa[0].length; columna++) {
            if (mapa[0][columna] == CAMINO) {
                caminosEnBorde.add(new Point(columna, 0));
            }
            if (mapa[mapa.length - 1][columna] == CAMINO) {
                caminosEnBorde.add(new Point(columna, mapa.length - 1));
            }
        }

        if (caminosEnBorde.isEmpty()) {
            return new Point(0, 0);
        }
        return primera ? caminosEnBorde.get(0) : caminosEnBorde.get(caminosEnBorde.size() - 1);
    }

    private static int[][] inicializarParedes(int filas, int columnas) {
        int[][] mapa = new int[filas][columnas];
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                mapa[fila][columna] = PARED;
            }
        }
        return mapa;
    }

    private static int ajustarDimension(int dimension) {
        int dimensionMinima = Math.max(dimension, MIN_DIMENSION);
        return dimensionMinima % 2 == 0 ? dimensionMinima + 1 : dimensionMinima;
    }

    private static List<int[]> obtenerVecinosNoVisitados(int fila, int columna, int[][] mapa) {
        List<int[]> vecinos = new ArrayList<>();
        int[][] direcciones = {
                { -2, 0 },
                { 2, 0 },
                { 0, -2 },
                { 0, 2 }
        };

        for (int[] dir : direcciones) {
            int nuevaFila = fila + dir[0];
            int nuevaColumna = columna + dir[1];
            if (nuevaFila > 0 && nuevaFila < mapa.length - 1 && nuevaColumna > 0 && nuevaColumna < mapa[0].length - 1) {
                if (mapa[nuevaFila][nuevaColumna] == PARED) {
                    vecinos.add(new int[] { nuevaFila, nuevaColumna });
                }
            }
        }
        return vecinos;
    }
    public Point celdaCaminoAleatoria(Random rnd) {
        int filas = mapa.length;
        int cols = mapa[0].length;

        while (true) {
            int f = 1 + rnd.nextInt(filas - 2);
            int c = 1 + rnd.nextInt(cols - 2);

            if (mapa[f][c] == 0) {
                return new Point(c, f); // x=col, y=fila
            }
        }
    }


}


















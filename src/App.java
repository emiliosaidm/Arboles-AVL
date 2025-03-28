public class App {
  public static void main(String[] args) throws Exception {
    ArbolAVL<Integer> arbol = new ArbolAVL();

    arbol.inserta(100);
    arbol.inserta(7);
    arbol.inserta(23);
    arbol.inserta(5);
    arbol.inserta(4);
    arbol.inserta(-10);
    arbol.inserta(10);
    arbol.inserta(6);
    arbol.inserta(300);
    arbol.inserta(8);
    arbol.inserta(50);

    System.out.println(arbol.imprimePorNivel());

    arbol.borra(7);
    arbol.borra(8);
    arbol.borra(10);

    System.out.println("----------------------- \n");

    System.out.println(arbol.imprimePorNivel());
  }
}

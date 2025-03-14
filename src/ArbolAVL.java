public class ArbolAVL<T extends Comparable<T>> {
  NodoAVL<T> raiz;
  int cont;

  public ArbolAVL() {
    raiz = null;
    cont = 0;
  }

  public void setRaiz(NodoAVL<T> raiz) {
    this.raiz = raiz;
  }

  public NodoAVL<T> getRaiz() {
    return raiz;
  }

  public void insertar(T elem) {
    // Si la raiz es vacia, se asigna el elemento a la raiz.
    if (raiz == null) {
      raiz = new NodoAVL<T>(elem);
      cont++;
      return;
    }

    // Buscamos el nodo papa del elemento que va insertarse
    NodoAVL<T> actual = raiz;
    NodoAVL<T> papa = null;
    boolean encontre = false;
    // Buscamos el nodo padre del nuevo nodo, de acuerdo al nuevo elemento, para
    // identificar su posicion.
    while (actual != null && !encontre) {
      papa = actual;
      if (actual.getDato().compareTo(elem) > 0)
        actual = actual.getIzq();
      else if (actual.getDato().compareTo(elem) < 0)
        actual = actual.getDer();
      else
        encontre = true;
    }
    // Si se encontro el elemento, omitimos la inserción.
    if (encontre)
      throw new RuntimeException("Ya existe ese elemento");

    // Crear el nuevo nodo.
    NodoAVL<T> nuevo = new NodoAVL<T>(elem);

    // Determinar si el nodo va a la derecha o izquierda del papa.
    if (papa.getDato().compareTo(elem) <= 0) {
      papa.setDer(nuevo);
    } else {
      papa.setIzq(nuevo);
    }
    nuevo.setPapa(papa);
    cont++;

    // Ahora, actualizar los factores de equilibrio y, en caso de encontrar |2|,
    // rotar.
    actual = nuevo;
    boolean termine = false;
    while (actual.getPapa() != null && !termine) {
      papa = actual.getPapa();

      if (papa.getDer() == actual)
        papa.setFactorEquilibrio(papa.getFactorEquilibrio() + 1);
      else
        papa.setFactorEquilibrio(papa.getFactorEquilibrio() - 1);
      if (papa.getFactorEquilibrio() == 0)
        termine = true;

      else if (Math.abs(papa.getFactorEquilibrio()) == 2) {
        // En este caso, ya habría un árbol desbalanceado. Por lo que hay que rotar.
        NodoAVL<T> papaDePapa = papa.getPapa();
        // Nueva raiz del subárbol, después de la rotación. Este logra balancear el
        // árbol.
        NodoAVL<T> nuevaRaiz = rota(papa);
        if (papaDePapa == null)
          raiz = nuevaRaiz;
        else {
          if (papaDePapa.getDato().compareTo(nuevaRaiz.getDato()) > 0)
            papaDePapa.setIzq(nuevaRaiz);
          else
            papaDePapa.setDer(nuevaRaiz);

          nuevaRaiz.setPapa(papaDePapa);
        }
        termine = true;
      }
      actual = papa;
    }
  }

  public void borra(T elem) {
    NodoAVL<T> actual = busca(elem);

    if (actual == null)
      throw new RuntimeException("No se encontró ese elemento en el árbol");

    // Existen 3 opciones:
    // 1. Solo es una hoja.
    if (actual.getIzq() == null && actual.getDer() == null) {
      if (actual == raiz) {
        raiz = null;
      } else {
        NodoAVL<T> papa = actual.getPapa();

        if (papa.getIzq() == actual) {
          papa.setIzq(null);
        } else {
          papa.setDer(null);
        }
      }
      cont--;

    } else if ((actual.getIzq() == null || actual.getDer() == null)) {
      // 2. Tiene 1 hijo.
      NodoAVL<T> hijo = actual.getIzq() != null ? actual.getIzq() : actual.getDer();
      if (actual == raiz) {
        raiz = hijo;
        cont--;
        return;
      }
      if (actual.getPapa().getIzq() == actual)
        actual.getPapa().setIzq(hijo);
      else
        actual.getPapa().setDer(hijo);

      hijo.setPapa(actual.getPapa());

      cont--;
    } else {
      // Caso 3: Nodo con 2 hijos.
      NodoAVL<T> sucesor = actual.getDer();
      while (sucesor.getIzq() != null) {
        sucesor = sucesor.getIzq();
      }
      actual.setDato(sucesor.getDato());

      if (sucesor.getPapa().getIzq() == sucesor) {
        sucesor.getPapa().setIzq(sucesor.getDer());
      } else {
        sucesor.getPapa().setDer(sucesor.getDer());
      }

      if (sucesor.getDer() != null) {
        sucesor.getDer().setPapa(sucesor.getPapa());
      }
      cont--;
    }

    // Ahora, se tienen que actualizar los factores de equilibrio.
    boolean termine = false;
    while (!termine && actual != raiz) {
      NodoAVL<T> papa = actual.getPapa();
      if (papa.getDer() == actual)
        papa.setFactorEquilibrio(papa.getFactorEquilibrio() - 1);
      else
        papa.setFactorEquilibrio(papa.getFactorEquilibrio() + 1);

      if (Math.abs(papa.getFactorEquilibrio()) == 1)
        termine = true;
      else if (Math.abs(papa.getFactorEquilibrio()) == 2) {
        // En este caso, ya habría un árbol desbalanceado. Por lo que hay que rotar.
        NodoAVL<T> papaDePapa = papa.getPapa();
        // Nueva raiz del subárbol, después de la rotación. Este logra balancear el
        // árbol.
        NodoAVL<T> nuevaRaiz = rota(papa);
        if (papaDePapa == null)
          raiz = nuevaRaiz;
        else {
          if (papaDePapa.getDato().compareTo(nuevaRaiz.getDato()) > 0)
            papaDePapa.setIzq(nuevaRaiz);
          else
            papaDePapa.setDer(nuevaRaiz);

          nuevaRaiz.setPapa(papaDePapa);
        }
      }
      actual = papa;
    }

  }

  private NodoAVL<T> rota(NodoAVL<T> actual) {
    NodoAVL<T> alfa, beta, gamma, B, C, raiz;
    raiz = actual;
    if (actual.getFactorEquilibrio() == -2 && actual.getIzq().getFactorEquilibrio() <= 0) { // Caso izq-izq
      System.out.println("Rotación izq-izq");
      alfa = actual;
      beta = alfa.getIzq();
      gamma = beta.getIzq();
      C = beta.getDer();

      beta.setDer(alfa);
      alfa.setPapa(beta);
      alfa.setIzq(C);

      if (C != null)
        C.setPapa(alfa);

      beta.setPapa(null);

      raiz = beta;
    } else if (actual.getFactorEquilibrio() == 2 && actual.getDer().getFactorEquilibrio() > 0) { // Caso der-der
      System.out.println("Rotación izq-izq");
      alfa = actual;
      beta = alfa.getDer();
      gamma = beta.getDer();
      B = beta.getIzq();

      beta.setIzq(alfa);
      alfa.setPapa(beta);

      alfa.setDer(B);
      if (B != null)
        B.setPapa(alfa);

      beta.setPapa(null);

      raiz = beta;
    } else if (actual.getFactorEquilibrio() == -2 && actual.getIzq().getFactorEquilibrio() > 0) { // izq-der
      alfa = actual;
      beta = alfa.getIzq();
      gamma = beta.getDer();
      B = gamma.getIzq();
      C = gamma.getDer();

      gamma.setIzq(beta);
      beta.setPapa(gamma);
      gamma.setDer(alfa);
      alfa.setPapa(gamma);

      beta.setDer(B);
      if (B != null)
        B.setPapa(beta);

      alfa.setIzq(C);
      if (C != null)
        C.setPapa(alfa);

      gamma.setPapa(null);

      raiz = gamma;
    } else if (actual.getFactorEquilibrio() == 2 && actual.getDer().getFactorEquilibrio() < 0) { // Caso der-izq
      alfa = actual;
      beta = alfa.getDer();
      gamma = beta.getIzq();
      B = gamma.getIzq();
      C = gamma.getDer();

      gamma.setIzq(alfa);
      alfa.setPapa(gamma);
      gamma.setDer(beta);
      beta.setPapa(gamma);
      alfa.setDer(B);

      if (B != null)
        B.setPapa(alfa);

      beta.setIzq(C);

      if (C != null)
        C.setPapa(beta);

      gamma.setPapa(null);

      raiz = gamma;
    }

    // La raiz es el nodo que se encuentra en la cima de la rotación. Actualizamos
    // el fe de este subárbol.
    actualizaFe(raiz);
    return raiz;
  }

  public NodoAVL<T> busca(T elem) {
    if (raiz == null)
      return null;

    return busca(raiz, elem);
  }

  private NodoAVL<T> busca(NodoAVL<T> actual, T elem) {
    if (actual == null)
      return null;

    if (actual.getDato().equals(elem))
      return actual;

    if (actual.getDato().compareTo(elem) > 0)
      return busca(actual.getIzq(), elem);
    else
      return busca(actual.getDer(), elem);

  }

  private void actualizaFe(NodoAVL<T> actual) {
    if (actual == null)
      return;

    actualizaFe(actual.getIzq());
    actualizaFe(actual.getDer());

    int feIzq = actual.getIzq() == null ? 0 : altura(actual.getIzq());
    int feDer = actual.getDer() == null ? 0 : altura(actual.getDer());

    actual.setFactorEquilibrio(feDer - feIzq);
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    toString(raiz, builder, "", "");
    return builder.toString();
  }

  private void toString(NodoAVL<T> actual,
      StringBuilder builder,
      String prefix,
      String childrenPrefix) {
    if (actual != null) {
      builder.append(prefix);
      builder.append(actual.getDato() + " fe: " + actual.getFactorEquilibrio());
      builder.append("\n");

      toString(actual.getIzq(), builder, childrenPrefix, childrenPrefix);
      toString(actual.getDer(), builder, childrenPrefix, childrenPrefix);
    }

  }

  private int altura(NodoAVL<T> actual) {
    if (actual == null) {
      return 0;
    }

    int izq = altura(actual.getIzq());
    int der = altura(actual.getDer());

    return 1 + Math.max(izq, der);
  }

  public static void main(String[] args) {
    ArbolAVL<Integer> arbol = new ArbolAVL();
    for (int i = 0; i < 5; i++) {
      arbol.insertar(i);
    }
    arbol.borra(2);
  }

}

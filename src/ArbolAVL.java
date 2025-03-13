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
    // Si se encontro el elemento, lo omitimos la inserción.
    if (encontre)
      throw new RuntimeException("Ya existe ese elemento");

    // Crear el nuevo nodo.
    NodoAVL<T> nuevo = new NodoAVL<T>(elem);

    // Determinar si el nodo va a la derecha o izquierda del papa.
    if (papa.getDato().compareTo(elem) <= 0) {
      papa.setIzq(nuevo);
    } else {
      papa.setDer(nuevo);
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
        papa = rota(papa);
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
      else if (Math.abs(papa.getFactorEquilibrio()) == 2)
        papa = rota(papa);

      actual = papa;
    }

  }

  private NodoAVL<T> rota(NodoAVL<T> nodo) {
    return nodo;
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

}

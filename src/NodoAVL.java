public class NodoAVL<T> {
  private T dato;
  private NodoAVL<T> izq, der, papa;
  private int factorEquilibrio;

  public NodoAVL(T dato) {
    this.dato = dato;
    this.izq = null;
    this.der = null;
    this.papa = null;
    this.factorEquilibrio = 0;
  }

  public T getDato() {
    return dato;
  }

  public void setDato(T dato) {
    this.dato = dato;
  }

  public NodoAVL<T> getIzq() {
    return izq;
  }

  public void setIzq(NodoAVL<T> izq) {
    this.izq = izq;
  }

  public NodoAVL<T> getDer() {
    return der;
  }

  public void setDer(NodoAVL<T> der) {
    this.der = der;
  }

  public NodoAVL<T> getPapa() {
    return papa;
  }

  public void setPapa(NodoAVL<T> papa) {
    this.papa = papa;
  }

  public int getFactorEquilibrio() {
    return factorEquilibrio;
  }

  public void setFactorEquilibrio(int fe) {
    factorEquilibrio = fe;
  }

}

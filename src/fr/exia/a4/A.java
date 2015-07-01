package fr.exia.a4;

public class A {

	int x = 0;
	
	static class B {
		public void run() {
			// Interdit, car associé à l'instance de A
			// x++;
		}
	}
	
	class C {
		public void run() {
			// Là on peut, les instances sont liées
			x++;
		}
	}
	
	public static void main(String[] args) {
		// Instanciation classique
		A a = new A();
		// Instanciation classique aussi, mais la sous-classe a une "boîte"
		B b = new A.B();
		// Là on instancie à partir d'une instance :-)
		C c = a.new C();
	}
	
}

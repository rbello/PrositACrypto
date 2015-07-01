package fr.exia.a4;

public class Test {

	static byte NONE = 0x000;
	static byte INFO = 0x001;
	static byte WARN = 0x002;
	static byte EROR = 0x004;
	static byte ALL  = 0x07f; // 128
	
	public static void main(String[] args) {
		System.out.println((int)NONE);
		System.out.println((int)INFO);
		System.out.println((int)WARN);
		System.out.println((int)EROR);
		System.out.println((int)ALL);
	}
	
}

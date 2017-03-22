package fr.exia.a4;

import vecmath.Matrix3f;

public class Crypto1 {



	public static void main(String[] args) {

		// First message
		String s1 = "Od pdwulfh gh frgdjh gx whpsoh hvw, sdu oljqh: -wurlv -wurlv -txdwuh chur xq xq wurlv wurlv wurlv";

		// Uncrypt
		String s2 = new ROT().decryption(s1, 3);

		// Debug
		System.out.println("Original data  : " + s1);
		System.out.println("Uncrypted data : " + s2);
		
		// Voici la matrice obtenue
		Matrix3f mat = new Matrix3f(
			-3, -3, -4,
		     0,  1,  1,
			 3,  3,  3
		);
		
		// Second message
		String s3 = "gvqsxedxefhi qccadw qccadwqdwrgqbkrpkrpgsdgefwneci hfcxcp rvwdgkwuegvp gvqsxeqdwrgqpbkrpkrpgsdgefwneci mykphx";
		
		// Padding
		while (s3.length() % mat.rows != 0) {
			s3 += "o";
		}
		
		// Modulo alphabet
		int mod = 'z' - 'a' + 1;
		
		// On inverse la matrice
		mat.invert();
		
		
		
		
//		int[] x3 = toCharArray(s3);
//		
//		String s4 = hill(x3, mat);
//		
//		System.out.println(s4);
//		
//		int[] x4 = toCharArray(s3);
//		
//		mat.invert();
//		
//		String s5 = hill(x4, mat);
//		
//		System.out.println(s5);
		
		

	}

	private static int[] toCharArray(String str) {
		// On en fait un tableau, et on ordonne les caractères par rapport à leurs positions dans l'alphabet
		int[] x3 = new int[str.length()];
		int i = 0;
		for (char c : str.toCharArray()) {
			x3[i++] = c - 97;
		}
		return x3;
	}
	
	public static String hill(int[] str, Matrix3f key) {
	    
		StringBuilder sb = new StringBuilder();
		int c[] = new int[300];
		

	    
	    int i = 0;
	    int zz = 0;
        for (int b = 0; b < str.length / 3; ++b)
        {
	        for (int j = 0; j < 3; ++j)
	        {
		        for (int x = 0; x < 3; ++x)
		        {
		        	c[i] += key.getElement(j, x) * str[x + zz];
		        }
		        i++;
	        }
	        zz+=3;
        }
            
	    for (int z = 0; z < str.length; z++) {
	        sb.append((char)((c[z] % 26) + 97));
	    }
	
		return sb.toString();
	}



}

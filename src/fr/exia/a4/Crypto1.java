package fr.exia.a4;

import vecmath.Matrix3f;

public class Crypto1 {



	public static void main(String[] args) {

		// First message
		String s1 = "Od pdwulfh gh frgdjh gx whpsoh hvw, sdu oljqh: -wurlv -wurlv -txdwuh chur xq xq wurlv wurlv wurlv";

		// Uncrypt
		String s2 = new ROT().decryption(s1, 3);

		// Debug
		System.out.println("FIRST MESSAGE");
		System.out.println("  Original data  : " + s1);
		System.out.println("  Uncrypted data : " + s2);
		
		// Voici la matrice obtenue
		Matrix3f mat = new Matrix3f(
			-3, -3, -4,
		     0,  1,  1,
			 3,  3,  3
		);
		
		// Second message
		String s3 = "gvqsxedxefhi qccadw qccadwqdwrgqbkrpkrpgsdgefwneci hfcxcp rvwdgkwuegvp gvqsxeqdwrgqpbkrpkrpgsdgefwneci mykphx";
		
		// Dechiffrement du message
		String s4 = new HillCipher3x3().decryption(s3, mat);
		
		// Debug
		System.out.println("SECOND MESSAGE");
		System.out.println("  Original data  : " + s3);
		System.out.println("  Uncrypted data : " + s4);

	}

}

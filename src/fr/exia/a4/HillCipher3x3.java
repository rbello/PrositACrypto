package fr.exia.a4;

import vecmath.Matrix3f;

public class HillCipher3x3 extends HillCipher<Matrix3f, String> {

	public HillCipher3x3() {
		super(3);
	}

	@Override
	public String encryption(String data, Matrix3f key) {

		// Prepare data
		data = normalizeText(data);
		data = fillStringPadding(data);
		
		// Prepare output string
		StringBuilder sb = new StringBuilder();
		
		// Grab blocks of text based on N
		for (int sIndex = 0; sIndex < data.length(); sIndex += N) {
			String temp = data.substring(sIndex, sIndex + N);
			// For every row in the key matrix
			for (int r = 0; r < N; r++) {
				// New encrypted block of size N
				int[] block = new int[N];
				// For every column in the key matrix
				for (int c = 0; c < N; c++) {
					// Set the character values as numbers
					block[c] = (int)key.getElement(r, c) * toNumber(temp.charAt(c));
				}
				// Getting encrypted characters out of out block
				int sum = 0;
				for (int i : block) {
					sum += i;
				}
				// Add character to the encryption string
				sb.append(toCharacter(sum));
			}
		}
		
		// Return 
		return sb.toString();
	}

	@Override
	public String decryption(String data, Matrix3f key) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

package fr.exia.a4;

public class SDES_BitAsBoolean implements ICipher<String, String> {

	@Override
	public String encryption(String data, String key) {
		
		boolean[] master_key = new boolean[10];
        for (int i = 0; i < key.length(); i++)
        {
        	master_key[i] = char2bin(key.charAt(i));
        }
        
        
		return null;
	}

	public boolean char2bin(char bit)
    {
        if (bit == '0') return false;
        else if (bit == '1') return true;
        else throw new RuntimeException("Key should be in binary format [0,1]");
    }
	
	/**
	 * La fonction de permutation est définie telle que :
	 * 	P10(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10) = (k3, k5, k2, k7, k4, k10, k1, k9, k8, k6)
	 * 
	 * @param key
	 * @return
	 */
    boolean[] P10(boolean[] key)
    {
        // 0 1 2 3 4 5 6 7 8 9
        // 2 4 1 6 3 9 0 8 7 5
        boolean[] permutatedArray = new boolean[10];
        permutatedArray[0] = key[2];
        permutatedArray[1] = key[4];
        permutatedArray[2] = key[1];
        permutatedArray[3] = key[6];
        permutatedArray[4] = key[3];
        permutatedArray[5] = key[9];
        permutatedArray[6] = key[0];
        permutatedArray[7] = key[8];
        permutatedArray[8] = key[7];
        permutatedArray[9] = key[5];
        return permutatedArray;
    }
    
    boolean[] Circular_left_shift(boolean[] key, int bits)
    {
        boolean[] shifted = new boolean[key.length];
        int index = 0;
        for (int i = bits; index < key.length; i++)
        {
            shifted[index++] = key[i % key.length]; 
        }
        return shifted;
    }
	
	@Override
	public String decryption(String data, String key) {
		// TODO Auto-generated method stub
		return null;
	}

}

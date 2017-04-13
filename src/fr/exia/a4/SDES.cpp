#include "stdafx.h"
#include <Windows.h>

#define CSL(c, sh, sz) ((c << sh) | ((c >> (sz - sh)) & ((1 << sz) - 1)))
#define K(c, i, j) (((c & (1 << i)) >> i ) << j)

void unitTests();

char* bin(DWORD n, int s = 32)
{
	static char buf[32];
	char *p = buf;

    for (int i = 1 << (s-1); i > 0; i = i / 2)
        *p++ = (n & i) ? '1' : '0';

	*p = 0;

	return buf;
}

/*
BYTE S0[] = {
	0xB1, //10 11 00 01
	0x1B, //00 01 10 11
	0xD8, //11 01 10 00
	0xB7  //10 11 01 11
};

BYTE S1[] = {
	0xE4, //11 10 01 00
	0xD2, //11 01 00 10
	0x13, //00 01 00 11
	0xC6  //11 00 01 10
};
*/

DWORD S0 = 0xB7D81BB1;
DWORD S1 = 0xC613D2E4;

WORD P10(WORD k)
{
	return (K(k,7,9)|K(k,5,8)|K(k,8,7)|K(k,3,6)|K(k,6,5)|K(k,0,4)|K(k,9,3)|K(k,1,2)|K(k,2,1)|K(k,4,0)) & 0x3FF;
}

BYTE P8(WORD k)
{
	return (K(k,4,7)|K(k,7,6)|K(k,3,5)|K(k,6,4)|K(k,2,3)|K(k,5,2)|K(k,0,1)|K(k,1,0)) & 0xFF;
}

BYTE P4(BYTE k)
{
	return (K(k,2,3)|K(k,0,2)|K(k,1,1)|K(k,3,0)) & 0xF;
}

WORD shift(WORD c, BYTE shift)
{
	WORD a, b;
	
	a = (c >> 5) & 0x1F;
	b = c & 0x1F;

	return ((CSL(a, shift, 5) & 0x1F) << 5) | (CSL(b, shift, 5) & 0x1F);
}

BYTE K1(WORD key)
{
	return P8(shift(P10(key), 1));
}

BYTE K2(WORD key)
{
	return P8(shift(P10(key), 3));
}

BYTE IP(BYTE d)
{
	return (K(d,6,7)|K(d,2,6)|K(d,5,5)|K(d,7,4)|K(d,4,3)|K(d,0,2)|K(d,3,1)|K(d,1,0)) & 0xFF;
}

BYTE RIP(BYTE d)
{
	return (K(d,4,7)|K(d,7,6)|K(d,5,5)|K(d,3,4)|K(d,1,3)|K(d,6,2)|K(d,0,1)|K(d,2,0)) & 0xFF;
}

BYTE EP(BYTE n)
{
	return (K(n,0,7)|K(n,3,6)|K(n,2,5)|K(n,1,4)|K(n,2,3)|K(n,1,2)|K(n,0,1)|K(n,3,0)) & 0xFF;
}

BYTE SBox(BYTE box, BYTE input)
{
	BYTE row, col;
	
	row = (BYTE)(K(input,3,1)|K(input,0,0));
	col = (BYTE)(K(input,2,1)|K(input,1,0));

	return ((((!box ? S0 : S1) >> (row << 3)) & 0xFF) >> (col << 1)) & 0x3;
}

BYTE FK(BYTE d, BYTE sk)
{
	BYTE l = (d >> 4) & 0xF, r = (d) & 0xF;

	//F Mapping
	BYTE f = (EP(r) ^ sk);
	BYTE fl = (f >> 4) & 0xF, fr = f & 0xF;

	f = P4((SBox(0, fl) << 2) | SBox(1, fr));

	return ((l ^ f & 0xF) << 4) | r; 
}

BYTE SW(BYTE d)
{
	return ((d & 0xF) << 4)|((d >> 4) & 0xF);
}

BYTE encrypt(BYTE b, WORD key)
{
	BYTE k1 = K1(key), k2 = K2(key);

	return RIP(FK(SW(FK(IP(b), k1)), k2));
}

BYTE decrypt(BYTE b, WORD key)
{
	BYTE k1 = K1(key), k2 = K2(key);

	return RIP(FK(SW(FK(IP(b), k2)), k1));
}

void decryptString(BYTE *b, char *dst, WORD key)
{
	while(*b)
		*dst++ = (char)decrypt(*b++, key);

	*dst = 0;
}

void encryptString(BYTE *b, char *dst, WORD key)
{
	while(*b)
		*dst++ = (char)encrypt(*b++, key);

	*dst = 0;
}

WORD bin2word(char *str)
{
	WORD r = 0;
	int i = strlen(str);

	while(*str)
		r |= ((*str++ - '0') & 1) << --i;

	return r;
}

void bruteForce()
{
	FILE *fd = fopen("msg.txt", "r");
	fseek(fd, 0L, SEEK_END);
	int sz = ftell(fd);
	rewind(fd);

	BYTE *buf = (BYTE*)malloc(sizeof(BYTE) * (sz + 1));
	fread(buf, sz, 1, fd);
	fclose(fd);
	
	//BRUTE FORCE MUHAH
	WORD key = 0x320; //end at 0x33F (1100100000 => 1100111111)
	BYTE *p = buf;

	fd = fopen("decrypted.txt", "w");
	char decrypted[512];
	for(WORD i = key; i <= 0x33F; i++)
	{
		memset(decrypted, 0, sizeof(char) * 512);
		decryptString(buf, decrypted, i);
		fprintf(fd, "%s: %s\n", bin(i, 10), decrypted); 
	}

	fclose(fd);
}

void benchmark()
{
	FILE *fd = fopen("lorem.txt", "r");
	fseek(fd, 0L, SEEK_END);
	int sz = ftell(fd);
	rewind(fd);

	BYTE *buf = (BYTE*)malloc(sizeof(BYTE) * (sz + 1));
	fread(buf, sz, 1, fd);
	fclose(fd);

	char *enc = (char*)malloc(sizeof(char) * (sz + 1));
	
	WORD key = bin2word("0111010001");

	LARGE_INTEGER li1, li2, freq;
	double res;
	QueryPerformanceFrequency(&freq);
	QueryPerformanceCounter(&li1);

	for(int i = 0; i < 100; i++)
		encryptString(buf, enc, key);

	QueryPerformanceCounter(&li2);

	res = (double)(li2.QuadPart-li1.QuadPart)/freq.QuadPart;

	printf("Elapsed time : %f ms\n", res*1000);
}

void testSDES()
{
	WORD key = bin2word("1100101101");
	
	char buf[128];
	
	encryptString((BYTE*)"hello world", buf, key);

	char *p = buf;
	while(*p)
		printf("%x ", (BYTE)*p++);

	printf("\n");
	system("PAUSE");
};

void unitTests()
{
	WORD key = bin2word("1010000010");;
	BYTE b = 'r';

	printf("b: %s\n", bin(b, 8));

	WORD tmp = P10(key);
	printf("p10: %s\n", bin(tmp, 10));

	tmp = shift(tmp, 1);
	printf("shift: %s\n", bin(tmp, 10));

	WORD k1 = P8(tmp);
	printf("k1: %s\n", bin(k1, 8));

	WORD k2 = shift(tmp, 2); 
	printf("shift: %s\n", bin(k2, 10));

	k2 = P8(k2);
	printf("k2: %s\n", bin(k2, 8));

	printf("==============\n");
	printf("clear: %s\n", bin(b, 8));
	
	BYTE ip = IP(b);
	printf("ip: %s\n", bin(ip, 8));

	BYTE l = (ip >> 4) & 0xF, r = (ip) & 0xF;
	printf("L: %s ", bin(l, 4));
	printf("-- R: %s\n", bin(r, 4));

	BYTE ep = EP(r);
	printf("ep: %s\n", bin(ep, 8));

	ep ^= k1;
	printf("ep ^ k1: %s\n", bin(ep, 8));

	BYTE fl = (ep >> 4) & 0xF, fr = ep & 0xF;
	BYTE s0 = SBox(0, fl), s1 = SBox(1, fr);
	printf("s0 : %s", bin(s0, 2));
	printf(" -- s1 : %s\n", bin(s1, 2));

	ep = P4((s0 << 2) | s1);
	printf("p4: %s\n", bin(ep, 4));

	ep ^= l;
	printf("p4 ^ l: %s\n", bin(ep, 4));

	BYTE fk = (ep << 4) | r;
	printf("fk: %s\n", bin(fk, 8));
}
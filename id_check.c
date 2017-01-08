#include <stdio.h>


int checksum(int number1[6],int number2[7])
{
	//Checksum
	int sum = number2[6]/*Last number of my card*/
	int tmp,res;
	//Hardcoding is fun 
	tmp = number1[0] * 2 + number1[1] * 3 + number1[2] * 4 + number1[3] * 5 + number1[4] * 6 + number1[5] * 7 + number2[0] * 8 + number2[1] * 9 + number2[2] * 2 + number2[3] * 3 + number2[4] * 4 + number2[5] * 5; 
	res = 11-(tmp%11);
	if (res == sum)
	{	//It's vaild card
		return 1;
	}
	else {
		//It's not vaild card
		return 0;
	}
	 
	
}

char* getbirth (int number1[6], int number2[7])
{
	int year[4],month[2],day[2],gender/*0= male, 1=female*/,year2;

	static char final[20];
	year[2] = number1[0];
	year[3] = number1[1];
	month[0] = number1[2];
	month[1] = number1[3];
	day[0] = number1[4];
	day[1] = number1[5];
	if (number2[0] == 1)
	{
		gender=0;
		year2=1900;
	}
	else if (number2[0] == 2)
	{
		gender=1;
		year2=1900;
	}
	else if (number2[0] == 3)
	{
		gender=0;
		year2=2000;
	}
	else if (number2[0] == 4)
	{
		gender=1;
		year2=2000;
	}
	else if (number2[0] == 5)
	{
		gender=0;
		year2=19001;
	}
	else if (number2[0] == 6)
	{
		gender=1;
		year2=19001;
	}
	else if (number2[0] == 7)
	{
		gender=0;
		year2=20001;
	}
	else if (number2[0] == 8)
	{
		gender=1;
		year2=20001;
	}

	if(year2==1900 || year2 == 19001)
	{
		year[0] = 1;
		year[1] = 9;
	}

	else if(year2==2000 || year2 == 20001)
	{
		year[0] = 2;
		year[1] = 0;
	}
	final[0] = year[0]+48;
	final[1] = year[1]+48;
	final[2] = year[2]+48;	
	final[3] = year[3]+48;	
	final[4] = ' ';
	final[5] = month[0]+48;		
	final[6] = month[1]+48;	
	final[7] = ' ';
	final[8] = day[0]+48;	
	final[9] = day[1]+48;
	final[10] = 0;
	// Testing	
	//printf("%d %c %d",year[0],final[0],final[0]);
	return final;
	
}
int main () {
	int number1[6]={7,5,0,4,0,3},number2[7]={2,6,4,6,1,2,7};
	

	printf("%s\n",checksum (number1,number2)? "vaild" : "NOT vaild");
	printf("%s\n",getbirth (number1,number2));
	

}

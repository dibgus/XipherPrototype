
import java.util.Scanner;
import java.util.Arrays;
public class random {

	/**
	 * key ideas:
	 * 		0				00000				<userinput>					<some delimeter>	
	 *	set bitsize?	settings (0-16)			keys/input for functions	end of user input block
	 *	(5 bits to set	0 is ?					OR prompt like credentials?						
	 *	0-16 if 1)		1-8 functions/inverses	can be made into certs file											
	 *					9 caesar
	 *					10 transposition
	 *					11 invert binary
	 *					12 block (binary key)
	 *					13 block (string key)
	 *					14 go to binary
	 *					15 go to string
	 *					16 <debug print>temporary
	 *example of key bits=16|3|5
	 *	1 11111	00011 00101
	 *  1111110001100101 
	 */
	
	/**
	 * BUG LOG:
	 * *xor and xorb options dont work with strings
	 * *some function does not properly decode all characters
	 * *'compact' is the buggiest thing in hell in conjunction with obfuscation: expected with how algorithms work
	 * >(compact tends to truncate extra bits (insignificant zeroes) that are required to modify the string properly)
	 * >>solution: change compact to a compression algorithm? OR characters that do not fit into buffer get bits truncated and shifted to their own octet/whatever
	 * rectangular transposition ciphers fail to work at all
	 * 
	 * TODO:
	 * stenographic encoding (the security of my encryption increases tenfold)
	 * File encryption
	 * Improved hashes (binary representations of keys?)
	 * better 'compact'
	 * see how secure my method of encryption is (seems nonstandard for some keys, making it harder?)
	 * @param args
	 */
	private static byte bitSize = 8;
	private static boolean compact = false;
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		String previousCommand = "";
		String previousKey = "";
		while(true)
		{
		System.out.print("Enter expression: ");
		String in = input.nextLine();
		System.out.print("Enter key: ");
		String ops = input.nextLine();
		if(in.equals("!p"))
			in = previousCommand;
		if(ops.equals("!p"))
			ops = previousKey;
		String encrypted = easyEncrypt(in, ops);
		System.out.println(encrypted);
		previousCommand = in; previousKey = ops;
		System.out.println(easyDecrypt(encrypted, ops));
		}
		//System.out.println(in.length());
		/*
		System.out.println(function1(in));
		System.out.println(function2(in));
		System.out.println("--------------");
		System.out.println(reverseFunction1(function1(in)));
		System.out.println(reverseFunction2(function2(in)));
		System.out.println(Arrays.toString(charToASCII(in)));
		System.out.println(BinarySequence.intToBinary(9));
		System.out.println(BinarySequence.stringToBinary(in));
		while(true)
		{
			System.out.print("Enter new expression: ");
			in = input.nextLine();
			System.out.print("Enter list of operation numbers (1 .. 10) (separated by spaces) : ");
			String[] operations = input.nextLine().split(" ");
			int[] ops = new int[operations.length];
			for(int i = 0; i < operations.length; i++)
			{
				ops[i] = Integer.parseInt(operations[i]);
			}
			String obfus = obfuscate(ops, in);
			System.out.println(obfus);
			System.out.println("Silly defus: " + defuscate(ops, in));
			System.out.println("translate back: " + defuscate(ops, obfus));
		}
		*/
		//input.close();
	}	
	
	/*
	 * DEPRECIATED
	 * @param ops
	 * stores a variety of bytes that refer to what function to apply.
	 * obfus can be reversed by running thru defuscate with ops
	 * @return an obfuscated string

	public static String obfuscate(int[] ops, String str)
	{
		for(int op : ops)
		{
			if(op == 1) str = function1(str);
			if(op == 2) str = function2(str);
			if(op == 3) str = function3(str);
			if(op == 4) str = function4(str);
			if(op == 5) str = caesarCipher(str);
			if(op == 6) str = reverseFunction1(str);
			if(op == 7) str = reverseFunction2(str);
			if(op == 8) str = reverseFunction3(str);
			if(op == 9) str = reverseFunction4(str);
			if(op == 10) str = reverseCaesarCipher(str);
		}
		return str;
	}
	public static String defuscate(int[] ops, String str)
	{
		for(int i = ops.length -1; i >= 0; i--)
		{
			if(ops[i] == 1) str = reverseFunction1(str);
			if(ops[i] == 2) str = reverseFunction2(str);
			if(ops[i] == 3) str = reverseFunction3(str);
			if(ops[i] == 4) str = reverseFunction4(str);
			if(ops[i] == 5) str = reverseCaesarCipher(str);
			if(ops[i] == 6) str = function1(str);
			if(ops[i] == 7) str = function2(str);
			if(ops[i] == 8) str = function3(str);
			if(ops[i] == 9) str = function4(str);
			if(ops[i] == 10) str = caesarCipher(str);
		}
		return str;
	}
	*/
	
	//encryptOrder settings: 1-8 = obfuscation functions | b = convert to octet binary OR 'b=20' to specify buffer size | s = convert binary to string | 'cc=20' = Caesar cipher x amount
	//'xor=words' Block cipher with key x | 'xorb=100110' Block cipher with binary string | 'tc=10x20' transposition cipher with an x by y matrix| ivb invert binary
	//'bits=8' set bit size to a different sized buffer: DO NOT GO ABOVE 16 (buggy and not as reliable: likely because java characters are already 16 bit)
	// 'sten=1.8' apply stenography to a string (numbers denote method to use): need to specify buffers with 'bits=x" or append '.x' at the end AND ALGORITHM 1 EXPANDS THE STRING BY <buffer * len>!
	//compact = flexible buffer sizes. good for using minimal data, but currently does not work properly because it intrensically removes 'insignificant' bits needed to decrypt algorithmically
	//i = percent inflation info
	//DEBUG FLAG: p to print
	//i.e: 1|2|5|b|5|4|s|1|3|cc=40
	
	public static String easyEncrypt(String expression, String encryptOrder)
	{
		if(expression.length() == 0)
		{
			System.out.println("Blank expression to encrypt!");
			return "";
		}
		encryptOrder = encryptOrder.toLowerCase();
		if(encryptOrder.length() > 4 && encryptOrder.substring(0,4).equals("bits"))
			bitSize = (byte)Integer.parseInt(parameterResolver(encryptOrder.substring(0, encryptOrder.indexOf("|"))));
		String encrypted = expression;
		for(String opt : encryptOrder.split("\\|"))
		{
			if(opt.length() > 4 && opt.substring(0, 4).equals("bits")) continue;
			switch(opt)
			{
			case "compact":
				compact = !compact;
				break;
			case "1":
				encrypted = function1(encrypted);
				break;
			case "2":
				encrypted = function2(encrypted);
				break;
			case "3":
				encrypted = function3(encrypted);
				break;
			case "4":
				encrypted = function4(encrypted);
				break;
			case "cc":
				encrypted = caesarCipher(encrypted, Integer.parseInt(parameterResolver(opt)));
				break;
			case "5":
				encrypted = reverseFunction1(encrypted);
				break;
			case "6":
				encrypted = reverseFunction2(encrypted);
				break;
			case "7":
				encrypted = reverseFunction3(encrypted);
				break;
			case "8":
				encrypted = reverseFunction4(encrypted);
				break;
			case "s":
				encrypted = BinarySequence.binaryToString(encrypted);
				break;
			case "b":
				//encrypted = BinarySequence.stringToOctetBinary(encrypted);
				//encrypted = BinarySequence.stringToXBinary(encrypted, BinarySequence.findGreatestLength(encrypted));
				if(!compact)
				encrypted = BinarySequence.stringToXBinary(encrypted, bitSize);
				else
				encrypted = BinarySequence.stringToXBinary(encrypted, BinarySequence.findGreatestLength(encrypted));
				break;
			case "p":
				System.out.println(encrypted);
				break;
			case "i":
				System.out.println("encrypted is " + (double)encrypted.length() / expression.length() + " times as big as original (" + (encrypted.length() - expression.length()) * bitSize + " bits more)");
			default:
				if(opt.length() > 4 && opt.substring(0, 4).equals("sten"))
				{
					int bits = bitSize;
					String function = parameterResolver(opt);
					if(function.contains(".")) bits = Integer.parseInt(function.substring(function.indexOf(".") + 1, function.length())); 
					switch(function.contains(".") ? function.substring(0, function.indexOf(".")) : function)
					{
					case "1":
						encrypted = Stenography.takeFirstBit(encrypted, bitSize, bits);
					}
					break;
				}
			    if(opt.length() > 2 && opt.substring(0, 3).equals("ivb")) 
			    {	
			    	encrypted = BinarySequence.invertBinary(encrypted, bitSize, compact);
			    	break;
			    }
			    if(opt.length() > 2 && opt.substring(0, 2).equals("cc")) 
			    {	
			    	encrypted = caesarCipher(encrypted, Integer.parseInt(parameterResolver(opt)));
			    	break;
			    }
			    if(opt.length() > 2 && opt.substring(0, 2).equals("tc")) 
			    {	
			    	encrypted = transpositionCipher(encrypted, parameterResolver(opt), false);
			    	break;
			    }
			    if(opt.length() > 4 && opt.substring(0, 4).equals("xorb"))
			    {
			    	encrypted = blockCipher(encrypted, parameterResolver(opt), true);
			    	break;
			    }
			    if(opt.length() > 3 && opt.substring(0, 3).equals("xor"))
			    {
			    	encrypted = blockCipher(encrypted, parameterResolver(opt), false);
			    	break;
			    }
			    if(opt.length() > 1 && opt.substring(0, 1).equals("b") && opt.contains("="))
			    {
			    	try
			    	{
			    	encrypted = BinarySequence.stringToXBinary(encrypted, Integer.parseInt(parameterResolver(opt)));
			    	} catch(java.lang.NumberFormatException ex) { System.out.println("Format error!"); break; }
			    	break;   	
			    }
			}
		}
		bitSize = 8;
		compact = false;
		return encrypted;
	}
	
	public static String parameterResolver(String input)
	{
		return input.substring(input.indexOf("=") + 1, input.length());
	}
	
	public static String easyDecrypt(String expression, String encryptOrder)
	{
		if(expression.length() == 0)
		{
			System.out.println("Blank expression to decrypt!");
			return "";
		}
		encryptOrder = encryptOrder.toLowerCase();
		String encrypted = expression;
		String[] opts = encryptOrder.split("\\|");
		if(encryptOrder.length() > 4 && encryptOrder.substring(0,4).equals("bits"))
			bitSize = (byte)Integer.parseInt(parameterResolver(opts[0].substring(0, opts[0].length())));
		for(int i = opts.length - 1; i >= 0; i--)
		{
			if(opts[i].length() > 4 && opts[i].substring(0, 4).equals("bits")) continue;
			switch(opts[i])
			{
			case "compact":
				compact = !compact;
				break;
			case "5":
				encrypted = function1(encrypted);
				break;
			case "6":
				encrypted = function2(encrypted);
				break;
			case "7":
				encrypted = function3(encrypted);
				break;
			case "8":
				encrypted = function4(encrypted);
				break;
			case "1":
				encrypted = reverseFunction1(encrypted);
				break;
			case "2":
				encrypted = reverseFunction2(encrypted);
				break;
			case "3":
				encrypted = reverseFunction3(encrypted);
				break;
			case "4":
				encrypted = reverseFunction4(encrypted);
				break;
			case "b":
				encrypted = BinarySequence.binaryToString(encrypted);
				break;
			case "s":
				//encrypted = BinarySequence.stringToOctetBinary(encrypted);
				//encrypted = BinarySequence.stringToXBinary(encrypted, BinarySequence.findGreatestLength(encrypted));
				if(!compact)
				encrypted = BinarySequence.stringToXBinary(encrypted, bitSize);
				else
					encrypted = BinarySequence.stringToXBinary(encrypted, BinarySequence.findGreatestLength(encrypted));
				break;
			default:
				if(opts[i].length() > 4 && opts[i].substring(0, 4).equals("sten"))
				{
					int bits = bitSize;
					String function = parameterResolver(opts[i]);
					if(function.contains(".")) bits = Integer.parseInt(function.substring(function.indexOf(".") + 1, function.length())); 
					switch(function.contains(".") ? function.substring(0, function.indexOf(".")) : function)
					{
					case "1":
						encrypted = Stenography.reverseTakeFirstBit(encrypted, bitSize, bits);
					}
					break;
				}
			    if(opts[i].length() > 2 && opts[i].substring(0, 3).equals("ivb")) 
			    {	
			    	encrypted = BinarySequence.invertBinary(encrypted, bitSize, compact);
			    	break;
			    }
			    if(opts[i].length() > 2 && opts[i].substring(0, 2).equals("cc")) 
			    {	
			    	encrypted = reverseCaesarCipher(encrypted, Integer.parseInt(parameterResolver(opts[i])));
			    	break;
			    }
			    if(opts[i].length() > 2 && opts[i].substring(0, 2).equals("tc")) 
			    {	
			    	encrypted = transpositionCipher(encrypted, parameterResolver(opts[i]), true);
			    	break;
			    }
			    if(opts[i].length() > 1 && opts[i].substring(0, 1).equals("s") && opts[i].contains("="))
			    {
			    	try
			    	{
					encrypted = BinarySequence.stringToXBinary(encrypted, Integer.parseInt(parameterResolver(opts[i])));
			    	} 
			    	catch(java.lang.NumberFormatException ex) { System.out.println("Error: check format of flag 's'"); break; }
					break;
			    }
			    if(opts[i].length() > 4 && opts[i].substring(0, 4).equals("xorb"))
			    {
			    	encrypted = blockCipher(encrypted, parameterResolver(opts[i]), true);
			    	break;
			    }
			    else if(opts[i].length() > 3 && opts[i].substring(0, 3).equals("xor"))
			    {
			    	encrypted = blockCipher(encrypted, parameterResolver(opts[i]), false);
			    	break;
			    }
			}
		}
		bitSize = 8;
		compact = false;
		return encrypted;
	}
	/**
	 * An obfuscation algorithm that changes abcd to acbd (every other letter from first letter, then every other letter from second letter)
	 * @param str string to encrypt
	 * @return an obfuscated string
	 */
	public static String function1(String str)
	{
		String ans = "";
		if(BinarySequence.isValidBinary(str)) str = str.replaceAll(" ",  "");
		for(int i = 0; i < str.length(); i+=2)
		{
			ans += str.substring(i, i+1);
		}
		for(int i = 1; i < str.length(); i+=2)
		{
			ans += str.substring(i, i+1);
		}
		if(BinarySequence.isValidBinary(ans)) ans = BinarySequence.splitChunk(ans, bitSize);
		return ans;
	}
	/**
	 * Reverses obfuscation done with function1 (every other letter from first letter, then every other letter from second letter)
	 * @param func obfuscated text resulting from func1
	 * @return a deciphered string
	 */
	
	public static String reverseFunction1(String func)
	{
		String ans = "";
		if(BinarySequence.isValidBinary(func)) func = func.replaceAll(" ",  "");
		int half = func.length() / 2;	
		if(func.length() % 2 == 0)
		{
			for(int i = 0; i < func.length() / 2; i++)
			{
				ans += "" + func.charAt(i) + func.charAt(i + half);
			}
		}
		else
		{
			for(int i = 1; i < func.length() / 2 + 1; i++)
			{
				ans += "" + func.charAt(i -1) + func.charAt(i + half);
			}
			ans += "" + func.charAt(func.length() / 2);
		}
		if(BinarySequence.isValidBinary(ans)) ans = BinarySequence.splitChunk(ans, bitSize);
		return ans;
	}
	/**
	 * obfuscation algorithm that changes abcd to badc (in other words, read in sets of 2 letters, read last letter then first)
	 * @param str
	 * @return an obfuscated string
	 */
	public static String function2(String str)
	{
	   String ans = "";
	   if(BinarySequence.isValidBinary(str)) str = str.replaceAll(" ",  "");
	   for(int i = 1; i < str.length(); i+=2)
	   {
		ans += "" + str.charAt(i) + str.charAt(i - 1);   
	   }
	   if(str.length() % 2 == 1) ans += str.charAt(str.length() -1);
	   if(BinarySequence.isValidBinary(ans)) ans = BinarySequence.splitChunk(ans, bitSize);
	   return ans;
	}
	/**fun
	 * opposite of function2... or exactly the same
	 * @param func obfuscated text from func2
	 * @return a deciphered string
	 */
	
	public static String reverseFunction2(String func)
	{ return function2(func); }
	/**
	 * Looks at the characters from a string and converts each into an ASCII value in an integer array
	 * @param str
	 * @return an array filled with character ASCII values.
	 * PostCondition: <returnedArray>.length = str.length()
	 */
	public static int[] charToASCII(String str)
	{
		int[] values = new int[str.length()];
		for(int i = 0; i < str.length(); i++)
		{
			values[i] = str.charAt(i);
		}
		return values;
	}
	
	/**obfuscation algorithm
	 * @param str
	 * @return a reversed version of str
	 */
	public static String function3(String str)
	{
		String ans = "";
		for(int i = str.length() - 1; i >= 0; i--)
		ans += "" + str.charAt(i);
		return ans;
	}
	/**
	 * reverses the obfuscation done in func3... or does the exact same thing
	 * @param str an obfuscated string
	 * @return a deciphered string
	 */
	
	public static String reverseFunction3(String str)
	{ return function3(str); }
	/**
	 * obfuscation algorithm
	 * @param str
	 * @return reverses words and not expression
	 */
	public static String function4(String str)
	{
		String ans = "";
		for(String s : str.split(" "))
		 ans += function3(s) + " ";
		if(ans.length() > 0) ans = ans.substring(0, ans.length() - 1); //truncate space
		return ans;
	}
	/**
	 * reverses the obfuscation done in func4... or does the exact same thing
	 * @param str an obfuscated string
	 * @return a deciphered string
	 */	
	public static String reverseFunction4(String str)
	{ return function4(str); }
	
	/**Performs a Caesar Cipher on str (takes all ASCII values in string and adds shift)
	 * @param str
	 * @param shift amount to characters
	 * @return a really weakly encrypted string 
	 */
	public static String caesarCipher(String str, int shift)
	{
		int[] chars = charToASCII(str);
		for(int i = 0; i < chars.length; i++) chars[i] += shift;
		String ans = "";
		for(int i : chars) ans += "" + (char)i;
		return ans;
	}
	/**Performs a Reverse Caesar Cipher on str (takes all ASCII values in string and adds shift)
	 * @param str
	 * @param shift amount to characters
	 * @return a really weakly encrypted string 
	 */
	public static String reverseCaesarCipher(String str, int shift)
	{
		int[] chars = charToASCII(str);
		for(int i = 0; i < chars.length; i++) chars[i] -= shift;
		String ans = "";
		for(int i : chars) ans += "" + (char)i;
		return ans;
	}
	
	public static String blockCipher(String str, String key, boolean keyIsBinary)
	{
		String ans = "";
		//String inBinary = BinarySequence.isValidBinary(str) ? str : BinarySequence.stringToXBinary(str, BinarySequence.findGreatestLength(str));
		String inBinary = BinarySequence.isValidBinary(str) ? str : !compact ? BinarySequence.stringToXBinary(str, bitSize) : BinarySequence.stringToBinary(str);
		if(keyIsBinary)
		{
			//System.out.println("yes " + inBinary);
			key = key.replaceAll(" ", "");
			int pos = 0;
			for(int i = 0; i < str.length(); i++)
			{
				if(pos >= key.length()) pos = 0;
				if(inBinary.charAt(i) == ' ') { ans+= " "; continue; }
				if(inBinary.charAt(i) == '1' && key.charAt(pos) == '1') ans += "0";
				else if(inBinary.charAt(i) == '1' || key.charAt(pos) == '1') ans += "1";
				else ans += '0';
				pos++;
			}
		}
		else
		{
			key = BinarySequence.stringToBinary(key).replaceAll(" ", "");
			int pos = 0;
			for(int i = 0; i < str.length(); i++)
			{
				if(pos >= key.length()) pos = 0;
				if(inBinary.charAt(i) == ' '){ ans+= " "; continue; }
				if(inBinary.charAt(i) == '1' && key.charAt(pos) == '1') ans += "0";
				else if(inBinary.charAt(i) == '1' || key.charAt(pos) == '1') ans += "1";
				else ans += '0';
				pos++;
			}
		}
		return BinarySequence.isValidBinary(str) ? ans: BinarySequence.binaryToString(ans);
	}
	
	public static String transpositionCipher(String str, String dimInfo, boolean isDecipher)
	{
		String ans = "";
		String[] params = dimInfo.split("x");
		int[] dimPair = {Integer.parseInt(params[0]), Integer.parseInt(params[1])};
		while(dimPair[0] * dimPair[1] < str.length())
		{
			dimPair[0]++;
			dimPair[1]++;
		}
		char[][] loadMatrix = new char[dimPair[0]][dimPair[1]];
		int pos = 0;
		for(int i = 0; i < loadMatrix.length; i++)
			for(int j = 0; j < loadMatrix[i].length; j++)
			{
				//TODO make salt characters closer to the average character values
				if(pos > str.length()) loadMatrix[i][j] = (char)(3000 + (int)(Math.random() * 40) - 20);
				else if(pos == str.length())
				{
					pos++;
					loadMatrix[i][j] = (char)(3000);
				}
				else loadMatrix[i][j] = str.charAt(pos++);
			}
		//matrix loaded
		outerloop:
		for(int i = 0; i < loadMatrix[0].length; i++)
			for(int j = 0; j < loadMatrix.length; j++)
			{
				if(loadMatrix[j][i] == (char)(3000) && isDecipher) break outerloop;
				ans += "" + loadMatrix[j][i];
			}
		
		return ans;
	}
}
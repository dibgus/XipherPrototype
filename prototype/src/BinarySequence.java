
public class BinarySequence {
	
	private String sequence;
	
	public BinarySequence(String sequence)
	{
		this.sequence = sequence;
	}
	
	public String getBits()
	{
		return sequence;
	}
	
	public void setBits(String newBits)
	{
		sequence = newBits;
	}
	public static int findGreatestLength(String str)
	{
		int ans = 0;
		if(!isValidBinary(str))
		{
			str = stringToBinary(str);
		}
		String[] segments = str.split(" ");
		for(int i = 0; i < segments.length; i++)
			if(segments[i].length() > ans) ans = segments[i].length();
		return ans;
	}
	
	public static String intToBinary(int val)
	{
		assert val >= 0 : "Signed nope";
		if(val == 0) return "0";
		String ans = "1";
		int checkBit = (int)(Math.log(val) / Math.log(2)) + 1;
		//System.out.println("start: " + checkBit);
		val -= Math.pow(2, checkBit - 1);
		checkBit--;
		while((checkBit > 0))
		{
			if(Math.floor((Math.log(val) / Math.log(2))) == checkBit - 1)
			{
				val -= Math.pow(2, checkBit - 1);
				ans += 1;
			}
			else ans += 0;
			checkBit--;
		}
		assert val == 0 : "Error in calculation: did not decrement properly (val = " + val + ")";
		return ans;
	}
	
	public static String stringToBinary(String str)
	{
		String ans = "";
		int[] inASCII = random.charToASCII(str);
		for(int i : inASCII) ans += intToBinary(i) + " ";
		if(ans.length() > 0) ans = ans.substring(0, ans.length() - 1); //truncate space
		return ans;
	}
	public static String stringToOctetBinary(String str)
	{
		String ans = "";
		int[] inASCII = random.charToASCII(str);
		for(int i : inASCII) ans += intToOctetBinary(i) + " ";
		if(ans.length() > 0) ans = ans.substring(0, ans.length() - 1); //truncate space
		return ans;
	}
	
	public static String stringToXBinary(String str, int buffer)
	{		
		String ans = "";
		int[] inASCII = random.charToASCII(str);
		for(int i : inASCII) ans += intToXBitBuffer(i, buffer) + " ";
		if(ans.length() > 0) ans = ans.substring(0, ans.length() - 1); //truncate space
		return ans;
	}
	
	public static String binaryToString(String bin)
	{
		assert isValidBinary(bin) : "ERROR: input is not a valid binary number: already a string? " + bin;
		String[] split = bin.split(" ");
		String ans = "";
		for(String bitset : split)
		{
			int value = binaryToInt(bitset);
			ans += (char)value;
		}
		return ans;
	}
	
	public static String invertBinary(String expression)
	{
		String ans = "";
		if(!isValidBinary(expression))
			expression = stringToBinary(expression);
		for(int i = 0; i < expression.length(); i++)
		{
			if(expression.charAt(i) == ' ') ans += " ";
			else if(expression.charAt(i) == '1') ans += "0";
			else ans += "1";
		}
		return ans;
	}
	public static String splitChunk(String bits, byte bufferSize)
	{
		String ans = "";
		for(int i = 0; i < bits.length(); i++)
		{
			if(i % (bufferSize) == 0 && i != 0)
				ans += " ";
				ans += bits.charAt(i) + "";
		}
		if(bits.length() % bufferSize != 0)
		{
			String padding = "";
			for(int i = bits.length() % bufferSize; i < bufferSize; i++) padding += "0";
			if(bits.lastIndexOf(" ") == -1) ans = padding + ans;
			else ans = ans.substring(0, bits.lastIndexOf(" ") + 1) + padding + ans.substring(bits.lastIndexOf(" ") + 1, ans.length());
		}
		return ans;
	}
	public static String invertBinary(String expression, byte bitSize, boolean compact)
	{
		String ans = "";
		if(!isValidBinary(expression))
		{
			expression = stringToXBinary(expression, bitSize);
			for(int i = 0; i < expression.length(); i++)
			{
				if(expression.charAt(i) == ' ') ans += " ";
				else if(expression.charAt(i) == '1') ans += "0";
				else ans += "1";
			}
			return binaryToString(ans);
		}
		else
		{
		for(int i = 0; i < expression.length(); i++)
		{
			if(expression.charAt(i) == ' ') ans += " ";
			else if(expression.charAt(i) == '1') ans += "0";
			else ans += "1";
		}
		return ans;
		}
	}
	
	public static boolean isValidBinary(String bin)
	{
		for(char c : bin.toCharArray())
		{
			if(c != '1' && c != '0' && c != ' ') return false;
		}
		return true;
	}
	
	public static int binaryToInt(String bin)
	{
		byte power = 0;
		int ans = 0;
		for(int i = bin.length() - 1; i >= 0; i--, power++)
		{
			if(bin.charAt(i) == '1') ans += Math.pow(2, power); 
		}
		return ans;
	}
	
	public static String intToOctetBinary(int val)
	{
		String binary = intToBinary(val);
		assert binary.length() <= 8 : "value cannot fit into an octet buffer!";
		while(binary.length() < 8) binary = "0" + binary;
		return binary;
	}
	
	public static String intToXBitBuffer(int val, int buffer)
	{
		String binary = intToBinary(val);
		assert binary.length() <= buffer : "value cannot fit into a " + buffer + " bit buffer! " + val;
		while(binary.length() < buffer) binary = "0" + binary;
		return binary;
	}
}

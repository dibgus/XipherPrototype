
public class Stenography {

	public static String takeFirstBit(String str, int bufferSize, int encodeSize)
	{
		String ans = "";
		String inBinary = BinarySequence.isValidBinary(str) ? str.replaceAll(" ", "") : BinarySequence.stringToXBinary(str, bufferSize).replaceAll(" ", "");
		for(int i = 0; i < inBinary.length(); i++)
		{
			String padding = "";
			for(int j = 0; j < encodeSize - 1; j++)
			{
				if((int)(Math.random() * 2) == 0) padding += "0";
				else padding += "1";
			}
			ans += inBinary.charAt(i) + padding + " ";
		}
		if(ans.length() > 0) ans = ans.substring(0, ans.length() - 1); //trim space
		if(BinarySequence.isValidBinary(str)) return ans;
		else return BinarySequence.binaryToString(ans);
	}
	public static String reverseTakeFirstBit(String str, int bufferSize, int encodeSize)
	{
		String ans = "";
		String inBinary = BinarySequence.isValidBinary(str) ? str : BinarySequence.stringToXBinary(str, encodeSize);
		for(String chunk : inBinary.split(" "))
		{
			if((ans.length() + 1) % (bufferSize + 1) == 0 && ans.length() != 0) ans += " ";
			ans += chunk.charAt(0);
		}
		if(BinarySequence.isValidBinary(str)) return ans;
		else return BinarySequence.binaryToString(ans);
	}
}

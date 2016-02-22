public class Test
{

	public static void main(String[] args)
	{
		LocalizedStrings.lang = Languages.SPANISH;
		LocalizedStrings.update();

		System.out.println(LocalizedStrings.getlocalString(TextFields.FILE));

		LocalizedStrings.lang = Languages.ENGLISH;
		LocalizedStrings.update();

		System.out.println(LocalizedStrings.getlocalString(TextFields.FILE));
	}

}

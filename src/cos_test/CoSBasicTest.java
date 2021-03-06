package cos_test;

import nexus_rest.ContentType;
import nexus_test.FileReaderClientTest;

/**
 * This class runs a basic test on the server
 * @author Mikko Hilpinen
 * @since 28.5.2015
 */
public class CoSBasicTest
{
	// CONSTRUCTOR	----------------------------
	
	private CoSBasicTest()
	{
		// The interface is static
	}

	
	// MAIN METHOD	---------------------------
	
	/**
	 * Starts the test
	 * @param args ip, port (optional, default = 7778), fileName 
	 * (optional, default = testInstructions.txt)
	 */
	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.out.println("Please provide the correct arguments: ip, port "
					+ "(optional, default = 7778) and fileName "
					+ "(optional, default = testInstructions.txt)");
			System.exit(0);
		}
		
		int port = 7778;
		if (args.length > 1)
			port = Integer.parseInt(args[1]);
		String fileName = "testInstructions.txt";
		if (args.length > 2)
			fileName = args[2];
		
		FileReaderClientTest.run(fileName, args[0], port, ContentType.JSON, true);
	}
}

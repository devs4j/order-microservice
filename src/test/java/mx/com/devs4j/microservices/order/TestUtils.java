package mx.com.devs4j.microservices.order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestUtils {
	
	public static String readFileAsString(String fileName) throws IOException {
		File file = new File(TestUtils.class.getClassLoader().getResource(fileName).getFile());
		return new String(Files.readAllBytes(file.toPath()));
	}

}

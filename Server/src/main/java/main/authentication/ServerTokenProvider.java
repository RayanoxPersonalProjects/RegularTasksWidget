package main.authentication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ServerTokenProvider {

	private final String classPathFile = "credentials_server.txt";
	private final String DEFAULT_SERVER_TOKEN = "test";
	
	private String serverToken;
	
	//@Value("classpath:" + classPathFile)
	@Value("classpath:credentials_server.txt")
	Resource credentialServerConfigFile;
	
	public ServerTokenProvider() throws IOException {
		
		File credentialFileConfig = credentialServerConfigFile.getFile();
		
		try {
			List<String> lines = readAllLines(credentialFileConfig);
			
			for (String line : lines) {
				String [] lineSplitted = line.split("=");
				if(lineSplitted.length >= 2) {
					String propName = lineSplitted[0];
					String valueName = lineSplitted[1];
					
					switch(propName) {
						case "token":
							this.serverToken = valueName;
							break;
						default:
							break;
						
					}
					
				}else {
					System.err.println("The credentials file must only contain 'token=...', but it doesn't.");
				}
			}
			
			System.out.println("The token value is well loaded and its value is ==> " + serverToken);
			
			if(this.serverToken.equals(DEFAULT_SERVER_TOKEN)) {
				System.out.println("WARN: The default token is used, not secured. We suggest you to set an other token in the credentials_server.txt file before compiling the program.");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean AuthorizeRequestToken(String token) {
		if(this.serverToken != null) {
			return this.serverToken.equals(token);
		}else {
			System.err.println("The server side token has not been loaded. So every incoming requests are rejected.");
			return false;
		}
	}
	
	public String getPassword() {
		return serverToken;
	}
	
	
	
	
	

	
	private List<String> readAllLines(File credentialFileConfig) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(credentialFileConfig));
		
		ArrayList<String> allLines = new ArrayList<>();
		String currentLine;
		
		while((currentLine = reader.readLine()) != null) {
			allLines.add(currentLine);
		}
		
		return allLines;
	}
}

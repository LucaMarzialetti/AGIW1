package it.uniroma3.agiw1.main;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.HttpStatusException;

import it.uniroma3.agiw1.http_connector.HttpConnector;

public class JsoupParseExceptionTester {
	public static void main(String[] args) {
		String l1 = "https://www.teatro.it/spettacoli/castello_feudale_d_ayala_valva/finito_il_teatrino_3704_13729";
		String l2 = "http://www.paginebianche.it/trentola-ducenta/alfredo-barbato.aibibefiag";
		String l3 = "https://it.linkedin.com/in/raffaele-paparo-49727144";
		LinkedList<String> links = new LinkedList<String>();
		links.add(l1);
		links.add(l2);
		links.add(l3);
		Iterator<String> i = links.iterator();
		while(i.hasNext()){
			String s = i.next();
			try {

				HttpConnector.getPageRetrieve(s,0,0,"Chrome");
				System.out.println("ok "+s);
			} 
			catch (HttpStatusException e){
				System.out.println("404!");
			}
			catch (IllegalCharsetNameException | IOException e) {
				System.out.println("IllegalCharsetNameException | IOException");
			}
			catch (Exception e){
				System.out.println("UNCATCH ! its dangerous to have uncatch exception with cuncurrency locks!");
			}
		}	
	}


	public static void readerCharsetsTest() {
		BufferedReader br = null;
		String outputFileName = "names.txt";
		try {
			FileInputStream is = new FileInputStream(outputFileName);
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.ISO_8859_1.name());
			br = new BufferedReader(isr);			
			System.out.println("[DataExtractor]: reading from file "+outputFileName);
			String line;
			while ((line = br.readLine()) != null){
				System.out.println(line);
				for(int i=0; i<line.length(); i++){
					char c = line.charAt(i);
					System.out.println("\t|"+c+"="+(int)c);
				}
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("[DataExtractor]: "+e.getMessage());
		} 
		catch (UnsupportedEncodingException e) {
			System.out.println("[DataExtractor]: "+e.getMessage());
		} 
		catch (IOException e) {
			System.out.println("[DataExtractor]: "+e.getMessage());
		}
	}
}

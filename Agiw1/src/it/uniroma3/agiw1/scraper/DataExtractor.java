package it.uniroma3.agiw1.scraper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.uniroma3.agiw1.http_connector.HttpConnector;
import it.uniroma3.agiw1.http_connector.MalformedPostAttributesException;
import it.uniroma3.agiw1.main.EngineBuilder;
import it.uniroma3.agiw1.main.Flags;

public class DataExtractor {
	public static final String outputFileName="names.txt";
	public static final String urlTarget = "http://www.ordineavvocati.roma.it/AlboElenchi/AlboAvvocati/AlboAvvocatiNew.asp";
	private ArrayList<String> attributes;
	private ArrayList<String> values;

	public DataExtractor(){
		//per online
		this.attributes = new ArrayList<String>(Arrays.asList("cognome_ric","cap_ric","localita_ric","submitf"));
		this.values = new ArrayList<String>(Arrays.asList(" ","","ROMA","yes"));
	}

	/*String formatter, normalize name and surname*/
	private String formatterAvvocato(Element e){
		String s = e.text().toLowerCase();
		if(s.startsWith("avv."))
			s=s.substring(5);
		s=s.replaceAll("\\sdetto$", "");		//detto alla fine della frase
		s=specialCharsNormalize(s);
		return s;
	}

	private String specialCharsNormalize(String s){
		s=
				s.replaceAll("\".*\"", "")		//virgolette
				.replaceAll("\\(.*\\)", "")		//punti
				.trim()							//spazi malformati
				.replaceAll((char)160+""," ")	//spazi nbsp
				.replaceAll("a'", "à")				
				.replaceAll("e'", "è")
				.replaceAll("i'", "ì")
				.replaceAll("o'", "ò")
				.replaceAll("u'", "ù")
				.replaceAll("[ ]*$","");		//spazi in coda
		return s;
	}

	/*HTML parser heavy DOM based (attribute nowrap)*/
	public JSONArray extract() {
		JSONArray array = new JSONArray();
		if(EngineBuilder.flags.contains(Flags.Remote_extract))
			array = remoteExtract();
		else
			if(EngineBuilder.flags.contains(Flags.Local_extract))
				array =	localExtract();
		return array;
	}

	/**WEB: write a new names file and already store in a json array the result**/
	private JSONArray remoteExtract() {
		JSONArray array = new JSONArray();
		try {
			Files.delete(Paths.get(outputFileName));
		}
		catch (IOException e) {
			System.out.println("[DataExtractor]: "+e.getMessage());
		}
		try {
			System.out.println("[DataExtractor]: downloading HTML source from "+urlTarget);
			Document doc = HttpConnector.postPageRetrieve(urlTarget, this.attributes, this.values, 0, 0, null);
			Elements elements = doc.getElementsByAttribute("nowrap");
			System.out.println("[DataExtractor]: extracted "+elements.size()+" DOM elements.");
			System.out.println("[DataExtractor]: DOM processing..");
			Set<String> lines = new HashSet<String>();
			for(Element e: elements){
				String formattedData = formatterAvvocato(e);
				lines.add(formattedData);
				array.put(formattedData);
			}
			LinkedList<String> names = new LinkedList<String>();
			names.addAll(lines);
			Collections.sort(names);
			Files.write(Paths.get(outputFileName), names, Charset.forName(StandardCharsets.ISO_8859_1.name()), StandardOpenOption.CREATE);
			System.out.println("[DataExtractor]: Names file written");
		}
		catch (IOException e) {
			System.out.println("[DataExtractor]: "+e.getMessage());
		} 
		catch (MalformedPostAttributesException e) {
			System.out.println("[DataExtractor]: "+e.getMessage());
		}
		catch (Exception e) {
			System.out.println("[DataExtractor]: uncatched exception! Jsoup.connect failed");
		}
		return array;
	}

	/**LOCAL-FILE: only read the names file and store it in the json array**/
	private JSONArray localExtract() {
		JSONArray array = new JSONArray();
		BufferedReader br = null;
		try {
			FileInputStream is = new FileInputStream(outputFileName);
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.ISO_8859_1.name());
			br = new BufferedReader(isr);			
			System.out.println("[DataExtractor]: reading from file "+outputFileName);
			String line;
			while ((line = br.readLine()) != null)
				array.put(specialCharsNormalize(line));
			System.out.println("[DataExtractor]: extracted "+array.length()+" elements");
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
		finally {
			try {
				br.close();
			} 
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		/*RETURN*/
		return array;
	}
}
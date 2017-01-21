
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.bg.BulgarianStemmer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


public class MainClass {
	
	public static void main(String[] args) throws Exception {
		File files = new File("offers");
		File outputFile = new File("outputOffers");
			
		for(File f : files.listFiles()) {
			stemFile(f,outputFile);
		}
		 
	}

	private static void stemFile(File inputFile, File outputFileDir) throws Exception, IOException, UnsupportedEncodingException, FileNotFoundException {
		Stemmer stem = new Stemmer();
		stem.loadStemmingRules("stem_rules_context_2_UTF-8.txt");
		
		 Analyzer analyzer = new StandardAnalyzer();
		 StringBuffer stemmedFile = new StringBuffer(); 
		 Files.readAllLines(inputFile.toPath()).stream().forEach(line -> {
			 TokenStream ts = analyzer.tokenStream("", new StringReader(line));	
			   try {
			    ts.reset();
				while(ts.incrementToken()) {
					String token = ts.getAttribute(CharTermAttribute.class).toString();
					String stemmedToken = stem.stem(token);
					stemmedFile.append(stemmedToken).append(";");
				}
				ts.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 });
		File outputFile = new File(outputFileDir.getAbsolutePath() + File.separator + inputFile.getName() + "_stemmed.txt");
		outputFile.createNewFile();
		BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
		bf.write(stemmedFile.toString());
		bf.close();
		System.out.println("Finish!!!");
	}

}

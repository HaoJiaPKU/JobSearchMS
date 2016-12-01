package utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class SegmenterUtil {
	
	public static CRFClassifier<CoreLabel> segmenter;
	public static final String SegmenterBaseDir = "../stanford-segmenter-2015-12-09/data";
	
	public static HashSet<String> stopword = new HashSet<String> ();
	public static final String StopwordFile = "stopwords.txt";
	public static final String StopSigns = "[\\p{P}~$`^=|<>～｀＄＾＋＝｜＜＞￥× \\s|\t|\r|\n]+";

	public static void loadSegmenter()
	{	
		Properties props = new Properties();
		props.setProperty("sighanCorporaDict", SegmenterBaseDir);
		//props.setProperty("NormalizationTable", "data/norm.simp.utf8");
		//props.setProperty("normTableEncoding", "UTF-8");
		//below is needed because CTBSegDocumentIteratorFactory accesses it
		props.setProperty("serDictionary", SegmenterBaseDir + "/dict-chris6.ser.gz");
		//props.setProperty("testFile", args[0]);
		props.setProperty("inputEncoding", "UTF-8");
		props.setProperty("sighanPostProcessing", "true");

		segmenter = new CRFClassifier<CoreLabel>(props);
		segmenter.loadClassifierNoExceptions(SegmenterBaseDir + "/ctb.gz", props);
		//segmenter.classifyAndWriteAnswers(args[0]);
		//System.out.println(segmenter.classifyToString("今天天气不错啊"));
	}
	
	
	public static void loadStopword() {
		FileInput fi = new FileInput(StopwordFile);
		String line = new String ();
		try {
			while ((line = fi.reader.readLine()) != null) {
				stopword.add(line.trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fi.closeInput();
	}
	
	public static void main(String [] args) {}	
}
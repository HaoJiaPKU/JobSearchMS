package cn.edu.pku.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Pattern;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class SegmenterUtil {
	
	public static CRFClassifier<CoreLabel> segmenter;
	public static final String SegmenterBaseDir = "../stanford-segmenter-2015-12-09/data";
	
	public static HashSet<String> stopword = new HashSet<String> ();
	public static final String StopwordFile = "stopwords.txt";
	public static final String StopSigns = "[\\p{P}~$`^=|<>～｀＄＾＋＝｜＜＞￥× \\s|\t|\r|\n]+";

	/**
	 * 加载分词器
	 * */
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
	
	/** 
	 * 加载停用词、停用符号表
	 * @param stopWordsFilePath 停用词表文件路径
	 * @throws IOException 找不到停用词、停用符号文件
	 */
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
	
	/** 
	 * 停用词、停用符号判断
	 * @param token 待识别的词素
	 * @return true表示该词素为停用词或停用符号，false表示该词素不是停用词或停用符号
	 */
	public static boolean isStopWords(String token)
	{
		if(stopword.contains(token))
			return true;
		return false;
	}
	
	/** 
	 * 将字母的大写形式转化为小写形式，如果不包含大写字母返回原值，否则返回小写形式
	 * @param token 待转化的词素
	 * @return 转化后的词素
	 */
	public static String lowerCase(String token)
	{
		if(Pattern.compile("(?i)[A-Z]").matcher(token).find())
			return token.toLowerCase();
		return token;
	}
	
	/** 
	 * 分词操作，保留c++，.net，c#，sqlserver等特殊词素
	 * @param textContent 待分词的文本
	 * @return 分词后的词素数组
	 */
	public static String [] stopWordExceptCPP(String textContent)
	{
		textContent = textContent.replaceAll("c#", "+++++");
		textContent = textContent.replaceAll("C#", "+++++");
		textContent = textContent.replaceAll(SegmenterUtil.StopSigns, " ");
//		System.out.println(textContent);
		textContent = textContent.replaceAll("[+][+][+][+][+]", "c# ");
		textContent = textContent.replaceAll("c[+][+]", "@pattern1@");
		textContent = textContent.replaceAll("C[+][+]", "@pattern1@");
		textContent = textContent.replaceAll(".net", "@pattern2@");
		textContent = textContent.replaceAll(".Net", "@pattern2@");
		textContent = textContent.replaceAll(".NET", "@pattern2@");
		textContent = textContent.replaceAll("div[+]css", "@pattern3@");
		textContent = textContent.replaceAll("css[+]div", "@pattern3@");
		textContent = textContent.replaceAll("DIV[+]CSS", "@pattern3@");
		textContent = textContent.replaceAll("CSS[+]DIV", "@pattern3@");
		textContent = textContent.replaceAll("Div[+]Css", "@pattern3@");
		textContent = textContent.replaceAll("Css[+]Div", "@pattern3@");
		textContent = textContent.replaceAll("[+]", " ");
		textContent = textContent.replaceAll("@pattern1@", "c++ ");
		textContent = textContent.replaceAll("@pattern2@", ".net");
		textContent = textContent.replaceAll("@pattern3@", "div+css");
		textContent = textContent.replaceAll("sql server", "sqlserver");
		textContent = textContent.replaceAll("Sql server", "sqlserver");
		textContent = textContent.replaceAll("Sql Server", "sqlserver");
		textContent = textContent.replaceAll("sql Server", "sqlserver");
		textContent = textContent.replaceAll("SQL server", "sqlserver");
		textContent = textContent.replaceAll("SQL Server", "sqlserver");
		textContent = SegmenterUtil.segmenter.classifyToString(textContent);
//		System.out.println(textContent);
		textContent = textContent.replaceAll("c #", "c#");
		return textContent.split(" +");
	}
	
	/**
	 * 对文本进行分词
	 * @param inputPath 输入文件路径
	 * @param outputPath 输出文件路径
	 * @param indices 需要分词的域的索引
	 * */
	public static void segmentation(String inputPath, String outputPath, int[] indices) {
		if (indices == null || indices.length == 0) {
			System.out.println("info : no indices specified");
			return;
		}
		
		FileInput fi = new FileInput(inputPath);
		FileOutput fo = new FileOutput(outputPath);
		SegmenterUtil.loadSegmenter();
		SegmenterUtil.loadStopword();
		
		int counter = 0;
		String line = new String();
		try {
			while ((line = fi.reader.readLine()) != null) {
				String content = new String();
				String [] fields = line.split("	");
				if (fields.length - 1 < indices[indices.length - 1]) {
					continue;
				}
				
				boolean [] flag = new boolean [fields.length];
				for (int i = 0; i < flag.length; i ++) {
					flag[i] = false;
				}
				for (int i = 0; i < indices.length; i ++) {
					flag[indices[i]] = true;
				}
				for (int i = 0; i < flag.length; i ++) {
					if (!flag[i]) {
						fo.t3.write(fields[i] + " ");
					}
				}
				
				for (int i = 0; i < fields.length; i ++) {
					if (fields[i] == null || fields[i].length() == 0 || fields[i].equals("")) {
						continue;
					}
					if (flag[i]) {
						content += fields[i];
					}
				}
				
				String [] tokens = stopWordExceptCPP(content.trim());
				Pattern p = Pattern.compile("[a-b]|[d-z]");
				for(int i = 0; i < tokens.length; i ++)
				{
					//如果不包含大写字母返回原值，否则返回小写形式
					tokens[i] = SegmenterUtil.lowerCase(tokens[i].trim());
						
					//去除c以外的其他单个字母和汉字
					if(tokens[i].length() == 1 && !tokens[i].equals("c")) {
						continue;
					}
					
					//去除完全是数字的词
					if(tokens[i].matches("[0-9]+")) {
						continue;
					}
					
					tokens[i] = tokens[i].replaceAll("\r", "");
					tokens[i] = tokens[i].replaceAll("\n", "");
					tokens[i] = tokens[i].replaceAll("\t", "");
					tokens[i] = tokens[i].trim();
					
					//去除停用词和网址等特殊词
					if(SegmenterUtil.isStopWords(tokens[i])
							|| tokens[i].length() == 0
							|| tokens[i].contains("-")
							|| tokens[i].contains("@")
							|| tokens[i].contains("COM")
							|| tokens[i].contains("com")
							|| tokens[i].contains("CN")
							|| tokens[i].contains("cn")
							|| tokens[i].contains("WWW")
							|| tokens[i].contains("www")) {
						continue;
					}
					fo.t3.write(tokens[i] + " ");
				}
				fo.t3.newLine();
				System.out.println(++ counter);
//				if (counter >= 10) {
//					break;
//				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fo.closeOutput();
		fi.closeInput();
	}
	
	public static void main(String [] args) {}	
}
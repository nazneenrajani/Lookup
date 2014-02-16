import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


public class Lookup {
	static Map<String,TreeSet<Long>> wordDocument = new HashMap<String, TreeSet<Long>>();
	static Map<Long,String> documentIndex = new HashMap<Long,String>();
	public static void main(String[] args){
		if(args.length<1){
			System.err.println("Please provide valid queries file");
			return;
		}
		index();
		lookup(args);
	}
	private static void lookup(String[] args) {
		BufferedReader br;
		try{			 
			String sCurrentLine;
			br = new BufferedReader(new FileReader(args[0]));
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println("query: "+sCurrentLine);
				String[] temp = sCurrentLine.split("\\s");
				List<String> queries = new ArrayList<String>();
				for(String token:temp){
					if(wordDocument.containsKey(token))
						queries.add(token);
				}
				intersection(queries);
				System.out.println("==========================================================");
				//System.out.println("\n");
			}
			br.close();
		}catch (IOException e) {
			e.printStackTrace();
		}


	}
	private static void intersection(List<String> queries) {
		TreeSet<Long> result = wordDocument.get(queries.get(0));
		if(queries.size()>1){
			for(int i =1;i<queries.size();i++){
				//System.out.println(queries[i]);
				if(wordDocument.containsKey(queries.get(i))){
					TreeSet<Long> results = wordDocument.get(queries.get(i));
					result.retainAll(results);
				}
			}
			if(result.size()>0){
				for(long out:result){
					System.out.println(documentIndex.get(out));
				}
			}
		}
		else{
			for(long out:result){
				System.out.println(documentIndex.get(out));
			}
		}


	}
	private static void index() {
		BufferedReader br;
		try{			 
			String sCurrentLine;
			br = new BufferedReader(new FileReader("docs.corpus"));
			while ((sCurrentLine = br.readLine()) != null) {
				String[] fields = sCurrentLine.split("\t");
				long documentID = Long.parseLong(fields[2]+fields[0]);
				documentIndex.put(documentID,fields[0]+"\t"+fields[2]+"\t"+fields[1]);
				String[] title = fields[1].split("\\s");
				for(String token:title){
					if(!wordDocument.containsKey(token)){
						TreeSet<Long> newID = new TreeSet<Long>(Collections.reverseOrder());
						newID.add(documentID);
						wordDocument.put(token,newID);
					}
					else{
						TreeSet<Long> getID = wordDocument.get(token);
						if (!getID.contains(documentID))
							getID.add(documentID);
						wordDocument.put(token,getID);			
					}
				}
			}
			br.close();
			//System.out.println(documentIndex.size());
			//System.out.println(wordDocument.size());
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
}

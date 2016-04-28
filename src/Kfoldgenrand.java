import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

// generate k fold input using parameters: fold to be used as testing
public class Kfoldgenrand {
	// function for two fold generation of Random Orientation
	public void kfold(HashMap<String, Double> undirected, HashMap<String, Double> directed, int k, int fold) throws IOException
	{
		int directedCount = directed.size();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("Orient_fold_"+fold+"_of_"+k+".txt"));
		
		Iterator<String> it = undirected.keySet().iterator();
		
		while(it.hasNext())
		{
			String edge = it.next();
			double weight = undirected.get(edge);
			
			String [] elements = edge.split("&");
			String tail = elements[0];
			String head = elements[1];
			
			bw.write(tail+"\t(pp)\t"+head+"\t"+weight+"\n");
		}
		
		Iterator<String> it1 = directed.keySet().iterator();
		int begin = (fold - 1)*(directedCount/k);
		int end = fold*(directedCount/k)-1;
		
		if(end > (k-1)*(directedCount/k))
			end = directedCount;
		
		System.out.println("Begining and end of current fold "+begin+" , "+end);
		
		int count = 0;
		while(it1.hasNext())
		{
			String edge = it1.next();
			double weight = directed.get(edge);
			
			String [] elements = edge.split("&");
			String tail = elements[0];
			String head = elements[1];
			
			if(count >= begin && count <= end) //fold mentioned is made undirected for testing 
			{
				bw.write(tail+"\t(pp)\t"+head+"\t"+weight+"\n");
			}
			else //rest of the folds remain directed
			{
				bw.write(tail+"\t(pd)\t"+head+"\t"+weight+"\n");
			}
			count++;
		}
		
		bw.close();
	}
	
	public static void main(String args[]) throws IOException
	{
		Kfoldgenrand object1 = new Kfoldgenrand();
		BufferedReader br = new BufferedReader(new FileReader("hin.txt"));
		HashMap<String, Double> undirected = new HashMap<String, Double>();
		HashMap<String, Double> directed = new HashMap<String, Double>();
		HashMap<String, Double> allUndirected = new HashMap<String, Double>();
		int count = 0;
		String record;
		int fold = Integer.parseInt(args[0]);
		int k = Integer.parseInt(args[1]);
		
		System.out.println("Generating input for fold "+fold+" of "+k+" folds");
		
		while((record = br.readLine()) != null)
		{
			String [] elements = record.split("\t");

			String tail = elements[0];
			String head = elements[1];
			double weight = Double.parseDouble(elements[2]);
			String opposite = head+"&"+tail;

			if(directed.containsKey(opposite))
			{
				double weight1 = directed.get(opposite);
				undirected.put(tail+"&"+head, weight);
				directed.remove(opposite, weight1);
			}
			else
			{
				directed.put(tail+"&"+head, weight);
				allUndirected.put(tail+"&"+head, weight);
			}
			
			count++;
			//System.out.println(count);
		}
		
		System.out.println("directed edges count "+directed.size());
		System.out.println("undirected edges count "+undirected.size());
		System.out.println("over all count "+count);
		
		object1.kfold(undirected, directed, k, fold);
		
		
		
		br.close();
		
	}

}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class kfoldevaluation {
	HashMap<String, Double> undirected = new HashMap<String, Double>();
	HashMap<String, Double> directed = new HashMap<String, Double>();
	HashMap<String, Double> allUndirected = new HashMap<String, Double>();
	HashSet<String> outputDirected = new HashSet<String>();
	
	public void evaluate(String output, int k, int fold) throws IOException
	{
		int totaldirected;
		BufferedReader br = new BufferedReader(new FileReader(output));
	
		System.out.println("===================FOLD "+fold+" EVALUATION==================");
		String line1 = "";
		while((line1 = br.readLine()) != null)
		{	
			String [] elements = line1.split("\t");
			String tail = elements[0];
			String head = elements[2];
			outputDirected.add(tail+"&"+head);
		}
		br.close();
		
		totaldirected = outputDirected.size();
		System.out.println("directed edges in output "+totaldirected);		
		
		int directedCount = directed.size();
		int begin = (fold - 1)*(directedCount/k);
		int end = fold*(directedCount/k)-1;
		
		System.out.println("begins and ends at "+begin+", "+end);
		
		Iterator<String> it  = directed.keySet().iterator();
		int count = 0;
		int correct = 0;
		int wrong = 0;
		int universe = 0;
		while(it.hasNext())
		{
			String edge = it.next();
			String [] elements = edge.split("&");
			
			String opposite = elements[1]+"&"+elements[0];
			
			if(count >= begin && count <= end)
			{
				universe++;
				if(outputDirected.contains(edge))
				{
					correct++;
				}
				else if(outputDirected.contains(opposite))
				{
					wrong++;
				}
			}
			
			count++;
		}
		System.out.println("correctly predicted "+correct);
		System.out.println("wrongly predicted "+wrong);
		System.out.println("Total universe "+universe);
		
		float precision = (float) (100 * correct)/(correct+wrong);
		float recall = (float) (100 * correct)/(universe);
		System.out.println("Precision and recall are "+precision+", "+recall);
	}
	
	
	public void evaluatefold2() throws IOException
	{
		int totaldirected;
		BufferedReader br = new BufferedReader(new FileReader("rotf2_edges.txt"));
	
		System.out.println("===================FOLD 2 EVALUATION==================");
		String line1 = "";
		while((line1 = br.readLine()) != null)
		{
			String [] elements = line1.split("\t");
			String tail = elements[0];
			String head = elements[2];
			outputDirected.add(tail+"&"+head);
		}
		br.close();
		totaldirected = outputDirected.size();
		System.out.println("initial count on directed edges "+totaldirected);		
		
		Iterator<String> it  = directed.keySet().iterator();
		int count = 0;
		int correct = 0;
		int wrong = 0;
		int universe = 0;
		while(it.hasNext())
		{
			String edge = it.next();
			String [] elements = edge.split("&");
			
			String opposite = elements[1]+"&"+elements[0];
			
			if(count <= (directed.size())/2)
			{
				universe++;
				if(outputDirected.contains(edge))
				{
					correct++;
				}
				else if(outputDirected.contains(opposite))
				{
					wrong++;
				}
			}
			
			count++;
		}
		System.out.println("correctly predicted "+correct);
		System.out.println("wrongly predicted "+wrong);
		System.out.println("Total universe "+universe);
		
		float precision = (float) (100 * correct)/(correct+wrong);
		float recall = (float) (100 * correct)/(universe);
		System.out.println("Precision and recall are "+precision+", "+recall);
	}
	
	public void readInput() throws NumberFormatException, IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("hin.txt"));

		int count = 0;
		String record;

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
		
		br.close();
	}

	public static void main(String args[]) throws IOException
	{
		kfoldevaluation obj = new kfoldevaluation();
	
		obj.readInput();
		String output = args[0];
		int k = Integer.parseInt(args[1]);
		int fold = Integer.parseInt(args[2]);
		
		obj.evaluate(output, k, fold);
		//obj.evaluatefold2();
		//object1.RandomTwoFold(undirected, directed, 2);

	}

}

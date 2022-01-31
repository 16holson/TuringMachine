
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.IOException;


class Vertex
{
	String vertexId;
	String readLetter;
	String writeLetter;
	String direction;
	String vertexDesId;
	public Vertex(String vi, String rl, String wl, String d, String vd)
	{
		vertexId = vi;
		readLetter = rl;
		writeLetter = wl;
		direction = d;
		vertexDesId = vd;
	}
	public Vertex(String vi)
	{
		vertexId = vi;
		readLetter = "";
		writeLetter = "";
		direction = "";
		vertexDesId = vi;
	}
	public String toString()
	{
		return "vertexId: " + vertexId + ", readLetter: " + readLetter + ", writeLetter: " + writeLetter + ", direction: " + direction + ", vertexDesId: " + vertexDesId + "\n";
	}
}
class Edge
{
	String startVer;
	String endVer;
	Vertex ver;
	public Edge(String sv, String ev, Vertex v)
	{
		startVer = sv;
		endVer = ev;
		ver = v;
	}
}

public class TuringMachine {
	
	private static Map<String, List<Vertex>> graph = new HashMap<String, List<Vertex>>();
	private static ArrayList<Edge> edges = new ArrayList<Edge>();
	private static List<String> vertexList = new ArrayList<String>();
	private static ArrayList<String> tape = initList();
	private static String graphKey = "";
	private static int vertexPos = 0;
	private static int tapePos = 0;
	private static int count = 0;
	
	public static void main(String[] args) 
	{
		
		if(args.length < 2)
		{
			System.out.println("Not enough parameters: <fileName> <WORD>");
		}
		else
		{
			String path = args[0];
			String word = args[1];
			String line = "";
			
			try 
			{
				BufferedReader input = new BufferedReader(new FileReader(path));
				
				int index = 0;
				
				//putting word into tape
				while(true)
				{
					tape.set(index, String.valueOf(word.charAt(index)));
					index++;
					if(index >= word.length())
					{
						break;
					}
				}
				
				//Setting the Turing Machine
				while((line = input.readLine()) != null)
				{
					if(line.contains("#")){}
					else
					{
						String values[] = line.split(",");
						if(values.length == 1)//Final State
						{
							Vertex v = new Vertex(values[0].trim());
							edges.add(new Edge(values[0].trim(), "-1", v));
							
						}
						else
						{
							String source = values[0].trim();
							String des = values[4].trim();
							values[1] = values[1].trim();
							values[2] = values[2].trim();
							values[3] = values[3].trim();
							if(source.toLowerCase().contains("start"))
							{
								graphKey = source;
							}
							Vertex v = new Vertex(source, values[1], values[2], values[3], des);
							edges.add(new Edge(source, des, v));
						}
					}
				}
	
				//Getting the number of verticies
				ArrayList<String> temp = new ArrayList<String>();
				for(int i = 0; i < edges.size(); i++)
				{
					temp.add(edges.get(i).startVer);
				}
				boolean hasFinalState = false;
				for(int i = 0; i < temp.size(); i++)
				{
					if(temp.get(i).toLowerCase().contains("halt"))
					{
						hasFinalState = true;
					}
				}
				
				HashSet<String> hset = new HashSet<String>(temp);
				Iterator<String> it = hset.iterator();
				int j = 0;
				//putting verticies into vertexList
				while(it.hasNext())
				{
					vertexList.add(j, it.next());
					j++;
				}
				//Adding to graph
				boolean hasFinalState2 = false;
				for(int i = 0; i < vertexList.size(); i++)
				{
					ArrayList<Vertex> temp2 = new ArrayList<Vertex>();
					for(int k = 0; k < edges.size(); k++)
					{
						if(vertexList.get(i).contains(edges.get(k).startVer))
						{
							temp2.add(edges.get(k).ver);
						}
						if(edges.get(k).endVer.toLowerCase().contains("halt"))
						{
							hasFinalState2 = true;
						}
					}
					graph.put(vertexList.get(i), temp2);
				}
				if(hasFinalState == false && hasFinalState2 == true)
				{
					Vertex v = new Vertex("Halt");
					ArrayList<Vertex> temp3 = new ArrayList<Vertex>();
					temp3.add(v);
					edges.add(new Edge("Halt", "-1", v));
					graph.put("Halt", temp3);
				}
				//Trying the word
				boolean answer = traversal();
				if(answer == true)
				{
					System.out.println("Accepted: " + word);
				}
				else
				{
					System.out.println("Rejected: " + word);
				}
				
				input.close();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}//end else
	}//end main
	
	public static boolean traversal()
	{
		if(graph.get(graphKey).get(vertexPos).vertexId.toLowerCase().contains("halt"))
		{
			return true;
		}
		else if(count > 499)
		{
			return false;
		}
		else if(graph.get(graphKey).get(vertexPos).readLetter.contains(tape.get(tapePos)))
		{
			tape.set(tapePos, graph.get(graphKey).get(vertexPos).writeLetter);
			count++;
			if(graph.get(graphKey).get(vertexPos).direction.toLowerCase().contains("r"))
			{
				tapePos++;
			}
			else
			{
				if(tapePos-1 == -1)
				{
					return false;
				}
				else
				{
					tapePos--;
				}
			}
			graphKey = graph.get(graphKey).get(vertexPos).vertexDesId;
			
			vertexPos = 0;
			return traversal();
		}
		else if(vertexPos == graph.get(graphKey).size()-1)
		{
			return false;
		}
		else
		{
			vertexPos++;
			return traversal();
		}
		
		
	}
	public static ArrayList<String> initList()
	{
		final ArrayList<String> list = new ArrayList<>();
		for(int i = 0; i < 500; i++)
		{
			list.add("_");
		}
		return list;
	}

}

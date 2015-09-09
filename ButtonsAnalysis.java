/**
 * @author Kevin Kim; March 25, 2014
 *
 * Description: 
 * Buttons is a combinatorial game in which players take turns selecting nodes from a given connected graph. 
 * Each node is colored either red or blue and a move consists of flipping a node to its other color.
 * A move can only take place when the node is connected to at least one other node with an edge. 
 * When a given node is flipped to its other color, if the adjacent nodes are of matching color, the edge is deleted.
 *
 * For exampe, suppose a graph is illustrated as such:
 *
 *      x---o---x
 *
 * Player 1 has three moves to choose from. If he selects the middle node, then he wins since the Player 2
 * had no legal moves thereafter.
 *
 *      x---o---x becomes x   o   x 
 *
 * If Player 1 selected the outernodes, then Player 2 wins:
 *
 *      x---o---x  becomes o   o---x then o   o   o
 *                         P1 move        P2 move
 *
 * From the given graph x---o---x, a player can move to a losing position (0 value), or to a winning value consisting
 * of 1 move (1 value).
 *
 * A Grundy value is the first value from the set of available moves whose value (assigned to natural numbers) is not present. For example, 
 * the values assigned to the graph n = x---o---x, are 0 and 1 so Grundy(n) = 2
 *
 * A non-zero grundy value implies there is a move in the graph that leads to a zero (bad move for player 2) thus 
 * Player 1 has an optimal move to win the game. 
 *
 * Phew... so after all that explanation, what this program does is calculate the grundy value of the buttons game whose
 * nodes form a path (i.e each node is adjacent to either 0, 1, or 2 nodes). Games can be represented as a string of 0's and 1's.
 * So in the example game given, the game can be represented by 010 or 101.
 *
 * Therefore AnyPathGrundy("101") outputs 2
 *
 */

package Analysis;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.math.BigInteger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ButtonsAnalysis
{

    /* hashtable to store previously determined grundry values so lessen the header of the recursive functions */
	public static Hashtable<Integer, Integer> GValues = new Hashtable<Integer, Integer>(250);

    /* function to translate a string to an array of characters */
    private static char[] fromString(String s)
    {
    	char[] value = new char[s.length()];
    	for (int i = 0; i < s.length(); i++)
    		value[i] = s.charAt(i);
    	return value;
    }

    /* AnyPathGrundy: Given a buttons game which forms a path, the grundy value of the game is outputted */
	public static int AnyPathGrundy(char[] g)
	{
        /* Array to hold the value of a given move */
		List toMex = new ArrayList();

        /* If the length of the game is 1, then we know the grundy value is 0 */
		if (g.length == 1)
		{
            /* Store grundy value into hashtable if it is not already there */
			if (!GValues.containsKey(Arrays.hashCode(g)))
				GValues.put(Arrays.hashCode(g), 0);
			return 0;
		}

        /* If the length of game is 2, then game can either be of value 0 or 1 */
		if (g.length == 2)
		{
			if (g[0] != g[1])
			{
                /* Store grundy value into hashtable if not already present */
				if (!GValues.containsKey(Arrays.hashCode(g)))
					GValues.put(Arrays.hashCode(g), 1);
				return 1;
			}
			else
			{
                /* Store grundy value into hashtable if not already present */
				if (!GValues.containsKey(Arrays.hashCode(g)))
					GValues.put(Arrays.hashCode(g), 0);
				return 0;
			}
		}
		else
		{
            /* For every node in the game, determine its value then store in an array of values */
			for (int i = 0; i < g.length; i++)
			{
				char[] Partition;
				char[] Partition0;
				if (i == 0)
				{
                    /* If the leftmost node and its adjacent node is of the same color */
					if (g[i] == g[i + 1])
					{
						char[] G = Arrays.copyOf(g, g.length);
						G[i] = (G[i] == '1' ? '0' : '1');
						if (GValues.containsKey(Arrays.hashCode(G)))
							toMex.add(GValues.get(Arrays.hashCode(G)));
						else
							toMex.add(AnyPathGrundy(G));
					}
                    /* If the leftmost node and its adjacent node is not of the same color */
					else
					{
						Partition = Arrays.copyOfRange(g, 1, g.length);
						if (GValues.containsKey(Arrays.hashCode(Partition)))
							toMex.add(GValues.get(Arrays.hashCode(Partition)));
						else
							toMex.add(AnyPathGrundy(Partition));
					}
				}
				else if (i == g.length - 1)
				{   
                    /* If the rightmost node and its adjacent node is of the same color */
					if (g[g.length-1] == g[g.length-2])
					{
						char[] G = Arrays.copyOf(g, g.length);
						G[i] = (G[i] == '1' ? '0' : '1');
						if (GValues.containsKey(Arrays.hashCode(G)))
							toMex.add(GValues.get(Arrays.hashCode(G)));
						else
							toMex.add(AnyPathGrundy(G));
					}
                    /* If the rightmost node and its adjacent node is of different color */
					else
					{
						Partition = Arrays.copyOfRange(g, 0, g.length - 1);
						if (GValues.containsKey(Arrays.hashCode(Partition)))
							toMex.add(GValues.get(Arrays.hashCode(Partition)));
						else
							toMex.add(AnyPathGrundy(Partition));
					}
				}
				else
				{
                    /* If adjacent nodes of non-outer nodes are of different color */
					if (g[i] != g[i-1] && g[i] != g[i + 1])
					{
                        /* We know the move will partition the game into two games */
						Partition = Arrays.copyOfRange(g, 0, i);
						Partition0 = Arrays.copyOfRange(g, i + 1, g.length);
						int P, P0;
                        /* First check if the partitioned games are already stored in the hashtable */
						if(GValues.containsKey(Arrays.hashCode(Partition)))
							P = GValues.get(Arrays.hashCode(Partition));
                        /* If the partitioned game is not in the hashtable, call recursive function with the partitioned portion */
						else
							P = AnyPathGrundy(Partition);
						if (GValues.containsKey(Arrays.hashCode(Partition0)))
							P0 = GValues.get(Arrays.hashCode(Partition0));
						else
							P0 = AnyPathGrundy(Partition0);
                        /* Add game value to value array*/
						toMex.add(P^P0);
					}

                    /* If only the left adjacent nodes is of different color */
					else if (g[i] != g[i - 1] && g[i] == g[i + 1])
					{
						char[] G = Arrays.copyOf(g, g.length);
						G[i] = (G[i] == '1' ? '0' : '1');
						Partition = Arrays.copyOfRange(G, 0, i);
						Partition0 = Arrays.copyOfRange(G, i, G.length);
						int P, P0;
						if(GValues.containsKey(Arrays.hashCode(Partition)))
							P = GValues.get(Arrays.hashCode(Partition));
						else
							P = AnyPathGrundy(Partition);
						if (GValues.containsKey(Arrays.hashCode(Partition0)))
							P0 = GValues.get(Arrays.hashCode(Partition0));
						else
							P0 = AnyPathGrundy(Partition0);
						toMex.add(P^P0);


					}
                    /* If only the right adjacent node is of different color */
					else if (g[i] == g[i - 1] && g[i] != g[i + 1])
					{
						char[] G = Arrays.copyOf(g, g.length);
						G[i] = (G[i] == '1' ? '0' : '1');
						Partition = Arrays.copyOfRange(G, 0, i + 1);
						Partition0 = Arrays.copyOfRange(G, i + 1, G.length);
						int P, P0;
						if(GValues.containsKey(Arrays.hashCode(Partition)))
							P = GValues.get(Arrays.hashCode(Partition));
						else
							P = AnyPathGrundy(Partition);
						if (GValues.containsKey(Arrays.hashCode(Partition0)))
							P0 = GValues.get(Arrays.hashCode(Partition0));
						else
							P0 = AnyPathGrundy(Partition0);
						toMex.add(P^P0);
					}
                    /* adjacent nodes are of the same color */
					else
					{
						char[] G = Arrays.copyOf(g, g.length);
						G[i] = (G[i] == '1' ? '0' : '1');
						if (GValues.containsKey(Arrays.hashCode(G)))
							toMex.add(GValues.get(Arrays.hashCode(G)));
						else
							toMex.add(AnyPathGrundy(G));
					}
				}

			}
		}
        
        /* Find the first game value that is not contained in the array. That will become our grundy value */
		for (int i = 0; i < toMex.size(); i++)
		{
			if (!toMex.contains(i))
			{
				if (!GValues.containsKey(Arrays.hashCode(g)))
					GValues.put(Arrays.hashCode(g), i);
				return i;
			}
		}
        /* If all values were found in the previous loop, we have found a new grundy value. Store game in the hash table,
         * return the size of the value array (which is the grundy value */
		if (!GValues.containsKey(Arrays.hashCode(g)))
			GValues.put(Arrays.hashCode(g), toMex.size());
		return toMex.size();
	}


	public static void main(String[] args)
	{
		/**
		System.out.println(AnyPathGrundy(fromString("100")));
		System.out.println(AnyPathGrundy(fromString("001")));
		System.out.println(AnyPathGrundy(fromString("110")));
		System.out.println(AnyPathGrundy(fromString("011")));
		System.out.println(AnyPathGrundy(fromString("1001")));
		System.out.println(AnyPathGrundy(fromString("101")));
		System.out.println(AnyPathGrundy(fromString("1001")));
		*/

		/**
		System.out.println(AnyPathGrundy(fromString("0")));
		System.out.println(AnyPathGrundy(fromString("10")));
		System.out.println(AnyPathGrundy(fromString("01")));
		System.out.println(AnyPathGrundy(fromString("100")));
		System.out.println(AnyPathGrundy(fromString("011")));
		System.out.println(AnyPathGrundy(fromString("101")));
		System.out.println(AnyPathGrundy(fromString("010")));
		*/


		for (int i = 0; i < 100; i++)
			System.out.print(AnyPathGrundy(fromString(Integer.toBinaryString(i + 1))));


/**
 * Uncomment to run grundy values of game of alternating vertices into a text file 
	 try
	 {
      	PrintStream out = new PrintStream(new FileOutputStream("BinaryOutput.txt"));
		BigInteger i = new BigInteger("1");
		BigInteger one = new BigInteger("1");
		int k = 1;
		while(k>0)
		{
			int Grundy = AnyPathGrundy(fromString(i.toString(2)));
			out.println((i) + ": \t\t\t\t" + Grundy);
			i = i.add(one);
		}
      	out.close();
	 }
	 catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    */


	}
}

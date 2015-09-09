# Buttons combinatorial game
Final project for CGT where game theorems were proven via dynamic programming.
# Description

@author Kevin Kim; March 25, 2014

Description: 
Buttons is a combinatorial game in which players take turns selecting nodes from a given connected graph.
Each node is colored either red or blue and a move consists of flipping a node to its other color.
A move can only take place when the node is connected to at least one other node with an edge.
When a given node is flipped to its other color, if the adjacent nodes are of matching color, the edge is deleted.

For exampe, suppose a graph is illustrated as such:

     x---o---x

Player 1 has three moves to choose from. If he selects the middle node, then he wins since the Player 2
had no legal moves thereafter.

     x---o---x becomes x   o   x 

If Player 1 selected the outernodes, then Player 2 wins:

     x---o---x  becomes o   o---x then o   o   o
                        P1 move        P2 move

From the given graph x---o---x, a player can move to a losing position (0 value), or to a winning value consisting
of 1 move (1 value).

A Grundy value is the first value from the set of available moves whose value (assigned to natural numbers) is not present. For example,
the values assigned to the graph n = x---o---x, are 0 and 1 so Grundy(n) = 2

A non-zero grundy value implies there is a move in the graph that leads to a zero (bad move for player 2) thus
Player 1 has an optimal move to win the game.

Phew... so after all that explanation, what this program does is calculate the grundy value of the buttons game whose
nodes form a path (i.e each node is adjacent to either 0, 1, or 2 nodes). Games can be represented as a string of 0's and 1's.
So in the example game given, the game can be represented by 010 or 101.

Therefore AnyPathGrundy("101") outputs 2


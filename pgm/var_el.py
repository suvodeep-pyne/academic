#
# Variable Elimination using heuristic
# Author: Suvodeep Pyne
#

import random
import operator
import copy
import pprint as pp

# Create an inverse graph mapping from child to parents
def inverse_graph(dag):
	inv_dag =  [[0 for x in range(len(dag))] for y in range(len(dag[0]))]
	for i in range(len(dag)):
		for j in range(len(dag[0])):
			inv_dag[j][i] = dag[i][j];
	return inv_dag

# Find all factors in the given DAG
def find_factors(dag):
	N = len(dag)
	inv_dag = inverse_graph(dag)
	factors = []
	for rv in range(N):
		factor = set([p for p in range(N) if inv_dag[rv][p] == 1])
		if len(factor) > 0:
			factor.add(rv)
			factors.append(factor)
	return factors

# Update edges of the graph based on a factor
def update_edges_factor(uag, factor):
	for f1 in factor:
		for f2 in factor:
			if f1 != f2:
				uag[f1][f2] = 1
	
# Fill all the edges present in the factors
def fill_edges(factors):
	N = len(dag)
	uag =  [[0 for x in range(N)] for y in range(N)]

	# Connect all random variables belonging to the same factor
	for factor in factors:
		update_edges_factor(uag, factor)
	return uag


# Heuristic: Minimum degree of nodes
def ordering(udag):
	N = len(udag)
	degree = {}
	for rv in range(N):
		degree[rv] = sum(udag[rv])
	
	return [elements[0] for elements in sorted(degree.iteritems(), key=operator.itemgetter(1))]

# Retrieve all rv's which share factors with rv
def dependents(rv, factors):
	dep = set()
	for factor in factors:
		if rv in factor:
			dep.update(factor)
	
	# Return dependents excluding the input variable rv
	dep.discard(rv)
	return dep

# Perform Variable Elimination
def var_elim(dag, var):
	factors = find_factors(dag)
	udag = fill_edges(factors)
	order = ordering(udag)

	# Starting induced Graph creation from base graph
	inducedGraph = udag

	maxcliquesize = 1
	for f in factors:
		maxcliquesize = max(maxcliquesize, len(f))
	
	oldfactors = copy.deepcopy(factors)
	newfactors = []
	# print oldfactors
	for velim in order:
		newfactor = set()
		for factor in oldfactors:
			if velim in factor:
				newfactor.update(factor)
			else:
				newfactors.append(factor)
		newfactor.discard(velim)
		if len(newfactor) > 1:
			newfactors.append(newfactor)
			update_edges_factor(inducedGraph, newfactor)
			maxcliquesize = max(maxcliquesize, len(newfactor))
		oldfactors = newfactors
		newfactors = []
		# print oldfactors
	
	inducedWidth = maxcliquesize - 1
	return inducedGraph, inducedWidth

def graph1():
	dag = [[0 for x in range(12)] for y in range(12)]
	dag[0][3] = 1 
	dag[1][3] = 1 
	dag[2][4] = 1 
	dag[3][6] = 1 
	dag[3][7] = 1 
	dag[4][7] = 1 
	dag[4][8] = 1 
	dag[5][8] = 1 
	dag[5][9] = 1 
	dag[6][10] = 1 
	dag[7][10] = 1 
	dag[7][11] = 1 
	dag[8][11] = 1 
	dag[8][9] = 1

	return dag

def graph2():
	dag2 = [[0 for x in range(10)] for y in range(10)]                          
	dag2[1][3] = 1
	dag2[3][0] = 1
	dag2[3][4] = 1
	dag2[3][6] = 1
	dag2[4][5] = 1
	dag2[4][2] = 1
	dag2[5][7] = 1
	dag2[6][8] = 1
	dag2[7][8] = 1
	dag2[8][9] = 1

	return dag2

def example_graph():
	N = 8
	dag = [[0 for x in range(N)] for y in range(N)]
	dag[0][2] = 1
	dag[1][2] = 1
	dag[2][4] = 1
	dag[2][5] = 1
	dag[4][6] = 1
	dag[5][6] = 1
	dag[5][3] = 1
	dag[5][7] = 1
	dag[6][7] = 1

	return dag

if __name__ == "__main__":
	dag = graph2()
	print 'Input Bayesian Network as Adj Matrix:'
	pp.pprint(dag)
	print 'Factors:', find_factors(dag)
	print 'Ordering:', ordering(fill_edges(find_factors(dag)))

	print 'Variable Elimination'
	inducedGraph, inducedWidth = var_elim(dag, 9)

	print 'Induced Graph'
	pp.pprint(inducedGraph)
	print 'Induced Width :', inducedWidth
	

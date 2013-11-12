#
# Variable Elimination using heuristic
# Author: Suvodeep Pyne
#

import random
import operator
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
		if len(factor) > 0: factors.append(factor)
	return factors

# Fill all the edges present in the factors
def fill_edges(factors):
	N = len(dag)
	udag =  [[0 for x in range(N)] for y in range(N)]

	# Connect all random variables belonging to the same factor
	for factor in factors:
		for f1 in factor:
			for f2 in factor:
				if f1 != f2:
					udag[f1][f2] = 1

	return udag

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
	for v, parents in factors.iteritems():
		if v == rv:
			dep.update(factors[rv])
		elif rv in parents:
			dep.update(parents)
			dep.add(v)
	
	# Return dependents excluding the input variable rv
	dep.discard(rv)
	return dep

# Perform Variable Elimination
def var_elim(dag, var):
	factors = find_factors(dag)
	udag = fill_edges(factors)
	order = ordering(udag)

	for velim in order:
		pass		

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
	dag = example_graph()
	print 'Input Bayesian Network as Adj Matrix:'
	pp.pprint(dag)
	pp.pprint(find_factors(dag))
	pp.pprint(fill_edges(find_factors(dag)))
	pp.pprint(ordering(fill_edges(find_factors(dag))))
	

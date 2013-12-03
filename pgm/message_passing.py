#
# Pearl's message passing algorithm
# Author: Suvodeep Pyne
#

import random
import operator
import copy
import pprint as pp

# Returns values of a variable X
# In the example graph, all variables are binary
def values(X):
	return [0, 1]

# Create an inverse graph mapping from child to parents
def inverse_graph(dag):
	inv_dag = {}
	for node in dag:
		inv_dag[node] = set()
	for p, children in dag.iteritems():
		for c in children:
			inv_dag[c].add(p)
		
	return inv_dag

# Get the roots of the graph
def find_roots(dag):
	roots = set(dag.keys())
	for children in dag.values():
		roots.difference_update(children)
	return roots

def initialize_network(dag):
	inv_dag = inverse_graph(dag)
	lambdas = {}
	pis = {}
	for var in dag:
		lambdas[var] = {}
		pis[var] = {}

	for var in dag:
		for x in values(var):
			lambas[var][x] = 1
		for p in inv_dag[var]:
			lambdas[var][p] = {}
			for x in values(p):
				lambdas[var][p][x] = 1
		for c in dag[var]:
			pis[var][c] = {}
			for x in values(c):
				pis[var][c][x] = 1

	

def example_graph():
	G = {}
	G['A'] = set(['C'])
	G['B'] = set(['C'])
	G['C'] = set(['D'])
	G['D'] = set(['F', 'G'])
	G['E'] = set()
	G['F'] = set()
	G['G'] = set()
	return G


if __name__ == "__main__":
	pass

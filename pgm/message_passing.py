#
# Pearl's message passing algorithm
# Author: Suvodeep Pyne
#

import random
import operator
import copy
import pprint as pp

E = {}
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
	global E
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
	
	for root in find_roots(dag):
		for x in values(root):
			pis[root][x] = prob[root]
			# P(root|e) = P(root)
		for c in dag[root]:
			send_pi_msg(root, c)

# X is a specific assignment: dict of Variable : value
# E is the dict of evidence variables and their values
def update_network(dag, V):
	# update evidence with the current assignment
	E = E.update(V)

	for var in V:
		for v in values(var):
			if v == V[var]:
				lambdas[var][v] = 1
				pis[var][v] = 1
				prob[var][v] = 1
			else:
				lambdas[var][v] = 0
				pis[var][v] = 0
				prob[var][v] = 0
	
	lambda_recipients = set()
	for var in V:
		for p in inv_dag[var]:
			if p not in E:
				send_lambda_msg(dag, var, p)

	for var in V:
		for c in dag[var]:
			send_pi_msg(dag, var, c)

def send_lambda_msg(dag, child, parent):
	for x in values(parent):
		


def example_input():
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

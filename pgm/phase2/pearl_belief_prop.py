#
# Pearl's message passing algorithm
# Author: Suvodeep Pyne
#

import copy

# Global parameters
E = set()
e = set()
lambdaX = {}
lambdaYX = {}
piX = {}
pi = {}
inv_dag = {}

# Returns values of a variable X
# In the example graph, all variables are binary
def values(X):
	return [0, 1]

# Create an inverse graph mapping from child to parents
def inverse_graph(dag):
	inv_dag = {}
	for node in dag:
		inv_dag[node] = []

	for p, children in dag.iteritems():
		for c in children:
			inv_dag[c].append(p)
	
	return inv_dag
	
# Get the roots of the graph
def find_roots(dag):
	roots = set(dag.keys())
	for children in dag.values():
		roots.difference_update(children)
	return roots

prob_evid = {}
prob = {}
def initialize_network(dag):
	#values is a dictionary of variables and the list of values the variable can take
	global E, e, lambdaX, lambdaYX, pi, inv_dag, piX, prob_evid, prob
	E = set()
	e = set()
	lambdaX = {}
	lambdaYX = {}
	pi = {}
	piX = {}
	prob_evid = {}

	inv_dag = inverse_graph(dag)
	for node in dag.keys():
		lambdaX[node] = {}
		for value in values(node):
			lambdaX[node][value] = 1

		lambdaYX[node] = {}
		
		for parent in inv_dag[node]:
			lambdaYX[node][parent] = {}
			for value in values(parent):
				lambdaYX[node][parent][value] = 1	

		for child in dag[node]:
			if child not in pi:
				pi[child] = {}
			pi[child][node] = {}
			for value in values(child):
				pi[child][node][value] = 1	
		
	for root in find_roots(dag):
		if root not in piX:
			piX[root] = {} 
		prob_evid[root] = {}
		for value in values(root):
			piX[root][value] = prob[str(root) + str(value)]
			prob_evid[root][value] = prob[str(root) + str(value)] 

		for child in dag[root]:
			send_pi_msg(dag, root, child);

#Update function to update the network with evidence variables
def update_network(dag, variable, value):
	global E, e, lambdaX, lambdaYX, pi, inv_dag, piX, prob_evid
	E = E.union(set([variable]))
	e = e.union(set([value]))

	for v in values(variable):
		if(v == value):
			lambdaX[variable][v] = 1.0
			piX[variable][v] = 1.0
			prob_evid[variable][v] = 1.0
		else:
			lambdaX[variable][v] = 0.0
			piX[variable][v] = 0.0
			prob_evid[variable][v] = 0.0

	for parent in inv_dag[variable]:
		if parent not in E: 
			send_lambda_msg(dag, variable, parent)

	for child in dag[variable]:
		send_pi_msg(dag, variable, child)

def marginalize_pi(X, x):
	assign = []
	return_value = all_combinations_pi(assign, inv_dag[X], 0, X, x)
	return return_value

def marginalize_lambda(Y, y, X, x):
	global E, e, lambdaX, lambdaYX, pi, inv_dag, piX, prob_evid

	parents = []
	for parent in inv_dag[Y]:
		if parent != X:
			parents.append(parent)

	assign = [str(X) + str(x)]
	marginalized_prob = all_combinations_lambda(assign, parents, 0, Y, y)
	return marginalized_prob 

def send_lambda_msg(dag, Y, X):
	global E, e, lambdaX, lambdaYX, pi, inv_dag, piX, prob_evid

	pTilde = {}
	for value in values(X):
		lambdaYX[Y][X][value] = 0
		for valuep in values(Y):
			lambdaYX[Y][X][value] += marginalize_lambda(Y, valuep, X, value) * lambdaX[Y][valuep]

		lambdaX[X][value] = 1	
		for child in dag[X]:
			lambdaX[X][value] = lambdaX[X][value] * lambdaYX[child][X][value]

		pTilde[value] = lambdaX[X][value] * piX[X][value]

	alpha = sum(pTilde.values())
	if X not in prob_evid:
		prob_evid[X] = {}

	for value in values(X):
		prob_evid[X][value] = (float)(pTilde[value]) / alpha

	for parent in inv_dag[X]:
		if parent not in E:
			send_lambda_msg(dag, X, parent)

	for child in (set(dag[X]) - set([Y])):
		send_pi_msg(dag, X, child)

def all_combinations_lambda(assign, parents, pos, Y, y):
	global prob
	if pos == len(parents):
		prob_r = prob[str(Y) + str(y)][''.join(sorted(assign))] 
		for parent in assign[1:]:
			prob_r = prob_r * pi[Y][parent[0]][int(parent[1])]
		return prob_r
	prob_r = 0
	possibleAssign = str(parents[pos]) + str(0)
	assign1 = copy.deepcopy(assign)
	assign2 = copy.deepcopy(assign)
	assign1.append(possibleAssign)
	prob_r += all_combinations_lambda(assign1, parents, pos + 1, Y, y)
	possibleAssign = str(parents[pos]) + str(1)
	assign2.append(possibleAssign)
	prob_r += all_combinations_lambda(assign2, parents, pos + 1, Y, y)
	return prob_r


def all_combinations_pi(assign, parents, pos, Y, y):
	if pos == len(parents):
		prob_r = prob[str(Y) + str(y)][''.join(sorted(assign))]
		for parent in assign:
			prob_r = prob_r * pi[Y][parent[0]][int(parent[1])]
		return prob_r 
	
	prob_r = 0
	possibleAssign = str(parents[pos]) + str(0)
	assign1 = copy.deepcopy(assign)
	assign2 = copy.deepcopy(assign)
	assign1.append(possibleAssign)
	prob_r += all_combinations_pi(assign1, parents, pos + 1, Y, y)
	possibleAssign = str(parents[pos]) + str(1)
	assign2.append(possibleAssign)
	prob_r += all_combinations_pi(assign2, parents, pos + 1, Y, y)
	return prob_r
	
def should_send_lambda_msg(X):
	global lambdaX

	for value in lambdaX[X]:
		if lambdaX[X][value] != 1.0: 
			return True
	
	return False

def send_pi_msg(dag, Z, X):
	global E, e, lambdaX, lambdaYX, pi, inv_dag, piX, prob_evid

	for z in values(Z):
		pi[X][Z][z] = piX[Z][z]
		for child in dag[Z]:
			if child != X:
				pi[X][Z][z] = pi[X][Z][z] * lambdaYX[child][Z][z]

	pTilde = {}
	if X not in piX:
		piX[X] = {}
	if X not in E:
		for x in values(X):
			piX[X][x] = marginalize_pi(X, x)
			pTilde[x] = piX[X][x] * lambdaX[X][x]

		alpha = sum(pTilde.values())
		if X not in prob_evid:
			prob_evid[X] = {}
		for x in values(X):
			prob_evid[X][x] = (float)(pTilde[x]) / alpha

		for child in dag[X]:
			send_pi_msg(dag, X, child)

	if should_send_lambda_msg(X):
		for parent in inv_dag[X]:
			if parent != Z and parent not in E:
				send_lambda_msg(dag, X, parent)

def example_probMat():
	global prob
	prob['A1'] = 0.7
	prob['A0'] = 0.3
	prob['B1'] = 0.4
	prob['B0'] = 0.6
	prob['C1'] = {}
	prob['C0'] = {}
	prob['C1']['A0B0'] = 0.1
	prob['C1']['A0B1'] = 0.5
	prob['C1']['A1B0'] = 0.3
	prob['C1']['A1B1'] = 0.9
	prob['C0']['A0B0'] = 0.9
	prob['C0']['A0B1'] = 0.5
	prob['C0']['A1B0'] = 0.7
	prob['C0']['A1B1'] = 0.1

	prob['D1'] = {}
	prob['D0'] = {}
	prob['D1']['C0'] = 0.8
	prob['D1']['C1'] = 0.3
	prob['D0']['C0'] = 0.2
	prob['D0']['C1'] = 0.7
	
	prob['E1'] = {}
	prob['E0'] = {}
	prob['E1']['C0'] = 0.2 
	prob['E1']['C1'] = 0.6
	prob['E0']['C0'] = 0.8
	prob['E0']['C1'] = 0.4

	prob['F1'] = {}
	prob['F0'] = {}
	prob['F1']['D0'] = 0.1
	prob['F1']['D1'] = 0.7
	prob['F0']['D0'] = 0.9
	prob['F0']['D1'] = 0.3

	prob['G1'] = {}
	prob['G0'] = {}
	prob['G1']['D0'] = 0.9
	prob['G1']['D1'] = 0.4
	prob['G0']['D0'] = 0.1
	prob['G0']['D1'] = 0.6
	return prob

def compute_probability(dag, Q, Z):
	initialize_network(dag)
	for variable, value in Z.iteritems():
		update_network(dag, variable, value)

	p = 1.0
	for variable, value in Q.iteritems():
		p *= prob_evid[variable][value]
		update_network(dag, variable, value)
	return p

def calc_probability(dag, Z, Q):	
	print "P(", Q, "|", Z, ") =", compute_probability(dag, Q, Z)

def example_dag():
	dag = {}
	dag['A'] = ['C']
	dag['B'] = ['C']
	dag['C'] = ['D', 'E']
	dag['D'] = ['F', 'G']
	dag['E'] = []
	dag['F'] = []
	dag['G'] = []
	return dag

if __name__ == '__main__':
	dag = example_dag() 
	prob = example_probMat()

	#Find the prob of A=1, given evidence
	Z = {'B':0}
	Q = {'A':1}
	calc_probability(dag, Z, Q)
	Z = {'D':0}
	calc_probability(dag, Z, Q)
	Z = {'D':0, 'B':0}
	calc_probability(dag, Z, Q)
	Z = {'D':0, 'G':1}
	
	#Find the prob for B=1, given evidence
	Q = {'B':1}
	Z = {'A':1}
	calc_probability(dag, Z, Q)
	Z = {'C':1}
	calc_probability(dag, Z, Q)
	Z = {'A':1, 'C':1}
	calc_probability(dag, Z, Q)
	
	#Find the prob of C=1, given evidence
	Q = {'C':1}
	Z = {}
	calc_probability(dag, Z, Q)
	Z = {'A':1}
	calc_probability(dag, Z, Q)
	Z = {'A':1, 'B':0}
	calc_probability(dag, Z, Q)
	Z = {'D':0}
	calc_probability(dag, Z, Q)
	Z = {'D':0, 'F':0}
	calc_probability(dag, Z, Q)
	
	#Find the prob of D=1, given evidence
	Q = {'D':1}
	Z = {}
	calc_probability(dag, Z, Q)
	Z = {'E':0}
	calc_probability(dag, Z, Q)
	Z = {'C':0}
	calc_probability(dag, Z, Q)
	Z = {'C':0, 'E':0}
	calc_probability(dag, Z, Q)
	Z = {'B':1, 'G':0}
	calc_probability(dag, Z, Q)
	Z = {'B':1, 'G':0, 'F':1}
	calc_probability(dag, Z, Q)

	#Find the prob of E=1, given evidence
	Q = {'E':1}
	Z = {}
	calc_probability(dag, Z, Q)
	Z = {'C':1}
	calc_probability(dag, Z, Q)
	Z = {'F':0}
	calc_probability(dag, Z, Q)
	Z = {'C':1, 'F':0}
	calc_probability(dag, Z, Q)
	Z = {'A':1, 'B':1}
	calc_probability(dag, Z, Q)
	
	#Find the prob of F=1, given evidence
	Q = {'F':1}
	Z = {}
	calc_probability(dag, Z, Q)
	Z = {'A':1}
	calc_probability(dag, Z, Q)
	Z = {'A':1, 'C':0}
	calc_probability(dag, Z, Q)
	Z = {'A':1, 'C':0, 'E':0}
	calc_probability(dag, Z, Q)
	Z = {'B':1, 'G':0}
	calc_probability(dag, Z, Q)

	#Find the prob of G=1, given evidence
	Q = {'G':1}
	Z = {}
	calc_probability(dag, Z, Q)
	Z = {'C':0}
	calc_probability(dag, Z, Q)
	Z = {'D':0, 'C':0}
	calc_probability(dag, Z, Q)
	Z = {'E':0}
	calc_probability(dag, Z, Q)
	Z = {'B':1, 'A':0}
	calc_probability(dag, Z, Q)


	Q = {'A':1, 'D':1}
	Z = {'F':0, 'B':1}
	calc_probability(dag, Z, Q)

	Q = {'C':0, 'E':1}
	Z = {'F':1, 'G':0}
	calc_probability(dag, Z, Q)

	Q = {'F':0, 'B':1}
	Z = {'G':0, 'E':1}
	calc_probability(dag, Z, Q)
	
	Q = {'G':1, 'B':0}
	Z = {'F':1, 'A':0}
	calc_probability(dag, Z, Q)

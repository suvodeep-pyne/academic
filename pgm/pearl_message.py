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
pi = {}
inv_dag = {}
originalDAG = {}
piX = {}
prob_evid = {}
prob = {}

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

def initialize_network(dag):
    #values is a dictionary of variables and the list of values the variable can take
    global E, e, lambdaX, lambdaYX, pi, inv_dag, originalDAG, piX, prob_evid, prob
    E = set()
    e = set()
    lambdaX = {}
    lambdaYX = {}
    pi = {}
    piX = {}
    prob_evid = {}

    inv_dag = inverse_graph(dag)
    originalDAG = dag
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
            send_pi_msg(root, child);

#Update function to update the network with evidence variables
def update_network(variable, value):
    global E, e, lambdaX, lambdaYX, pi, inv_dag, originalDAG, piX, prob_evid
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
        if parent in E: 
            continue

        send_lambda_msg(variable, parent)

    for child in originalDAG[variable]:
        send_pi_msg(variable, child)

def find_all_combs(assign, parents, index, Y, y):
    global prob
    if index == len(parents):
        assign_sort = sorted(assign)
        result = ''.join(assign_sort)
        prob_r = prob[str(Y) + str(y)][result] 
        for parent in assign[1:]:
            #print "Value of pi is:", pi
            #print parent[0], Y, parent[1]
            prob_r = prob_r * pi[Y][parent[0]][int(parent[1])]
        return prob_r
   
    prob_r = 0
    possibleAssign = str(parents[index]) + str(0)
    assign1 = copy.deepcopy(assign)
    assign2 = copy.deepcopy(assign)
    assign1.append(possibleAssign)
    prob_r += find_all_combs(assign1, parents, index+1, Y, y)
    possibleAssign = str(parents[index]) + str(1)
    assign2.append(possibleAssign)
    prob_r += find_all_combs(assign2, parents, index+1, Y, y)
    return prob_r
    

def marginalize(Y, y, X, x):
    global E, e, lambdaX, lambdaYX, pi, inv_dag, originalDAG, piX, prob_evid

    parents = []
    for parent in inv_dag[Y]:
        if parent == X:
            continue
        parents.append(parent)

    assign = [str(X) + str(x)]
    marginalized_prob = 0
    marginalized_prob = find_all_combs(assign, parents, 0, Y, y)
    return marginalized_prob 

def send_lambda_msg(Y, X):
    global E, e, lambdaX, lambdaYX, pi, inv_dag, originalDAG, piX, prob_evid

    pTilde = {}
    for value in values(X):
        lambdaYX[Y][X][value] = 0
        for valuep in values(Y):
            lambdaYX[Y][X][value] += marginalize(Y, valuep, X, value) * lambdaX[Y][valuep]

        lambdaX[X][value] = 1    
        for child in originalDAG[X]:
            lambdaX[X][value] = lambdaX[X][value] * lambdaYX[child][X][value]

        #print "Lambda node is:", lambdaX[X][value]
        #print "Pi node is:", piX[X][value]
        pTilde[value] = lambdaX[X][value] * piX[X][value]

    #print "lambda node after message passing", lambdaX
    alpha = sum(pTilde.values())
   
    if X not in prob_evid:
        prob_evid[X] = {}

    for value in values(X):
        prob_evid[X][value] = (float)(pTilde[value]) / alpha

    for parent in inv_dag[X]:
        if parent in E:
            continue

        send_lambda_msg(X, parent)

    for child in (set(originalDAG[X]) - set([Y])):
        send_pi_msg(X, child)

def find_all_combs_pi(assign, parents, index, Y, y):
    if index == len(parents):
        assign_sort = sorted(assign)
        result = ''.join(assign_sort)
        prob_r = prob[str(Y) + str(y)][result]
        for parent in assign:
            prob_r = prob_r * pi[Y][parent[0]][int(parent[1])]
        return prob_r 
    
    prob_r = 0
    possibleAssign = str(parents[index]) + str(0)
    assign1 = copy.deepcopy(assign)
    assign2 = copy.deepcopy(assign)
    assign1.append(possibleAssign)
    prob_r += find_all_combs_pi(assign1, parents, index+1, Y, y)
    possibleAssign = str(parents[index]) + str(1)
    assign2.append(possibleAssign)
    prob_r += find_all_combs_pi(assign2, parents, index+1, Y, y)
    return prob_r
    
def marginalize_pi(X, x):
    assign = []
    return_value = 0
    return_value = find_all_combs_pi(assign, inv_dag[X], 0, X, x)
    #print "return value is", return_value
    return return_value

def check_lambda(X):
    global E, e, lambdaX, lambdaYX, pi, inv_dag, originalDAG, piX, prob_evid

    for value in lambdaX[X]:
		if lambdaX[X][value] != 1.0: 
			return True
    
    return False

def send_pi_msg(Z, X):
    #print "Sending pi message from ", Z, "to", X
    global E, e, lambdaX, lambdaYX, pi, inv_dag, originalDAG, piX, prob_evid

    for z in values(Z):
        #print "Value of pi is:", pi[X], X, Z
        pi[X][Z][z] = piX[Z][z]
        for child in originalDAG[Z]:
            if child == X:
                continue
            pi[X][Z][z] = pi[X][Z][z] * lambdaYX[child][Z][z]

    pTilde = {}
    if X not in piX:
        piX[X] = {}
    if X not in E:
        #check this
        for x in values(X):
            piX[X][x] = marginalize_pi(X, x)
            pTilde[x] = piX[X][x] * lambdaX[X][x]
    

        alpha = sum(pTilde.values())
        if X not in prob_evid:
            prob_evid[X] = {}
   
        for x in values(X):
            prob_evid[X][x] = (float)(pTilde[x]) / alpha

        for child in originalDAG[X]:
            send_pi_msg(X, child)

    if check_lambda(X):
        for parent in inv_dag[X]:
            if parent == Z:
                continue
            if parent in E:
                continue
            send_lambda_msg(X, parent)

def get_prob():
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



def get_final_probs(dag, nodes, evidence, observe):    
    initialize_network(nodes, dag)
    for variable, value in evidence.iteritems():
        update_network(variable, value)


    for variable, value in observe.iteritems():
        print prob_evid[variable][value]

if __name__ == '__main__':
    dag = {}
    dag['A'] = ['C']
    dag['B'] = ['C']
    dag['C'] = ['D', 'E']
    dag['D'] = ['F', 'G']
    dag['E'] = []
    dag['F'] = []
    dag['G'] = []
    
    nodes = dag.keys()
    prob = get_prob()

    #Find the prob of A=1, given evidence
    evidence = {'B':0}
    observe = {'A':1}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'D':0}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'D':0, 'B':0}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'D':0, 'G':1}
    
    #Find the prob for B=1, given evidence
    observe = {'B':1}
    evidence = {'A':1}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'C':1}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'A':1, 'C':1}
    get_final_probs(dag, nodes, evidence, observe)
    
    #Find the prob of C=1, given evidence
    observe = {'C':1}
    evidence = {}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'A':1}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'A':1, 'B':0}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'D':0}
    get_final_probs(dag, nodes, evidence, observe)
    evidence = {'D':0, 'F':0}
    get_final_probs(dag, nodes, evidence, observe)
     


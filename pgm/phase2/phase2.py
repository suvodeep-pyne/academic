#
# Probabilistic Graphical Models Phase 2
# Author: Suvodeep Pyne
#

import re
import math
import itertools
import datetime
import pprint as pp

import pearl_belief_prop as pearl

MAX_SUBSET_SIZE = 4
EPSILON = 0.001

class FileHandler:
	def __init__(self):
		pass

	def read_file(self, filename):
		input_file = open(filename, 'r')
		matrix = []
		for line in input_file: 
			values = re.compile('\s+').split(line.strip())
			row = [int(a) for a in values]
			matrix.append(row)
		return matrix

class StructureLearner:
	graph = []
	probX = []
	independencies = {}

	def __init__(self, train_data):
		self.train_data = train_data
		self.nsamples = len(train_data)
		if self.nsamples > 0:
			self.ndim = len(train_data[0])
			self.calcProb()
			self.graph = [[1 if i != j else 0 for i in range(self.ndim)] for j in range(self.ndim)]
	
	def calcProb(self):
		for X in xrange(0, self.ndim):
			counts = 0.0
			for entry in range(0, self.nsamples):
				counts += self.train_data[entry][X]
			prob = counts / self.nsamples
			self.probX.append([1 - prob, prob])
		
	def subset_candidates(self, n1, n2):
		candidates = set()
		for adj in range(0, self.ndim):
			if self.graph[n1][adj] == 1:
				candidates.add(adj)
		candidates.discard(n2)
		return candidates

	def subsets_by_size(self, n1, n2, subsetsize):
		nodes = self.subset_candidates(n1, n2)
		return itertools.combinations(nodes, subsetsize)
	
	def all_subsets(self, n1, n2):
		nodes = self.subset_candidates(n1, n2)
		subsets = []
		subsets.append(())
		for subsetsize in range(1, MAX_SUBSET_SIZE + 1):
			subsets.extend(itertools.combinations(nodes, subsetsize))
		return subsets

	# Input:
	# 		Z: is a list
	def assign(self, Z, assignment):
		z = {}
		for i in range(0, len(Z)):
			v = assignment & 1
			V = Z[i]
			z[V] = v
			assignment >>= 1
		return z

	# Return P(X1, ... ,Xk)
	def intersect_prob(self, Q):
		# Empty Query is true for all cases when Evidence is true
		return self.cond_prob(Q, {})

	# Return P(Q1, .. Qk | E1, ... , El)
	def cond_prob(self, Q, E):
		sum_cp = 0.0
		total_cond_valid = 0.0
		for entry in range(0, self.nsamples):
			# Conditionally valid
			cond_valid = True
			for V, v in E.iteritems():
				if self.train_data[entry][V] != v:
					cond_valid = False
					break
			if cond_valid == True:
				total_cond_valid += 1
				query_valid = True
				for V, v in Q.iteritems():
					if self.train_data[entry][V] != v:
						query_valid = False
						break
				if query_valid == True:
					sum_cp += 1
		prob = sum_cp / total_cond_valid
		return prob

	# Mutual Information 
	def mutual_info(self, X, Y, Z):
		mi = 0.0
		for assignment in range(0, 2 ** len(Z)):
			z = self.assign(Z, assignment)
			prob_evid = self.intersect_prob(z)
			sumxy = 0.0
			for x in range(0, 2):
				prob_Xx = self.cond_prob({X:x}, z)
				for y in range(0, 2):
					Q = { X: x, Y: y }
					cond_prob = self.cond_prob(Q, z)
					if cond_prob == 0: continue
					log_exp = cond_prob / (prob_Xx * self.cond_prob({Y:y}, z))
					sumxy += cond_prob * math.log(log_exp, 2)
			mi += prob_evid * sumxy
		return mi

	def count_edges(self):
		count = 0
		for n1 in range(0, self.ndim):
			for n2 in range(0, self.ndim):
				if self.graph[n1][n2]:
					count += 1
		return count
	
	def learn_skeleton(self):
		start_time = datetime.datetime.now()
		for subsetsize in range(0, 5):
			self.remove_edges(subsetsize)
		end_time = datetime.datetime.now()
		print 'Time taken to learn skeleton:', (end_time - start_time).seconds, 'sec'
		
	def remove_edges(self, subsetsize):
		for n1 in range(0, self.ndim - 1):
			for n2 in range(n1 + 1, self.ndim):
				if self.graph[n1][n2] == 0: continue
				for subset in self.subsets_by_size(n1, n2, subsetsize):
					minfo = self.mutual_info(n1, n2, subset)
					if minfo < EPSILON:
						self.independencies[(n1, n2)] = set(subset)
						self.graph[n1][n2] = 0
						self.graph[n2][n1] = 0

	def find_v_structures(self):
		count = 0
		for n1 in range(0, self.ndim - 2):
			for n2 in range(n1 + 1, self.ndim - 1):
				if self.graph[n1][n2] == 0: continue
				for n3 in range(0, self.ndim):
					if n1 == n3 or n2 == n3 or self.graph[n2][n3] == 0: continue
					pair = (n1, n3) if n1 <= n3 else (n3, n1)
					if pair not in self.independencies or len(self.independencies[pair]) != 0:
						continue
					# V structure case
					count += 1
					self.process_v_structure(n1, n2, n3)
		return count

	# n2 is the V node
	def process_v_structure(self, n1, n2, n3):
		self.graph[n2][n1] = 0
		self.graph[n2][n3] = 0

	def is_dir_edge(self, n1, n2):
		return self.graph[n1][n2] != 0 and self.graph[n2][n1] == 0

	def is_undir_edge(self, n1, n2):
		return self.graph[n1][n2] != 0 and self.graph[n2][n1] != 0
	
	def is_conn(self, n1, n2):
		return self.graph[n1][n2] != 0 or self.graph[n2][n1] != 0
		
	def create_adj_list(self, adj_mat):
		adjl = []
		for n1 in range(0, self.ndim):
			adjl.append(set())
			for n2 in range(0 , self.ndim):
				if (adj_mat[n1][n2] != 0):
					adjl[n1].add(n2)
		return adjl
		
	def orient_edges(self):
		self.adjl = self.create_adj_list(self.graph)
		add_edges = True
		while add_edges:
			add_edges = False
			add_edges = add_edges or self.process_case1()
			add_edges = add_edges or self.process_case2()
			add_edges = add_edges or self.process_case3()
	
	# For each uncoupled X -> Z - Y , orient Z -> Y
	def process_case1(self):
		removed_edges = set()
		for X in range(0, self.ndim):
			for Z in self.adjl[X]:
				# Require a directed edge
				if self.graph[Z][X] != 0: continue
				for Y in self.adjl[Z]:
					# Require an undirected edge
					if not self.is_undir_edge(Z, Y): continue
					# Require uncoupled nodes
					if self.is_conn(X, Y): continue
					removed_edges.add((Y, Z))
		
		for edge in removed_edges:
			self.graph[edge[0]][edge[1]] = 0
			self.adjl[edge[0]].remove(edge[1])
		return len(removed_edges) > 0
	
	def process_case2(self):
		removed_edges = set()
		for X in range(0, self.ndim):
			for Y in self.adjl[X]:
				# Require a undirected edge
				if not self.is_undir_edge(X, Y): continue
				if self.dir_path_exists(X, Y): 
					print (Y, X)
					removed_edges.add((Y, X))
		
		for edge in removed_edges:
			self.graph[edge[0]][edge[1]] = 0
			self.adjl[edge[0]].remove(edge[1])
		return len(removed_edges) > 0
	
	def dir_path_exists(self, X, Y):
		stack = []
		stack.append(X)
		visited = set()
		while len(stack) > 0:
			n = stack.pop()
			visited.add(n)
			for adj in self.adjl[n]:
				if self.is_dir_edge(n, adj):
					if adj == Y: return True
					if adj not in visited:
						stack.append(adj)
		return False
	
	# For each uncoupled X - Z - Y
	# such that X -> W, Y -> W, Z - W
	# orient Z -> W
	def process_case3(self):
		removed_edges = set()
		for X in range(0, self.ndim):
			for Z in self.adjl[X]:
				# Require an undirected edge
				if not self.is_undir_edge(X, Z): continue
				for Y in self.adjl[Z]:
					# Require an undirected edge
					if not self.is_undir_edge(Z, Y): continue
					# Require uncoupled nodes
					if self.is_conn(X, Y): continue
					
					candidates = self.adjl[X].intersection(self.adjl[Y])
					candidates.intersection_update(self.adjl[Z])
					for W in candidates:
						if self.is_dir_edge(X, W) and self.is_dir_edge(Y, W) and self.is_undir_edge(Z, W):
							removed_edges.add((Y, Z))
		
		for edge in removed_edges:
			self.graph[edge[0]][edge[1]] = 0
			self.adjl[edge[0]].remove(edge[1])
		return len(removed_edges) > 0
		
	def print_graph(self):
		print 'Graph:'
		for n1 in range(0, self.ndim):
			line = ""
			for n2 in range(0, self.ndim):
				line += str(self.graph[n1][n2]) + " "
			print line
	
	def learn_structure(self):
		self.learn_skeleton()
		n_vstructs = self.find_v_structures()
		self.orient_edges()
		
		print 'Number of Edges:', self.count_edges()
		print 'Number of V Structures', n_vstructs
	
	# Create an inverse graph mapping from child to parents
	def inverse_graph(self, dag):
		inv_dag = {}
		for node in range(0, self.ndim):
			inv_dag[node] = []
	
		for node in range(0, self.ndim):
			for c in dag[node]:
				inv_dag[c].append(node)
		
		return inv_dag
	
	def cpd_key(self, Q, E):
		return (tuple(sorted(Q.items())), tuple(sorted(E.items())))
	"""
		# Code for alphabetical keys for probabilities
		key = ""
		for k, v in Q.iteritems():
			key += str(chr(65 + k)) + str(v)
		key += '|'
		for k, v in sorted(E.items()):
			key += str(chr(65 + k)) + str(v)
		return key
	"""
	def stringify(self, Q):
		key = ""
		for k, v in sorted(Q.items()):
			key += str(chr(65 + k)) + str(v)
		return key
	
	def process_cpds(self, dag):
		cpd = {}
		inv_dag = self.inverse_graph(dag)
		for X in range(0, self.ndim):
			paX = inv_dag[X]
			for x in 0, 1:
				Q = {X:x}
				for assignment in range(0, 2 ** len(paX)):
					# parents of X with assignments x
					paXx = self.assign(paX, assignment)
					cond_prob = self.cond_prob(Q, paXx)
					if len(paXx) > 0:
						if self.stringify(Q) not in cpd:
							cpd[self.stringify(Q)] = {}
						cpd[self.stringify(Q)][self.stringify(paXx)] = cond_prob
					else:
						cpd[self.stringify(Q)] = cond_prob
		return cpd

if __name__ == '__main__':
	fh = FileHandler();
	train_data = fh.read_file('training-test-data/train1000.txt')

	# pp.pprint(mat)
	sl = StructureLearner(train_data)
	# pp.pprint(sl.probX)
	# pp.pprint(sl.all_subsets(0, 1))
	# print 'assignment:',sl.assign([3, 7, 8], 5)	
	# print 'intersect_prob:', sl.intersect_prob({2:1, 4:1})
	# print 'cond_prob:', sl.cond_prob({0:1, 1:0},{})
	# print 'Mutual Info', sl.mutual_info(0, 1, [])
	
	# sl.learn_structure()
	# pp.pprint(sl.adjl)
	# sl.print_graph()
	
	bn = fh.read_file('ref_graph.txt')
	adjl = sl.create_adj_list(bn)
	
	cpd = sl.process_cpds(adjl)
	pp.pprint(cpd)
	
	pearl_adjl = {}
	for key in range(0, sl.ndim):
		pearl_adjl[chr(65 + key)] = [ chr(65 + n) for n in adjl[key] ]
	
	pearl.prob = cpd	
	Z = {1:0}
	Q = {0:1}
	pearl.calc_probability(pearl_adjl, Z, Q)
	
	test_data = { 
# 				'test50a.txt' : 3, 
# 				'test50b.txt' : 5, 
# 				'test50c.txt' : 7,
# 				'test50d.txt' : 9
				}
	for filename in test_data:
		test_set = fh.read_file('training-test-data/' + filename)
		n_evidences = test_data[filename]
		print 'Results for', filename
		
		expected_sum = 0.0
		actual_sum = 0.0
		for entry in test_set:
			E = {}
			for n in range(0, n_evidences):
				E[n] = entry[n]
			for n in range(10, 20):
				for v in 0, 1:
					Q = { n : v }
					p = 0.005
					predicted = 1
					# Print the probability value
					print 'P(', Q, '|', E, ') =', p
					
					expected_sum += p
					actual_sum += (predicted == entry[n])
		print 'Expected prediction accuracy:', (expected_sum/500)
		print 'Actual prediction accuracy:', (actual_sum/500)
					
			
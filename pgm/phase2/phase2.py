#
# Probabilistic Graphical Models Phase 2
# Author: Suvodeep Pyne
#

import math
import itertools
import datetime
import pprint as pp

MAX_SUBSET_SIZE = 4
EPSILON = 0.001

class FileHandler:
	def __init__(self):
		pass

	def read_file(self, filename):
		input_file = open(filename, 'r')
		matrix = []
		for line in input_file: 
			values = line.strip().split('\t')
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
						self.graph[n1][n2] = 0
						self.graph[n2][n1] = 0

	def print_graph(self):
		print 'Graph:'
		for n1 in range(0, self.ndim):
			line = ""
			for n2 in range(0, self.ndim):
				line += str(self.graph[n1][n2]) + " "
			print line


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
	
	sl.learn_skeleton()
	print 'Number of Edges:', sl.count_edges()
	sl.print_graph()

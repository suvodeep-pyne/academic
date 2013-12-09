#
# Probabilistic Graphical Models Phase 2
# Author: Suvodeep Pyne
#

import os
import itertools
import pprint as pp

MAX_SUBSET_SIZE = 4

class FileHandler:
	def __init__(self):
		pass

	def read_file(self, filename):
		file = open(filename, 'r')
		matrix = []
		for line in file: 
			values = line.strip().split('\t')
			row = [int(a) for a in values]
			matrix.append(row)
		return matrix

class StructureLearner:
	graph = []
	probX = []

	def __init__(self, train_data):
		self.train_data = train_data
		self.nsamples = len(train_data)
		if self.nsamples > 0:
			self.ndim = len(train_data[0])
			self.calcProb()
	
	def calcProb(self):
		for X in xrange(0, self.ndim):
			sum = 0.0
			for entry in range(0, self.nsamples):
				sum += self.train_data[entry][X]
			prob = sum / self.nsamples
			self.probX.append([1 - prob, prob])
		
	def subset_candidates(self, excl):
		all = set(range(0, self.ndim))
		all.difference_update(excl)
		return all

	def all_subsets(self, n1, n2):
		MAX = 4

		# append empty subset
		nodes = self.subset_candidates([n1, n2])
		subsets = []
		subsets.append(())
		for subsetsize in range(1, MAX_SUBSET_SIZE + 1):
			subsets.extend(itertools.combinations(nodes, subsetsize))
		return subsets

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
		return self.cond_prob({}, Q)

	# Return P(Q1, .. Qk | E1, ... , El)
	def cond_prob(self, Q, E):
		sum = 0.0
		for entry in range(0, self.nsamples):
			# Conditionally valid
			cond_valid = True
			for V, v in E.iteritems():
				if self.train_data[entry][V] != v:
					cond_valid = False
					break
			if cond_valid == True:
				query_valid = True
				for V, v in Q.iteritems():
					if self.train_data[entry][V] != v:
						query_valid = False
						break
				if query_valid == True:
					sum += 1
		prob = sum / self.nsamples
		return prob

	# Mutual Information 
	def mutual_info(self, X, Y, Z):
		for assignment in range(0, 2 ** len(Z)):
			z = assign(Z, assignment) 

	def learn_skeleton(self):
		for n1 in range(0, self.ndim - 1):
			for n2 in range(n1 + 1, self.ndim):
				subsets = all_subsets(n1, n2)
								

if __name__ == '__main__':
	fh = FileHandler();
	train_data = fh.read_file('training-test-data/train1000.txt')

	# pp.pprint(mat)
	sl = StructureLearner(train_data)
	# pp.pprint(sl.probX)
	# print 'assignment:',sl.assign([3, 7, 8], 5)	
	# print 'cond_prob:', sl.cond_prob({2:1, 4:1},{0:1, 10:1})
	print sl.graph

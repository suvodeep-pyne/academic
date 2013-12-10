import os
import re

ndim = 20
filename = 'sample_graph.txt'
input_file = open(filename, 'r')
graph = [[0 for i in range(ndim)] for j in range(ndim)]
for line in input_file: 
	values = re.compile('\s+').split(line.strip())
	if values:
		row = [ord(a) - 65 for a in values]
		for n in range(1, len(row)):
			graph[row[0]][row[n]] = 1

for n1 in range(0, ndim):
	line = ""
	for n2 in range(0, ndim):
		line += str(graph[n1][n2]) + " "
	print line


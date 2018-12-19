import sys
import uuid
from collections import deque

if len(sys.argv) != 3:
	print "Provide 2 arguments: total number of nodes and number of children per node."
	exit()

try:
	total_nodes = int(sys.argv[1])
except ValueError:
	print "Total number of nodes is not an int"
	exit()

try:
	children_per_node = int(sys.argv[2])
except ValueError:
	print "Number of children per node is not an int"
	exit()

#print "Total number of nodes: ", total_nodes
#print "Number of children per node: ", children_per_node

db_format =  "INSERT INTO AMAZINGCO VALUES('%s', '%s', '%s', %i);"
csv_format = "%s,%s,%s,%i"

nodes = deque([("root", "null", "root", 0)])

while total_nodes > 0:
	current = nodes.popleft()
	print csv_format % current
	total_nodes -= 1
	for i in range(children_per_node):
		id = uuid.uuid4()
		node = (id.hex, current[0], "root", current[3] + 1)
		nodes.append(node)

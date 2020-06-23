# Definition for a binary tree node.
from typing import List


class TreeNode:
  def __init__(self, val=0, left=None, right=None):
    self.val = val
    self.left = left
    self.right = right


class Solution:
  def inorderTraversal(self, root: TreeNode) -> List[int]:
    if not (root and root.val):
      return []


    l = []
    s = []
    c = root
    while True:
      if c:
        s.append(c)
        c = c.left
      elif s:
        c = s.pop()
        l.append(c.val)
        c = c.right
      else:
        break
    return l


print(Solution().inorderTraversal(TreeNode(1, None, TreeNode(2, TreeNode(3)))))
from collections import defaultdict
from typing import List


class Solution:
  def twoSum(self, nums: List[int], target: int) -> List[int]:
    d = defaultdict(list)
    for i, n in enumerate(nums):
      d[n].append(i)

    for i, n in enumerate(nums):
      n2 = target - n
      if n2 in d:
        idxs = d[n2]
        if n2 == n:
          if len(idxs) > 1:
            return idxs[:2]
        else:
          return [i, idxs[0]]

    return None



print(Solution().twoSum([3,2,4], 6))

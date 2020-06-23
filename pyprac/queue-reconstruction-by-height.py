import operator
from typing import List


class Solution:
  def reconstructQueue(self, people: List[List[int]]) -> List[List[int]]:
    people.sort(key=operator.itemgetter(1, 0))
    N = len(people)

    i = 0
    while i < N:
      p = people[i]
      if p[1] == 0:
        i += 1
        continue

      # if p is at the correct position
      c = 0
      for j in range(i):
        if people[j][0] >= p[0]:
          c += 1

      # ith element is in the right place
      curr = i
      while c > p[1] and people[curr - 1][0] > people[curr][0]:
        # swap left/right if it improves position
        people[curr - 1], people[curr] = people[curr], people[curr - 1]
        curr -= 1
        c -= 1
      i += 1

    return people


if __name__ == '__main__':
  print(Solution().reconstructQueue(
      [[7, 0], [4, 4], [7, 1], [5, 0], [6, 1], [5, 2]]))

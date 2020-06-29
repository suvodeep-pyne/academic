class Solution:

  def get_block_size(self, n):
    i = 0
    sum = 0
    prev_sum = sum
    prev_i = i
    while sum <= n:
      prev_sum = sum
      prev_i = i
      sum += 2 ** i
      i += 1
    return prev_sum, prev_i


  def pure(self, n) -> str:
    block, size = self.get_block_size(n)

    binary = ("{0:0%db}" % size).format(n - block)

    flip = binary \
      .replace('0', '4') \
      .replace('1', '5')

    return flip + flip[::-1]


if __name__ == '__main__':
  for i in range(1, 20):
    print(i, Solution().pure(i))

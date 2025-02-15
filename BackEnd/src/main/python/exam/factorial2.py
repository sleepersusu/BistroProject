# 題目：計算一個數的雙階乘，定義為：N!!=N×(N−2)×(N−4)×… 直到最後一個乘的數值變為 1（如果 N 是奇數）或 2（如果N 是偶數）。
# 範例：5!!=5×3×1=15
# 範例：6!!=6×4×2=48
from typing import final

#解法:while
N = int(input("while請輸入一個正整數N: "))
res = 1
while N > 0 :
    res = res * N
    N = N - 2 # 每次減去 2 就會達到最後一個乘的數值變為 1或 2
print(f"bifactorial res : {res}")

#解法:for
M = int(input("for請輸入一個正整數M: "))
resfor = 1
for k in range(M,0,-2):
    resfor = resfor * k




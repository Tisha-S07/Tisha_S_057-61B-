t0 = 5 + 3
b0 = t0
t1 = 10 - 4
b1 = t1
t2 = 5 * 6
b2 = t2
t3 = 20 / 5
b3 = t3
t4 = 6 * 3
t5 = 5 + t4
b4 = t5
t6 = 5 + 6
t7 = t6 * 3
b5 = t7
t8 = 5 + 2
t9 = 3 + 4
t10 = t8 * t9
b6 = t10
t11 = 2 + 3
t12 = 5 * 2
t13 = 4 + t12
t14 = t11 * t13
b7 = t14
b0 = 5
b1 = 10
t15 = b0 + b1
b2 = t15
t16 = b1 * 2
t17 = b0 + t16
b3 = t17
b0 = 15
t18 = b0 + 5
b1 = t18
t19 = b0 + b1
t20 = 2 * 4
t21 = 3 + t20
t22 = t19 * t21
b5 = t22
t23 = b0 < b1
if t23 != 0 goto L0
goto L1
L0:
b0 = 100
goto L2
L1:
b0 = 200
L2:
L3:
t24 = b0 < 205
if t24 != 0 goto L4
goto L5
L4:
t25 = b0 + 1
b0 = t25
goto L3
L5:

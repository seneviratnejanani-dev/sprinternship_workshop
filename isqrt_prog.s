   .data

# array terminated by 0 (which is not part of the array)
xarr:
   .word 1
   .word 12
   .word 225
   .word 169
   .word 16
   .word 25
   .word 100
   .word 81
   .word 99
   .word 121
   .word 144
   .word 0 

   .text

# main(): ##################################################
#   uint* j = xarr
#   while (*j != 0):
#     printf(" %d\n", isqrt(*j))
#     j++
#
main:
   # PROLOGUE
   subu $sp, $sp, 8        # expand stack by 8 bytes
   sw   $ra, 8($sp)        # push $ra (ret addr, 4 bytes)
   sw   $fp, 4($sp)        # push $fp (4 bytes)
   addu $fp, $sp, 8        # set $fp to saved $ra

   subu $sp, $sp, 8        # save s0, s1 on stack before using them
   sw   $s0, 8($sp)        # push $s0
   sw   $s1, 4($sp)        # push $s1

   la   $s0, xarr          # use s0 for j. init to xarr
main_while:
   lw   $s1, ($s0)         # use s1 for *j
   beqz $s1, main_end      # if *j == 0 go to main_end
   move $a0, $s1           # result (in v0) = isqrt(*j)
   jal  isqrt              # 
   move $a0, $v0           # print_int(result)
   li   $v0, 1
   syscall
   li   $a0, 10            # print_char('\n')
   li   $v0, 11
   syscall
   addu $s0, $s0, 4        # j++
   b    main_while
main_end:
   lw   $s0, -8($fp)       # restore s0
   lw   $s1, -12($fp)      # restore s1

   # EPILOGUE
   move $sp, $fp           # restore $sp
   lw   $ra, ($fp)         # restore saved $ra
   lw   $fp, -4($sp)       # restore saved $fp
   j    $ra                # return to kernel
# end main #################################################
# Name: Janani Seneviratne
# UID: 122696167
# Directory ID: jsenevir

# Name: Janani Seneviratne
# UID: 122696167
# Directory ID: jsenevir

isqrt:
    # PROLOGUE
    subu $sp, $sp, 8
    sw $ra, 8($sp)
    sw $fp, 4($sp)
    addu $fp, $sp, 8

    # BODY
    subu $sp, $sp, 4    # save callee saved register to stack
    sw $s0, 4($sp)      # bottom of stack after old $fp now contains old $s0 value

    subu $sp, $sp, 8    # make space for local variables 'small' and 'large'
    move $s0, $a0       # s0 now contains arg n

    li $t0, 2
    blt $a0, $t0, ret_n # base case: if n < 2, goto ret_n

    srl $a0, $a0, 2     # new arg (n in the next recursive call) = n >> 2
    jal isqrt
    sll $t0, $v0, 1     # small = return value << 1 ; t0 holds the value of small
    sw $t0, 8($sp)      # push value of small to stack
    addi $t1, $t0, 1    # large = small + 1 ; t1 holds the value of large
    sw $t1, 4($sp)      # push value of large to stack

    mul $t2, $t1, $t1   # t2 = large^2
    bgt $t2, $s0, ret_small # if large^2 > n, goto ret_small

ret_large:
    move $v0, $t1       # return value = large
    j end
ret_small:
    move $v0, $t0       # return value = small
    j end
ret_n:
    move $v0, $a0       # return value = n
end:
    lw $s0, -8($fp)  # restore s0 (which is the previous a0 value if caller was isqrt/currently executing a recursive call)
    # EPILOGUE
    move $sp, $fp
    lw $ra, 0($fp)
    lw $fp, -4($sp)
    jr $ra


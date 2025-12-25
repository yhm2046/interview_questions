#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <ctype.h>
#include <limits.h>
#include <errno.h>

typedef enum {
    STATUS_SUCCESS,
    STATUS_INVALID,
    STATUS_DIVIDE_BY_ZERO
} FuncStatus_t;

static int is_operator(const char *s) {
    if (!s) return 0;
    size_t len = strlen(s);
    if (len != 1) return 0;
    char c = s[0];
    return (c == '+' || c == '-' || c == '*' || c == '/');
}

static int get_valid_integer(const char *s, int32_t *out_val) {
    if (!s || *s == '\0') return 0;
    
    const char *p = s;
    while (isspace((unsigned char)*p)) p++;
    if (*p == '\0') return 0;

    char *endptr;
    errno = 0;
    long val = strtol(p, &endptr, 10);

    if (endptr == p) return 0;
    
    while (isspace((unsigned char)*endptr)) endptr++;
    if (*endptr != '\0') return 0;
    
    if (errno == ERANGE || val > INT_MAX || val < INT_MIN) {
        return 0;
    }

    *out_val = (int32_t)val;
    return 1;
}

FuncStatus_t evaluatePostfix(size_t postfix_size,
                             const char* postfix[],
                             int32_t *result)
{
    if (!postfix || !result) {
        return STATUS_INVALID;
    }
    
    if (postfix_size == 0) {
        return STATUS_INVALID;
    }

    int32_t *stack = (int32_t*)calloc(postfix_size, sizeof(int32_t));
    if (!stack) return STATUS_INVALID;

    size_t top = 0;
    FuncStatus_t status = STATUS_SUCCESS;

    for (size_t i = 0; i < postfix_size; i++) {
        const char *t = postfix[i];
        
        if (!t || *t == '\0') {
            status = STATUS_INVALID;
            break;
        }
        
        int32_t val = 0;

        if (get_valid_integer(t, &val)) {
            stack[top++] = val;
        } 
        else if (is_operator(t)) {
            if (top < 2) {
                status = STATUS_INVALID;
                break;
            }

            int32_t b = stack[--top];
            int32_t a = stack[--top];
            
            int64_t res_long = 0;
            int error_flag = 0;

            switch (t[0]) {
                case '+': 
                    res_long = (int64_t)a + b; 
                    break;
                case '-': 
                    res_long = (int64_t)a - b; 
                    break;
                case '*': 
                    res_long = (int64_t)a * b; 
                    break;
                case '/': 
                    if (b == 0) {
                        status = STATUS_DIVIDE_BY_ZERO;
                        error_flag = 1;
                    } else {
                        res_long = (int64_t)a / b;
                    }
                    break;
                default: 
                    status = STATUS_INVALID; 
                    error_flag = 1;
                    break;
            }

            if (status != STATUS_SUCCESS || error_flag) break;

            if (res_long > INT_MAX || res_long < INT_MIN) {
                status = STATUS_INVALID;
                break;
            }

            stack[top++] = (int32_t)res_long;
        } else {
            status = STATUS_INVALID;
            break;
        }
    }

    if (status == STATUS_SUCCESS) {
        if (top != 1) {
            status = STATUS_INVALID;
        } else {
            *result = stack[0];
        }
    }

    free(stack);
    return status;
}

int main(void)
{
    size_t n;
    if (scanf("%zu", &n) != 1) {
        printf("%d\n", STATUS_INVALID);
        printf("%d\n", INT_MAX);
        return 0;
    }

    char **tokens = NULL;
    const char **postfix = NULL;
    char *data_block = NULL;

    if (n > 0) {
        tokens = (char**)calloc(n, sizeof(char*));
        postfix = (const char**)calloc(n, sizeof(const char*));
        data_block = (char*)calloc(n * 64, sizeof(char));

        if (!tokens || !postfix || !data_block) {
            free(tokens);
            free(postfix);
            free(data_block);
            printf("%d\n", STATUS_INVALID);
            printf("%d\n", INT_MAX);
            return 0;
        }

        for (size_t i = 0; i < n; i++) {
            tokens[i] = data_block + (i * 64);
            if (scanf("%63s", tokens[i]) != 1) {
                tokens[i][0] = '\0';
            }
            postfix[i] = tokens[i];
        }
    }

    int32_t result = INT_MAX;
    
    FuncStatus_t status = evaluatePostfix(n, postfix, &result);

    printf("%d\n", status);
    printf("%d\n", result);

    if (n > 0) {
        free(data_block);
        free(tokens);
        free(postfix);
    }

    return 0;
}
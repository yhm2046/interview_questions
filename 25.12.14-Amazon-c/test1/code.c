#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>

typedef enum {
    STATUS_GOOD,
    STATUS_ISSUE
} FuncStatus_t;

FuncStatus_t schedule_tasks(const int32_t arrive_order[], uint32_t num_tasks, int32_t *days_needed) {
    if (num_tasks == 0) {
        *days_needed = 0;
        return STATUS_GOOD;
    }

    int32_t *pos = (int32_t*)malloc((num_tasks + 1) * sizeof(int32_t));
    if (pos == NULL) {
        return STATUS_ISSUE;
    }

    for (uint32_t i = 0; i <= num_tasks; i++) {
        pos[i] = -1;
    }

    for (uint32_t i = 0; i < num_tasks; i++) {
        int32_t taskId = arrive_order[i];

        if (taskId <= 0 || (uint32_t)taskId > num_tasks) {
            free(pos);
            return STATUS_ISSUE;
        }

        if (pos[taskId] != -1) {
            free(pos);
            return STATUS_ISSUE;
        }

        pos[taskId] = (int32_t)i;
    }

    int32_t days = 1;
    int32_t current_idx = pos[1];

    for (uint32_t k = 2; k <= num_tasks; k++) {
        if (pos[k] < current_idx) {
            days++;
        }
        current_idx = pos[k];
    }

    *days_needed = days;
    free(pos);
    return STATUS_GOOD;
}

int main() {
    int32_t num_tasks_in;
    if (scanf("%d", &num_tasks_in) != 1) return 0;

    uint32_t num_tasks = (uint32_t)num_tasks_in;
    int32_t *arrive_order = NULL;

    if (num_tasks > 0) {
        arrive_order = (int32_t*)malloc(num_tasks * sizeof(int32_t));
        if (arrive_order == NULL) return 0;

        for (uint32_t i = 0; i < num_tasks; i++) {
            if (scanf("%d", &arrive_order[i]) != 1) {
                free(arrive_order);
                return 0;
            }
        }
    }

    int32_t days_needed = 0;
    FuncStatus_t status = schedule_tasks(arrive_order, num_tasks, &days_needed);

    if (status == STATUS_GOOD) {
        printf("Completed\n");
        printf("%d\n", days_needed);
    } else {
        printf("Not Completed\n");
    }

    if (arrive_order != NULL) {
        free(arrive_order);
    }

    return 0;

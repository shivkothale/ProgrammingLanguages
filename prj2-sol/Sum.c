#include <stdio.h>
#include <stdlib.h>

int N_ENTRIES;
int* a; 
int* b; 
int* c;

void readVector(int* vec) {
    for (int i = 0; i < N_ENTRIES; ++i) {
        scanf("%d", &vec[i]);
    }
}

void addVectors(int* v1, int* v2, int* sum) {
    for (int i = 0; i < N_ENTRIES; ++i) {
        c[i] = a[i] + b[i];
    }
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <N_OPS> <N_ENTRIES>\n", argv[0]);
        return 1;
    }
    int N_OPS = atoi(argv[1]);
    N_ENTRIES = atoi(argv[2]); 
    for (int i = 0; i < N_OPS; i++) {
        a = (int*)malloc(N_ENTRIES * sizeof(int));
        b = (int*)malloc(N_ENTRIES * sizeof(int));
        c = (int*)malloc(N_ENTRIES * sizeof(int));
        readVector(a);
        readVector(b);
        addVectors(a, b, c);
        for (int i = 0; i < N_ENTRIES; i++) {
            printf("%d ", c[i]);
        }
        free(a);
        free(b);
        free(c);
        printf("\n\n");
        
    }
    return 0;
}

#include <iostream>
#include <stdio.h>

#define MAX 101

using namespace std;

int K = 0;
double ps, pr, pi, pu, pw, pd, pl;
double pi_values[3];


double tennison(int wins, int losses, double psun) {
	if (losses == K) return 0;
	if (wins == K) return 1;

	if (psun <= 0) psun = 0.0;
	if (psun >= 1) psun = 1.0;

	double prain = 1 - psun;

	double pwinr = ps * psun + pr * prain;
	double plossr = 1 - pwinr;

	double pwin = pwinr * ((1 - pw) * tennison(wins + 1, losses, psun) +
			pw * tennison(wins + 1, losses, psun + pu));
	double ploss = plossr * ((1 - pl) * tennison(wins, losses + 1, psun) +
			pl * tennison(wins, losses + 1, psun - pd));
	return pwin + ploss;
}

static double M[MAX][MAX][3][MAX][MAX];
void initM() {
	for (int i1 = 0; i1 < MAX; i1++)
		for (int i2 = 0; i2 < MAX; i2++)
			for (int i3 = 0; i3 < 3; i3++)
				for (int i4 = 0; i4 < MAX; i4++)
					for (int i5 = 0; i5 < MAX; i5++)
						M[i1][i2][i3][i4][i5] = -1.0;
}
/**
 * N matches
 */
double tennison(int wins, int losses, int pi_idx, int c_pu, int c_pd) {
	double &memo = M[wins][losses][pi_idx][c_pu][c_pd];
	if (memo >= 0.0) return memo;

	if (losses == K) return 0;
	if (wins == K) return 1;

	double pi = pi_values[pi_idx];
	double psun = pi + c_pu * pu - c_pd * pd;
	if (psun <= 0) { psun = 0.0; pi_idx = 0; c_pu = 0; c_pd = 0;}
	if (psun >= 1) { psun = 1.0; pi_idx = 1; c_pu = 0; c_pd = 0;}

	double prain = 1 - psun;

	double pwinr = ps * psun + pr * prain;
	double plossr = 1 - pwinr;

	double pwin = pwinr * ((1 - pw) * tennison(wins + 1, losses, pi_idx, c_pu, c_pd) +
			pw * tennison(wins + 1, losses, pi_idx, c_pu + 1, c_pd));
	double ploss = plossr * ((1 - pl) * tennison(wins, losses + 1, pi_idx, c_pu, c_pd) +
			pl * tennison(wins, losses + 1, pi_idx, c_pu, c_pd + 1));
	memo = pwin + ploss;
	return memo;
}

double prob_tennison() {
	cin >> K;
	cin >> ps >> pr >> pi >> pu >> pw >> pd >> pl;

	if (pi <= 0) pi = 0;
	if (pi >= 1) pi = 1;

	pi_values[0] = 0.0;
	pi_values[1] = 1.0;
	pi_values[2] = pi;

	initM();
	// printf("%.6f %.6f", tennison(0, 0, 2, 0, 0), tennison(0, 0, pi));
	printf("%.6f", tennison(0, 0, 2, 0, 0));
}

int main() {
	int T = 0;
	cin >> T;

	for (int t = 1; t <= T; t++) {
		printf("Case #%d: ", t);
		prob_tennison();
		printf("\n");
	}

	return 0;
}

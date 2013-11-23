#include <iostream>
#include <stdio.h>

using namespace std;
	
int K = 0;
double ps, pr, pi, pu, pw, pd, pl;

/**
 * N matches
 */
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

double prob_tennison() {
	cin >> K;
	cin >> ps >> pr >> pi >> pu >> pw >> pd >> pl;

	if (pi <= 0) pi = 0;
	if (pi >= 1) pi = 1;

	return tennison(0, 0, pi);
}

int main() {
	int T = 0;
	cin >> T;

	for (int t = 1; t <= T; t++) {
		printf("Case #%d: %.6f\n", t, prob_tennison());
	}

	return 0;
}

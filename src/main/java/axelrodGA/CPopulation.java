package axelrodGA;


public class CPopulation {
	int popSize;
	CIndividual[] member;
	// 統計値:平均、分散、最大値、最小値、標準偏差
	double mAve, mDev, mMax, mMin, mSigma;
	// 最大値個体の番号
	int mMaxID;
	public CPopulation(int popsize) {
		popSize = popsize;
		member = new CIndividual[popSize];
		for (int i = 0; i < member.length; i++) {
			member[i] = new CIndividual();
		}
		mAve = mDev = mMax = mMin = 0.0;
		mSigma = 0.0;
	} // end of constructor
	
	//集団の初期化
	public void initialize() {
		for(int i=0;i<this.member.length;i++) {
			this.member[i].initialize();
		}
		mAve = mDev = mMax = mMin = 0.0;
		mSigma = 0.0;
	}

	// 統計値計算：平均
	public void calcStat() {
		//集団の平均利得を考える場合、「個体の利得」としてどれを使うのかを考えなければならない。
		//ゲームは複数回数行われるのだから、やはり平均利得が適切である。
		// 平均
		double sum = 0.0;
		for (int i = 0; i < this.popSize; i++) {
			//System.out.println("member["+i+"]="+this.member[i].getAvePayoff());
			sum += this.member[i].getAvePayoff();
		}
		this.mAve = sum / this.popSize;
		// 分散
		sum = 0.0;
		for (int i = 0; i < this.popSize; i++) {
			double v = (this.member[i].getAvePayoff() - this.mAve);
			sum += v * v;
		}
		this.mDev = sum / (this.popSize); // 標本分散で良い
		this.mSigma = Math.sqrt(mDev);
		// 最大・最小
		double max = 0.0;
		double min = 0.0;
		mMaxID = 0;
		max = this.member[0].getAvePayoff();
		min = this.member[0].getAvePayoff();
		for (int i = 1; i < this.popSize; i++) {
			if (max < this.member[i].getAvePayoff()) {
				max = this.member[i].getAvePayoff();
				mMaxID = i;
			}
			if (min > this.member[i].getAvePayoff())
				min = this.member[i].getAvePayoff();
		}
		this.mMax = max;
		this.mMin = min;
	}// end of calcStat()
	
		// スケーリングメソッド
	public void scaling() {
		double fmultiple = 2.0;
		double delta, a, b;
		if (this.mMin > (fmultiple * this.mAve - this.mMax) / (fmultiple - 1.0)) {
			delta = this.mMax - this.mAve;
			a = (fmultiple - 1.0) * this.mAve / delta;
			b = fmultiple * this.mAve - a * this.mMax;
		} else {
			delta = this.mAve - this.mMin;
			a = this.mAve / delta;
			b = -this.mMin * this.mAve / delta;
		}
		// 以上で線形スケーリングの係数が決定した。
		// 以下でスケーリングを行い、数値を個体に格納する。
		//ここでも個体の「利得」は平均利得を用いる
		delta = 0.0;// 使い回す
		for (int m = 0; m < this.popSize; m++) {
			double payoff = this.member[m].getAvePayoff();
			this.member[m].scaledPayoff = a * payoff + b;
		}
	} // end of スケーリング
}

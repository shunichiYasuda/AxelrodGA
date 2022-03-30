package axelrodGA;


public class CPopulation {
	int popSize;
	CIndividual[] member;
	// ���v�l:���ρA���U�A�ő�l�A�ŏ��l�A�W���΍�
	double mAve, mDev, mMax, mMin, mSigma;
	// �ő�l�̂̔ԍ�
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
	
	//�W�c�̏�����
	public void initialize() {
		for(int i=0;i<this.member.length;i++) {
			this.member[i].initialize();
		}
		mAve = mDev = mMax = mMin = 0.0;
		mSigma = 0.0;
	}

	// ���v�l�v�Z�F����
	public void calcStat() {
		//�W�c�̕��ϗ������l����ꍇ�A�u�̗̂����v�Ƃ��Ăǂ���g���̂����l���Ȃ���΂Ȃ�Ȃ��B
		//�Q�[���͕����񐔍s����̂�����A��͂蕽�ϗ������K�؂ł���B
		// ����
		double sum = 0.0;
		for (int i = 0; i < this.popSize; i++) {
			//System.out.println("member["+i+"]="+this.member[i].getAvePayoff());
			sum += this.member[i].getAvePayoff();
		}
		this.mAve = sum / this.popSize;
		// ���U
		sum = 0.0;
		for (int i = 0; i < this.popSize; i++) {
			double v = (this.member[i].getAvePayoff() - this.mAve);
			sum += v * v;
		}
		this.mDev = sum / (this.popSize); // �W�{���U�ŗǂ�
		this.mSigma = Math.sqrt(mDev);
		// �ő�E�ŏ�
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
	
		// �X�P�[�����O���\�b�h
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
		// �ȏ�Ő��`�X�P�[�����O�̌W�������肵���B
		// �ȉ��ŃX�P�[�����O���s���A���l���̂Ɋi�[����B
		//�����ł��̂́u�����v�͕��ϗ�����p����
		delta = 0.0;// �g����
		for (int m = 0; m < this.popSize; m++) {
			double payoff = this.member[m].getAvePayoff();
			this.member[m].scaledPayoff = a * payoff + b;
		}
	} // end of �X�P�[�����O
}

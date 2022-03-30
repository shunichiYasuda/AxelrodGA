package axelrodGA;


public class CIndividual {
	// 染色体の長さなどの各種定数は CHeader.java で定義。
		char[] chrom; // 染色体。長さは対戦履歴の長さに応じて決まる
		char[] memRec; // 対戦履歴。長さはCConst で決められた回数ｘ2
		char[] strategicRec; // あった方が便利 Mar10
		int adr; // 履歴が示す染色体上の位置
		char myChoice; // 履歴が示す染色体上の位置にある行動を選択する。
		// 利得関係
		double payoff, scaledPayoff, cumPayoff, avePayoff;
		// ゲームカウント。個体によってゲーム回数が異なるので
		int gameCount = 0;
		// constructor
		public CIndividual() {
			chrom = new char[CHeader.LENGTH]; // 染色体。記憶も含む
			memRec = new char[2 * CHeader.PRE]; // 記憶の長さ「自分の手・相手の手」ｘ記憶しているゲーム回数
			//戦略領域のビット数は chromの長さから記憶領域の長さを引いた分である。
			strategicRec = new char[chrom.length - memRec.length];
			initBinary(chrom);
			// 染色体の最初の 2*PRE分を対戦履歴として memRecにコピー
			for (int i = 0; i < memRec.length; i++) {
				this.memRec[i] = this.chrom[i];
			}
			// 戦略領域 Mar10
			for (int i = 0; i < strategicRec.length; i++) {
				this.strategicRec[i] = this.chrom[memRec.length + i];
			}
			// 記憶が指し示すアドレス 0から数えるので、配列のアドレスになる（ビットを数えるときに注意）
			String tmp = new String(this.memRec);
			this.adr = Integer.parseInt(tmp, 2);
			// そのアドレスにある行動（0:協力'C', 1：裏切り'D'）。ただし最初の6ビットは記憶領域なので、
			// this.adr が示すのは戦略領域のビット数。そこで「戦略領域」配列を別にしておいた方が間違いがない
			// この点に気づいていなかったので修正 Mar10
			this.myChoice = this.strategicRec[this.adr];
			// 利得関係初期化
			payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
		}// end of constructor
		
		// 自分自身の初期化
		public void initialize() {
			initBinary(this.chrom);
			// 染色体の最初の 2*PRE分を対戦履歴として memRecにコピー
			for (int i = 0; i < memRec.length; i++) {
				this.memRec[i] = this.chrom[i];
			}
			// 戦略領域 Mar10
			for (int i = 0; i < strategicRec.length; i++) {
				this.strategicRec[i] = this.chrom[memRec.length + i];
			}
			// 記憶が指し示すアドレス 0から数えるので、配列のアドレスになる（ビットを数えるときに注意）
			String tmp = new String(this.memRec);
			this.adr = Integer.parseInt(tmp, 2);
			// そのアドレスにある行動（0:協力'C', 1：裏切り'D'）
			this.myChoice = this.strategicRec[this.adr];
			// 利得関係初期化
			payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
		}
		// setter
		public void setPayoff(double p) {
			// ゲームの利得がはいってくる、ということはゲームが1回終わったということなので
			// このメソッドの中でgameCount をすすめ、平均利得も計算しておく
			this.gameCount++;
			this.payoff = p;
			this.cumPayoff += p;
			this.avePayoff = cumPayoff / gameCount;
		}

		// getter
		public int getAdr() {
			return this.adr;
		}

		public char[] getChrom() {
			return this.chrom;
		}

		public char[] getMemory() {
			return this.memRec;
		}
		
		public char[] getStrategicRec() {
			return this.strategicRec;
		}

		public double getPayoff() {
			return this.payoff;
		}

		public double getCumPayoff() {
			return this.cumPayoff;
		}

		public double getAvePayoff() {
			return this.avePayoff;
		}

		public double getScaledPayoff() {
			return this.scaledPayoff;
		}

		public char getChoice() {
			return this.myChoice;
		}

		//
		// 対戦履歴の更新。一回の対戦ごとに更新される。受け入れるのは対戦相手のchoice
		public void reMem(char in) {
			// 記憶領域のビット長
			int L = this.memRec.length;
			char[] tmp = new char[L];
			// はじめに最初の2ビットを捨てて、以降を詰める
			for (int i = 0; i < tmp.length - 2; i++) {
				tmp[i] = this.memRec[2 + i];
			}
			// 終わりから2ビット目 [L-2] は自分が出した choice である。
			tmp[L - 2] = this.getChoice();
			// 最後のビットは in である。
			tmp[L - 1] = in;
			// tmp で memRec を更新する。
			for (int i = 0; i < L; i++) {
				this.memRec[i] = tmp[i];
			}
			// 記憶が更新されたら染色体自体が更新される。
			// 染色体の先頭 L ビットが対戦履歴に置き換わる。
			for (int i = 0; i < L; i++) {
				this.chrom[i] = this.memRec[i];
			}
			// 記憶が更新されたら、adr が変わり、 myChoice が変わる。
			// 記憶の更新（ゲームのプレイ）がトリガーになるということだ。
			String str = new String(this.memRec);
			this.adr = Integer.parseInt(str, 2);
			this.myChoice = this.strategicRec[this.adr];
		}

		// replace 新しい染色体を受け入れて自分自身を更新する。
		public void replace(char[] in) {
			for (int i = 0; i < CHeader.LENGTH; i++) {
				this.chrom[i] = in[i];
			}
			// 染色体の最初の 2*PRE分を対戦履歴として memRecにコピー
			for (int i = 0; i < memRec.length; i++) {
				this.memRec[i] = this.chrom[i];
			}
			// 戦略領域 Mar10
			for (int i = 0; i < strategicRec.length; i++) {
				this.strategicRec[i] = this.chrom[memRec.length + i];
			}
			// 記憶が指し示す染色体上のアドレス 0から数えるので、配列のアドレスになる（ビットを数えるときに注意）
			String tmp = new String(this.memRec);
			this.adr = Integer.parseInt(tmp, 2);
			// そのアドレスにある行動（0:協力'C', 1：裏切り'D'）
			this.myChoice = this.strategicRec[this.adr];
			// 利得関係初期化
			payoff = scaledPayoff = cumPayoff = avePayoff = 0.0;
			// ゲームカウントも初期化
			this.gameCount = 0;
		}

		// 文字列初期化
		public void initBinary(char[] in) {
			double d;
			for (int i = 0; i < in.length; i++) {
				d = Math.random();
				if (d > 0.5) {
					in[i] = '1';
				} else {
					in[i] = '0';
				}
			} // end of for
		} // end of void initBinary()
}

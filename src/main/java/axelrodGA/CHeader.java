package axelrodGA;

public class CHeader {
	static int PRE= 3; //記憶しているゲームの回数
	static int MEM = PRE*2;//add Mar01 Main のメソッドで記憶配列の長さが必要
	static int LENGTH = (int)Math.pow(2.0, (double)PRE*2)+PRE*2; //染色体の長さ
	//public static final double crossProb = 0.25; //交叉確率
	//public static final double mutProb = 0.01; //突然変異確率 Mar01
}

package org.ty.exercise.nowcoder;



import java.util.Scanner;

/**
 * <dl>
 * <dt>study-all</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2017年11月08日</dd>
 * </dl>
 *
 * @author Tangyun
 */
public class Sample170001 {


    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        while (in.hasNext()) {
//            String s = in.next();
//        }
        Sample170001 sample170001 = new Sample170001();
        System.out.println(sample170001.returnMaxEvenStrLength("abaababaab"));
    }


    /**
     * 牛牛喜欢彩色的东西,尤其是彩色的瓷砖。牛牛的房间内铺有L块正方形瓷砖。每块砖的颜色有四种可能:红、绿、蓝、黄。给定一个字符串S, 如果S的第i个字符是'R', 'G', 'B'或'Y',那么第i块瓷砖的颜色就分别是红、绿、蓝或者黄。
     牛牛决定换掉一些瓷砖的颜色,使得相邻两块瓷砖的颜色均不相同。请帮牛牛计算他最少需要换掉的瓷砖数量。
     * @输入 包括一行,一个字符串S,字符串长度length(1 ≤ length ≤ 10),字符串中每个字符串都是'R', 'G', 'B'或者'Y'。
     * @输出 一个整数,表示牛牛最少需要换掉的瓷砖数量
     *
     * @思路：从相同的头两位开始循环，每次取三位，替换中间一位。
     */
    public int replace(String S) {
        int i = 0;
        int x = 0;
        while (i < S.length()-1) {
            String pre = S.substring(i, ++ i);
            String cur = S.substring(i, ++ i);
            if (!pre.equals(cur)) {
                i--;
                continue;
            }
            String nex = "";
            if (i < S.length()) {
                nex = S.substring(i, i + 1);
            }
            if (pre.equals(cur) || cur.equals(nex)) {
                x++;//替换一次
            }
        }
        return x;
    }

    /**
     * 牛牛从生物科研工作者那里获得一段字符串数据s,牛牛需要帮助科研工作者从中找出最长的DNA序列。DNA序列指的是序列中只包括'A','T','C','G'。牛牛觉得这个问题太简单了,就把问题交给你来解决。
     例如: s = "ABCBOATER"中包含最长的DNA片段是"AT",所以最长的长度是2。
     *@输入 包括一个字符串s,字符串长度length(1 ≤ length ≤ 50),字符串中只包括大写字母('A'~'Z')。
     *@输出 一个整数,表示最长的DNA片段
     *
     *@思路 依次循环，最终返回最大值即可
     */
    public int returnDnaLength(String s) {
        String ruler = "ATCG";
        int i = 0;
        int j = 0;
        int z = 0;
        while (i < s.length()) {
            String r = s.substring(i, ++i);
            if (ruler.contains(r)) {
                j++;
            } else {
                if (z < j) {
                    z = j;
                }
                j = 0;
            }
        }
        if (j > z) {
            z = j;
        }
        return z;
    }

    /**
     * 如果一个字符串由两个相同字符串连接而成,就称这个字符串是偶串。例如"xyzxyz"和"aaaaaa"是偶串,但是"ababab"和"xyzxy"却不是。
     * 牛牛现在给你一个只包含小写字母的偶串s,你可以从字符串s的末尾删除1和或者多个字符,保证删除之后的字符串还是一个偶串,牛牛想知道删除之后得到最长偶串长度是多少
     *
     * @输入 包括一个字符串s, 字符串长度length(2 ≤ length ≤ 200), 保证s是一个偶串且由小写字母构成
     * @输出 一个整数, 表示删除之后能得到的最长偶串长度是多少。保证测试数据有非零解
     * @思路 以2为单位循环即可
     */
    public int returnMaxEvenStrLength(String s) {
        int i = 0;
        String r = s;
        int middle = 0;
        int z = 0;
        while (i < s.length()) {
            i = i + 2;
            r = r.substring(0, r.length() - 2);

            middle = r.length() / 2;

            String pre = r.substring(0, middle);
            String nex = r.substring(middle, r.length());
            if (pre.equals(nex)) {
                z = r.length();
                break;
            }
        }
        return z;
    }
}

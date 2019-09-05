package cn.zch.util;
/***
 * @class CrcYzMenjin
 * @description TODO
 * @author zch
 * @date 2019/8/6
 * @version V0.0.1.201908061712.01
 * @function
 * @modfiyDate
 * @createDate
 * @package
 */
public class CrcYzMenjin {
    /**
     * 比较校验值
     */
    public static boolean yzMenJinCrcCompare(byte[] bytes) {
        byte crcByte = yzMenJinCrcCalc(bytes);
        // 原串当中的校验和
        byte originalCrcByte = bytes[bytes.length - 2];
        return (crcByte == originalCrcByte);
    }
    /**
     * 计算校验值
     */
    public static byte yzMenJinCrcCalc(byte[] bytes) {
        bytes = StringUtil.subByte(bytes, 1, bytes.length - 3);
        int crcSum = 0;
        for (byte aByte : bytes) {
            crcSum += aByte;
        }
        byte[] crcBytes = StringUtil.int2ByteArray(crcSum);
        return crcBytes[crcBytes.length - 1];
    }
    /**
     * 生成带校验值
     */
    public static void generate(byte[] buf) {
        int crc = yzMenJinCrcCalc(buf);
        buf[buf.length - 2] = (byte) (crc);
        buf[buf.length - 1] = (byte) (126);
    }
    /**
     * 测试和校验
     */
    private static void testYzMenJinCrc() {
        // String randomStr = "192BFFBE";
        String randomStr = "12345678";
        // String cmdStr = ("7E 4D432D35393132543139303630303032 FFFFFFFF " + randomStr + " 06020000000000 79 7E").replace(" ", "");
        String cmdStr = ("4D432D35393132543139303630303032 FFFFFFFF " + randomStr + " 06020000000000").replace(" ", "");
        System.out.println(cmdStr);
        byte[] s = StringUtil.yzMjStrToBytes(cmdStr);
        System.out.println(LogUtil.bytes2HexString(s));
        generate(s);
        // 7E1906677946432D38393230413235303530323835FFFFFFFF3102000000001046432D38393230413235303530323835007E
        // 7E1906677946432D38393230413235303530323835FFFFFFFF3102000000001046432D383932304132353035303238350800
        System.out.println(LogUtil.bytes2HexString(s));
    }
    public static void main(String[] args) {
        //        7E 4D432D35393132543139303630303032 FFFFFFFF 192BFFBE 060200000000 0079 7E
        //        7E 4D432D35393132543139303630303032 FFFFFFFF 17481A52 060200000000 0043 7E
        //        7E 4D432D35393132543139303630303032 FFFFFFFF 1BC87897 060200000000 006A 7E
        //        7E 4D432D35393132543139303630303032 FFFFFFFF 190153C6 060200000000 00AB 7E
        //        7E 4D432D35393132543139303630303032 FFFFFFFF 1DC878D7 060200000000 006A 7E
        testYzMenJinCrc();
    }
}

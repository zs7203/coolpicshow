package toy.hellozs.com.coolpicshow.util;

/**
 * Created by Administrator on 2015/12/10.
 */
public class Category {

    public static final String WEI_MEI_TU_PIAN = "http://www.wmpic.me/tupian/wmpic";
    public static final String XIAO_QING_XIN_TU_PIAN = "http://www.wmpic.me/tupian/qingxin";
    public static final String WEI_MEI_SHE_YING = "http://www.wmpic.me/tupian/sheying";
    public static final String GAO_XIAO_TU_PIAN = "http://www.wmpic.me/tupian/funny";
    public static final String MING_XING_TU_PIAN = "http://www.wmpic.me/tupian/mingxing";
    public static final String MEI_NV_TU_PIAN = "http://www.wmpic.me/meinv";
    public static final String KE_AI_TU_PIAN = "http://www.wmpic.me/tupian/cute";

    public static String chooseCategory(int index) {
        String url = "";
        switch (index) {
            case 0:
                url = WEI_MEI_TU_PIAN;
                break;
            case 1:
                url = XIAO_QING_XIN_TU_PIAN;
                break;
            case 2:
                url = WEI_MEI_SHE_YING;
                break;
            case 3:
                url = GAO_XIAO_TU_PIAN;
                break;
            case 4:
                url = MING_XING_TU_PIAN;
                break;
            case 5:
                url = MEI_NV_TU_PIAN;
                break;
            case 6:
                url = KE_AI_TU_PIAN;
                break;
            default:
                break;
        }
        return url;
    }
}

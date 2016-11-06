package poly.fall16.pro2051.group8.raovat.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by giang on 9/26/2016.
 */

public class MyString {
    //    public static final String BASE_URL = "http://demophp2.esy.es/";
//    public static final String BASE_URL = "http://newsky95.tk/";

    public static final String BASE_URL = "http://nghiahoang.net/api/raovatapp/";
    public static final String URL_CITY = "http://bdsrongbay.todo.vn/api/app/getcities.html?root=0";
    public static final String URL = BASE_URL + "product.php";
    public static final String URL_PRODUCTS = BASE_URL + "product.php?action=ShowAll";
    public static final String URL_GET_DETAIL = BASE_URL + "product.php?action=getDetail&pid=";
    public static final String URL_GET_NEWS = BASE_URL + "product.php?action=getNew";
    public static final String URL_GET_PROPOSE = BASE_URL + "product.php?action=getRand";
    public static final String URL_GET_LAST_ITEM = BASE_URL + "product.php?action=getLastestPid";
    public static final String URL_GET_CATEGORY = BASE_URL + "product.php?action=getCategory&txtCategory=";
    public static final String URL_USER = BASE_URL + "user.php";
    public static final String URL_MY_POSTS = BASE_URL + "product.php?action=getUserProduct&txtUsername=";
    public static final String URL_GET_LAST_ITEM_CATEGORY = BASE_URL + "product.php?action=getLastestCategory&txtCategory=";
    public static final String URL_GET_BOOKMARK =  BASE_URL + "bookmark.php?action=getBookmark&txtUsername=";
    public static final String URL_ADD_BOOKMARK_PART1 = BASE_URL + "bookmark.php?action=saveBookmark&txtUsername=";
    public static final String URL_REMOVE_BOOKMARK_PART1 = BASE_URL + "bookmark.php?action=removeBookmark&txtUsername=";
    public static final String URL_ADD_BOOKMARK_PART2 =  "&txtCategory=";
    public static final String URL_ADD_FAVORITE_PART1 =  BASE_URL + "favorite.php?action=addFavorite&txtUsername=";
    public static final String URL_CHECK_FAVORITE_PART1 =  BASE_URL + "favorite.php?action=checkFavorite&txtUsername=";
    public static final String URL_REMOVE_FAVORITE_PART1 =  BASE_URL + "favorite.php?action=removeFavorite&txtUsername=";
    public static final String URL_ADD_FAVORITE_PART2 =  "&txtPid=";
    public static final String URL_GET_FAVORITE_LIST = BASE_URL + "favorite.php?action=getFavorite&txtUsername=";
    public static final String URL_USER_INFO = BASE_URL + "user.php?action=getDetail&txtUsername=";






    public static final String[] arrCategory = {"Thời trang, phụ kiện", "Nội, ngoại thất", "Điện máy, gia dụng", "Mẹ & bé", "Điện thoại, máy tính bảng", "Dịch vụ, giải trí", "Đi chợ online", "Ô tô, xe máy", "Nhà đất"};
    public static final String[] arrCategoryServer = {"ThoiTrangPhuKien", "NoiNgoaiThat", "DienMayGiaDung", "MeVaBe", "DienThoaiMayTinhBang", "DichVuGiaiTri", "DiChoOnline", "OToXeMay", "NhaDat"};
    public static final int TIME_CALLBACK_GETNEWS = 10000;
    public static final int TIME_CALLBACK_GETNEWS_SERVICES = 30000;

    public static String handingPrice(String originPrice) {
        double amount = Double.parseDouble(originPrice);
        DecimalFormat formatter = new DecimalFormat("#,###");

        originPrice = formatter.format(amount);
        char[] charPrice = originPrice.toCharArray();
        ArrayList<String> alPrice = new ArrayList<>();
        for (int i = 0; i < charPrice.length; i++) {
            if (String.valueOf(charPrice[i]).equals(",")) {
                alPrice.add(".");
            } else {
                alPrice.add(String.valueOf(charPrice[i]));
            }

        }
        originPrice = "";
        for (int i = 0; i < charPrice.length; i++) {
            originPrice += alPrice.get(i);
        }

        return originPrice + " đ";
    }
}

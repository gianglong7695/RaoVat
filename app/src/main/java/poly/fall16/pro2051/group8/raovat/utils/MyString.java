package poly.fall16.pro2051.group8.raovat.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by giang on 9/26/2016.
 */

public class MyString {
//    public static final String BASE_URL = "http://demophp2.esy.es/";
    public static final String BASE_URL = "http://newsky95.tk/";
    public static final String URL_CITY = "http://bdsrongbay.todo.vn/api/app/getcities.html?root=0";
    public static final String URL = BASE_URL + "product.php";
    public static final String URL_PRODUCTS = BASE_URL + "product.php?action=ShowAll";
    public static final String URL_GET_DETAIL = BASE_URL + "product.php?action=getDetail&pid=";
    public static final String URL_GET_NEWS = BASE_URL + "product.php?action=getNew";
    public static final String URL_GET_LAST_ITEM = BASE_URL + "product.php?action=getLastestPid";
    public static final String URL_GET_CATEGORY = BASE_URL + "product.php?action=getCategory&txtCategory=";
    public static final String URL_USER = BASE_URL + "user.php";



    public static final String [] arrCategory = {"Thời trang, phụ kiện", "Nội, ngoại thất", "Điện máy, gia dụng", "Mẹ & bé", "Điện thoại, máy tính bảng", "Dịch vụ, giải trí", "Đi chợ online", "Ô tô, xe máy", "Nhà đất"};
    public static final String [] arrCategoryServer = {"ThoiTrangPhuKien", "NoiNgoaiThat", "DienMayGiaDung", "MeVaBe", "DienThoaiMayTinhBang", "DichVuGiaiTri", "DiChoOnline", "OToXeMay", "NhaDat"};
    public static final int TIME_CALLBACK_GETNEWS = 5000;

    public static String handingPrice(String originPrice){
        double amount = Double.parseDouble(originPrice);
        DecimalFormat formatter = new DecimalFormat("#,###");

        originPrice = formatter.format(amount);
        char [] charPrice = originPrice.toCharArray();
        ArrayList<String> alPrice = new ArrayList<>();
        for (int i = 0; i < charPrice.length; i++) {
            if(String.valueOf(charPrice[i]).equals(",")){
                alPrice.add(".");
            }else{
                alPrice.add(String.valueOf(charPrice[i]));
            }

        }
        originPrice = "";
        for (int i = 0; i < charPrice.length; i++) {
            originPrice+=alPrice.get(i);
        }

        return originPrice+ " đ";
    }
}

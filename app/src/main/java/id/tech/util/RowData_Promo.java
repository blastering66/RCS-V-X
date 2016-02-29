package id.tech.util;

/**
 * Created by RebelCreative-A1 on 10/12/2015.
 */
public class RowData_Promo {
    public String id_promo, stock_name,promo_name,promo_start_date, promo_end_date;

    public RowData_Promo(String id_promo, String stock_name,String promo_name, String promo_start_date, String promo_end_date) {
        this.id_promo = id_promo;
        this.stock_name = stock_name;
        this.promo_name = promo_name;
        this.promo_start_date = promo_start_date;
        this.promo_end_date = promo_end_date;
    }

    public RowData_Promo(String id_promo, String promo_name) {
        this.id_promo = id_promo;
        this.promo_name = promo_name;

    }
}

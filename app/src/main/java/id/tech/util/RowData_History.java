package id.tech.util;

/**
 * Created by RebelCreative-A1 on 15/12/2015.
 */
public class RowData_History  {
    public String outlet_name,outlet_username,outlet_email,outlet_topup, alamat_outlet, telepon_outlet, confirm;

    public RowData_History(String outlet_name, String outlet_username, String outlet_email, String outlet_topup,
                           String alamat_outlet,String telepon_outlet,String confirm) {
        this.outlet_name = outlet_name;
        this.outlet_username = outlet_username;
        this.outlet_email = outlet_email;
        this.outlet_topup = outlet_topup;
        this.alamat_outlet = alamat_outlet;
        this.telepon_outlet = telepon_outlet;
        this.confirm = confirm;
    }
}

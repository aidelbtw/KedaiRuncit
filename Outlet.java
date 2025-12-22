public class Outlet {
    private String OutletCode;
    private String OutletName;

    public Outlet(String OutletCode, String OutletName) {
        this.OutletCode = OutletCode;
        this.OutletName = OutletName;
    }

public String getOutletCode(){
    return OutletCode;
}
public String getOutletName(){
    return OutletName;
}
}
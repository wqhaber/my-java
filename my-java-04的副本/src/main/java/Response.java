/**
 * Created by wqh on 2017/8/4.
 */
public class Response{


    private int IphoneNum;
    private float CashBack;
    private String Message;

    public int getIphoneNum() {
        return IphoneNum;
    }

    public void setIphoneNum(int iphoneNum) {
        IphoneNum = iphoneNum;
    }

    public float getCashBack() {
        return CashBack;
    }

    public void setCashBack(float cashBack) {
        CashBack = cashBack;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String Response(int sum){

        IphoneNum = sum/8000;
        CashBack = sum%8000;
        Message = "购买的手机个数为"+IphoneNum+"'"+"余额为"+CashBack;
        return Message;
    }

}

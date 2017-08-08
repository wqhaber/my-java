/**
 * Created by wqh on 2017/8/4.
 */
public class RealSale implements IphoneIndustry,Message {
    public int sale(int num){

        if (num >= 8000) {
            return (num/8000);
        } else {
            return -1;
        }

    }

    public Response Response(){

          return new Response();
    }
}

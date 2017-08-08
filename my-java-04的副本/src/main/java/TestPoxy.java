import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wqh on 2017/8/4.
 */
public class TestPoxy {
    public static void main(String[] args) {
        final RealSale target = new RealSale();

        ClassLoader loader = ClassLoader.getSystemClassLoader();

        Class[] interfaces = {IphoneIndustry.class, Message.class};

        InvocationHandler h = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(target, args);
            }
        };

        IphoneIndustry proxy = (IphoneIndustry) Proxy.newProxyInstance(loader, interfaces, h);

        int a =proxy.sale(20000) ;
        System.out.println("在代理商这边购买手机个数"+a);

        String b = ((Message) proxy).Response().Response(30000);

        System.out.println(b);
    }
}

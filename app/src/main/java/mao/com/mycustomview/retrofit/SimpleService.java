package mao.com.mycustomview.retrofit;

/**
 * Created by lenovo on 2018/12/5 0005.
 */

public class SimpleService {
    public static class Contributor {
        public final String login;
        public final int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }
}

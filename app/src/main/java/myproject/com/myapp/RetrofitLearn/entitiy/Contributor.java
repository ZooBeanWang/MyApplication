package myproject.com.myapp.RetrofitLearn.entitiy;

/**
 * Created by wang on 07/04/17.
 */

public class Contributor {
    public String login;
    public int contributions;

    public Contributor(String login, int contributions) {
        this.login = login;
        this.contributions = contributions;
    }

    @Override
    public String toString() {
        return "Contributor{" +
                "login='" + login + '\'' +
                ", contributions=" + contributions +
                '}';
    }
}

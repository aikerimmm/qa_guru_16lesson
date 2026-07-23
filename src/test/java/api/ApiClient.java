package api;

public class ApiClient {
    public final AuthApi auth = new AuthApi();
    public final UsersApi users = new UsersApi();
    public final ClubsApi clubs = new ClubsApi();
}
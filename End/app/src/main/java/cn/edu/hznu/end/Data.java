package cn.edu.hznu.end;

public class Data {
    private static String username;

    public static String getUsername() {
        return username;
    }
    public Data(){
    }
    public static void setUsername(String username) {
        Data.username = username;
    }
}

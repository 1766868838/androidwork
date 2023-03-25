package cn.edu.hznu.addressbook;

class GloablId {
    private static int id;

    public GloablId() {
    }

    public void setId(int id) {
        GloablId.id = id;
    }

    public int getId() {
        return id;
    }
}

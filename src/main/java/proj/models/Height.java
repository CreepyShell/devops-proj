package proj.models;

public enum Height {
    height1(10060),
    height2(10360),
    height3(10670),
    height4(10973),
    height5(11280),
    height6(11580),
    height7(11890),
    height8(12190),
    height9(12500),
    height10(12800);

    private int height;
    Height(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
}

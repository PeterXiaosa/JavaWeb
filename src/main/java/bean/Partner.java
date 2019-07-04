package bean;

public class Partner {
    String matchCode;

    User partner1;

    User partner2;

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public User getPartner1() {
        return partner1;
    }

    public void setPartner1(User partner1) {
        this.partner1 = partner1;
    }

    public User getPartner2() {
        return partner2;
    }

    public void setPartner2(User partner2) {
        this.partner2 = partner2;
    }
}

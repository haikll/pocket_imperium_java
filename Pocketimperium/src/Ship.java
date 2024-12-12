package ProjetLatest;

class Ship {
    private Player owner;
    private Sector location;

    public void moveTo(Sector sector) {
        this.location = sector;
    }
}
